package qx.leizige.common.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;


@Data
public class BaseHistoryDto implements Serializable {

	/**
	 * 浏览(目标[商品/店铺等])唯一编码
	 */
	private String code;

	/**
	 * 浏览日期
	 */
	private LocalDate browsingHistoryDate;
}
