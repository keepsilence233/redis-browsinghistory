package qx.leizige.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import qx.leizige.common.dto.BrowsingHistoryReqDto;
import qx.leizige.common.dto.Pager;
import qx.leizige.common.dto.SpuResDto;
import qx.leizige.service.domain.BrowsingHistoryDomainServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class BrowsingHistoryServiceImpl implements BrowsingHistoryService<BrowsingHistoryReqDto, SpuResDto> {

	@Autowired
	private BrowsingHistoryDomainServiceImpl domainService;

	@Override
	public String add(BrowsingHistoryReqDto reqDto) {
		//校验下参数
		String key = getKey();
		String value = getValue(reqDto);
		return domainService.add(key, value);

	}

	@Override
	public Pager<SpuResDto> queryBrowsingHistoryPage(Map<String, String> params) {
		Integer currentPage = Integer.parseInt(Optional.ofNullable(params.get("currentPage"))
				.orElse(String.valueOf(1)));
		Integer pageSize = Integer.parseInt(Optional.ofNullable(params.get("pageSize")).orElse(String.valueOf(10)));
		String key = getKey();

		Pager<SpuResDto> spuResDtoPager = new Pager<>();
		try {
			spuResDtoPager = domainService.queryBrowsingHistoryPage(key, currentPage, pageSize);
		}
		catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
		return spuResDtoPager;

	}

	@Override
	public void batchRemove(List<BrowsingHistoryReqDto> reqDtoList) {
		if (!CollectionUtils.isEmpty(reqDtoList)) {
			String key = getKey();
			List<String> valueList = getValueList(reqDtoList);

			domainService.batchRemove(key, valueList);
		}

	}

	@Override
	public void removeAll() {
		String key = getKey();
		domainService.removeAll(key);
	}


	@Override
	public String getUserId() {

		//获取当前登陆用户
		return "";
	}

	@Override
	public String getAppId() {
		//获取当前用户端
		return "";
	}

	@Override
	public String getValue(BrowsingHistoryReqDto reqDto) {
		//拼接一个redis中保存的 value
		String value = "";
		return value;
	}

	@Override
	public List<String> getValueList(List<BrowsingHistoryReqDto> reqDtoList) {
		List<String> valueList = Lists.newArrayListWithExpectedSize(reqDtoList.size());

		reqDtoList.forEach(reqDto -> {
			//根据入参拼接成redis保存的value
			String value = "";
			valueList.add(value);
		});
		return valueList;
	}

}
