package com.msht.examination.common;

public class ResultInfo {
	
	public static final String SUCCESS = "0000";

	private String code;

	private String message;

	public ResultInfo() {
		this.code = SUCCESS;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public enum ResultCode {
		SUCCESS("0000", "操作成功"), FAILED("0001", "操作失败"), NOT_LOGIN_ERROR("0002", "用户没有登录"), FILE_TYPE_ERROR("0003",
				"文件类型错误"), FILE_HEAD_FORMAT_ERROR("0004", "文件头部格式不正确"), INVALID_PARAM("0005",
						"参数无效"), INVALID_STATUS("0006", "无效状态"), INVALID_USERNAME("0007", "用户名或者密码错误")
		,INVALID_SIGN("0008", "无效签名"),INVALID_ACCOUNT("0009", "无效账号"),INVALID_EQUIPMENT("0010", "无效设备")
		,BALANCE_NOT_ENOUGH("0011", "余额不足")
		
		//订单
		,INVALID_ORDER("1001","无效订单"),ORDER_AMOUNT_INCONSISTENT("1002","订单金额不一致"),PAY_ACCOUNT_IS_EMPTY("1003","支付账号为空")
		,REPEAT_PAID("1004","重复支付"),PROMOTION_ACTIVITY_END("1005","促销活动已经结束");

		private String code;
		private String message;

		private ResultCode(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public static ResultCode getResultCode(String code) {

			switch (code) {
			case "0000":
				return SUCCESS;
			case "0001":
				return FAILED;
			case "0002":
				return NOT_LOGIN_ERROR;
			case "0003":
				return FILE_TYPE_ERROR;
			case "0004":
				return FILE_HEAD_FORMAT_ERROR;
			case "0005":
				return INVALID_PARAM;
			case "0006":
				return INVALID_STATUS;
			case "0007":
				return INVALID_USERNAME;
			case "0008":
				return INVALID_SIGN;
			case "0009":
				return INVALID_ACCOUNT;
			case "0010":
				return INVALID_EQUIPMENT;
				
			case "1001":
				return INVALID_ORDER;
			case "1002":
				return ORDER_AMOUNT_INCONSISTENT;
			case "1003":
				return PAY_ACCOUNT_IS_EMPTY;
			case "1004":
				return REPEAT_PAID;
			default:
				return FAILED;
			}
		}
	}
}
