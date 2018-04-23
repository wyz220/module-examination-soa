package com.msht.retailwater.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.msht.retailwater.client.RWClient2;
import com.msht.retailwater.protocol.CommonMsg;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;


@Sharable
public class RWClientHandler2
    extends ChannelInboundHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger(RWClientHandler2.class);
	
	private RWClient2 client;
	
	public RWClientHandler2(RWClient2 client){
		this.client = client;
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	 CommonMsg commonMsg = (CommonMsg) msg;
        System.out.println(
                "Client received: " + commonMsg.toString());
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
/*				SocketChannel channel = (SocketChannel) ctx.channel();
				client.sendStatusReqt(channel);*/
				break;
			default:
				break;
			}
		}
	}
}
