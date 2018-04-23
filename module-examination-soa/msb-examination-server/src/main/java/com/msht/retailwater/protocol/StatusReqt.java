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
public class StatusReqt extends AbstractCommonMsg {

	/** 设备号,设备号为 8 位数字，占 4byte，如设备号： 12345678  十六进制为 0x00BC614E */
	private int equipmentNo;
	/** 安装号 */
	private short installationNo;
	/** 臭氧工作时间,1~120S 可调，如 120S 为 0x78 */
	private byte ozoneWorkingTime;
	
	/** 本机单价,1 元人民币打水 20S 到 99S 可调，如 20S为 0x14 */
	private byte unitPrice;
	
	/** 本机当前湿度 */
	private byte humidity;
	/** 本机当前温度 */
	private byte temperature;
	
	/** 原水 TDS */
	private short rawWaterTds;
	/** 净水 TDS */
	private short purifyWaterTds;
	/** 工作状态
	 * BIT0 : 1 开机，0 关机 
	 * BIT1 : 1 可售水，0 非
	 * BIT2 : 1 缺水，0 非 
	 * BIT3 : 1 水满，0 非 
	 * BIT4 : 1 制水中，0 非
	 * BIT5 : 保留
	 * BIT6 : 保留 
	 * BIT7 : 保留*/
	private byte workStatus;
	
	/** 单位为 L，由设备上传，由后台累加 */
	private short produceWaterQuantity;
	
	/** 保留 */
	private byte reserve1;
	private byte reserve2;
	private byte reserve3;
	private byte reserve4;
	private byte reserve5;
	private byte reserve6;
	private byte reserve7;
	private byte reserve8;
	private byte reserve9;
	private byte reserve10;
	private byte reserve11;
	private byte reserve12;
	private byte reserve13;
	private byte reserve14;
	private byte reserve15;
	
	public int getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(int equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public short getInstallationNo() {
		return installationNo;
	}

	public void setInstallationNo(short installationNo) {
		this.installationNo = installationNo;
	}

	public byte getOzoneWorkingTime() {
		return ozoneWorkingTime;
	}

	public void setOzoneWorkingTime(byte ozoneWorkingTime) {
		this.ozoneWorkingTime = ozoneWorkingTime;
	}

	public byte getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(byte unitPrice) {
		this.unitPrice = unitPrice;
	}

	public byte getHumidity() {
		return humidity;
	}

	public void setHumidity(byte humidity) {
		this.humidity = humidity;
	}

	public byte getTemperature() {
		return temperature;
	}

	public void setTemperature(byte temperature) {
		this.temperature = temperature;
	}

	public short getRawWaterTds() {
		return rawWaterTds;
	}

	public void setRawWaterTds(short rawWaterTds) {
		this.rawWaterTds = rawWaterTds;
	}

	public short getPurifyWaterTds() {
		return purifyWaterTds;
	}

	public void setPurifyWaterTds(short purifyWaterTds) {
		this.purifyWaterTds = purifyWaterTds;
	}

	public byte getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(byte workStatus) {
		this.workStatus = workStatus;
	}

	public short getProduceWaterQuantity() {
		return produceWaterQuantity;
	}

	public void setProduceWaterQuantity(short produceWaterQuantity) {
		this.produceWaterQuantity = produceWaterQuantity;
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

	public byte getReserve4() {
		return reserve4;
	}

	public void setReserve4(byte reserve4) {
		this.reserve4 = reserve4;
	}

	public byte getReserve5() {
		return reserve5;
	}

	public void setReserve5(byte reserve5) {
		this.reserve5 = reserve5;
	}

	public byte getReserve6() {
		return reserve6;
	}

	public void setReserve6(byte reserve6) {
		this.reserve6 = reserve6;
	}

	public byte getReserve7() {
		return reserve7;
	}

	public void setReserve7(byte reserve7) {
		this.reserve7 = reserve7;
	}

	public byte getReserve8() {
		return reserve8;
	}

	public void setReserve8(byte reserve8) {
		this.reserve8 = reserve8;
	}

	public byte getReserve9() {
		return reserve9;
	}

	public void setReserve9(byte reserve9) {
		this.reserve9 = reserve9;
	}

	public byte getReserve10() {
		return reserve10;
	}

	public void setReserve10(byte reserve10) {
		this.reserve10 = reserve10;
	}

	public byte getReserve11() {
		return reserve11;
	}

	public void setReserve11(byte reserve11) {
		this.reserve11 = reserve11;
	}

	public byte getReserve12() {
		return reserve12;
	}

	public void setReserve12(byte reserve12) {
		this.reserve12 = reserve12;
	}

	public byte getReserve13() {
		return reserve13;
	}

	public void setReserve13(byte reserve13) {
		this.reserve13 = reserve13;
	}

	public byte getReserve14() {
		return reserve14;
	}

	public void setReserve14(byte reserve14) {
		this.reserve14 = reserve14;
	}

	public byte getReserve15() {
		return reserve15;
	}

	public void setReserve15(byte reserve15) {
		this.reserve15 = reserve15;
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.STATUS_REQT.getType());
		buf.writeInt(this.equipmentNo);
		buf.writeShort(this.installationNo);
		buf.writeByte(this.ozoneWorkingTime);
		buf.writeByte(this.unitPrice);
		buf.writeByte(this.humidity);
		buf.writeByte(this.temperature);
		buf.writeShort(this.rawWaterTds);
		buf.writeShort(this.purifyWaterTds);
		buf.writeByte(this.workStatus);
		buf.writeShort(this.produceWaterQuantity);
		
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve1);

	}

	@Override
	public void mergeContent(RWByteBuf buf) {

		this.equipmentNo = buf.getBuf().readInt();
		this.installationNo = buf.getBuf().readShort();
		this.ozoneWorkingTime = buf.getBuf().readByte();
		this.unitPrice = buf.getBuf().readByte();
		this.humidity = buf.getBuf().readByte();
		this.temperature = buf.getBuf().readByte();
		this.rawWaterTds = buf.getBuf().readShort();
		this.purifyWaterTds = buf.getBuf().readShort();
		this.workStatus = buf.getBuf().readByte();
		this.produceWaterQuantity = buf.getBuf().readShort();
		
		byte[] reserveData = new byte[15];
		buf.getBuf().readBytes(reserveData);
				
	}

	@Override
	public String toString() {
		return "StatusReqt [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.command)) 
		+ ",equipmentNo=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.equipmentNo)) 
		+ ",installationNo=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.installationNo))
		+ ",ozoneWorkingTime=" + HexUtils.appendPrefixHex(new byte[]{this.ozoneWorkingTime}) 
		+ ",unitPrice=" + HexUtils.appendPrefixHex(new byte[]{this.unitPrice})
		+ ",humidity=" + HexUtils.appendPrefixHex(new byte[]{this.humidity})
		+ ",temperature=" + HexUtils.appendPrefixHex(new byte[]{this.temperature})
		+ ",rawWaterTds=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.rawWaterTds))
		+ ",purifyWaterTds=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.purifyWaterTds))
		+ ",workStatus=" + HexUtils.appendPrefixHex(new byte[]{this.workStatus}) 
		+ ",produceWaterQuantity=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.produceWaterQuantity))
		+ ",checkCode=" + this.checkCode + "]";
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 46;
	}
	
	@Override
	public void setLength(int length) {
		this.length = length;
	}
	

}
