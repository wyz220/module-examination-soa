/**
 * 
 */
package com.msht.retailwater.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msht.retailwater.common.SysConfig;
import com.msht.retailwater.protocol.CommonMsg;
import com.msht.retailwater.utils.ByteUtils;
import com.msht.retailwater.utils.HexUtils;
import com.msht.retailwater.utils.RWUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author lindaofen
 *
 */
public class RWEncoder extends MessageToByteEncoder<CommonMsg> {

	private Logger logger = LoggerFactory.getLogger(RWEncoder.class);
	
	@Override
	protected void encode(ChannelHandlerContext ctx, CommonMsg msg, ByteBuf out) throws Exception {
		
		try {
			if (!(msg instanceof CommonMsg)) {
				return;
			}
			
			out.writeByte(SysConfig.PROTOCOL_START_FLAG);
			out.writeShort(msg.getLength());
			msg.build(out);
			
			out.markReaderIndex();
			out.readByte();
			byte[] dst = new byte[msg.getLength() - 2];
			out.readBytes(dst);
			byte[] checkCodeData = RWUtils.crcCalculateByte(dst.length ,dst);
			out.writeBytes(checkCodeData);
			out.resetReaderIndex();
			logger.info("---" + ctx.channel().remoteAddress() + ",send message: " + ByteBufUtil.hexDump(out));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("encode error:{}", e);
			throw e;
		}
	}

}
