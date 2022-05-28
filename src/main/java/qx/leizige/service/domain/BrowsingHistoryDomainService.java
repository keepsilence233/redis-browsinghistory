package qx.leizige.service.domain;

import java.util.List;
import java.util.concurrent.ExecutionException;

import qx.leizige.common.dto.Pager;
import qx.leizige.common.dto.SpuResDto;

public interface BrowsingHistoryDomainService {

	String add(String key, String value);

	Pager<SpuResDto> queryBrowsingHistoryPage(String key,Integer currentPage,Integer pageSize) throws ExecutionException, InterruptedException;

	void batchRemove(String key, List<String> valueList);

	void removeAll(String key);

}
