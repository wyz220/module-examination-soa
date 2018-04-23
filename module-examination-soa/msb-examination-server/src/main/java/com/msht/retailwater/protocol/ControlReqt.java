/**
 * 
 */
package com.msht.retailwater.protocol;

import com.msht.retailwater.common.RWByteBuf;
import com.msht.retailwater.utils.ByteUtils;
import com.msht.retailwater.utils.HexUtils;

import io.netty.buffer.ByteBuf;

/**
 * @author lindaofen
 *
 */
public class ControlReqt extends AbstractCommonMsg {

	/** 设备号,设备号为 8 位数字，占 4byte，如设备号： 12345678  十六进制为 0x00BC614E */
	private int equipmentNo;
	/** 卡号类型，卡号为 10 位数字，如 99—87654321。 前两位数字表示卡类型，1 字节表示；后 8 位数字表示卡号流水号，4 字节 */
	private byte cardType;
	/** 卡号流水号 */
	private int cardNumber;
	
	/** 当前触发的日期时间,当前触发的日期时间，可用来表示如当前 消费日期或重置滤芯时间等，时间用 6 字 节，如：
160512   18:30:00
 */
	private byte[] triggerTime;
	
	/** 消费类型,0x01  、刷卡，0x02   投币，0x03   民生App扫码打水，0x04 IC卡充值*/
	private byte consumeType;
	
	/** 消费前余额，消费前卡内余额用 2 字节表示，以分为单位，如消费前卡内余额 600 元，即 60000分，16 进制为 0XEA60 */
	private int beforeBalance;
	
	/** 本次消费金额类型：（主要用于记录因误操作导致的误扣费） 0x01 表示下方的本次消费金额【】为正 值，0x02   表示下方的本次消费金额【】为负值 */
	private byte amountType;
	
	/** 本次消费金额： 本次消费金额用 2 字节表示，以分为单位，最高位代表如本次消费 1.5 元，即 150 分，16 进制为 0X0096 */
	private int amount;
	
	/** 消费后余额：消费后卡内余额用 2 字节表示，以分为单位，如消费后卡内余额 598.5 元，即 59850分，16 进制为 0XE9CA*/
	private int afterBalance;
	
	/** 本次打水水量：设备上传本次打水水量，单位为 ml,如本次打水 1L，即 1000ml, 16 进制为 0X03E8(实际为打水时间) */
	private int produceWaterQuantity;
	
	/** 充值额度：主要用于服务器发送充值指令给设备，以分为单位。 */
	private int rechargeAmount;
	
	/** 修改安装号 */
	private short installationNo;
	
	/** 修改单价,1 元人民币打水 20S 到 99S 可调，如 20S为 0x14 */
	private byte unitPrice;
	
	/** 1~120S 可调，如 120S 为 */
	private byte ozoneWorkingTime;

	/** 开关键设置：0x01 开机、0x02 关机 */
	private byte switchMachineKeySet;
	
	/** 取水使能设置：0x01 允许取水，0x02 禁止取水 */
	private byte getWaterSet;
	
	/** 滤芯重置设置：0x01 滤芯正常，0x02 滤芯重置 */
	private byte filterResetSet;
	
	/** 滤芯重置级别：1~9 级滤芯 */
	private byte filterResetLevel;
	
	/** 预扣金额设置 */
	private short preDeductAmountSet;
	
	/** 黑名单卡标记：0x01 正常卡0x02 黑名单卡*/
	private byte blackCardFlag;
	
	/** 按键代码：0x01 罐装按键，0x02 停止按键，0x04 第3按键，0x08 第4按键*/
	private byte buttonCode;
	
	/** 保留 */
	private byte reserve1;
	private byte reserve2;
	private byte reserve3;
	
	/*
	 * 每一位对应一个字节数据，0 代表无变化,1 代表有变化：
	 *  第 0 位：设备号【0】 
	 *  第 1 位：设备号【1】 
	 *  第 2 位：设备号【2】
	 *  第 3位：设备号【3】
	 *  第 4 位：卡号类型
	 *  第 5 位：卡号流水号【0】
                        第 6 位：卡号流水号【1】 
                        第 7 位：卡号流水号【2】
	 * 
	 */
	private byte updateflag1;
	
	/*
	 * 每一位对应一个字节数据，0 代表无变化
1 代表有变化：
第 0 位：卡号流水号【3】
第 1 位：当前触发的日期时间【0】
第 2 位：当前触发的日期时间【1】
第 3 位：当前触发的日期时间【2】
第 4 位：当前触发的日期时间【3】
第 5 位：当前触发的日期时间【4】
第 6 位：当前触发的日期时间【5】
第 7 位：消费类型

	 */
	private byte updateflag2;
	
	/*
	 * 每一位对应一个字节数据，0 代表无变化
1 代表有变化：
第 0 位：消费前余额【0】
第 1 位：消费前余额【1】
第 2 位：本次消费金额类型
第 3 位：本次消费金额【0】
第 4 位：本次消费金额【1】
第 5 位：消费后余额【0】
第 6 位：消费后余额【1】
第 7 位：本次打水水量【0】

	 */
	private byte updateflag3;
	
	/*
	 * 每一位对应一个字节数据，0 代表无变化
1 代表有变化：
第 0 位：本次打水水量【1】
第 1 位：充值额度【0】
第 2 位：充值额度【1】
第 3 位：修改安装号【0】
第 4 位：修改安装号【1】
第 5 位：修改单价
第 6 位：修改臭氧工作时间
第 7 位：开关键设置

	 */
	private byte updateflag4;
	
	/*
	 * 每一位对应一个字节数据，0 代表无变化
1 代表有变化：
第 0 位：取水使能设置
第 1 位：滤芯重置设置
第 2 位：滤芯重置级别
第 3 位：预扣金额设置【0】
第 4 位：预扣金额设置【1】
第 5 位：黑卡标记
第 6 位：有按键标记
第 7 位：保留
	 */
	private byte updateflag5;
	
	/*
	 * 每一位对应一个字节数据，0 代表无变化
1 代表有变化：
第 0 位：保留
第 1 位：保留
第 2 位：0
第 3 位：0
第 4 位：0
第 5 位：0
第 6 位：0
第 7 位：0

	 */
	private byte updateflag6;
	
	
	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 62;
	}
	
	@Override
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.CONTROL_REQT.getType());
		buf.writeInt(this.equipmentNo);
		buf.writeByte(this.cardType);
		buf.writeInt(this.cardNumber);
		buf.writeBytes(this.triggerTime);
		buf.writeByte(this.consumeType);
		buf.writeShort(this.beforeBalance);
		buf.writeByte(this.amountType);
		buf.writeShort(this.amount);
		buf.writeShort(this.afterBalance);
		buf.writeShort(this.produceWaterQuantity);
		buf.writeShort(this.rechargeAmount);
		buf.writeShort(this.installationNo);
		buf.writeByte(this.unitPrice);
		buf.writeByte(this.ozoneWorkingTime);
		buf.writeByte(this.switchMachineKeySet);
		buf.writeByte(this.getWaterSet);
		buf.writeByte(this.filterResetSet);
		buf.writeByte(this.filterResetLevel);
		buf.writeShort(this.preDeductAmountSet);
		buf.writeByte(this.blackCardFlag);
		buf.writeByte(this.buttonCode);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve2);
		buf.writeByte(this.reserve3);
		buf.writeByte(this.updateflag1);
		buf.writeByte(this.updateflag2);
		buf.writeByte(this.updateflag3);
		buf.writeByte(this.updateflag4);
		buf.writeByte(this.updateflag5);
		buf.writeByte(this.updateflag6);
	}

	@Override
	public void mergeContent(RWByteBuf buf) {
		
		this.equipmentNo = buf.getBuf().readInt();
		this.cardType = buf.getBuf().readByte();
		this.cardNumber = buf.getBuf().readInt();
		this.triggerTime = new byte[6];
		buf.getBuf().readBytes(triggerTime);
		this.consumeType = buf.getBuf().readByte();
		this.beforeBalance = buf.getBuf().readUnsignedShort();
		this.amountType = buf.getBuf().readByte();
		this.amount = buf.getBuf().readUnsignedShort();
		this.afterBalance = buf.getBuf().readUnsignedShort();
		this.produceWaterQuantity = buf.getBuf().readUnsignedShort();
		this.rechargeAmount = buf.getBuf().readUnsignedShort();
		this.installationNo = buf.getBuf().readShort();
		this.unitPrice = buf.getBuf().readByte();
		this.ozoneWorkingTime = buf.getBuf().readByte();
		this.switchMachineKeySet = buf.getBuf().readByte();
		this.getWaterSet = buf.getBuf().readByte();
		this.filterResetSet = buf.getBuf().readByte();
		this.filterResetLevel = buf.getBuf().readByte();
		this.preDeductAmountSet = buf.getBuf().readShort();
		this.blackCardFlag = buf.getBuf().readByte();
		this.buttonCode = buf.getBuf().readByte();
		this.reserve1 = buf.getBuf().readByte();
		this.reserve2 = buf.getBuf().readByte();
		this.reserve3 = buf.getBuf().readByte();
		
		this.updateflag1 = buf.getBuf().readByte();
		this.updateflag2 = buf.getBuf().readByte();
		this.updateflag3 = buf.getBuf().readByte();
		this.updateflag4 = buf.getBuf().readByte();
		this.updateflag5 = buf.getBuf().readByte();
		this.updateflag6 = buf.getBuf().readByte();
	}
	
	@Override
	public String toString() {
		return "ControlReqt [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.command)) 
		+ ",equipmentNo=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.equipmentNo)) 
		+ ",cardType=" + HexUtils.appendPrefixHex(new byte[]{this.cardType})
		+ ",cardNumber=" + this.cardNumber 
		+ ",triggerTime=" + HexUtils.appendPrefixHex(this.triggerTime)
		+ ",consumeType=" + HexUtils.appendPrefixHex(new byte[]{this.consumeType})
		
		+ ",beforeBalance=" + this.beforeBalance
		+ ",amount=" + this.amount
		+ ",afterBalance=" + this.afterBalance
		+ ",produceWaterQuantity=" + this.produceWaterQuantity
		+ ",rechargeAmount=" + this.rechargeAmount
		+ ",installationNo=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.installationNo))
		+ ",unitPrice=" + HexUtils.appendPrefixHex(new byte[]{this.unitPrice})
		+ ",ozoneWorkingTime=" + HexUtils.appendPrefixHex(new byte[]{this.ozoneWorkingTime})
		+ ",switchMachineKeySet=" + HexUtils.appendPrefixHex(new byte[]{this.switchMachineKeySet})
		+ ",getWaterSet=" + HexUtils.appendPrefixHex(new byte[]{this.getWaterSet})
		+ ",filterResetSet=" + HexUtils.appendPrefixHex(new byte[]{this.filterResetSet})
		+ ",filterResetLevel=" + HexUtils.appendPrefixHex(new byte[]{this.filterResetLevel})
		+ ",preDeductAmountSet=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.preDeductAmountSet))
		+ ",blackCardFlag=" + HexUtils.appendPrefixHex(new byte[]{this.blackCardFlag})
		+ ",buttonCode=" + HexUtils.appendPrefixHex(new byte[]{this.buttonCode})
		+ ",reserve1=" + HexUtils.appendPrefixHex(new byte[]{this.reserve1})
		+ ",reserve2=" + HexUtils.appendPrefixHex(new byte[]{this.reserve2})
		+ ",reserve3=" + HexUtils.appendPrefixHex(new byte[]{this.reserve3})
		+ ",updateflag1=" + HexUtils.appendPrefixHex(new byte[]{this.updateflag1})
		+ ",updateflag2=" + HexUtils.appendPrefixHex(new byte[]{this.updateflag2})
		+ ",updateflag3=" + HexUtils.appendPrefixHex(new byte[]{this.updateflag3})
		+ ",updateflag4=" + HexUtils.appendPrefixHex(new byte[]{this.updateflag4})
		+ ",updateflag5=" + HexUtils.appendPrefixHex(new byte[]{this.updateflag5})
		+ ",updateflag6=" + HexUtils.appendPrefixHex(new byte[]{this.updateflag6})
		
		+ ",checkCode=" + this.checkCode + "]";
	}

	public int getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(int equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public byte getCardType() {
		return cardType;
	}

	public void setCardType(byte cardType) {
		this.cardType = cardType;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public byte[] getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(byte[] triggerTime) {
		this.triggerTime = triggerTime;
	}

	public byte getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(byte consumeType) {
		this.consumeType = consumeType;
	}

	public int getBeforeBalance() {
		return beforeBalance;
	}

	public void setBeforeBalance(int beforeBalance) {
		this.beforeBalance = beforeBalance;
	}

	public byte getAmountType() {
		return amountType;
	}

	public void setAmountType(byte amountType) {
		this.amountType = amountType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAfterBalance() {
		return afterBalance;
	}

	public void setAfterBalance(int afterBalance) {
		this.afterBalance = afterBalance;
	}

	public int getProduceWaterQuantity() {
		return produceWaterQuantity;
	}

	public void setProduceWaterQuantity(int produceWaterQuantity) {
		this.produceWaterQuantity = produceWaterQuantity;
	}

	public int getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(int rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public short getInstallationNo() {
		return installationNo;
	}

	public void setInstallationNo(short installationNo) {
		this.installationNo = installationNo;
	}

	public byte getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(byte unitPrice) {
		this.unitPrice = unitPrice;
	}

	public byte getOzoneWorkingTime() {
		return ozoneWorkingTime;
	}

	public void setOzoneWorkingTime(byte ozoneWorkingTime) {
		this.ozoneWorkingTime = ozoneWorkingTime;
	}

	public byte getSwitchMachineKeySet() {
		return switchMachineKeySet;
	}

	public void setSwitchMachineKeySet(byte switchMachineKeySet) {
		this.switchMachineKeySet = switchMachineKeySet;
	}

	public byte getGetWaterSet() {
		return getWaterSet;
	}

	public void setGetWaterSet(byte getWaterSet) {
		this.getWaterSet = getWaterSet;
	}

	public byte getFilterResetSet() {
		return filterResetSet;
	}

	public void setFilterResetSet(byte filterResetSet) {
		this.filterResetSet = filterResetSet;
	}

	public byte getFilterResetLevel() {
		return filterResetLevel;
	}

	public void setFilterResetLevel(byte filterResetLevel) {
		this.filterResetLevel = filterResetLevel;
	}

	public short getPreDeductAmountSet() {
		return preDeductAmountSet;
	}

	public void setPreDeductAmountSet(short preDeductAmountSet) {
		this.preDeductAmountSet = preDeductAmountSet;
	}

	public byte getBlackCardFlag() {
		return blackCardFlag;
	}

	public void setBlackCardFlag(byte blackCardFlag) {
		this.blackCardFlag = blackCardFlag;
	}

	public byte getButtonCode() {
		return buttonCode;
	}

	public void setButtonCode(byte buttonCode) {
		this.buttonCode = buttonCode;
	}

	public byte getReserve1() {
		return reserve1;
	}

	public void setReserve1(byte reserve1) {
		this.reserve1 = reserve1;
	}

	public byte getReserve2() {
		return reserve2;
	}

	public void setReserve2(byte reserve2) {
		this.reserve2 = reserve2;
	}

	public byte getReserve3() {
		return reserve3;
	}

	public void setReserve3(byte reserve3) {
		this.reserve3 = reserve3;
	}

	public byte getUpdateflag1() {
		return updateflag1;
	}

	public void setUpdateflag1(byte updateflag1) {
		this.updateflag1 = updateflag1;
	}

	public byte getUpdateflag2() {
		return updateflag2;
	}

	public void setUpdateflag2(byte updateflag2) {
		this.updateflag2 = updateflag2;
	}

	public byte getUpdateflag3() {
		return updateflag3;
	}

	public void setUpdateflag3(byte updateflag3) {
		this.updateflag3 = updateflag3;
	}

	public byte getUpdateflag4() {
		return updateflag4;
	}

	public void setUpdateflag4(byte updateflag4) {
		this.updateflag4 = updateflag4;
	}

	public byte getUpdateflag5() {
		return updateflag5;
	}

	public void setUpdateflag5(byte updateflag5) {
		this.updateflag5 = updateflag5;
	}

	public byte getUpdateflag6() {
		return updateflag6;
	}

	public void setUpdateflag6(byte updateflag6) {
		this.updateflag6 = updateflag6;
	}
	
	public enum ConsumeType{
		IC_CARD_PAY(0x01), APP_SCAN_CODE(0x03),IC_CARD_RECHARGE(0x04),GET_WATER(0x05),OTHER(0x99);
		
		private int type;
		
		private ConsumeType(int type){
			this.type = type;
		}

		public int getType() {
			return type;
		}
		
		public static ConsumeType getType(int type) {
			switch (type){
			case 0x01:
				return IC_CARD_PAY;
			case 0x03:
				return APP_SCAN_CODE;
			case 0x04:
				return IC_CARD_RECHARGE;
			case 0x05:
				return GET_WATER;
			}
			return OTHER;
		}
	}
	
	public enum SwitchMachineKeySet{
		ON(0x01), OFF(0x02);
		
		private int type;
		
		private SwitchMachineKeySet(int type){
			this.type = type;
		}

		public int getType() {
			return type;
		}
		
		public static SwitchMachineKeySet getType(int type) {
			switch (type){
			case 0x01:
				return ON;
			case 0x02:
				return OFF;
			}
			return ON;
		}
	}

	
}
