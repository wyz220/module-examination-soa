/**
 * 
 */
package com.msht.retailwater.protocol;

import com.msht.retailwater.common.RWByteBuf;
import com.msht.retailwater.utils.HexUtils;

import io.netty.buffer.ByteBuf;

/**
 * @author lindaofen
 *
 */
public abstract class AbstractCommonMsg implements CommonMsg {
	
	protected int length;
	
	protected byte version;
	
	protected byte backup;
	
	protected int dataFrameSeq;
	
	protected short reserve;

	protected short command;
	
	protected String checkCode;
	
	protected long sendTime;
	
	protected long expireTime;
	
	@Override
	public void build(ByteBuf buf) {
		writeHead(buf);
		writeContent(buf);
	}
	
	public void writeHead(ByteBuf buf){
		buf.writeByte(Byte.valueOf(this.version));
		buf.writeByte(Byte.valueOf(this.backup));
		buf.writeInt(Integer.valueOf(this.dataFrameSeq));
		buf.writeShort(Short.valueOf(this.reserve));
	}
	
	public abstract void writeContent(ByteBuf buf);

	@Override
	public void mergeFrom(RWByteBuf buf) {
		mergeHead(buf);
		mergeContent(buf);
	}

	public void mergeHead(RWByteBuf buf){
	    this.version = buf.getBuf().readByte();
	    this.backup = buf.getBuf().readByte();
		this.dataFrameSeq = buf.getBuf().readInt();
		this.reserve = buf.getBuf().readShort();
		this.command = buf.getBuf().readShort();
	}
	
	public abstract void mergeContent(RWByteBuf buf);
	
	@Override
	public short getCommand() {
		return command;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getBackup() {
		return backup;
	}

	public void setBackup(byte backup) {
		this.backup = backup;
	}

	public int getDataFrameSeq() {
		return dataFrameSeq;
	}

	public void setDataFrameSeq(int dataFrameSeq) {
		this.dataFrameSeq = dataFrameSeq;
	}

	public short getReserve() {
		return reserve;
	}

	public void setReserve(short reserve) {
		this.reserve = reserve;
	}

	@Override
	public String getCheckCode() {
		return checkCode;
	}

	@Override
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public void setCommand(short command) {
		this.command = command;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	
}
