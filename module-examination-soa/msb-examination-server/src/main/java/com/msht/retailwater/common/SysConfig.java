/**
 * 
 */
package com.msht.retailwater.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.msht.framework.common.utils.FileUtils;
import com.msht.retailwater.protocol.CommonMsg;
import com.msht.retailwater.protocol.HeartbeatReqt;

/**
 * 系统配置
 * @author lindaofen
 *
 */
public class SysConfig {

	public static final int PROTOCOL_START_FLAG = 0x51;
	public static final byte PROTOCOL_VERSION = 0x10;
	public static final byte PROTOCOL_BACKUP = 0x00;
	public static final short PROTOCOL_RESERVE = 0x0000;
	
	public static final String TEMPERATURE_START_CODE = "temperature_start";
	public static final String TEMPERATURE_END_CODE = "temperature_end";
	public static final String MAX_HUMIDITY_CODE = "max_humidity";
	public static final String TDS_DESALINATION_RATE_CODE = "tds_desalination_rate";
	public static final String EQUIP_OUT_WATER_TIME = "equip_out_water_time";
	public static final String EQUIP_OUT_WATER_QUANTITY = "equip_out_water_quantity";
	public static final String APP_BUY_WATER_MAX_AMOUNT = "app_buy_water_max_amount";
	
/*	public static final int PROTOCOL_HEARTBEAT_DATA_FRAME_SEQ = 0x00000008;
	public static final int PROTOCOL_STATUS_DATA_FRAME_SEQ = 0x00000021;
	public static final int PROTOCOL_CONTROL_DATA_FRAME_SEQ = 0x00000020;
	public static final int PROTOCOL_TIME_DATA_FRAME_SEQ = 0x00000022;
	public static final int PROTOCOL_BUSINESS_NOTICE_DATA_FRAME_SEQ = 0x00000023;*/
	
	public static String SERVER_NAME;
	
	public static long SERVER_MESSAGE_EXPIRETIME=1800000;
	public static long SERVER_MESSAGE_RESENDTIME=15000;
	public static long SERVER_MESSAGE_ICRECHARGE_EXPIRETIME = 30000;
	
	
	public static Map<String, String> commandMap = new HashMap<String, String>();
	
	public static String ZK_ADDRESS;
	public static String ZK_ROOT_PATH;
	public static String ZK_SERVER_IP;
	
	static {

		try{
			InputStream in  = FileUtils.getResourceAsStream("application.properties");
			Properties props = new Properties();
			props.load(in);
			init(props);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void init(Properties props ){

		
		commandMap.put("0x0108", "com.msht.retailwater.protocol.HeartbeatReqt");
		commandMap.put("0x0208", "com.msht.retailwater.protocol.HeartbeatResp");
		commandMap.put("0x0105", "com.msht.retailwater.protocol.StatusReqt");
		commandMap.put("0x0205", "com.msht.retailwater.protocol.StatusResp");
		commandMap.put("0x0104", "com.msht.retailwater.protocol.ControlReqt");
		commandMap.put("0x0204", "com.msht.retailwater.protocol.ControlResp");
		
		commandMap.put("0x0106", "com.msht.retailwater.protocol.TimeReqt");
		commandMap.put("0x0206", "com.msht.retailwater.protocol.TimeResp");
		
		commandMap.put("0x0107", "com.msht.retailwater.protocol.BusinessNoticeReqt");
		commandMap.put("0x0207", "com.msht.retailwater.protocol.BusinessNoticeResp");
		
		commandMap.put("0x0109", "com.msht.retailwater.protocol.GetPackageReqt");
		commandMap.put("0x0209", "com.msht.retailwater.protocol.GetPackageResp");
		
		commandMap.put("0x0103", "com.msht.retailwater.protocol.GetParamReqt");
		commandMap.put("0x0203", "com.msht.retailwater.protocol.GetParamResp");
		
		SERVER_NAME = props.getProperty("server.name");
		
		SERVER_MESSAGE_EXPIRETIME = Long.valueOf(props.getProperty("server.message.expireTime"));
		SERVER_MESSAGE_RESENDTIME = Long.valueOf(props.getProperty("server.message.resendTime"));
		SERVER_MESSAGE_ICRECHARGE_EXPIRETIME = Long.valueOf(props.getProperty("server.message.icRecharge.expireTime"));
		
		ZK_ADDRESS = props.getProperty("zk.address");
		ZK_ROOT_PATH = props.getProperty("zk.root.path");
		ZK_SERVER_IP = props.getProperty("zk.server.ip");
	}

}
