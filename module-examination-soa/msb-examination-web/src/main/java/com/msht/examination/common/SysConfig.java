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

	public static String USER_REGISTER_DEFAULTROLEID;
	
	public static String SYS_DOMAIN_ADDRESS;
	
	public static String ALIYUN_OSS_BUCKETNAME;
	public static String ALIYUN_OSS_USERIMGSAVEPATH;
	public static String ALIYUN_OSS_EQUIPIMGSAVEPATH;
	
	public static Map<String, String> runStatusMap = new HashMap<String, String>();
	
	public static Map<String, String> paramStatusMap = new HashMap<String, String>();
	
	public static Map<Integer, String> waterLevelMap = new HashMap<Integer, String>();
	
	public static Map<Integer, String> rawWaterStatusMap = new HashMap<Integer, String>();
	
	public static Map<Integer, String> workStatusMap = new HashMap<Integer, String>();
	public static Map<Integer, String> produceStatusMap = new HashMap<Integer, String>();
	
	public static Map<Integer, String> activityTypeMap = new HashMap<Integer, String>();
	public static Map<Integer, String> activityScopeMap = new HashMap<Integer, String>();
	public static Map<String, String> activityStatusMap = new HashMap<String, String>();
	
	public static Map<Integer, String> memberTypeMap = new HashMap<Integer, String>();
	public static Map<Integer, String> walletHistoryTypeMap = new HashMap<Integer, String>();
	
	public static Map<String, String> consumeOrderStatusMap = new HashMap<String, String>();
	public static Map<Integer, String> orderSourceMap = new HashMap<Integer, String>();
	public static Map<Integer, String> payTypeMap = new HashMap<Integer, String>();
	
	public static Map<String, String> rechargeOrderStatusMap = new HashMap<String, String>();
	
	public static final String EQUIP_OUT_WATER_TIME = "equip_out_water_time";
	public static final String EQUIP_OUT_WATER_QUANTITY = "equip_out_water_quantity";
	public static final String WATER_QUANTITY = "water_quantity";
	public static final String WATER_PRICE = "water_price";
	public static final String PRE_DEDUCT_AMOUNT = "pre_deduct_amount";
	public static final String WATER_UNIT_PRICE = "water_unit_price";
	
/*	//设备出水时间（单位：秒）
	public static int EQUIP_OUT_WATER_TIME = 40;
	//设备出水量（单位：升）
	public static int EQUIP_OUT_WATER_QUANTITY = 5;*/
	
	public static int PACKAGE_IC_TOTALNUM = 5;
	
	public static String TASK_ACTIVITY_TIME = "0/30 * * * * ?";
	public static boolean TASK_ACTIVITY_SWITCH = true;
	
	/** 导出excel文件标题 */
	public static final String[] EQUIP_FILE_TITLES = new String[]{"设备ID","型号","投放时间","投放小区","地址","销售额(元)","售水量(L)","充值金额","上次维护距今(天)","参数状态","开机状态"};
	public static final String[] MEMBER_FILE_TITLES = new String[]{"用户类型","用户账号","总余额","充值余额","赠送余额","激活时间","累计消费","累计充值"};
	public static final String[] CONSUME_FILE_TITLES = new String[]{"订单类型","订单号","用户 账号","订单金额","购买升数","设备ID","设备地址","小区名称","支付时间","订单状态"};
	public static final String[] RECHARGE_FILE_TITLES = new String[]{"订单类型","订单号","充值 账号","充值金额","赠送金额","充值时间","支付方式","支付状态","充值余额","赠送余额","付款账号","充值设备ID","设备地址","小区名称"};
	public static final String[] ACTIVITY_USER_FILE_TITLES = new String[]{"订单号","用户","赠送金额","时间"};
	public static final String[] IC_RECHARGE_FILE_TITLES = new String[]{"订单号","充值卡号","充值金额","赠送金额","充值时间","订单状态","写卡时间","操作人"};
	
	
	public static final String TEMPERATURE_START_CODE = "temperature_start";
	public static final String TEMPERATURE_END_CODE = "temperature_end";
	public static final String MAX_HUMIDITY_CODE = "max_humidity";
	public static final String TDS_DESALINATION_RATE_CODE = "tds_desalination_rate";
	
	public static Map<Integer, String> syncWriteCardMap = new HashMap<Integer, String>();

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
		USER_REGISTER_DEFAULTROLEID = props.getProperty("user.register.defaultRoleId");
		
		SYS_DOMAIN_ADDRESS = props.getProperty("sys.domain.address");
		
		ALIYUN_OSS_BUCKETNAME = props.getProperty("aliyun.oss.bucketName");
		ALIYUN_OSS_EQUIPIMGSAVEPATH = props.getProperty("aliyun.oss.equipImgSavePath");
		ALIYUN_OSS_USERIMGSAVEPATH = props.getProperty("aliyun.oss.userImgSavePath");
		
/*		EQUIP_OUT_WATER_TIME = Integer.valueOf(props.getProperty("equip.out.water.time"));
		EQUIP_OUT_WATER_QUANTITY = Integer.valueOf(props.getProperty("equip.out.water.quantity"));*/
		
		PACKAGE_IC_TOTALNUM = Integer.valueOf(props.getProperty("package.ic.totalNum"));
		
		TASK_ACTIVITY_TIME = props.getProperty("task.activity.time");
		TASK_ACTIVITY_SWITCH = Boolean.valueOf(props.getProperty("task.activity.switch"));
		
		runStatusMap.put("0", "关机");
		runStatusMap.put("1", "开机");
		
		paramStatusMap.put("0", "正常");
		paramStatusMap.put("1", "异常");
		
		waterLevelMap.put(0, "未满");
		waterLevelMap.put(1, "水满");

		rawWaterStatusMap.put(0, "正常");
		rawWaterStatusMap.put(1, "缺水");
		
		workStatusMap.put(0, "不可售水");
		workStatusMap.put(1, "售水中");
		
		produceStatusMap.put(0, "未制水");
		produceStatusMap.put(1, "制水中");
		
		activityTypeMap.put(1, "充值满赠");
		activityTypeMap.put(2, "小区活动");
		activityTypeMap.put(3, "小区抽奖");
		
		activityScopeMap.put(1, "民生宝充值");
		activityScopeMap.put(2, "IC卡充值");
		activityStatusMap.put("0", "待生效");
		activityStatusMap.put("1", "生效中");
		activityStatusMap.put("2", "已作废");
		activityStatusMap.put("3", "已过期");
		
		memberTypeMap.put(1, "民生宝");
		memberTypeMap.put(2, "IC卡");
		
		walletHistoryTypeMap.put(1, "充值");
		walletHistoryTypeMap.put(2, "消费");
		walletHistoryTypeMap.put(3, "余额退款");
		
		//consumeOrderStatusMap.put("0", "正在支付");
		consumeOrderStatusMap.put("1", "支付成功");
		//consumeOrderStatusMap.put("2", "支付失败");
		consumeOrderStatusMap.put("3", "申请退款");
		consumeOrderStatusMap.put("4", "已退款");
		
		rechargeOrderStatusMap.put("0", "正在支付");
		rechargeOrderStatusMap.put("1", "支付成功");
		rechargeOrderStatusMap.put("2", "支付失败");
		rechargeOrderStatusMap.put("3", "申请退款");
		rechargeOrderStatusMap.put("4", "已退款");
		
		
		orderSourceMap.put(1, "民生宝");
		orderSourceMap.put(2, "IC卡");
		
/*		1、	支付宝
		2、	支付宝wap
		3、	银联
		4、	银联wap
		5、	微信
		6、	现金
		7、	招行一网通
		8、	民生宝钱包*/

		payTypeMap.put(1, "支付宝");
		payTypeMap.put(2, "支付宝wap");
		payTypeMap.put(3, "银联");
		payTypeMap.put(4, "银联wap");
		payTypeMap.put(5, "微信");
		payTypeMap.put(6, "现金");
		payTypeMap.put(7, "招行一网通");
		payTypeMap.put(8, "民生宝钱包");
		
		syncWriteCardMap.put(0, "待写卡");
		syncWriteCardMap.put(1, "正在写卡");
		syncWriteCardMap.put(2, "写卡成功");
	}

}
