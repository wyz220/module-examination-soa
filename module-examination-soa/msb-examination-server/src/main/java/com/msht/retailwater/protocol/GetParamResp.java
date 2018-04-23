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
public class GetParamResp extends AbstractCommonMsg {

	private int equipmentNo;
	
	/** 本机单价,1 元人民币打水 20S 到 99S 可调，如 20S为 0x14 */
	private byte unitPrice;
	
	/** 设备输水时间（单位：秒） */
	private byte outWaterTime;
	
	/** 设备输水量（单位：升） */
	private byte outWaterQuantity;
	
	
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

	@Override
	public int getLength() {
		return 21;
	}

	@Override
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.GET_PARAM_RESP.getType());
		buf.writeInt(this.equipmentNo);
		buf.writeByte(this.unitPrice);
		buf.writeByte(this.outWaterTime);
		buf.writeByte(this.outWaterQuantity);
	}

	@Override
	public void mergeContent(RWByteBuf buf) {
		this.equipmentNo = buf.getBuf().readInt();
		this.unitPrice = buf.getBuf().readByte();
		this.outWaterTime = buf.getBuf().readByte();
		this.outWaterQuantity = buf.getBuf().readByte();
	}
	
	@Override
	public String toString() {
		return "GetParamResp [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.int2byte(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2(this.command)) 
		+ ",equipmentNo=" + this.equipmentNo
		+ ",unitPrice=" + HexUtils.appendPrefixHex(new byte[]{this.unitPrice}) 
		+ ",outWaterTime=" + HexUtils.appendPrefixHex(new byte[]{this.outWaterTime}) 
		+ ",outWaterQuantity=" + HexUtils.appendPrefixHex(new byte[]{this.outWaterQuantity}) 
		+ ",checkCode=" + this.checkCode + "]";
	}

}
