package com.msht.retailwater.handler;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.msht.framework.cluster.zk.server.BalanceUpdateProvider;
import com.msht.framework.common.utils.CurrencyUtils;
import com.msht.framework.common.utils.StringUtils;
import com.msht.retailwater.common.ChannelMap;
import com.msht.retailwater.common.SysConfig;
import com.msht.retailwater.engine.RWProcessor;
import com.msht.retailwater.equipment.entity.EquipmentPo;
import com.msht.retailwater.equipment.entity.EquipmentPo.BinaryCode;
import com.msht.retailwater.equipment.entity.EquipmentPo.ParamStatus;
import com.msht.retailwater.equipment.entity.EquipmentPo.ProduceStatus;
import com.msht.retailwater.equipment.entity.EquipmentPo.RawWaterStatus;
import com.msht.retailwater.equipment.entity.EquipmentPo.RunStatus;
import com.msht.retailwater.equipment.entity.EquipmentPo.WaterLevel;
import com.msht.retailwater.equipment.entity.EquipmentPo.WorkStatus;
import com.msht.retailwater.marketing.entity.PackagePo;
import com.msht.retailwater.order.entity.OrderPo;
import com.msht.retailwater.order.entity.OrderPo.OrderSource;
import com.msht.retailwater.order.entity.OrderPo.OrderStatus;
import com.msht.retailwater.order.entity.OrderPo.OrderType;
import com.msht.retailwater.protocol.BusinessNoticeReqt;
import com.msht.retailwater.protocol.BusinessNoticeResp;
import com.msht.retailwater.protocol.CommandType;
import com.msht.retailwater.protocol.CommonMsg;
import com.msht.retailwater.protocol.ControlReqt;
import com.msht.retailwater.protocol.ControlReqt.ConsumeType;
import com.msht.retailwater.protocol.ControlResp;
import com.msht.retailwater.protocol.GetPackageReqt;
import com.msht.retailwater.protocol.GetPackageResp;
import com.msht.retailwater.protocol.GetParamReqt;
import com.msht.retailwater.protocol.GetParamResp;
import com.msht.retailwater.protocol.HeartbeatReqt;
import com.msht.retailwater.protocol.HeartbeatResp;
import com.msht.retailwater.protocol.StatusReqt;
import com.msht.retailwater.protocol.StatusResp;
import com.msht.retailwater.protocol.TimeReqt;
import com.msht.retailwater.protocol.TimeReqt.TimeFormat;
import com.msht.retailwater.protocol.TimeResp;
import com.msht.retailwater.utils.ByteUtils;
import com.msht.retailwater.utils.HexUtils;
import com.msht.retailwater.utils.RWUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;

@Sharable
@Component
public class RWServerHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(RWServerHandler.class);
	
	/** 负载均衡累加数值 */
	private static final Integer BALANCE_STEP = 1;
	private BalanceUpdateProvider balanceUpdater;

	@Autowired
	private ChannelMap channelMap;
	
/*	@Autowired
	private OrderService orderService;
	
	@Autowired
	private EquipmentService equipmentService;*/
	
	@Autowired
	private RWProcessor processor;

	public void setBalanceUpdater(BalanceUpdateProvider balanceUpdater) {
		this.balanceUpdater = balanceUpdater;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		CommonMsg commonMsg = (CommonMsg) msg;
		try{
			if (!this.validCheckCode(commonMsg)){
				return;
			}
			switch (CommandType.getType(commonMsg.getCommand())) {
			case HEARTBEAT_REQT:
				HeartbeatReqt heartbeatReqt = (HeartbeatReqt) commonMsg;
				logger.info(ctx.channel().remoteAddress() + ": " + heartbeatReqt.toString());
				sendHeartbeatResp(ctx, heartbeatReqt);
				break;
			case STATUS_REQT:
				StatusReqt statusReqt = (StatusReqt) commonMsg;
				InetSocketAddress remoreAddress = (InetSocketAddress) ctx.channel()
	                        .remoteAddress();
				logger.info(remoreAddress.toString() + ": " + statusReqt.toString());
				EquipmentPo equipment = convertStatusReqt(statusReqt);
				equipment.setServerName(SysConfig.SERVER_NAME);
				equipment.setEquipIp(remoreAddress.getHostName());
				equipment.setEquipPort(remoreAddress.getPort());
				processor.handleStatus(equipment);
				String clientId = String.valueOf(statusReqt.getEquipmentNo());
				Channel channel = channelMap.get(clientId);
				if (channel == null || channel != ctx.channel()){
					channelMap.add(clientId, (SocketChannel) ctx.channel());
					//logger.debug(channelMap.toString());
				}
				sendStatusResp(ctx, statusReqt);
				break;
			case CONTROL_REQT:
				ControlReqt controlReqt = (ControlReqt) commonMsg;
				logger.info(ctx.channel().remoteAddress() + ": " + controlReqt.toString());
				OrderPo order = convertControlReqt(controlReqt);
				if (controlReqt.getAmount() == 0){
					sendControlResp(ctx,controlReqt);
					processor.handleICRechargeReqt(controlReqt, order);
					break;
				}
				if (controlReqt.getBeforeBalance() < controlReqt.getAmount()){
					logger.warn("Account balance lack,discard: " + ctx.channel().remoteAddress() + ": " + controlReqt.toString());
					break;
				}
				processor.handleOrder(controlReqt,order);
				sendControlResp(ctx,controlReqt);
				break;
			case CONTROL_RESP:
				ControlResp controlResp = (ControlResp) commonMsg;
				SocketChannel socketChannel = (SocketChannel) ctx.channel();
			    String equipmentNo = channelMap.get(socketChannel);
				processor.handleControlResp(controlResp, equipmentNo);
				logger.info(ctx.channel().remoteAddress() + ": " + controlResp.toString());
				break;
			case TIME_REQT:
				TimeReqt timeReqt = (TimeReqt) commonMsg;
				logger.info(ctx.channel().remoteAddress() + ": " + timeReqt.toString());
				sendTimeResp(ctx, timeReqt);
				break;
			case BUSINESS_NOTICE_RESP:
				BusinessNoticeResp businessNoticeResp = (BusinessNoticeResp) commonMsg;
				logger.info(ctx.channel().remoteAddress() + ": " + businessNoticeResp.toString());
			    socketChannel = (SocketChannel) ctx.channel();
			    equipmentNo = channelMap.get(socketChannel);
				processor.handleBusinessNoticeResp(equipmentNo,businessNoticeResp);
				break;
			case BUSINESS_NOTICE_REQT:
				BusinessNoticeReqt businessNoticeReqt = (BusinessNoticeReqt) commonMsg;
				logger.info(ctx.channel().remoteAddress() + ": " + businessNoticeReqt.toString());
				processor.handleBusinessNoticeReqt(businessNoticeReqt);
				sendBusinessNoticeResp(ctx,businessNoticeReqt);
				break;
			case GET_PACKAGE_REQT:
				GetPackageReqt getPackageReqt = (GetPackageReqt) commonMsg;
				logger.info(ctx.channel().remoteAddress() + ": " + getPackageReqt.toString());
				List<PackagePo> packList = processor.getPackageList();
				sendGetPackageResp(ctx,getPackageReqt,packList);
				break;
			case GET_PARAM_REQT:
				GetParamReqt getParamReqt = (GetParamReqt) commonMsg;
				logger.info(ctx.channel().remoteAddress() + ": " + getParamReqt.toString());
			    socketChannel = (SocketChannel) ctx.channel();
			    equipmentNo = channelMap.get(socketChannel);
			    if (StringUtils.isBlank(equipmentNo)){
			    	logger.warn("Unable to find equipment,discard: " + commonMsg.toString());
			    	break;
			    }
			    EquipmentPo equip = processor.getEquipmentById(equipmentNo);
			    if (equip == null){
			    	logger.warn("Unable to find equipment,discard: " + commonMsg.toString());
			    	break;
			    }
			    sendGetParamResp(ctx, getParamReqt, equip);
				break;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.error("server caught exception", cause);
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("---" + ctx.channel().remoteAddress() + " is active---");
		logger.debug(channelMap.toString());
		if (this.balanceUpdater != null){
			balanceUpdater.addBalance(BALANCE_STEP);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("---" + ctx.channel().remoteAddress() + " is inactive---");
		SocketChannel socketChannel = (SocketChannel) ctx.channel();
		String clientId = channelMap.get(socketChannel);
		if (StringUtils.isNotBlank(clientId)){
			processor.handleIDLE(clientId);
		}
		
		channelMap.remove((SocketChannel) ctx.channel());
		logger.debug(channelMap.toString());
		
		if (this.balanceUpdater != null){
			balanceUpdater.reduceBalance(BALANCE_STEP);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case READER_IDLE:
				break;
			case WRITER_IDLE:
				break;
			case ALL_IDLE:
				logger.info("---client " + ctx.channel().remoteAddress().toString() + " all timeout, close it---");
				SocketChannel channel = (SocketChannel) ctx.channel();
				String clientId = channelMap.get(channel);
/*				EquipmentPo po = new EquipmentPo();
				po.setEquipmentNo(clientId);
				EquipmentPo equipment = equipmentService.get(po);
				
				equipment.setNetworkStatus(NetworkStatus.CONNECTION_EXCEPT.getStatus());
				equipmentService.update(equipment);*/
				if (StringUtils.isNotBlank(clientId)){
					processor.handleIDLE(clientId);
				}
				channelMap.remove(channel);
				channel.close();
				logger.debug(channelMap.toString());
				
				if (this.balanceUpdater != null){
					balanceUpdater.reduceBalance(BALANCE_STEP);
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void sendHeartbeatResp(ChannelHandlerContext ctx,HeartbeatReqt reqt){
		HeartbeatResp heartbeatResp = new HeartbeatResp();
		heartbeatResp.setVersion(SysConfig.PROTOCOL_VERSION);
		heartbeatResp.setBackup(SysConfig.PROTOCOL_BACKUP);
		heartbeatResp.setDataFrameSeq(reqt.getDataFrameSeq());
		heartbeatResp.setCommand((short) CommandType.HEARTBEAT_RESP.getType());
		heartbeatResp.setData("00000000");
		ctx.writeAndFlush(heartbeatResp);
		logger.info(ctx.channel().remoteAddress() + ": " + heartbeatResp.toString());
	}
	
	private void sendStatusResp(ChannelHandlerContext ctx,StatusReqt reqt){
		StatusResp resp = new StatusResp();
		resp.setVersion(SysConfig.PROTOCOL_VERSION);
		resp.setBackup(SysConfig.PROTOCOL_BACKUP);
		resp.setDataFrameSeq(reqt.getDataFrameSeq());
		resp.setCommand((short) CommandType.STATUS_RESP.getType());
		ctx.writeAndFlush(resp);
		logger.info(ctx.channel().remoteAddress() + ": " + resp.toString());
	}
	
	private void sendControlResp(ChannelHandlerContext ctx,ControlReqt reqt){
		ControlResp resp = new ControlResp();
		resp.setVersion(SysConfig.PROTOCOL_VERSION);
		resp.setBackup(SysConfig.PROTOCOL_BACKUP);
		resp.setDataFrameSeq(reqt.getDataFrameSeq());
		resp.setCommand((short) CommandType.CONTROL_RESP.getType());
		ctx.writeAndFlush(resp);
		logger.info(ctx.channel().remoteAddress() + ": " + resp.toString());
	}
	
	private EquipmentPo convertStatusReqt(StatusReqt reqt){
		EquipmentPo equipment = new EquipmentPo();
		equipment.setEquipmentNo(String.valueOf(reqt.getEquipmentNo()));
		equipment.setInstallationNo(String.valueOf(reqt.getInstallationNo()));
		equipment.setOzoneWorkingTime(ByteUtils.unsignedByteToInt(reqt.getOzoneWorkingTime()));
		equipment.setUnitPrice(ByteUtils.unsignedByteToInt(reqt.getUnitPrice()));
		equipment.setTemperature(String.valueOf(ByteUtils.unsignedByteToInt(reqt.getTemperature())));
		equipment.setHumidity(String.valueOf(ByteUtils.unsignedByteToInt(reqt.getHumidity())));
		equipment.setRawWaterTds(String.valueOf(reqt.getRawWaterTds()));
		equipment.setPurifyWaterTds(String.valueOf(reqt.getPurifyWaterTds()));

		String workStatus = RWUtils.convertToBinaryString(reqt.getWorkStatus());
		int bit0 = Integer.valueOf(Character.toString(workStatus.charAt(7)));
		int bit1 = Integer.valueOf(Character.toString(workStatus.charAt(6)));
		int bit2 = Integer.valueOf(Character.toString(workStatus.charAt(5)));
		int bit3 = Integer.valueOf(Character.toString(workStatus.charAt(4)));
		int bit4 = Integer.valueOf(Character.toString(workStatus.charAt(3)));
		switch (BinaryCode.getCode(bit0)){
		case ZERO:
			equipment.setRunStatus(RunStatus.OFF.getStatus());
			break;
		case ONE:
			equipment.setRunStatus(RunStatus.ON.getStatus());
			break;
		}
		
		switch (BinaryCode.getCode(bit1)){
		case ZERO:
			equipment.setWorkStatus(WorkStatus.NOT_SELL_WATER.getStatus());
			break;
		case ONE:
			equipment.setWorkStatus(WorkStatus.CAN_SELL_WATER.getStatus());
			break;
		}
		
		switch (BinaryCode.getCode(bit4)){
		case ZERO:
			equipment.setProduceStatus(ProduceStatus.NO.getStatus());
			break;
		case ONE:
			equipment.setProduceStatus(ProduceStatus.PRODUCING.getStatus());
			break;
		}
		
		switch (BinaryCode.getCode(bit2)){
		case ZERO:
			equipment.setRawWaterStatus(RawWaterStatus.NORMAL.getStatus());
			break;
		case ONE:
			equipment.setRawWaterStatus(RawWaterStatus.LACK_WATER.getStatus());
			break;
		}
		
		switch (BinaryCode.getCode(bit3)){
		case ZERO:
			equipment.setWaterLevel(WaterLevel.NORMAL.getLevel());
			break;
		case ONE:
			equipment.setWaterLevel(WaterLevel.WATER_FULL.getLevel());
			break;
		}
		
		equipment.setProduceWaterQuantity(Short.valueOf(reqt.getProduceWaterQuantity()).doubleValue());
		equipment.setParamStatus(ParamStatus.NORMAL.getStatus());
		return equipment;

	}
	
	private OrderPo convertControlReqt(ControlReqt reqt){
		OrderPo order = new OrderPo();
		
		switch (ConsumeType.getType(reqt.getConsumeType())){
		case IC_CARD_PAY:
			order.setType(OrderType.CONSUME.getType());
			order.setSource(OrderSource.IC.getSource());
			break;
		case APP_SCAN_CODE:
			order.setType(OrderType.CONSUME.getType());
			order.setSource(OrderSource.MSB.getSource());
			break;
		case IC_CARD_RECHARGE:
			order.setType(OrderType.RECHARGE.getType());
			order.setSource(OrderSource.IC.getSource());
			break;
		default:
			return null;
		}
		
		order.setOrderNo(StringUtils.generateOrderNo());
		order.setStatus(OrderStatus.PAYED_SUCCESS.getStatus());
		order.setAmount(CurrencyUtils.div(reqt.getAmount(), 100));
		order.setWaterFee(order.getAmount());
		order.setSaleWaterQuantity(Integer.valueOf(reqt.getProduceWaterQuantity()).doubleValue());
		order.setPayFee(0.00);
		order.setGiveFee(0.00);
		Date currentTime = new Date();
		order.setPayTime(currentTime);
		order.setBeforeBalance(CurrencyUtils.div(reqt.getBeforeBalance(), 100));
		order.setPayBalance(CurrencyUtils.div(reqt.getAfterBalance(), 100));
		order.setGiveBalance(0.00);
		order.setEquipmentNo(String.valueOf(reqt.getEquipmentNo()));
		StringBuffer accountBuffer = new StringBuffer();
		String cardType = Integer.valueOf(reqt.getCardType()).toString();
		if (cardType.length() < 2){
			cardType = "0" + cardType;
		}
		accountBuffer.append(cardType);
		String cardNumber = String.valueOf(reqt.getCardNumber());
		if (cardNumber.length() < 8){
			int len = 8 - cardNumber.length();
			for (int i = 0; i < len; i++){
				cardNumber = "0" + cardNumber;
			}
		}
		accountBuffer.append(cardNumber);
		order.setAccount(accountBuffer.toString());
		order.setCreateTime(currentTime);
		return order;
	}
	
	private boolean validCheckCode(CommonMsg commonMsg){
		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
		try{
			//buf.writeByte(SysConfig.PROTOCOL_START_FLAG);
			buf.writeShort(commonMsg.getLength());
			commonMsg.build(buf);
			
			byte[] dst = new byte[commonMsg.getLength() - 2];
			buf.readBytes(dst);
			//logger.info(HexUtils.bytes2HexString(dst));
			byte[] checkCodeData = RWUtils.crcCalculateByte(dst.length ,dst);
			String hexCheckCode = HexUtils.bytes2HexString(checkCodeData).toUpperCase();
			if (commonMsg.getCheckCode().equalsIgnoreCase(hexCheckCode)){
				return true;
			}
			logger.warn("Check code error, calculated check code:" + hexCheckCode + ", discard: " + commonMsg.toString());
			
		}catch (Exception e){
			logger.error("Check code exception ocurred, cause by:{}", e);
		} finally{
			if (buf != null){
				buf.release();
			}
		}
		return false;
	}
	
	private void sendTimeResp(ChannelHandlerContext ctx, TimeReqt reqt){
		TimeResp resp = new TimeResp();
		resp.setVersion(SysConfig.PROTOCOL_VERSION);
		resp.setBackup(SysConfig.PROTOCOL_BACKUP);
		resp.setDataFrameSeq(reqt.getDataFrameSeq());
		resp.setCommand((short) CommandType.TIME_RESP.getType());
		
		resp.setFormat(reqt.getFormat());
		switch (TimeFormat.getFormat(reqt.getFormat())){
		case GMT:
			//设置基础时间为格林威治时间
			TimeZone gmtTz = TimeZone.getTimeZone("GMT");
			GregorianCalendar gCalendar = new GregorianCalendar(gmtTz);
			Date date = gCalendar.getTime();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yy");
	        int year = Integer.valueOf(dateFormat.format(date));
	        int month = gCalendar.get(Calendar.MONTH) + 1;
	        int day = gCalendar.get(Calendar.DATE);
	        int hour = gCalendar.get(Calendar.HOUR_OF_DAY);
	        int minute = gCalendar.get(Calendar.MINUTE);
	        int second = gCalendar.get(Calendar.SECOND);
	        int week = gCalendar.get(Calendar.DAY_OF_WEEK);
	        int timezone = gCalendar.get(Calendar.ZONE_OFFSET)/(60*60*1000);

	        resp.setYear((byte) year);
	        resp.setMonth((byte) month);
	        resp.setDay((byte) day);
	        resp.setHour((byte) hour);
	        resp.setMinute((byte) minute);
	        resp.setSecond((byte) second);
	        resp.setWeek((byte) week);
	        resp.setTimezone((byte) timezone);
	        resp.setReserve1((byte) 0x00);
	        resp.setReserve2((byte) 0x00);
	        resp.setReserve3((byte) 0x00);
	        resp.setReserve4((byte) 0x00);
	        resp.setReserve5((byte) 0x00);
	        resp.setReserve6((byte) 0x00);
	        resp.setReserve7((byte) 0x00);
			break;
		case CHINA:
	        Calendar calendar = Calendar.getInstance(Locale.CHINA);
	        date = calendar.getTime();
	        dateFormat = new SimpleDateFormat("yy");
	        year = Integer.valueOf(dateFormat.format(date));
	        month = calendar.get(Calendar.MONTH) + 1;
	        day = calendar.get(Calendar.DATE);
	        hour = calendar.get(Calendar.HOUR_OF_DAY);
	        minute = calendar.get(Calendar.MINUTE);
	        second = calendar.get(Calendar.SECOND);
	        week = calendar.get(Calendar.DAY_OF_WEEK);
	        timezone = calendar.get(Calendar.ZONE_OFFSET)/(60*60*1000);

	        resp.setYear((byte) year);
	        resp.setMonth((byte) month);
	        resp.setDay((byte) day);
	        resp.setHour((byte) hour);
	        resp.setMinute((byte) minute);
	        resp.setSecond((byte) second);
	        resp.setWeek((byte) week);
	        resp.setTimezone((byte) timezone);
	        resp.setReserve1((byte) 0x00);
	        resp.setReserve2((byte) 0x00);
	        resp.setReserve3((byte) 0x00);
	        resp.setReserve4((byte) 0x00);
	        resp.setReserve5((byte) 0x00);
	        resp.setReserve6((byte) 0x00);
	        resp.setReserve7((byte) 0x00);
			break;
		}

		ctx.writeAndFlush(resp);
		logger.info(ctx.channel().remoteAddress() + ": " + resp.toString());
	}
	
	private void sendBusinessNoticeResp(ChannelHandlerContext ctx,BusinessNoticeReqt reqt){
		BusinessNoticeResp resp = new BusinessNoticeResp();
		resp.setVersion(SysConfig.PROTOCOL_VERSION);
		resp.setBackup(SysConfig.PROTOCOL_BACKUP);
		resp.setDataFrameSeq(reqt.getDataFrameSeq());
		resp.setCommand((short) CommandType.BUSINESS_NOTICE_RESP.getType());
		ctx.writeAndFlush(resp);
		logger.info(ctx.channel().remoteAddress() + ": " + resp.toString());
	}
	
	private void sendGetPackageResp(ChannelHandlerContext ctx,GetPackageReqt reqt, List<PackagePo> packList){
		GetPackageResp resp = new GetPackageResp();
		resp.setVersion(SysConfig.PROTOCOL_VERSION);
		resp.setBackup(SysConfig.PROTOCOL_BACKUP);
		resp.setDataFrameSeq(reqt.getDataFrameSeq());
		resp.setCommand((short) CommandType.GET_PACKAGE_RESP.getType());
		PackagePo pack1 = packList.size() >= 1 ? packList.get(0) : null;
		if (pack1 != null){
			Double payFee1 = CurrencyUtils.mul(pack1.getAmount(), 100);
			resp.setPayFee1(payFee1.intValue());
			if (pack1.getActivity() != null){
				Double giveFee1 = CurrencyUtils.mul(pack1.getActivity().getGiveFee(), 100);
				resp.setGiveFee1(giveFee1.intValue());
			}else{
				resp.setGiveFee1(0x00);
			}
		}else{
			resp.setPayFee1(0x00);
			resp.setGiveFee1(0x00);
		}
		
		PackagePo pack2 = packList.size() >= 2 ? packList.get(1) : null;
		if (pack2 != null){
			Double payFee2 = CurrencyUtils.mul(pack2.getAmount(), 100);
			resp.setPayFee2(payFee2.intValue());
			if (pack2.getActivity() != null){
				Double giveFee2 = CurrencyUtils.mul(pack2.getActivity().getGiveFee(), 100);
				resp.setGiveFee2(giveFee2.intValue());
			}else{
				resp.setGiveFee2(0x00);
			}
		}else{
			resp.setPayFee2(0x00);
			resp.setGiveFee2(0x00);
		}
		
		PackagePo pack3 = packList.size() >= 3 ? packList.get(2) : null;
		if (pack3 != null){
			Double payFee3 = CurrencyUtils.mul(pack3.getAmount(), 100);
			resp.setPayFee3(payFee3.intValue());
			if (pack3.getActivity() != null){
				Double giveFee3 = CurrencyUtils.mul(pack3.getActivity().getGiveFee(), 100);
				resp.setGiveFee3(giveFee3.intValue());
			}else{
				resp.setGiveFee3(0x00);
			}
		}else{
			resp.setPayFee3(0x00);
			resp.setGiveFee3(0x00);
		}
		
		PackagePo pack4 = packList.size() >= 4 ? packList.get(3) : null;
		if (pack4 != null){
			Double payFee4 = CurrencyUtils.mul(pack4.getAmount(), 100);
			resp.setPayFee4(payFee4.intValue());
			if (pack4.getActivity() != null){
				Double giveFee4 = CurrencyUtils.mul(pack4.getActivity().getGiveFee(), 100);
				resp.setGiveFee4(giveFee4.intValue());
			}else{
				resp.setGiveFee4(0x00);
			}
		}else{
			resp.setPayFee4(0x00);
			resp.setGiveFee4(0x00);
		}
		
		PackagePo pack5 = packList.size() >= 5 ? packList.get(4) : null;
		if (pack5 != null){
			Double payFee5 = CurrencyUtils.mul(pack5.getAmount(), 100);
			resp.setPayFee5(payFee5.intValue());
			if (pack5.getActivity() != null){
				Double giveFee5 = CurrencyUtils.mul(pack5.getActivity().getGiveFee(), 100);
				resp.setGiveFee5(giveFee5.intValue());
			}else{
				resp.setGiveFee5(0x00);
			}
		}else{
			resp.setPayFee5(0x00);
			resp.setGiveFee5(0x00);
		}
		
		ctx.writeAndFlush(resp);
		logger.info(ctx.channel().remoteAddress() + ": " + resp.toString());
	}
	
	private void sendGetParamResp(ChannelHandlerContext ctx,GetParamReqt reqt, EquipmentPo equip){
		GetParamResp resp = new GetParamResp();
		resp.setVersion(SysConfig.PROTOCOL_VERSION);
		resp.setBackup(SysConfig.PROTOCOL_BACKUP);
		resp.setDataFrameSeq(reqt.getDataFrameSeq());
		resp.setCommand((short) CommandType.GET_PARAM_RESP.getType());
		resp.setEquipmentNo(Integer.valueOf(equip.getEquipmentNo()));
		resp.setUnitPrice(equip.getUnitPrice().byteValue());
		resp.setOutWaterTime(equip.getOutWaterTime().byteValue());
		resp.setOutWaterQuantity(equip.getOutWaterQuantity().byteValue());
		ctx.writeAndFlush(resp);
		logger.info(ctx.channel().remoteAddress() + ": " + resp.toString());
	}
}
