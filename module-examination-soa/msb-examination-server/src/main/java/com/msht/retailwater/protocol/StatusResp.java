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
public class StatusResp extends AbstractCommonMsg {

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.STATUS_RESP.getType());
	}

	@Override
	public void mergeContent(RWByteBuf buf) {

	}
	
	@Override
	public String toString() {
		return "StatusResp [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.intToByte4(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByteArray(this.command)) 
		+ ",checkCode=" + this.checkCode + "]";
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 14;
	}

	@Override
	public void setLength(int length) {
		this.length = length;
	}
}
