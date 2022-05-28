package qx.leizige.common.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 继承 BaseHistoryDto 自定义添加唯一标识
 * 如：某人在某天某店铺浏览了code为666ITEM_CODE的商品
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BrowsingHistoryReqDto extends BaseHistoryDto implements Serializable {

	/**
	 * 店铺编码
	 */
	private String storeCode;


}
