/**
 * 
 */
package com.msht.retailwater.protocol;

import com.msht.retailwater.common.RWByteBuf;
import com.msht.retailwater.mq.ControlMessage.MessageType;
import com.msht.retailwater.utils.ByteUtils;
import com.msht.retailwater.utils.HexUtils;

import io.netty.buffer.ByteBuf;

/**
 * @author hei
 *
 */
public class BusinessNoticeReqt extends AbstractCommonMsg {
	
	/** 业务类型 */
	private byte type;

	private long account;
	
	/** 消费前余额，消费前卡内余额用 2 字节表示，以分为单位，如消费前卡内余额 600 元，即 60000分，16 进制为 0XEA60 */
	private int beforeBalance;
	
	/** 本次消费金额： 本次消费金额用 2 字节表示，以分为单位，最高位代表如本次消费 1.5 元，即 150 分，16 进制为 0X0096 */
	private int amount;
	
	/** 消费后余额：消费后卡内余额用 2 字节表示，以分为单位，如消费后卡内余额 598.5 元，即 59850分，16 进制为 0XE9CA*/
	private int afterBalance;
	
	private int equipmentNo;
	
	/** 本机单价,1 元人民币打水 20S 到 99S 可调，如 20S为 0x14 */
	private byte unitPrice;
	
	/** 设备输水时间（单位：秒） */
	private byte outWaterTime;
	
	/** 设备输水量（单位：升） */
	private byte outWaterQuantity;
	
	/** 用户购水时间（单位：秒） */
	private int totalOutWaterTime;
	
	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
		this.account = account;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getBeforeBalance() {
		return beforeBalance;
	}

	public void setBeforeBalance(int beforeBalance) {
		this.beforeBalance = beforeBalance;
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
	
	public int getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(int equipmentNo) {
		this.equipmentNo = equipmentNo;
	}
	
	

	public byte getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(byte unitPrice) {
		this.unitPrice = unitPrice;
	}

	public byte getOutWaterTime() {
		return outWaterTime;
	}

	public void setOutWaterTime(byte outWaterTime) {
		this.outWaterTime = outWaterTime;
	}

	public byte getOutWaterQuantity() {
		return outWaterQuantity;
	}

	public void setOutWaterQuantity(byte outWaterQuantity) {
		this.outWaterQuantity = outWaterQuantity;
	}
	
	public int getTotalOutWaterTime() {
		return totalOutWaterTime;
	}

	public void setTotalOutWaterTime(int totalOutWaterTime) {
		this.totalOutWaterTime = totalOutWaterTime;
	}

	@Override
	public int getLength() {
		return 44;
	}

	@Override
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.BUSINESS_NOTICE_REQT.getType());
		buf.writeByte(this.type);
		buf.writeLong(this.account);
		buf.writeInt(this.beforeBalance);
		buf.writeInt(this.amount);
		buf.writeInt(this.afterBalance);
		buf.writeInt(this.equipmentNo);
		buf.writeByte(this.unitPrice);
		buf.writeByte(this.outWaterTime);
		buf.writeByte(this.outWaterQuantity);
		buf.writeShort(this.totalOutWaterTime);
	}

	@Override
	public void mergeContent(RWByteBuf buf) {
		this.type = buf.getBuf().readByte();
		this.account = buf.getBuf().readLong();
		this.beforeBalance = buf.getBuf().readInt();
		this.amount = buf.getBuf().readInt();
		this.afterBalance = buf.getBuf().readInt();
		this.equipmentNo = buf.getBuf().readInt();
		this.unitPrice = buf.getBuf().readByte();
		this.outWaterTime = buf.getBuf().readByte();
		this.outWaterQuantity = buf.getBuf().readByte();
		this.totalOutWaterTime = buf.getBuf().readUnsignedShort();
	}

	@Override
	public String toString() {
		return "BusinessNoticeReqt [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.command)) 
		+ ",type=" + HexUtils.appendPrefixHex(new byte[]{this.type}) 
		+ ",account=" + this.account
		+ ",beforeBalance=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.beforeBalance)) 
		+ ",amount=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.amount)) 
		+ ",afterBalance=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.afterBalance)) 
		+ ",equipmentNo=" + this.equipmentNo
		+ ",unitPrice=" + HexUtils.appendPrefixHex(new byte[]{this.unitPrice}) 
		+ ",outWaterTime=" + HexUtils.appendPrefixHex(new byte[]{this.outWaterTime}) 
		+ ",outWaterQuantity=" + HexUtils.appendPrefixHex(new byte[]{this.outWaterQuantity}) 
		+ ",totalOutWaterTime=" + this.totalOutWaterTime 
		+ ",checkCode=" + this.checkCode + "]";
	}
	
	public enum BusinessType {
		APP_SCAN_NOTICE(0x01),GET_WATER_NOTICE(0x02), IC_RECHARGE_NOTICE(0x03), OTHER(99);

		private int type;

		private BusinessType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}

		public static BusinessType getType(int type) {
			switch (type) {
			case 0x01:
				return APP_SCAN_NOTICE;
			case 0x02:
				return GET_WATER_NOTICE;
			case 0x03:
				return IC_RECHARGE_NOTICE;
			}
			
			return OTHER;
		}
	}
}
