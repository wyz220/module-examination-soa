/**
 * 
 */
package com.msht.retailwater.engine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.common.utils.CurrencyUtils;
import com.msht.framework.common.utils.DateUtils;
import com.msht.framework.common.utils.Delimiters;
import com.msht.framework.common.utils.StringUtils;
import com.msht.framework.mq.BaseConsumer;
import com.msht.retailwater.common.ChannelMap;
import com.msht.retailwater.common.SysConfig;
import com.msht.retailwater.equipment.entity.EquipmentPo;
import com.msht.retailwater.equipment.entity.EquipmentPo.NetworkStatus;
import com.msht.retailwater.equipment.entity.EquipmentPo.ParamStatus;
import com.msht.retailwater.equipment.entity.EquipmentPo.RunStatus;
import com.msht.retailwater.equipment.service.EquipmentService;
import com.msht.retailwater.marketing.entity.ActivityPo;
import com.msht.retailwater.marketing.entity.ActivityPo.ActivityScope;
import com.msht.retailwater.marketing.entity.ActivityPo.ActivityStatus;
import com.msht.retailwater.marketing.entity.PackagePo;
import com.msht.retailwater.marketing.service.ActivityService;
import com.msht.retailwater.marketing.service.PackageService;
import com.msht.retailwater.member.entity.MemberPo;
import com.msht.retailwater.member.service.MemberService;
import com.msht.retailwater.mq.ControlMessage;
import com.msht.retailwater.mq.ControlMessage.MessageType;
import com.msht.retailwater.order.entity.OrderPo;
import com.msht.retailwater.order.entity.OrderPo.OrderStatus;
import com.msht.retailwater.order.entity.OrderPo.OrderType;
import com.msht.retailwater.order.entity.OrderPo.SyncWriteCard;
import com.msht.retailwater.order.service.OrderService;
import com.msht.retailwater.protocol.BusinessNoticeReqt;
import com.msht.retailwater.protocol.BusinessNoticeReqt.BusinessType;
import com.msht.retailwater.protocol.BusinessNoticeResp;
import com.msht.retailwater.protocol.CommandType;
import com.msht.retailwater.protocol.ControlReqt;
import com.msht.retailwater.protocol.ControlReqt.ConsumeType;
import com.msht.retailwater.protocol.ControlReqt.SwitchMachineKeySet;
import com.msht.retailwater.protocol.ControlResp;
import com.msht.retailwater.report.entity.StatReportPo;
import com.msht.retailwater.report.service.OrderReportService;
import com.msht.retailwater.sys.entity.ParamPo;
import com.msht.retailwater.sys.service.ParamService;
import com.msht.retailwater.utils.RWUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

/**
 * @author lindaofen
 *
 */
@Component
public class RWProcessor {

	private Logger logger = LoggerFactory.getLogger(RWProcessor.class);
	
	private final ReentrantLock lock = new ReentrantLock();
	
	@Autowired   
	@Qualifier("taskExecutor") 
	private Executor executor;
	
	/** 消息处理过期时间（单位：毫秒） */
	private long msgExpireTime = 1800000;
	/** 消息重发时间间隔（单位：毫秒） */
	private long msgResendTime = 15000;
	/** IC充值消息处理过期时间（单位：毫秒） */
	private long icRechargeMsgExpireTime = 30000;
	
	private int queueSize = 10000;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private EquipmentService equipmentService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ParamService paramService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private OrderReportService orderReportService;
	
	@Autowired
	private ChannelMap channelMap;
	
	private Map<String, ControlReqt> handleControlReqtQueue;
	
	private Map<String, BusinessNoticeReqt> handleBusinessNoticeReqtQueue;
	
	private BlockingQueue<ICRecharge> icRechargeQueue;
	private Map<String, ICRecharge> handleICRechargeQueue;
	
	@PostConstruct
	public void init(){
		this.handleControlReqtQueue = new ConcurrentHashMap<String, ControlReqt>(this.queueSize);
		this.handleBusinessNoticeReqtQueue = new ConcurrentHashMap<String, BusinessNoticeReqt>(this.queueSize);
		this.icRechargeQueue = new ArrayBlockingQueue<ICRecharge>(
				this.queueSize);
		this.handleICRechargeQueue = new ConcurrentHashMap<String, ICRecharge>(this.queueSize);
		
		msgExpireTime = SysConfig.SERVER_MESSAGE_EXPIRETIME;
		msgResendTime = SysConfig.SERVER_MESSAGE_RESENDTIME;
		icRechargeMsgExpireTime = SysConfig.SERVER_MESSAGE_ICRECHARGE_EXPIRETIME;
		
		executor.execute(new MessageResendThread());
		executor.execute(new ICRechargeHandleThread());
		
		//初始化mq消费连接
		RWConsumer consumer = new RWConsumer(SysConfig.SERVER_NAME, false, true);
		consumer.receiveMsg();
	}
	
	public void putControlReqtToHandleQueue(ControlReqt reqt) {
		String key = this.getKey(String.valueOf(reqt.getEquipmentNo()), String.valueOf(reqt.getDataFrameSeq()));
		this.handleControlReqtQueue.put(key, reqt);
	}
	
	public ControlReqt removeControlReqtFromHandleQueue(String key) {
		return this.handleControlReqtQueue.remove(key);
	}
	
	public void putBusinessNoticeReqtToHandleQueue(BusinessNoticeReqt reqt) {
		String key = this.getKey(String.valueOf(reqt.getEquipmentNo()), String.valueOf(reqt.getDataFrameSeq()));
		this.handleBusinessNoticeReqtQueue.put(key, reqt);
	}
	
	public BusinessNoticeReqt removeBusinessNoticeReqtFromHandleQueue(String key) {
		return this.handleBusinessNoticeReqtQueue.remove(key);
	}
	
	public void putICRechargeToHandleQueue(ICRecharge reqt) {
		String key = this.getKey(String.valueOf(reqt.getEquipmentNo()), String.valueOf(reqt.getDataFrameSeq()));
		this.handleICRechargeQueue.put(key, reqt);
	}
	
	public ICRecharge removeICRechargeFromHandleQueue(String key) {
		return this.handleICRechargeQueue.remove(key);
	}
	
	public ICRecharge removeLikeICRechargeFromHandleQueue(String key) {
		if (this.handleICRechargeQueue.size() <= 0){
			return null;
		}
		Set<String> set = handleICRechargeQueue.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()){
			String tempKey = it.next();
			if (tempKey.startsWith(key)){
				return this.handleICRechargeQueue.remove(tempKey);
			}
		}
		return null;
	}
	
	public ICRecharge getLikeICRechargeFromHandleQueue(String key) {
		if (this.handleICRechargeQueue.size() <= 0){
			return null;
		}
		Set<String> set = handleICRechargeQueue.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()){
			String tempKey = it.next();
			if (tempKey.startsWith(key)){
				return this.handleICRechargeQueue.get(tempKey);
			}
		}
		return null;
	}
	
	public ICRecharge getNextICRecharge() {
		ICRecharge icRecharge = null;
		try {
			icRecharge = this.icRechargeQueue.take();
		} catch (InterruptedException e) {
			logger.error(
					"Get ic recharge for queue exception occurred, cause by:{}",
					e);
		}
		return icRecharge;

	}
	
	public void putICRechargeToQueue(ICRecharge icRecharge) {
		try {
			if (this.icRechargeQueue.remainingCapacity() == 0) {
				logger.error("{"
						+ Thread.currentThread().getName()
						+ "} : The ic recharge queue is full, current pool video size:"
						+ this.icRechargeQueue.size() + ".");
			}
			this.icRechargeQueue.put(icRecharge);
		} catch (InterruptedException e) {
			logger.error(
					"put ic recharge to queue exception occurred, cause by:{}",
					e);
		}
	}
	
	public void handleStatus(EquipmentPo po) throws BusinessException {
		
		ParamPo queryParam = new ParamPo();
		Map<String, ParamPo> paramMap = paramService.map(queryParam);
		int outWaterTime = Integer.valueOf(paramMap.get(SysConfig.EQUIP_OUT_WATER_TIME).getParamValue());
	    int outWaterQuantity = Integer.valueOf(paramMap.get(SysConfig.EQUIP_OUT_WATER_QUANTITY).getParamValue());
		po.setOutWaterTime(outWaterTime);
		po.setOutWaterQuantity(outWaterQuantity);
		
		//判断设备目前情况
		ParamPo temperatureStartParam = paramMap.get(SysConfig.TEMPERATURE_START_CODE);
		ParamPo temperatureEndParam = paramMap.get(SysConfig.TEMPERATURE_END_CODE);
		ParamPo maxHumidityParam = paramMap.get(SysConfig.MAX_HUMIDITY_CODE);
		ParamPo tdsDesalinationRateParam = paramMap.get(SysConfig.TDS_DESALINATION_RATE_CODE);
		
		if (temperatureStartParam != null){
			double temperatureStart = Double.valueOf(temperatureStartParam.getParamValue());
			if (Double.valueOf(po.getTemperature()).doubleValue() < temperatureStart){
				po.setParamStatus(ParamStatus.EXCEPTION.getStatus());
			}
		}
		if (temperatureEndParam != null){
			double temperatureEnd = Double.valueOf(temperatureEndParam.getParamValue());
			if (Double.valueOf(po.getTemperature()).doubleValue() > temperatureEnd){
				po.setParamStatus(ParamStatus.EXCEPTION.getStatus());
			}
		}
		
		if (maxHumidityParam != null){
			double maxHumidity = Double.valueOf(maxHumidityParam.getParamValue());
			if (Double.valueOf(po.getHumidity()).doubleValue() >= maxHumidity){
				po.setParamStatus(ParamStatus.EXCEPTION.getStatus());
			}
		}
		
		if (tdsDesalinationRateParam != null){
			double rawWaterTdsValue = Double.valueOf(po.getRawWaterTds());
			double purifyWaterTds = Double.valueOf(po.getPurifyWaterTds());
			if (rawWaterTdsValue != 0.0){
				double tdsDesalinationRate = CurrencyUtils.div(CurrencyUtils.sub(rawWaterTdsValue, purifyWaterTds), rawWaterTdsValue) * 100;
				if (tdsDesalinationRate < Double.valueOf(tdsDesalinationRateParam.getParamValue()).doubleValue()){
					po.setParamStatus(ParamStatus.EXCEPTION.getStatus());
				}
			}

		}
		
		
		equipmentService.handleStatus(po);
	}
	
	public void handleICRechargeReqt(ControlReqt reqt,OrderPo po) throws BusinessException {
		

		try{
			lock.lock();
			//同步会员信息，记录会员信息
			this.syncMember(reqt,po);

			//30秒内不重复充值
			ICRecharge recharge = this.getLikeICRechargeFromHandleQueue(String.valueOf(reqt.getEquipmentNo()) + "_");
			if (recharge == null){
				//30s后再次刷卡，存在充值不成功，从数据库获取充值订单重新充值。
				OrderPo queryParam = new OrderPo();
				//queryParam.setAccount(po.getAccount());
				queryParam.setStatus(OrderStatus.PAYED_SUCCESS.getStatus());
				//queryParam.setType(OrderType.RECHARGE.getType());
				//queryParam.setSyncWriteCard(SyncWriteCard.SYNCHRONIZING.getFlag());
				queryParam.getPage().setCondition(" and account='" + po.getAccount() + "'" 
				+ " and (type=" + OrderType.RECHARGE.getType() + " or type=" + OrderType.ACTIVITY_PRESENT.getType() + " or type=" + OrderType.LOTTERY_DRAW.getType() + ")"
				+ " and (sync_write_card=" + SyncWriteCard.NO_SYNC.getFlag() + " or sync_write_card=" + SyncWriteCard.SYNCHRONIZING.getFlag() + ")");
				List<OrderPo> noSyncWriteCardList = orderService.list(queryParam);
				if (noSyncWriteCardList == null || noSyncWriteCardList.size() <= 0){
					lock.unlock();
					return;
				}
				for (OrderPo order : noSyncWriteCardList){
					if (SyncWriteCard.getFlag(order.getSyncWriteCard()) == SyncWriteCard.NO_SYNC){
						OrderPo updateParam = new OrderPo();
						updateParam.setId(order.getId());
						updateParam.setSyncWriteCard(SyncWriteCard.SYNCHRONIZING.getFlag());
						orderService.update(updateParam);
					}
				}
				lock.unlock();
				
				ICRecharge icRecharge = new ICRecharge(String.valueOf(reqt.getDataFrameSeq()),
						String.valueOf(reqt.getEquipmentNo()), po.getAccount(),noSyncWriteCardList);
				this.putICRechargeToQueue(icRecharge);
			}

		}finally{
			if (lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		
	}
	
	public void handleOrder(ControlReqt reqt, OrderPo po) throws BusinessException {
		MemberPo member = this.syncMember(reqt, po);
		orderService.handleOrder(member, po);
	}
	
	public void handleControlResp(ControlResp resp, String equipmentNo) throws BusinessException {

		ControlReqt reqt = this.handleControlReqtQueue.remove(this.getKey(equipmentNo, String.valueOf(resp.getDataFrameSeq())));
		if (reqt != null){
			switch (ConsumeType.getType(reqt.getConsumeType())){
			case IC_CARD_PAY:
				EquipmentPo param = new EquipmentPo();
				param.setEquipmentNo(String.valueOf(reqt.getEquipmentNo()));
				EquipmentPo po = equipmentService.get(param);
				EquipmentPo equip = new EquipmentPo();
				equip.setId(po.getId());
				//远程开关机
				if (reqt.getSwitchMachineKeySet() > 0){
					switch (SwitchMachineKeySet.getType(reqt.getSwitchMachineKeySet())){
					case ON:
						equip.setRunStatus(RunStatus.ON.getStatus());
						equip.setUpdateTime(new Date());
						equipmentService.update(equip);
						break;
					case OFF:
						equip.setRunStatus(RunStatus.OFF.getStatus());
						equip.setUpdateTime(new Date());
						equipmentService.update(equip);
						break;
					}
				}

				//修改单价
				if (reqt.getUnitPrice() > 0){
					equip.setUnitPrice((int) reqt.getUnitPrice());
					equip.setUpdateTime(new Date());
					equipmentService.update(equip);
				}
				break;
			}
		}
		
		//处理IC卡充值回复逻辑
		ICRecharge recharge = null;
		try{

			String key = this.getKey(equipmentNo, String.valueOf(resp.getDataFrameSeq()));
		    recharge = this.removeICRechargeFromHandleQueue(key);
			if (recharge != null){
				logger.info("Handle ic card recharge data: " + JSON.toJSONString(recharge));
				orderService.handleICRecharge(equipmentNo, recharge.getOrders());
				
				//如果充值成功，发送业务通知给android板
				double rechargeAmount = 0.0;
				for (OrderPo order : recharge.getOrders()){
					rechargeAmount = CurrencyUtils.add(CurrencyUtils.add(order.getPayFee(), order.getGiveFee()), rechargeAmount);
				}
				ControlMessage message = new ControlMessage();
				message.setType(MessageType.IC_RECHARGE_NOTICE.getType());
				message.setAccount(recharge.getAccount());
				message.setEquipmentNo(equipmentNo);
				message.setRechargeAmount(rechargeAmount);

				BusinessNoticeReqt businessNoticeReqt = convertMessageToBusinessNoticeReqt(message);
				Channel channel = channelMap.get(String.valueOf(message.getEquipmentNo()));
				if (channel == null){
					logger.error("The device has no connection, equipmentNo=" + message.getEquipmentNo());
					return;
				}
				channel.writeAndFlush(businessNoticeReqt);
				logger.info(channel.remoteAddress() + ": " + businessNoticeReqt.toString());
			}
		}catch (Exception e){
			logger.error("Handle ic card recharge response exception ocurred, cause by:{}", e);
			putICRechargeToHandleQueue(recharge);
		}

	}
	
	public void handleIDLE(String clientId) throws BusinessException{
		EquipmentPo po = new EquipmentPo();
		po.setEquipmentNo(clientId);
		EquipmentPo equipment = equipmentService.get(po);
		
		equipment.setNetworkStatus(NetworkStatus.CONNECTION_EXCEPT.getStatus());
		equipment.setUpdateTime(new Date());
		equipmentService.update(equipment);
	}
	
	public void handleBusinessNoticeResp(String equipmentNo, BusinessNoticeResp resp) throws BusinessException {
		String key = this.getKey(equipmentNo, String.valueOf(resp.getDataFrameSeq()));
		this.removeBusinessNoticeReqtFromHandleQueue(key);
	}
	
	public void handleBusinessNoticeReqt(BusinessNoticeReqt reqt) throws BusinessException {
		
		OrderPo order = new OrderPo();
		switch (BusinessType.getType(reqt.getType())){
		case APP_SCAN_NOTICE:
			// 将订单金额转换成以元为单位
			double amount = CurrencyUtils.div(reqt.getAmount(), 100);
			order.setAmount(amount);
			order.setWaterFee(amount);
			order.setAccount(String.valueOf(reqt.getAccount()));
			order.setEquipmentNo(String.valueOf(reqt.getEquipmentNo()));
			order.setSaleWaterQuantity(Double.valueOf(reqt.getTotalOutWaterTime()));
			orderService.handleBusinessNotice(order);
			break;
		case GET_WATER_NOTICE:
			break;
		}
		

	}
	
	public List<PackagePo> getPackageList() throws BusinessException {
		List<PackagePo> list = new ArrayList<PackagePo>();
		try{
			PackagePo packParam = new PackagePo();
			packParam.setType(ActivityScope.IC.getScope());
			list = packageService.list(packParam);
			if (list != null && list.size() > 0){
				Map<String, PackagePo> map = new HashMap<String, PackagePo>();
				StringBuffer condition = new StringBuffer();
				condition.append(" and package_id in (");
				for (int i = 0; i < list.size(); i++){
					PackagePo po = list.get(i);
					map.put(String.valueOf(po.getId()), po);
					if (i == list.size() - 1){
						condition.append(po.getId() + ")");
					}else{
						condition.append(po.getId() + ",");
					}
					
				}
				
				condition.append(" and (total_num = 0 OR used_num < total_num)");
				ActivityPo activityParam = new ActivityPo();
				activityParam.setStatus(ActivityStatus.EFFECTIVING.getStatus());
				activityParam.getPage().setCondition(condition.toString());
				List<ActivityPo> activityList = activityService.list(activityParam);
				for (int i = 0; i < activityList.size(); i++){
					ActivityPo po = activityList.get(i);;
					if (map.containsKey(String.valueOf(po.getPackageId()))){
						map.get(String.valueOf(po.getPackageId())).setActivity(po);
					}
				}
			}

		} catch (BusinessException e){
			logger.error(
					"Get package list exception occurred, cause by:{}",
					e);
			return list;
		} catch (Exception e){
			logger.error(
					"Get package list exception occurred, cause by:{}",
					e);
			return list;

		}
		return list;
	}
	
	public class RWConsumer extends BaseConsumer{

		public RWConsumer(String subject, boolean topic, boolean transacted) {
			super(subject, topic, transacted);
		}

		@Override
		public void onMessage(Message message) {

			TextMessage text = (TextMessage) message;
			try {
				logger.info("Receive MQ message: " + text.getText());
				if (StringUtils.isBlank(text.getText())){
					logger.warn("The receive MQ message is empty!");
					return;
				}
				ControlMessage controlMessage = JSON.parseObject(text.getText(), ControlMessage.class);

				switch (MessageType.getType(controlMessage.getType())){
				case IC_RECHARGE:
					/*ControlReqt reqt = convertMessageToCardRechargeReqt(controlMessage);
					Channel channel = channelMap.get(String.valueOf(controlMessage.getEquipmentNo()));
					if (channel == null){
						logger.error("The device has no connection, equipmentNo=" + controlMessage.getEquipmentNo());
						this.session.rollback();
						return;
					}
					//放入处理中队列
					long currentTimeMillis = System.currentTimeMillis();
					reqt.setSendTime(currentTimeMillis + msgResendTime);
					reqt.setExpireTime(currentTimeMillis + msgExpireTime); 
					putControlReqtToHandleQueue(reqt);
					channel.writeAndFlush(reqt);
					if (this.transacted){
						this.session.commit();
					}
					logger.info(channel.remoteAddress() + ": " + reqt.toString());*/
					break;
				case APP_SCAN_NOTICE:
					BusinessNoticeReqt businessNoticeReqt = convertMessageToBusinessNoticeReqt(controlMessage);
					Channel channel = channelMap.get(String.valueOf(controlMessage.getEquipmentNo()));
					if (channel == null){
						logger.error("The device has no connection, equipmentNo=" + controlMessage.getEquipmentNo());
						this.session.rollback();
						return;
					}
					//放入处理中队列
					long currentTimeMillis = System.currentTimeMillis();
					businessNoticeReqt.setSendTime(currentTimeMillis + msgResendTime);
					businessNoticeReqt.setExpireTime(currentTimeMillis + msgExpireTime); 
					putBusinessNoticeReqtToHandleQueue(businessNoticeReqt);
					channel.writeAndFlush(businessNoticeReqt);
					if (this.transacted){
						this.session.commit();
					}
					logger.info(channel.remoteAddress() + ": " + businessNoticeReqt.toString());
					break;
				case DEVICE_OFF:
				case DEVICE_ON:
					ControlReqt controlReqt = convertMessageToControlReqt(controlMessage);
					channel = channelMap.get(String.valueOf(controlMessage.getEquipmentNo()));
					if (channel == null){
						logger.error("The device has no connection, equipmentNo=" + controlMessage.getEquipmentNo());
						this.session.rollback();
						return;
					}
					//放入处理中队列
					currentTimeMillis = System.currentTimeMillis();
					controlReqt.setSendTime(currentTimeMillis + msgResendTime);
					controlReqt.setExpireTime(currentTimeMillis + msgExpireTime); 
					putControlReqtToHandleQueue(controlReqt);
					channel.writeAndFlush(controlReqt);
					if (this.transacted){
						this.session.commit();
					}
					logger.info(channel.remoteAddress() + ": " + controlReqt.toString());
					break;
				case ADJUST_PRICE:
				    controlReqt = convertMessageToControlReqt(controlMessage);
					channel = channelMap.get(String.valueOf(controlMessage.getEquipmentNo()));
					if (channel == null){
						logger.error("The device has no connection, equipmentNo=" + controlMessage.getEquipmentNo());
						this.session.rollback();
						return;
					}
					//放入处理中队列
					currentTimeMillis = System.currentTimeMillis();
					controlReqt.setSendTime(currentTimeMillis + msgResendTime);
					controlReqt.setExpireTime(currentTimeMillis + msgExpireTime); 
					putControlReqtToHandleQueue(controlReqt);
					channel.writeAndFlush(controlReqt);
					if (this.transacted){
						this.session.commit();
					}
					logger.info(channel.remoteAddress() + ": " + controlReqt.toString());
					break;
				}

				
			} catch (JMSException e) {
				logger.error(
						"Handle MQ message exception occurred, cause by:{}",
						e);
				try {
					if (this.transacted){
					this.session.rollback();
					}
				} catch (JMSException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	private ControlReqt convertMessageToCardRechargeReqt(ControlMessage message){
		ControlReqt reqt = new ControlReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.CONTROL_REQT.getType());
		
		reqt.setEquipmentNo(Integer.valueOf(message.getEquipmentNo()));
		String cardType = message.getAccount().substring(0,2);
		String cardNumber = message.getAccount().substring(2, message.getAccount().length());
		reqt.setCardType((byte) Byte.valueOf(cardType));
		reqt.setCardNumber(Integer.valueOf(cardNumber));
		
		Date operTime = DateUtils.parseDate(message.getOperTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(operTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
        int year = Integer.valueOf(dateFormat.format(operTime));
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        ByteBuf triggerTimeBuf = ByteBufAllocator.DEFAULT.buffer();
        triggerTimeBuf.writeByte((byte) year);
        triggerTimeBuf.writeByte((byte) month);
        triggerTimeBuf.writeByte((byte) day);
        triggerTimeBuf.writeByte((byte) hour);
        triggerTimeBuf.writeByte((byte) minute);
        triggerTimeBuf.writeByte((byte) second);
        byte[] triggerTimeData = new byte[6];
        triggerTimeBuf.readBytes(triggerTimeData);
        reqt.setTriggerTime(triggerTimeData);
		reqt.setConsumeType((byte) ConsumeType.IC_CARD_RECHARGE.getType());
	    short beforeBalance = CurrencyUtils.mul(message.getBeforeBalance(), 100).shortValue();
		reqt.setBeforeBalance(beforeBalance);
		reqt.setAmountType((byte) 0x00);
		reqt.setAmount((short) 0x0000);
	    short afterBalance = CurrencyUtils.mul(message.getAfterBalance(), 100).shortValue();
		reqt.setAfterBalance(afterBalance);
		reqt.setProduceWaterQuantity((short) 0x0000);
		short rechargeAmount = CurrencyUtils.mul(message.getRechargeAmount(), 100).shortValue();
		reqt.setRechargeAmount((short) rechargeAmount);

		reqt.setInstallationNo((short) 0x0000);
		reqt.setUnitPrice((byte) 0x00);
		reqt.setOzoneWorkingTime((byte) 0x00);
		reqt.setSwitchMachineKeySet((byte) 0x00);
		reqt.setGetWaterSet((byte) 0x00);
		reqt.setFilterResetSet((byte) 0x00);
		reqt.setFilterResetLevel((byte) 0x00);
		reqt.setPreDeductAmountSet((short) 0x0000);
		reqt.setBlackCardFlag((byte) 0x00);
		reqt.setButtonCode((byte) 0x00);
		reqt.setReserve1((byte) 0x00);
		reqt.setReserve2((byte) 0x00);
		reqt.setReserve3((byte) 0x00);
		
		//updateFlag1:1111 0111
		//updateFlag2:1111 1111
		//updateFlag3:1100 0110
		//updateFlag4:0110 0000
		//updateFlag5:0000 0000
		//updateFlag4:0000 0000
		reqt.setUpdateflag1((byte) 0xF7);
		reqt.setUpdateflag2((byte) 0xFF);
		reqt.setUpdateflag3((byte) 0xC6);
		reqt.setUpdateflag4((byte) 0x60);
		reqt.setUpdateflag5((byte) 0x00);
		reqt.setUpdateflag6((byte) 0x00);
        return reqt;
		
	}
	
	private ControlReqt convertMessageToCardRechargeReqt(ICRecharge recharge){
		ControlReqt reqt = new ControlReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(Integer.valueOf(recharge.getDataFrameSeq()));
		reqt.setCommand((short) CommandType.CONTROL_REQT.getType());
		
		reqt.setEquipmentNo(Integer.valueOf(recharge.getEquipmentNo()));
		String cardType = recharge.getOrders().get(0).getAccount().substring(0,2);
		String cardNumber = recharge.getOrders().get(0).getAccount().substring(2, recharge.getOrders().get(0).getAccount().length());
		reqt.setCardType((byte) Byte.valueOf(cardType));
		reqt.setCardNumber(Integer.valueOf(cardNumber));
		
		Date operTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(operTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
        int year = Integer.valueOf(dateFormat.format(operTime));
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        ByteBuf triggerTimeBuf = ByteBufAllocator.DEFAULT.buffer();
        triggerTimeBuf.writeByte((byte) year);
        triggerTimeBuf.writeByte((byte) month);
        triggerTimeBuf.writeByte((byte) day);
        triggerTimeBuf.writeByte((byte) hour);
        triggerTimeBuf.writeByte((byte) minute);
        triggerTimeBuf.writeByte((byte) second);
        byte[] triggerTimeData = new byte[6];
        triggerTimeBuf.readBytes(triggerTimeData);
        reqt.setTriggerTime(triggerTimeData);
		reqt.setConsumeType((byte) ConsumeType.IC_CARD_RECHARGE.getType());
		reqt.setBeforeBalance((short) 0x0000);
		reqt.setAmountType((byte) 0x00);
		reqt.setAmount((short) 0x0000);
		reqt.setAfterBalance((short) 0x0000);
		reqt.setProduceWaterQuantity((short) 0x0000);
		
		double rechargeAmount = 0.0;
		for (OrderPo order : recharge.getOrders()){
			double amount = CurrencyUtils.add(order.getPayFee(), order.getGiveFee());
			rechargeAmount = CurrencyUtils.add(amount,rechargeAmount);
		}
		int totalRechargeAmount = CurrencyUtils.mul(rechargeAmount, 100).intValue();
		reqt.setRechargeAmount(totalRechargeAmount);

		reqt.setInstallationNo((short) 0x0000);
		reqt.setUnitPrice((byte) 0x00);
		reqt.setOzoneWorkingTime((byte) 0x00);
		reqt.setSwitchMachineKeySet((byte) 0x00);
		reqt.setGetWaterSet((byte) 0x00);
		reqt.setFilterResetSet((byte) 0x00);
		reqt.setFilterResetLevel((byte) 0x00);
		reqt.setPreDeductAmountSet((short) 0x0000);
		reqt.setBlackCardFlag((byte) 0x00);
		reqt.setButtonCode((byte) 0x00);
		reqt.setReserve1((byte) 0x00);
		reqt.setReserve2((byte) 0x00);
		reqt.setReserve3((byte) 0x00);
		
		//updateFlag1:1111 1111
		//updateFlag2:1111 1111
		//updateFlag3:0000 0000
		//updateFlag4:0000 0110
		//updateFlag5:0000 0000
		//updateFlag4:0000 0000
		reqt.setUpdateflag1((byte) 0xFF);
		reqt.setUpdateflag2((byte) 0xFF);
		reqt.setUpdateflag3((byte) 0x00);
		reqt.setUpdateflag4((byte) 0x06);
		reqt.setUpdateflag5((byte) 0x00);
		reqt.setUpdateflag6((byte) 0x00);
        return reqt;
		
	}
	
/*	private ControlReqt convertMessageToAppScanNoticeReqt(ControlMessage message){
		ControlReqt reqt = new ControlReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(SysConfig.PROTOCOL_CONTROL_DATA_FRAME_SEQ);
		reqt.setCommand((short) CommandType.CONTROL_REQT.getType());
		
		reqt.setEquipmentNo(Integer.valueOf(message.getEquipmentNo()));
		reqt.setCardType((byte) 0x00);
		reqt.setCardNumber(message.getAccount());
		
        ByteBuf triggerTimeBuf = ByteBufAllocator.DEFAULT.buffer();
        triggerTimeBuf.writeByte((byte) 0x00);
        triggerTimeBuf.writeByte((byte) 0x00);
        triggerTimeBuf.writeByte((byte) 0x00);
        triggerTimeBuf.writeByte((byte) 0x00);
        triggerTimeBuf.writeByte((byte) 0x00);
        triggerTimeBuf.writeByte((byte) 0x00);
        byte[] triggerTimeData = new byte[6];
        triggerTimeBuf.readBytes(triggerTimeData);
        reqt.setTriggerTime(triggerTimeData);
		reqt.setConsumeType((byte) ConsumeType.APP_SCAN_CODE.getType());
	    short beforeBalance = CurrencyUtils.mul(message.getBeforeBalance(), 100).shortValue();
		reqt.setBeforeBalance(beforeBalance);
		reqt.setAmountType((byte) 0x00);
		reqt.setAmount((short) 0x0000);
		reqt.setAfterBalance((short) 0x0000);
		reqt.setProduceWaterQuantity((short) 0x0000);
		reqt.setRechargeAmount((short) 0x0000);
		reqt.setInstallationNo((short) 0x0000);
		reqt.setUnitPrice((byte) 0x00);
		reqt.setOzoneWorkingTime((byte) 0x00);
		reqt.setSwitchMachineKeySet((byte) 0x00);
		reqt.setGetWaterSet((byte) 0x00);
		reqt.setFilterResetSet((byte) 0x00);
		reqt.setFilterResetLevel((byte) 0x00);
		reqt.setPreDeductAmountSet((short) 0x0000);
		reqt.setBlackCardFlag((byte) 0x00);
		reqt.setButtonCode((byte) 0x00);
		reqt.setReserve1((byte) 0x00);
		reqt.setReserve2((byte) 0x00);
		reqt.setReserve3((byte) 0x00);
		
		//updateFlag1:1111 0111
		//updateFlag2:1110 0000
		//updateFlag3:0011 0000
		//updateFlag4:0000 0000
		//updateFlag5:0000 0000
		//updateFlag4:0000 0000
		reqt.setUpdateflag1((byte) 0xF7);
		reqt.setUpdateflag2((byte) 0xE0);
		reqt.setUpdateflag3((byte) 0x30);
		reqt.setUpdateflag4((byte) 0x00);
		reqt.setUpdateflag5((byte) 0x00);
		reqt.setUpdateflag6((byte) 0x00);
        return reqt;
		
	}*/
	
	private BusinessNoticeReqt convertMessageToBusinessNoticeReqt(ControlMessage message){
		
		ParamPo paramQuery = new ParamPo();
		paramQuery.setParamCode(SysConfig.APP_BUY_WATER_MAX_AMOUNT);
		ParamPo param = paramService.get(paramQuery);
		MemberPo memberParam = new MemberPo();
		memberParam.setAccount(message.getAccount());
		MemberPo member = memberService.get(memberParam);
		
		BusinessNoticeReqt reqt = new BusinessNoticeReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.BUSINESS_NOTICE_REQT.getType());
		
		switch (MessageType.getType(message.getType())){
		case APP_SCAN_NOTICE:
			reqt.setType((byte) BusinessType.APP_SCAN_NOTICE.getType());
			reqt.setAccount(Long.valueOf(message.getAccount()));
			int beforeBalance  = CurrencyUtils.mul(message.getBeforeBalance(), 100).intValue();
			reqt.setBeforeBalance(beforeBalance);
			
			double amount = Double.valueOf(param.getParamValue());
			double balance = CurrencyUtils.add(member.getPayBalance(), member.getGiveBalance());
			if (balance < amount){
				amount = balance;
			}
			reqt.setAmount(CurrencyUtils.mul(amount, 100).intValue());
			reqt.setAfterBalance(0x0000);
			reqt.setEquipmentNo(Integer.valueOf(message.getEquipmentNo()));
			reqt.setUnitPrice(message.getUnitPrice().byteValue());
			reqt.setOutWaterTime(message.getOutWaterTime().byteValue());
			reqt.setOutWaterQuantity(message.getOutWaterQuantity().byteValue());
			break;
		case IC_RECHARGE_NOTICE:
			reqt.setType((byte) BusinessType.IC_RECHARGE_NOTICE.getType());
			reqt.setAccount(Long.valueOf(message.getAccount()));
		    balance = CurrencyUtils.add(member.getPayBalance(),member.getGiveBalance());
		    double beforeRechargeBalance  = CurrencyUtils.sub(balance, message.getRechargeAmount());
			reqt.setBeforeBalance(CurrencyUtils.mul(beforeRechargeBalance, 100).intValue());
			
		    amount = message.getRechargeAmount();
			reqt.setAmount(CurrencyUtils.mul(amount, 100).intValue());
			reqt.setAfterBalance(CurrencyUtils.mul(balance, 100).intValue());
			reqt.setEquipmentNo(Integer.valueOf(message.getEquipmentNo()));
			reqt.setUnitPrice((byte) 0x00);
			reqt.setOutWaterTime((byte) 0x00);
			reqt.setOutWaterQuantity((byte) 0x00);
			break;
		}
		return reqt;
	}
	
	private ControlReqt convertMessageToControlReqt(ControlMessage message){
		ControlReqt reqt = new ControlReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.CONTROL_REQT.getType());
		
		reqt.setEquipmentNo(Integer.valueOf(message.getEquipmentNo()));
		reqt.setCardType((byte) 0x00);
		reqt.setCardNumber(0x00000000);
		
		Date operTime = DateUtils.parseDate(message.getOperTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(operTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
        int year = Integer.valueOf(dateFormat.format(operTime));
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        ByteBuf triggerTimeBuf = ByteBufAllocator.DEFAULT.buffer();
        triggerTimeBuf.writeByte((byte) year);
        triggerTimeBuf.writeByte((byte) month);
        triggerTimeBuf.writeByte((byte) day);
        triggerTimeBuf.writeByte((byte) hour);
        triggerTimeBuf.writeByte((byte) minute);
        triggerTimeBuf.writeByte((byte) second);
        byte[] triggerTimeData = new byte[6];
        triggerTimeBuf.readBytes(triggerTimeData);
        reqt.setTriggerTime(triggerTimeData);
        
		reqt.setConsumeType((byte) ConsumeType.IC_CARD_PAY.getType());
		reqt.setBeforeBalance((short) 0x0000);
		reqt.setAmountType((byte) 0x00);
		reqt.setAmount((short) 0x0000);
		reqt.setAfterBalance((short) 0x0000);
		reqt.setProduceWaterQuantity((short) 0x0000);
		reqt.setRechargeAmount((short) 0x0000);
		reqt.setInstallationNo((short) 0x0000);
		reqt.setUnitPrice((byte) 0x00);
		reqt.setOzoneWorkingTime((byte) 0x00);
		switch (MessageType.getType(message.getType())){
		case DEVICE_OFF:
			reqt.setSwitchMachineKeySet((byte) 0x02);
			reqt.setPreDeductAmountSet((short) 0x0000);
			//updateFlag4:1000 0000
			reqt.setUpdateflag4((byte) 0x80);
			reqt.setUpdateflag5((byte) 0x00);
			break;
		case DEVICE_ON:
			reqt.setSwitchMachineKeySet((byte) 0x01);
			reqt.setPreDeductAmountSet((short) 0x0000);
			reqt.setUpdateflag4((byte) 0x80);
			reqt.setUpdateflag5((byte) 0x00);
			break;
		case ADJUST_PRICE:
			reqt.setUnitPrice(message.getUnitPrice().byteValue());
			short preDeductAmount = CurrencyUtils.mul(message.getPreDeductAmount(), 100).shortValue();
			reqt.setPreDeductAmountSet(preDeductAmount);
			//updateFlag4:0010 0000
			reqt.setUpdateflag4((byte) 0x20);
			reqt.setUpdateflag5((byte) 0x18);
			break;
		}
		reqt.setGetWaterSet((byte) 0x00);
		reqt.setFilterResetSet((byte) 0x00);
		reqt.setFilterResetLevel((byte) 0x00);
		//reqt.setPreDeductAmountSet((short) 0x0000);
		reqt.setBlackCardFlag((byte) 0x00);
		reqt.setButtonCode((byte) 0x00);
		reqt.setReserve1((byte) 0x00);
		reqt.setReserve2((byte) 0x00);
		reqt.setReserve3((byte) 0x00);
		
		//updateFlag1:0000 1111
		//updateFlag2:1111 1110 
		//updateFlag3:0000 0000
		//updateFlag4:1000 0000
		//updateFlag5:0000 0000
		//updateFlag4:0000 0000
		reqt.setUpdateflag1((byte) 0x0F);
		reqt.setUpdateflag2((byte) 0xFE);
		reqt.setUpdateflag3((byte) 0x00);

		//reqt.setUpdateflag5((byte) 0x00);
		reqt.setUpdateflag6((byte) 0x00);
        return reqt;
		
	}
	
	public class MessageResendThread implements Runnable {

		@Override
		public void run() {

			while (true) {
				List<ControlReqt> delControlReqtList = null;
				List<BusinessNoticeReqt> delBusinessNoticeReqtList = null;
				List<ICRecharge> delICRechargeList = null;
				try {

					//logger.info("Begin to perform message resend task threads...");
					for (ControlReqt reqt : handleControlReqtQueue.values()) {
						if (reqt.getExpireTime() <= System.currentTimeMillis()) {
							if (delControlReqtList == null){
								delControlReqtList = new ArrayList<ControlReqt>();
							}
							delControlReqtList.add(reqt);
						}
					}

					for (BusinessNoticeReqt reqt : handleBusinessNoticeReqtQueue.values()) {
						if (reqt.getExpireTime() <= System.currentTimeMillis()) {
							if (delBusinessNoticeReqtList == null){
								delBusinessNoticeReqtList = new ArrayList<BusinessNoticeReqt>();
							}

							delBusinessNoticeReqtList.add(reqt);
						}
					}
					
					//ic卡充值消息过期，清理内存，不清理db
					for (ICRecharge recharge : handleICRechargeQueue.values()){
						if (recharge.getExpireTime() <= System.currentTimeMillis()){
							if (delICRechargeList == null){
								delICRechargeList = new ArrayList<ICRecharge>();
							}
							delICRechargeList.add(recharge);
						}
					}

					// 删除
					if (delControlReqtList != null){
						for (int i = 0; i < delControlReqtList.size(); i++) {
							ControlReqt reqt = delControlReqtList.get(i);
							logger.info("Message expiration, discarded: " + reqt.toString());
							String key = getKey(String.valueOf(reqt.getEquipmentNo()), String.valueOf(reqt.getDataFrameSeq()));
							handleControlReqtQueue.remove(key);
							delControlReqtList.clear();
						}
					}
					if (delBusinessNoticeReqtList != null){
						for (int i = 0; i < delBusinessNoticeReqtList.size(); i++) {
							BusinessNoticeReqt reqt = delBusinessNoticeReqtList.get(i);
							logger.info("Message expiration, discarded: " + reqt.toString());
							String key = getKey(String.valueOf(reqt.getEquipmentNo()), String.valueOf(reqt.getDataFrameSeq()));
							handleBusinessNoticeReqtQueue.remove(key);
							delBusinessNoticeReqtList.clear();
						}
					}
					
					if (delICRechargeList != null){
						for (int i = 0; i < delICRechargeList.size(); i++){
							ICRecharge recharge = delICRechargeList.get(i);
							logger.info("IC recharge message expiration, discarded: " + JSON.toJSONString(recharge));
							String key = getKey(recharge.getEquipmentNo(), recharge.getDataFrameSeq());
							handleICRechargeQueue.remove(key);
							
/*							//如果充值不成功，修改订单同步写卡字段，等待用户下次写卡
							for (OrderPo order : recharge.getOrders()){
								OrderPo updateOrder = new OrderPo();
								updateOrder.setId(order.getId());
								updateOrder.setSyncWriteCard(SyncWriteCard.NO_SYNC.getFlag());
								orderService.update(updateOrder);
							}*/
							delICRechargeList.clear();
						}
					}

					// resend
					for (Entry<String, ControlReqt> entry : handleControlReqtQueue.entrySet()){
						ControlReqt reqt = entry.getValue();
						if (reqt.getSendTime() <= System.currentTimeMillis()) {
							Channel channel = channelMap.get(String.valueOf(reqt.getEquipmentNo()));
							if (channel != null && channel.isActive()) {
								reqt.setSendTime(System.currentTimeMillis() + msgResendTime);
								channel.writeAndFlush(reqt);
								logger.info(channel.remoteAddress() + "->resend: " + reqt.toString());
							}
						}
					}
					for (Entry<String, BusinessNoticeReqt> entry : handleBusinessNoticeReqtQueue.entrySet()){
						BusinessNoticeReqt reqt = entry.getValue();
						if (reqt.getSendTime() <= System.currentTimeMillis()) {
							Channel channel = channelMap.get(String.valueOf(reqt.getEquipmentNo()));
							if (channel != null && channel.isActive()) {
								reqt.setSendTime(System.currentTimeMillis() + msgResendTime);
								channel.writeAndFlush(reqt);
								logger.info(channel.remoteAddress() + "->resend: " + reqt.toString());
							}

						}
					}

					//logger.info("Run message resend task thread finished.");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error("Check the handling exceed time task exception ocurred, cause by:{}", e);
				} catch (Exception e) {
					logger.error("Check the handling exceed time task exception ocurred, cause by:{}", e);
				}
			}
		}

	}
	
	public class ICRechargeHandleThread implements Runnable {

		@Override
		public void run() {

			while (true) {
				try {
					ICRecharge icRecharge = getNextICRecharge();
					ControlReqt reqt = convertMessageToCardRechargeReqt(icRecharge);
					Channel channel = channelMap.get(String.valueOf(reqt.getEquipmentNo()));
					if (channel == null){
						logger.error("The device has no connection, equipmentNo=" + reqt.getEquipmentNo());
						putICRechargeToQueue(icRecharge);
						return;
					}
					channel.writeAndFlush(reqt);
					//放入处理队列
					long currentTimeMillis = System.currentTimeMillis();
					icRecharge.setExpireTime(currentTimeMillis + icRechargeMsgExpireTime);
					putICRechargeToHandleQueue(icRecharge);
					logger.info(channel.remoteAddress() + ": " + reqt.toString());
					logger.info("Run message ic card recharge task thread finished.");
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.error("IC card recharge processing task exception ocurred, cause by:{}", e);
				} catch (Exception e) {
					logger.error("IC card recharge processing task exception ocurred, cause by:{}", e);
				}
			}
		}

	}
	
	public class ICRecharge{
		private String dataFrameSeq;
		private String equipmentNo;
		private String account;
		private List<OrderPo> orders;
		private long expireTime;
		public ICRecharge(String dataFrameSeq,String equipmentNo, String account,List<OrderPo> orders){
			this.equipmentNo = equipmentNo;
			this.dataFrameSeq = dataFrameSeq;
			this.account = account;
			this.orders = orders;
		}
		public String getDataFrameSeq() {
			return dataFrameSeq;
		}
		public void setDataFrameSeq(String dataFrameSeq) {
			this.dataFrameSeq = dataFrameSeq;
		}
		public String getEquipmentNo() {
			return equipmentNo;
		}
		public void setEquipmentNo(String equipmentNo) {
			this.equipmentNo = equipmentNo;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public List<OrderPo> getOrders() {
			return orders;
		}
		public void setOrders(List<OrderPo> orders) {
			this.orders = orders;
		}
		public long getExpireTime() {
			return expireTime;
		}
		public void setExpireTime(long expireTime) {
			this.expireTime = expireTime;
		}
		
		

	}
	
	public EquipmentPo getEquipmentById(String equipmentNo){
		EquipmentPo param = new EquipmentPo();
		param.setEquipmentNo(equipmentNo);
		EquipmentPo equip = equipmentService.get(param);
		return equip;
	}
	
	private String getKey(String equipmentNo, String dataFrameSeq){
		return equipmentNo + Delimiters.UNDERLINE + dataFrameSeq;
	}
	
	public MemberPo syncMember(ControlReqt reqt,OrderPo po) throws BusinessException {
		Date currentTime = new Date();
		MemberPo memberParam = new MemberPo();
		memberParam.setAccount(po.getAccount());
		MemberPo member = memberService.get(memberParam);
		if (member == null) {
			member = new MemberPo();
			member.setType(MemberPo.UserType.IC.getType());
			member.setAccount(po.getAccount());
			member.setPayBalance(po.getPayBalance());
			member.setGiveBalance(0.00);
			member.setConsumeAmount(0.0);
			member.setRechargeAmount(po.getBeforeBalance());
			member.setActivationTime(currentTime);
			member.setCreateTime(currentTime);
			memberService.insert(member);
		} else {
			//重新获取IC卡余额,如果IC卡余额等于系统余额（说明IC卡充值成功，由于网路异常没有回复，异常流程，目前不会出现）
			double userActualBalance = 0.0;
			StatReportPo icUserAmountData = orderReportService.icUserAmountStat();
			if (icUserAmountData != null){
				double userTotalRechargeAmount = CurrencyUtils.add(icUserAmountData.getIcRechargeAmount(), icUserAmountData.getIcGiveAmount());
			    userActualBalance = CurrencyUtils.sub(userTotalRechargeAmount, icUserAmountData.getIcSaleAmount());
			}

			if (po.getBeforeBalance().doubleValue() > userActualBalance){
				//从内存获取数据
				ICRecharge icRecharge = this.removeLikeICRechargeFromHandleQueue(String.valueOf(reqt.getEquipmentNo() + "_"));
				if (icRecharge != null){
					double totalRechargeAmount = 0.0;
					for (OrderPo order : icRecharge.getOrders()){
						double rechargeAmount = CurrencyUtils.add(order.getPayFee(), order.getGiveFee());
						totalRechargeAmount = CurrencyUtils.add(totalRechargeAmount, rechargeAmount);
					}
					
					//如果金额相等，表示上次发送充值指令，写卡成功（没有收到回复）
					logger.info("Handle ic card recharge data: " + JSON.toJSONString(icRecharge));
					orderService.handleICRecharge(String.valueOf(reqt.getEquipmentNo()), icRecharge.getOrders());
				} else{
					//系统重启或者30s后再次刷卡，存在上次充值成功，没有得到回复的情况
					OrderPo queryParam = new OrderPo();
					//queryParam.setAccount(po.getAccount());
					queryParam.setStatus(OrderStatus.PAYED_SUCCESS.getStatus());
					queryParam.setType(OrderType.RECHARGE.getType());
					queryParam.setSyncWriteCard(SyncWriteCard.SYNCHRONIZING.getFlag());
					queryParam.getPage().setCondition(" and account='" + po.getAccount() + "'");
					List<OrderPo> syncingWriteCardList = orderService.list(queryParam);
					if (syncingWriteCardList != null && syncingWriteCardList.size() > 0){
						double totalRechargeAmount = 0.0;
						for (OrderPo order : syncingWriteCardList){
							double rechargeAmount = CurrencyUtils.add(order.getPayFee(), order.getGiveFee());
							totalRechargeAmount = CurrencyUtils.add(totalRechargeAmount, rechargeAmount);
						}
						
						ICRecharge recharge = new ICRecharge("", String.valueOf(reqt.getEquipmentNo()),po.getAccount(), syncingWriteCardList);
						logger.info("Handle ic card recharge data: " + JSON.toJSONString(recharge));
						orderService.handleICRecharge(String.valueOf(reqt.getEquipmentNo()), syncingWriteCardList);
					}
				}
			}
			
			//如果amount大于0，表示用户购水
			if (po.getAmount().doubleValue() > 0.0){
				//记录充值消费金额和赠送消费金额
				double payConsumeAmount = 0.00;
				double giveConsumeAmount = 0.00;
				if (member.getPayBalance().doubleValue() >= po.getAmount().doubleValue()) {
					member.setPayBalance(CurrencyUtils.sub(member.getPayBalance(), po.getAmount()));
					payConsumeAmount = po.getAmount();
				} else {
					double value = CurrencyUtils.sub(po.getAmount(), member.getPayBalance());
					member.setPayBalance(0.00);
					member.setGiveBalance(CurrencyUtils.sub(member.getGiveBalance(), value));
					payConsumeAmount = member.getPayBalance();
					giveConsumeAmount = value;
				}
				member.setPayConsumeAmount(payConsumeAmount);
				member.setGiveConsumeAmount(giveConsumeAmount);
				
				//计算累计消费金额
				double consumeAmount = CurrencyUtils.add(member.getConsumeAmount(), po.getAmount());
				member.setConsumeAmount(consumeAmount);
				
				member.setUpdateTime(currentTime);
				memberService.update(member);

			}
			
		}
		
		return member;
	}
}
