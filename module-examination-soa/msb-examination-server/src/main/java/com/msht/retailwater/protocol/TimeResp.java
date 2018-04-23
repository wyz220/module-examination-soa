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
public class TimeResp extends AbstractCommonMsg {

	private byte format;
	
	private byte year;
	
	private byte month;
	
	private byte day;
	
	private byte hour;
	
	private byte minute;
	
	private byte second;
	
	private byte week;
	
	private byte timezone;

	private byte reserve1;
	private byte reserve2;
	private byte reserve3;
	private byte reserve4;
	private byte reserve5;
	private byte reserve6;
	private byte reserve7;
	
	
	public byte getFormat() {
		return format;
	}

	public void setFormat(byte format) {
		this.format = format;
	}

	public byte getYear() {
		return year;
	}

	public void setYear(byte year) {
		this.year = year;
	}

	public byte getMonth() {
		return month;
	}

	public void setMonth(byte month) {
		this.month = month;
	}

	public byte getDay() {
		return day;
	}

	public void setDay(byte day) {
		this.day = day;
	}

	public byte getHour() {
		return hour;
	}

	public void setHour(byte hour) {
		this.hour = hour;
	}

	public byte getMinute() {
		return minute;
	}

	public void setMinute(byte minute) {
		this.minute = minute;
	}

	public byte getSecond() {
		return second;
	}

	public void setSecond(byte second) {
		this.second = second;
	}

	public byte getWeek() {
		return week;
	}

	public void setWeek(byte week) {
		this.week = week;
	}

	public byte getTimezone() {
		return timezone;
	}

	public void setTimezone(byte timezone) {
		this.timezone = timezone;
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

	@Override
	public String toString() {
		return "TimeResp [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.command)) 
		+ ",format=" + HexUtils.appendPrefixHex(new byte[]{this.format}) 
		+ ",year=" + HexUtils.appendPrefixHex(new byte[]{this.year}) 
		+ ",month=" + HexUtils.appendPrefixHex(new byte[]{this.month}) 
		+ ",day=" + HexUtils.appendPrefixHex(new byte[]{this.day}) 
		+ ",hour=" + HexUtils.appendPrefixHex(new byte[]{this.hour}) 
		+ ",minute=" + HexUtils.appendPrefixHex(new byte[]{this.minute}) 
		+ ",second=" + HexUtils.appendPrefixHex(new byte[]{this.second}) 
		+ ",week=" + HexUtils.appendPrefixHex(new byte[]{this.week}) 
		+ ",timezone=" + HexUtils.appendPrefixHex(new byte[]{this.timezone}) 
		+ ",reserve1=" + HexUtils.appendPrefixHex(new byte[]{this.reserve1}) 
		+ ",reserve2=" + HexUtils.appendPrefixHex(new byte[]{this.reserve2}) 
		+ ",reserve3=" + HexUtils.appendPrefixHex(new byte[]{this.reserve3}) 
		+ ",reserve4=" + HexUtils.appendPrefixHex(new byte[]{this.reserve4}) 
		+ ",reserve5=" + HexUtils.appendPrefixHex(new byte[]{this.reserve5}) 
		+ ",reserve6=" + HexUtils.appendPrefixHex(new byte[]{this.reserve6}) 
		+ ",reserve7=" + HexUtils.appendPrefixHex(new byte[]{this.reserve7}) 
		+ ",checkCode=" + this.checkCode + "]";
	}

	@Override
	public void mergeContent(RWByteBuf buf) {
		try {
			this.format = buf.getBuf().readByte();
			this.year = buf.getBuf().readByte();
			this.month = buf.getBuf().readByte();
			this.day = buf.getBuf().readByte();
			this.hour = buf.getBuf().readByte();
			this.minute = buf.getBuf().readByte();
			this.second = buf.getBuf().readByte();
			this.week = buf.getBuf().readByte();
			this.timezone = buf.getBuf().readByte();
			this.reserve1 = buf.getBuf().readByte();
			this.reserve2 = buf.getBuf().readByte();
			this.reserve3 = buf.getBuf().readByte();
			this.reserve4 = buf.getBuf().readByte();
			this.reserve5 = buf.getBuf().readByte();
			this.reserve6 = buf.getBuf().readByte();
			this.reserve7 = buf.getBuf().readByte();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.TIME_RESP.getType());
		buf.writeByte(this.format);
		buf.writeByte(this.year);
		buf.writeByte(this.month);
		buf.writeByte(this.day);
		buf.writeByte(this.hour);
		buf.writeByte(this.minute);
		buf.writeByte(this.second);
		buf.writeByte(this.week);
		buf.writeByte(this.timezone);
		buf.writeByte(this.reserve1);
		buf.writeByte(this.reserve2);
		buf.writeByte(this.reserve3);
		buf.writeByte(this.reserve4);
		buf.writeByte(this.reserve5);
		buf.writeByte(this.reserve6);
		buf.writeByte(this.reserve7);
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 30;
	}
	
	@Override
	public void setLength(int length) {
		this.length = length;
	}
}
