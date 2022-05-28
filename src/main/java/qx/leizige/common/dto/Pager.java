package qx.leizige.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pager<T> {

	private List<T> data;

}
