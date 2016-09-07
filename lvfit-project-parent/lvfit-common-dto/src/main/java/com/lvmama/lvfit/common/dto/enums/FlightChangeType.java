package com.lvmama.lvfit.common.dto.enums;

/**
 * 航变类型
 */
public enum FlightChangeType {

	FLIGHT_CHANGE("航班变化"),
	FLIGHT_CANCEL("航班取消");

	private String cnName;

	private FlightChangeType(String cnName) {
		this.cnName = cnName;
	}

	public String getCnName() {
		return cnName;
	}

}
