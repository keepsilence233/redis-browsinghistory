package qx.leizige.service.domain;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import qx.leizige.common.HistoryRedisUtils;
import qx.leizige.common.dto.Pager;
import qx.leizige.common.dto.SpuResDto;

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

		valueSet.forEach(value -> {
			//根据value不同值的拼接符split,拿到唯一编码去获取不同的值
			String[] valueArray = StringUtils.split(value, "-");
		});
		return new Pager<>();
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
