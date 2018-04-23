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
public class GetPackageReqt extends AbstractCommonMsg {

	@Override
	public String toString() {
		return "GetPackageReqt [version=" + HexUtils.appendPrefixHex(new byte[]{this.version}) 
		+ ",backup=" + HexUtils.appendPrefixHex(new byte[]{this.backup}) 
		+ ",dataFrameSeq=" + HexUtils.appendPrefixHex(ByteUtils.int2byte(this.dataFrameSeq)) 
		+ ",reserve=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2(this.reserve))
		+ ",command=" + HexUtils.appendPrefixHex(ByteUtils.shortToByte2(this.command)) 
		+ ",checkCode=" + this.checkCode + "]";
	}
	
	@Override
	public int getLength() {
		return 14;
	}

	@Override
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public void writeContent(ByteBuf buf) {
		buf.writeShort((short) CommandType.GET_PACKAGE_REQT.getType());
	}

	@Override
	public void mergeContent(RWByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

}
