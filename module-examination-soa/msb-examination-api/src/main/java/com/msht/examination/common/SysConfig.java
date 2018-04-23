/**
 * 
 */
package com.msht.examination.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.msht.framework.common.utils.FileUtils;

/**
 * 系统配置
 * @author lindaofen
 *
 */
public class SysConfig {

	public static final String WATER_PRICE = "water_price";
	public static final String BUCKET_PRICE = "bucket_price";
	public static final String DELIVERY_PRICE = "delivery_price";
	public static final String PLATFORM_FEE = "platform_fee";
	public static final String WATER_QUANTITY = "water_quantity";
	
	public static String SECURITY_SIGN_KEY;
	public static String SECURITY_ENCRYPT_KEY;
	
	public static Map<Integer, String> payTypeMap = new HashMap<Integer, String>();
	
/*	//设备出水时间（单位：秒）
	public static int EQUIP_OUT_WATER_TIME = 40;
	//设备出水量（单位：升）
	public static int EQUIP_OUT_WATER_QUANTITY = 5;*/

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

		SECURITY_SIGN_KEY = props.getProperty("security.sign.key");
		SECURITY_ENCRYPT_KEY = props.getProperty("security.encrypt.key");
		
/*		EQUIP_OUT_WATER_TIME = Integer.valueOf(props.getProperty("equip.out.water.time"));
		EQUIP_OUT_WATER_QUANTITY = Integer.valueOf(props.getProperty("equip.out.water.quantity"));*/
		
		payTypeMap.put(1, "支付宝");
		payTypeMap.put(2, "支付宝wap");
		payTypeMap.put(3, "银联");
		payTypeMap.put(4, "银联wap");
		payTypeMap.put(5, "微信");
		payTypeMap.put(6, "现金");
		payTypeMap.put(7, "招行一网通");
		payTypeMap.put(8, "民生宝钱包");
	}

}
