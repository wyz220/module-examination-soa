package com.msht.retailwater.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.msht.retailwater.codec.RWDecoder;
import com.msht.retailwater.codec.RWEncoder;
import com.msht.retailwater.common.SysConfig;
import com.msht.retailwater.handler.RWClientHandler2;
import com.msht.retailwater.protocol.CommandType;
import com.msht.retailwater.protocol.ControlReqt;
import com.msht.retailwater.protocol.HeartbeatReqt;
import com.msht.retailwater.protocol.StatusReqt;
import com.msht.retailwater.utils.HexUtils;
import com.msht.retailwater.utils.RWUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class RWClient2 {
	private Logger logger = LoggerFactory.getLogger(RWClient2.class);

	private Bootstrap bootstrap;
	private Channel channel;
	private String host;
	private int port;

	public RWClient2(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup(1);
		try {
			bootstrap= new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
			        .option(ChannelOption.TCP_NODELAY, true)
			        .option(ChannelOption.SO_KEEPALIVE,true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {

							ChannelPipeline p = ch.pipeline();
							p.addLast(new IdleStateHandler(0, 0, 20));
					        p.addLast(new RWDecoder());
	                        p.addLast(new RWEncoder());
							p.addLast(new RWClientHandler2(RWClient2.this));
						}
					});
			
			doConnect();
		} catch (Exception e) {
            e.printStackTrace();
        }
	}

	 /**
     * 重连机制,每隔2s重新连接一次服务器
     */
    public void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }

        ChannelFuture future = bootstrap.connect(host, port);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture futureListener) throws Exception {
                if (futureListener.isSuccess()) {
                    channel = futureListener.channel();
                    sendStatusReqt((SocketChannel) channel);
                    System.out.println("Connect to server successfully!");
                    
                    //Thread.sleep(5000);
                    //sendControlReqt();
                } else {
                    System.out.println("Failed to connect to server, try connect after 5s");
                    futureListener.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 5, TimeUnit.SECONDS);
                }
            }
        });
    }
    
	public void sendHeartbeatReqt(SocketChannel socketChannel){

		HeartbeatReqt reqt = new HeartbeatReqt();
		reqt.setCommand((short) CommandType.HEARTBEAT_REQT.getType());
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		
		reqt.setData("00000000");
		socketChannel.writeAndFlush(reqt);
	}
	
	public void sendStatusReqt(SocketChannel socketChannel){
		StatusReqt reqt = new StatusReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.STATUS_REQT.getType());
		
		reqt.setEquipmentNo(0x05397FB1);
		reqt.setInstallationNo((short)0x03E7);
		reqt.setOzoneWorkingTime((byte)0x78);
		reqt.setUnitPrice((byte) 0x14);
		reqt.setHumidity((byte)0x00);
		reqt.setTemperature((byte)0x00);
		reqt.setRawWaterTds((short) 0x0000);
		reqt.setPurifyWaterTds((short) 0x0000);
		reqt.setWorkStatus((byte)0xD8);
		reqt.setProduceWaterQuantity((short)0x14);
		reqt.setReserve1((byte)0x00);
		socketChannel.writeAndFlush(reqt);
	}
	
	public void sendControlReqt(){
		//IC卡购水
		ControlReqt reqt = new ControlReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.CONTROL_REQT.getType());
		
		reqt.setEquipmentNo(0x00BC614E);
		reqt.setCardType((byte) 0x63);
		reqt.setCardNumber(87654321);
		reqt.setTriggerTime(HexUtils.hexString2Bytes("10050C121E00"));
		reqt.setConsumeType((byte) 0x01);
		reqt.setBeforeBalance((short) 0XEA60);
		reqt.setConsumeType((byte) 0x01);
		reqt.setAmount((short) 0X0096);
		reqt.setAfterBalance((short) 0XE9CA);
		reqt.setProduceWaterQuantity((short) 0X03E8);
		reqt.setRechargeAmount((short) 0X0000);
		reqt.setInstallationNo((short) 0x0001);
		reqt.setUnitPrice((byte) 0x14);
		reqt.setOzoneWorkingTime((byte) 0x78);
		reqt.setSwitchMachineKeySet((byte) 0x01);
		reqt.setGetWaterSet((byte) 0x01);
		reqt.setFilterResetSet((byte) 0x01);
		reqt.setFilterResetLevel((byte) 0x00);
		reqt.setProduceWaterQuantity((short) 0x0096);
		reqt.setBlackCardFlag((byte) 0x01);
		reqt.setButtonCode((byte) 0x01);
		
		reqt.setReserve1((byte)0x00);
		reqt.setReserve2((byte)0x00);
		reqt.setReserve3((byte)0x00);
		
		reqt.setUpdateflag1((byte) 0xF7); //1111 0111
		reqt.setUpdateflag2((byte) 0xFF); //1111 1111
		reqt.setUpdateflag3((byte) 0xFF); //1111 1111
		reqt.setUpdateflag4((byte) 0x80); //1111 1111
		reqt.setUpdateflag5((byte) 0x00); //0000 0000
		reqt.setUpdateflag6((byte) 0x00); //0000 0000
		
		channel.writeAndFlush(reqt);
	}
	
	public static void main(String[] args) throws Exception {

		RWClient2 echoClient = new RWClient2("127.0.0.1", 16080);
		echoClient.start();
		
		/*System.out.println(HexUtils.hexString2Bytes("10050C121E00").length);*/

	}
}

