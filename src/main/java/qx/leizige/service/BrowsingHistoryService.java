package qx.leizige.service;

import java.util.List;
import java.util.Map;

import qx.leizige.common.dto.BaseHistoryDto;
import qx.leizige.common.dto.BrowsingHistoryReqDto;
import qx.leizige.common.dto.Pager;

public interface BrowsingHistoryService<T extends BaseHistoryDto,R> extends CommonHistorySupport<T> {

	String add(T reqDto);

	Pager<R> queryBrowsingHistoryPage(Map<String, String> params);

	void batchRemove(List<BrowsingHistoryReqDto> reqDtoList);

	void removeAll();

}
