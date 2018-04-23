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
public class HeartbeatResp extends AbstractCommonMsg {

	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "HeartbeatResp [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.command)) 
		+ ",data=" + data
		+ ",checkCode=" + this.checkCode + "]";
	}

	@Override
	public void mergeContent(RWByteBuf buf) {
		try {
			byte[] data = new byte[buf.getLength()];
		    buf.getBuf().readBytes(data);
			this.data = new String(data, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.HEARTBEAT_RESP.getType());
		buf.writeBytes(data.getBytes());
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 22;
	}
	
	@Override
	public void setLength(int length) {
		this.length = length;
	}
}
