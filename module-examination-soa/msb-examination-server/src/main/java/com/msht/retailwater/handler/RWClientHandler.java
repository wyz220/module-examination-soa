package com.msht.retailwater.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msht.retailwater.client.RWClient;
import com.msht.retailwater.common.SysConfig;
import com.msht.retailwater.protocol.CommandType;
import com.msht.retailwater.protocol.CommonMsg;
import com.msht.retailwater.protocol.ControlReqt;
import com.msht.retailwater.protocol.ControlResp;
import com.msht.retailwater.utils.RWUtils;
import com.msht.retailwater.protocol.BusinessNoticeReqt;
import com.msht.retailwater.protocol.BusinessNoticeResp;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;


@Sharable
public class RWClientHandler
    extends ChannelInboundHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger(RWClientHandler.class);
	
	private RWClient client;
	
	public RWClientHandler(RWClient client){
		this.client = client;
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
		CommonMsg commonMsg = (CommonMsg) msg;
		//System.out.println(ctx.channel().remoteAddress() + ": " + commonMsg.toString());
	    //System.out.println(client.getEquipmentNo());
		switch (CommandType.getType(commonMsg.getCommand())) {
		case CONTROL_REQT:
			ControlReqt reqt = (ControlReqt) commonMsg;
			sendControlResp(ctx,reqt);
			break;
		case BUSINESS_NOTICE_REQT:
			BusinessNoticeReqt queryAccountNoticeReqt = (BusinessNoticeReqt) commonMsg;
			logger.info(ctx.channel().remoteAddress() + ": " + queryAccountNoticeReqt.toString());
			sendBusinessNoticeResp(ctx,queryAccountNoticeReqt);
			break;
		}
        
    }

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        logger.error("client caught exception", cause);
        ctx.close();
    }
    
    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("---" + ctx.channel().remoteAddress() + " is active---");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("---" + ctx.channel().remoteAddress() + " is inactive---");
		//client.doConnect();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case READER_IDLE:
				break;
			case WRITER_IDLE:
				break;
			case ALL_IDLE:
				SocketChannel channel = (SocketChannel) ctx.channel();
				client.sendStatusReqt1(channel);
				break;
			default:
				break;
			}
		}
	}
	
	private void sendControlResp(ChannelHandlerContext ctx,ControlReqt reqt){
		ControlResp resp = new ControlResp();
		resp.setVersion(SysConfig.PROTOCOL_VERSION);
		resp.setBackup(SysConfig.PROTOCOL_BACKUP);
		resp.setDataFrameSeq(reqt.getDataFrameSeq());
		resp.setCommand((short) CommandType.CONTROL_RESP.getType());
		ctx.writeAndFlush(resp);
		logger.info(resp.toString());
	}
	
	private void sendBusinessNoticeResp(ChannelHandlerContext ctx,BusinessNoticeReqt reqt){
		BusinessNoticeResp resp = new BusinessNoticeResp();
		resp.setVersion(SysConfig.PROTOCOL_VERSION);
		resp.setBackup(SysConfig.PROTOCOL_BACKUP);
		resp.setDataFrameSeq(reqt.getDataFrameSeq());
		resp.setCommand((short) CommandType.BUSINESS_NOTICE_RESP.getType());
		ctx.writeAndFlush(resp);
		logger.info(resp.toString());
	}
}
