package qx.leizige.service;

import java.util.List;

import qx.leizige.common.dto.BaseHistoryDto;

public interface CommonHistorySupport<T extends BaseHistoryDto> {

	String getUserId();

	String getAppId();

	String getValue(T reqDto);

	List<String> getValueList(List<T> reqDtoList);

	default String cachePrefix() {
		return "HISTORY:";
	}

	default String getKeySeparator() {
		return "_";
	}

	default String getKey() {
		return cachePrefix() +
				getKeySeparator() +
				getAppId() +
				getKeySeparator() +
				getUserId();
	}
}
