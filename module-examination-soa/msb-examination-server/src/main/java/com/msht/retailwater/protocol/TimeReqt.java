/**
 * 
 */
package com.msht.retailwater.protocol;

import com.msht.retailwater.common.RWByteBuf;
import com.msht.retailwater.protocol.ControlReqt.ConsumeType;
import com.msht.retailwater.utils.ByteUtils;
import com.msht.retailwater.utils.HexUtils;

import io.netty.buffer.ByteBuf;

/**
 * @author lindaofen
 *
 */
public class TimeReqt extends AbstractCommonMsg {

	private byte format;

	public byte getFormat() {
		return format;
	}

	public void setFormat(byte format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return "TimeReqt [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.command)) 
		+ ",format=" + HexUtils.appendPrefixHex(new byte[]{this.format})
		+ ",checkCode=" + this.checkCode + "]";
	}

	@Override
	public void mergeContent(RWByteBuf buf) {
		try {
			this.format = buf.getBuf().readByte();
			byte[] data = new byte[buf.getLength() - 1];
		    buf.getBuf().readBytes(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.TIME_REQT.getType());
		buf.writeByte(this.format);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
		buf.writeByte(0x00);
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
	
	public enum TimeFormat{
		 GMT(0),CHINA(1);
		
		private int format;
		
		private TimeFormat(int format){
			this.format = format;
		}

		public int getFormat() {
			return format;
		}
		
		public static TimeFormat getFormat(int format) {
			switch (format){
			case 0:
				return GMT;
			case 1:
				return CHINA;
			}
			return CHINA;
		}
	}
}
