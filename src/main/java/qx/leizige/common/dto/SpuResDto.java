package qx.leizige.common.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class SpuResDto implements Serializable {

	//省略其他信息

	private String spuCode;

	private String browsingHistoryDate;
}
