package qx.leizige.service.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import qx.leizige.common.HistoryRedisUtils;
import qx.leizige.common.dto.Pager;
import qx.leizige.common.dto.SpuResDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class BrowsingHistoryDomainServiceImpl implements BrowsingHistoryDomainService{

	@Value("${browsingHistory.maxSize:100}")
	private Long maxSize;

	private final Integer DEFAULT_EXPIRE = 10000;

	@Autowired
	private HistoryRedisUtils historyRedisUtils;

	@Override
	public String add(String key, String value) {
		historyRedisUtils.add(key, value, System.currentTimeMillis());
		log.info("key:{},value:{},新增浏览历史成功!", key, value);

		Long valueSize = historyRedisUtils.zCard(key);
		if (valueSize > maxSize) {
			Long removeRange = historyRedisUtils.removeRange(key, 0, 0);
			log.info("超出最大长度,移除元素,{}", removeRange);
		}

		historyRedisUtils.expire(key, DEFAULT_EXPIRE);
		return key;

	}

	@Override
	public Pager<SpuResDto> queryBrowsingHistoryPage(String key, Integer currentPage, Integer pageSize) throws ExecutionException, InterruptedException {

		log.info("queryBrowsingHistoryPage redis key : {}", key);

		Long valueSize = historyRedisUtils.zCard(key);
		if (null == valueSize || valueSize == 0) {
			log.info("{}浏览历史为空!", key);
			return new Pager<>();
		}

		Set<String> valueSet = historyRedisUtils.reverseRangeByScore(key, currentPage, pageSize);
		log.info("redis reverseRangeByScore valueSet : {},size: {}", JSON.toJSONString(valueSet), valueSet.size());

		Set<String> codeSet = Sets.newHashSet();
		List<BrowsingHistoryEntityList> browsingHistoryEntityList = new ArrayList<>();
		valueSet.forEach(value -> {
			//根据value不同值的拼接符split,拿到唯一编码去获取不同的值
			String[] valueArray = StringUtils.split(value, "-");
			codeSet.add(valueArray[0]);
			browsingHistoryEntityList.add(new BrowsingHistoryEntityList(valueArray[0],valueArray[1]));
		});

		List<SpuResDto> spuResDtoList = Lists.newArrayList();	//	模拟根据codeSet获取到的商品集合
		Map<String, SpuResDto> oldSpuMap = spuResDtoList.stream().collect(Collectors.toMap(SpuResDto::getSpuCode, Function.identity(), (o1, o2) -> o2));
		List<SpuResDto> newSpuResDtoList = Lists.newLinkedList();
		browsingHistoryEntityList.forEach(entity -> {

			String spuCode = entity.getCode();
			SpuResDto oldSpuResDto = oldSpuMap.get(spuCode);

			List<String> newSpuCodeList = newSpuResDtoList.stream().map(SpuResDto::getSpuCode).collect(Collectors.toList());

			if (oldSpuResDto != null && !newSpuCodeList.contains(spuCode)) {
				oldSpuResDto.setBrowsingHistoryDate(entity.getBrowsingHistoryDate());
				newSpuResDtoList.add(oldSpuResDto);
			}

			//例:2021/06/08浏览了A商品,2021/06/09也浏览了A商品,所以copy一份相同的商品出来,只是浏览日期不同
			if (oldSpuResDto != null && newSpuCodeList.contains(spuCode)) {
				SpuResDto spuResDto = new SpuResDto();
				BeanUtils.copyProperties(oldSpuResDto, spuResDto);
				spuResDto.setBrowsingHistoryDate(entity.getBrowsingHistoryDate());
				newSpuResDtoList.add(spuResDto);
			}
		});


		return new Pager(newSpuResDtoList);
	}

	@Override
	public void batchRemove(String key, List<String> valueList) {
		valueList.forEach(value -> {
			log.info("key:{},value:{},批量删除浏览历史", key, value);
			historyRedisUtils.remove(key, value);
		});
	}

	@Override
	public void removeAll(String key) {
		historyRedisUtils.delete(key);
		log.info("清空浏览历史,{}", key);
	}
}
