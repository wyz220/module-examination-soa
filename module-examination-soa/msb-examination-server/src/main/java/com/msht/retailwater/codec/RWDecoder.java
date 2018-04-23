/**
 * 
 */
package com.msht.retailwater.codec;

import java.nio.ByteOrder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msht.framework.common.utils.StringUtils;
import com.msht.retailwater.common.RWByteBuf;
import com.msht.retailwater.common.SysConfig;
import com.msht.retailwater.protocol.CommonMsg;
import com.msht.retailwater.utils.HexUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * @author lindaofen
 *
 */
public class RWDecoder extends ByteToMessageDecoder {

	private Logger logger = LoggerFactory.getLogger(RWEncoder.class);
	
    private static final int HEAD_LENGTH = 3;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		if (in.readableBytes() < HEAD_LENGTH){
			return;
		}
	
	      
		//标记一下当前的readIndex的位置
		in.markReaderIndex();                 
		byte flag = in.readByte();
		logger.info("---" + ctx.channel().remoteAddress() + ",start flag: " + HexUtils.appendPrefixHex(new byte[]{flag}));
		//判断起始标志
		if (flag != SysConfig.PROTOCOL_START_FLAG){
			return;
		}
		
		int length = in.readShort();
		logger.info("---" + ctx.channel().remoteAddress() + ",data length: " + length);
		int readableBytes = in.readableBytes();
		//读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (in.readableBytes() < length - 2) { 
            in.resetReaderIndex();
            return;
        }
  
        //打印消息内容
        in.markReaderIndex();
        byte[] data = new byte[length - 2];
        in.readBytes(data);
        logger.info("---" + ctx.channel().remoteAddress() + ",receive message: " + ByteBufUtil.hexDump(data));
        in.resetReaderIndex();
        
		//标记一下当前的readIndex的位置
		in.markReaderIndex();   
        byte[] temp = new byte[8];
        in.readBytes(temp);
        //读取消息类型
        //short msgType = in.readShort();
        byte[] msgTypeArr = new byte[2];
        in.readBytes(msgTypeArr);
        String msgType = HexUtils.appendPrefixHex(msgTypeArr);
        RWByteBuf buf = new RWByteBuf();
        buf.setLength(length - 14);
        buf.setBuf(in);
        String className = SysConfig.commandMap.get(msgType);
        if (StringUtils.isBlank(className)){
        	logger.warn("Invalid message type,discard: " +  ByteBufUtil.hexDump(data));
        	return;
        }
        in.resetReaderIndex();
        
        Class target = Class.forName(className);
        CommonMsg targetBean = (CommonMsg) target.newInstance();
        targetBean.mergeFrom(buf);
        targetBean.setLength(length);
        
        byte[] checkCodeArr = new byte[2];
        in.readBytes(checkCodeArr);
        String checkCode = ByteBufUtil.hexDump(checkCodeArr).toUpperCase();
        targetBean.setCheckCode(checkCode);
        out.add(targetBean);
	
	}

}
