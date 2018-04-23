/**
 * 
 */
package com.msht.retailwater.protocol;


/**
 * 消息类型
 * @author lindaofen
 *
 */
public enum CommandType {

	HEARTBEAT_REQT(0x0108), HEARTBEAT_RESP(0x0208),STATUS_REQT(0x0105),STATUS_RESP(0x0205)
	,CONTROL_REQT(0x0104),CONTROL_RESP(0x0204),TIME_REQT(0x0106),TIME_RESP(0x0206),
	BUSINESS_NOTICE_REQT(0x0107),BUSINESS_NOTICE_RESP(0x0207),
	GET_PACKAGE_REQT(0x0109),GET_PACKAGE_RESP(0x0209),GET_PARAM_REQT(0x0103),GET_PARAM_RESP(0x0203);
	
	private int type;
	
	private CommandType(int type){
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
	public static CommandType getType(int type) {
		switch (type){
		case 0x0108:
			return HEARTBEAT_REQT;
		case 0x0208:
			return HEARTBEAT_RESP;
		case 0x0105:
			return STATUS_REQT;
		case 0x0205:
			return STATUS_RESP;
		case 0x0104:
			return CONTROL_REQT;
		case 0x0204:
			return CONTROL_RESP;
		case 0x0106:
			return TIME_REQT;
		case 0x0206:
			return TIME_RESP;
		case 0x0107:
			return BUSINESS_NOTICE_REQT;
		case 0x0207:
			return BUSINESS_NOTICE_RESP;
		case 0x0109:
			return GET_PACKAGE_REQT;
		case 0x0209:
			return GET_PACKAGE_RESP;
		case 0x0103:
			return GET_PARAM_REQT;
		case 0x0203:
			return GET_PARAM_RESP;
		}
		return STATUS_REQT;
	}
}
