package com.msht.examination.common;

public class ResultInfo {
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int NOT_LOGIN_ERROR = 2;

	public static final int FILE_TYPE_ERROR = 3;
	public static final int FILE_HEAD_FORMAT_ERROR = 4;

	public static final int INVALID_PARAM = 5;

	public static final int INVALID_STATUS = 6;

	private int code;

	private String message;

	public ResultInfo() {
		this.code = SUCCESS;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public enum ResultCode {
		SUCCESS(0, "操作成功"), FAILED(1, "操作失败"), NOT_LOGIN_ERROR(2, "用户没有登录"), FILE_TYPE_ERROR(3,
				"文件类型错误"), FILE_HEAD_FORMAT_ERROR(4, "文件头部格式不正确"), INVALID_PARAM(5, "参数无效"), INVALID_STATUS(6, "无效状态")
		, INVALID_VERIFY_CODE(7, "无效验证码"), USER_NOT_EXIST(8, "用户不存在"),USER_ALREADY_EXISTS(9, "用户已经存在"),
		PASSWORD_MISTAKE(10, "密码不正确"),INVALID_EQUIPMENT(11, "无法连接设备"),
		
		ACTIVITY_SCHEDULING_CONFLICTS(1001,"活动排期时间冲突"),NO_RECHARGE_ORDERS(1002,"用户没有充值"),
		ACTIVITY_IS_OVER(1003,"活动已经结束");

		private int code;
		private String message;

		private ResultCode(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public static ResultCode getResultCode(int code) {

			switch (code) {
			case 0:
				return SUCCESS;
			case 1:
				return FAILED;
			case 2:
				return NOT_LOGIN_ERROR;
			case 3:
				return FILE_TYPE_ERROR;
			case 4:
				return FILE_HEAD_FORMAT_ERROR;
			case 5:
				return INVALID_PARAM;
			case 6:
				return INVALID_STATUS;
			case 7:
				return INVALID_VERIFY_CODE;
			case 8:
				return USER_NOT_EXIST;
			case 9:
				return USER_ALREADY_EXISTS;
			case 10:
				return PASSWORD_MISTAKE;
			case 11:
				return INVALID_EQUIPMENT;
			default:
				return FAILED;
			}
		}
	}
}
