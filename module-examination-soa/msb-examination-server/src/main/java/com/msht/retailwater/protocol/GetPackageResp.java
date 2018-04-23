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
public class GetPackageResp extends AbstractCommonMsg {

	private byte type;
	private int payFee1;
	private int giveFee1;
	private int payFee2;
	private int giveFee2;
	private int payFee3;
	private int giveFee3;
	private int payFee4;
	private int giveFee4;
	private int payFee5;
	private int giveFee5;
	
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
	public int getPayFee1() {
		return payFee1;
	}

	public void setPayFee1(int payFee1) {
		this.payFee1 = payFee1;
	}

	public int getGiveFee1() {
		return giveFee1;
	}

	public void setGiveFee1(int giveFee1) {
		this.giveFee1 = giveFee1;
	}

	public int getPayFee2() {
		return payFee2;
	}

	public void setPayFee2(int payFee2) {
		this.payFee2 = payFee2;
	}

	public int getGiveFee2() {
		return giveFee2;
	}

	public void setGiveFee2(int giveFee2) {
		this.giveFee2 = giveFee2;
	}

	public int getPayFee3() {
		return payFee3;
	}

	public void setPayFee3(int payFee3) {
		this.payFee3 = payFee3;
	}

	public int getGiveFee3() {
		return giveFee3;
	}

	public void setGiveFee3(int giveFee3) {
		this.giveFee3 = giveFee3;
	}

	public int getPayFee4() {
		return payFee4;
	}

	public void setPayFee4(int payFee4) {
		this.payFee4 = payFee4;
	}

	public int getGiveFee4() {
		return giveFee4;
	}

	public void setGiveFee4(int giveFee4) {
		this.giveFee4 = giveFee4;
	}

	public int getPayFee5() {
		return payFee5;
	}

	public void setPayFee5(int payFee5) {
		this.payFee5 = payFee5;
	}

	public int getGiveFee5() {
		return giveFee5;
	}

	public void setGiveFee5(int giveFee5) {
		this.giveFee5 = giveFee5;
	}

	@Override
	public int getLength() {
		return 35;
	}

	@Override
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.GET_PACKAGE_RESP.getType());
		buf.writeByte(this.type);
		buf.writeShort(this.payFee1);
		buf.writeShort(this.giveFee1);
		buf.writeShort(this.payFee2);
		buf.writeShort(this.giveFee2);
		buf.writeShort(this.payFee3);
		buf.writeShort(this.giveFee3);
		buf.writeShort(this.payFee4);
		buf.writeShort(this.giveFee4);
		buf.writeShort(this.payFee5);
		buf.writeShort(this.giveFee5);
	}

	@Override
	public void mergeContent(RWByteBuf buf) {
		this.type = buf.getBuf().readByte();
		this.payFee1 = buf.getBuf().readUnsignedShort();
		this.giveFee1 = buf.getBuf().readUnsignedShort();
		this.payFee2 = buf.getBuf().readUnsignedShort();
		this.giveFee2 = buf.getBuf().readUnsignedShort();
		this.payFee3 = buf.getBuf().readUnsignedShort();
		this.giveFee3 = buf.getBuf().readUnsignedShort();
		this.payFee4 = buf.getBuf().readUnsignedShort();
		this.giveFee4 = buf.getBuf().readUnsignedShort();
		this.payFee5 = buf.getBuf().readUnsignedShort();
		this.giveFee5 = buf.getBuf().readUnsignedShort();
	}
	
	@Override
	public String toString() {
		return "GetPackageResp [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.int2byte(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2(this.command)) 
		+ ",type=" + HexUtils.appendPrefixHex(new byte[]{this.type})
		+ ",payFee1=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.payFee1))
		+ ",giveFee1=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.giveFee1))
		+ ",payFee2=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.payFee2))
		+ ",giveFee2=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.giveFee2))
		+ ",payFee3=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.payFee3))
		+ ",giveFee3=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.giveFee3))
		+ ",payFee4=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.payFee4))
		+ ",giveFee4=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.giveFee4))
		+ ",payFee5=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.payFee5))
		+ ",giveFee5=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2((short) this.giveFee5))
		+ ",checkCode=" + this.checkCode + "]";
	}
}
