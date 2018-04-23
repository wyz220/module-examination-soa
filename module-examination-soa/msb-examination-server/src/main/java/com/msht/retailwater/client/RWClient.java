package com.msht.retailwater.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.msht.framework.cluster.zk.ServerData;
import com.msht.framework.cluster.zk.client.BalanceProvider;
import com.msht.framework.cluster.zk.client.DefaultBalanceProvider;
import com.msht.framework.common.utils.CurrencyUtils;
import com.msht.retailwater.codec.RWDecoder;
import com.msht.retailwater.codec.RWEncoder;
import com.msht.retailwater.common.SysConfig;
import com.msht.retailwater.handler.RWClientHandler;
import com.msht.retailwater.protocol.BusinessNoticeReqt;
import com.msht.retailwater.protocol.BusinessNoticeReqt.BusinessType;
import com.msht.retailwater.protocol.CommandType;
import com.msht.retailwater.protocol.ControlReqt;
import com.msht.retailwater.protocol.GetPackageReqt;
import com.msht.retailwater.protocol.GetParamReqt;
import com.msht.retailwater.protocol.HeartbeatReqt;
import com.msht.retailwater.protocol.StatusReqt;
import com.msht.retailwater.protocol.TimeReqt;
import com.msht.retailwater.utils.HexUtils;
import com.msht.retailwater.utils.RWUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
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

public class RWClient implements Runnable{
	private Logger logger = LoggerFactory.getLogger(RWClient.class);
	
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

	public static int equipmentNoSeqInit = 60000000;
	public static AtomicInteger equipmentNoSeq = new AtomicInteger(equipmentNoSeqInit);
	
	private Bootstrap bootstrap;
	private Channel channel;
	private String host;
	private int port;
	
	private int equipmentNo;

	public int getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(int equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public RWClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public RWClient(String host, int port, int equipmentNo) {
		this.host = host;
		this.port = port;
		this.equipmentNo = equipmentNo;
	}

	public void start() throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup(1);
		try {
			bootstrap= new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class)
			        .option(ChannelOption.TCP_NODELAY, true)
			        .option(ChannelOption.SO_KEEPALIVE,true)
			        .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {

							ChannelPipeline p = ch.pipeline();
							p.addLast(new IdleStateHandler(0, 0, 25));
					        p.addLast(new RWDecoder());
	                        p.addLast(new RWEncoder());
							p.addLast(new RWClientHandler(RWClient.this));
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
                    sendStatusReqt1((SocketChannel) channel);
                    
                    System.out.println("Connect to server successfully!");
                    
                    
/*        			Runnable runnable = new Runnable() {  
        	            public void run() {  
        	            	sendStatusReqt1((SocketChannel) channel);
        	            }  
        	        };  

        	        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
        	        service.scheduleAtFixedRate(runnable, 25, 25, TimeUnit.SECONDS);*/
        	        
                    //scheduleSendControlReqt();
                    //sendTimeReqt();
                    //sendControlReqt();
                    //sendBusinessNoticeReqt();
                    //sendGetPackageReqt();
                    //sendGetParamReqt();
                    
                    //sendICRechargeReqt();
                    //Thread.sleep(1000);
                    //sendControlReqt();
                    //Thread.sleep(31000);
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
		
		reqt.setEquipmentNo(0x00BC614E);
		//reqt.setEquipmentNo(0x00A98AC7);
		reqt.setInstallationNo((short)0x03E7);
		reqt.setOzoneWorkingTime((byte)0x78);
		reqt.setUnitPrice((byte) 0x14);
		reqt.setHumidity((byte)0x59);
		reqt.setTemperature((byte)0x32);
		reqt.setRawWaterTds((short) 0x0000);//0x0064
		reqt.setPurifyWaterTds((short) 0x0000);//0x000F
		reqt.setWorkStatus((byte)0x0B); //0000 1011
		reqt.setProduceWaterQuantity((short)0x14);
		reqt.setReserve1((byte)0x00);
		socketChannel.writeAndFlush(reqt);
	}
	
	public void sendICRechargeReqt(){
		//IC卡购水
		ControlReqt reqt = new ControlReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.CONTROL_REQT.getType());
		
		//12345678
		reqt.setEquipmentNo(0x003938701);
		//reqt.setEquipmentNo(0x00A98AC7);
		reqt.setCardType((byte) 0x63);
		reqt.setCardNumber(87654321);
		reqt.setTriggerTime(HexUtils.hexString2Bytes("10050C121E00"));
		reqt.setConsumeType((byte) 0x01);
		//reqt.setBeforeBalance((short) 0X2EE0);
		reqt.setBeforeBalance((short) 0X0000);
		reqt.setConsumeType((byte) 0x01);
		//reqt.setConsumeType((byte) 0x03);
		reqt.setAmount((short) 0X0000);
		//reqt.setAfterBalance((short) 0X2E4A);
		reqt.setAfterBalance((short) 0X0000);
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
		
		reqt.setUpdateflag1((byte) 0xFF); //1111 1111
		reqt.setUpdateflag2((byte) 0xFF); //1111 1111
		reqt.setUpdateflag3((byte) 0xFF); //1111 1111
		reqt.setUpdateflag4((byte) 0x80); //1000 0000
		reqt.setUpdateflag5((byte) 0x00); //0000 0000
		reqt.setUpdateflag6((byte) 0x00); //0000 0000
		
		channel.writeAndFlush(reqt);
	}
	
	public void sendControlReqt(){
		//IC卡购水
		ControlReqt reqt = new ControlReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.CONTROL_REQT.getType());
		
		reqt.setEquipmentNo(0x00BC614E);
		//reqt.setEquipmentNo(0x00A98AC7);
		reqt.setCardType((byte) 0x63);
		reqt.setCardNumber(87654321);
		reqt.setTriggerTime(HexUtils.hexString2Bytes("10050C121E00"));
		reqt.setConsumeType((byte) 0x01);
		reqt.setBeforeBalance((short) 0X2EE0);
		reqt.setConsumeType((byte) 0x01);
		//reqt.setConsumeType((byte) 0x03);
		reqt.setAmount((short) 0X0096);
		reqt.setAfterBalance((short) 0X2E4A);
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
		
		reqt.setUpdateflag1((byte) 0xFF); //1111 1111
		reqt.setUpdateflag2((byte) 0xFF); //1111 1111
		reqt.setUpdateflag3((byte) 0xFF); //1111 1111
		reqt.setUpdateflag4((byte) 0x80); //1000 0000
		reqt.setUpdateflag5((byte) 0x00); //0000 0000
		reqt.setUpdateflag6((byte) 0x00); //0000 0000
		
		channel.writeAndFlush(reqt);
	}
	
	public void sendTimeReqt(){

		TimeReqt reqt = new TimeReqt();
		reqt.setCommand((short) CommandType.TIME_REQT.getType());
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		
		reqt.setFormat((byte) 0x01);
		channel.writeAndFlush(reqt);
	}
	
	private void scheduleSendControlReqt(){
		Runnable runnable = new Runnable() {  
            public void run() {  
                // task to run goes here  
            	sendControlReqt();
            }  
        };  
        ScheduledExecutorService service = Executors  
                .newSingleThreadScheduledExecutor();  
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
        service.scheduleAtFixedRate(runnable, 15, 15, TimeUnit.SECONDS);
	}
	
	public void sendBusinessNoticeReqt(){
		BusinessNoticeReqt reqt = new BusinessNoticeReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.BUSINESS_NOTICE_REQT.getType());
		
		reqt.setType((byte) BusinessType.APP_SCAN_NOTICE.getType());
		reqt.setAccount(18907573557l);
		short beforeBalance  = CurrencyUtils.mul(240, 100).shortValue();
		reqt.setBeforeBalance(beforeBalance);
		reqt.setAmount(0x012C);
		reqt.setAfterBalance(0x5C94);
		reqt.setEquipmentNo(12345678);
		reqt.setUnitPrice((byte) 0x16);
		reqt.setOutWaterTime((byte) 0x28);
		reqt.setOutWaterQuantity((byte) 0x05);
		channel.writeAndFlush(reqt);
	}

	public void sendGetPackageReqt(){

		GetPackageReqt reqt = new GetPackageReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setReserve(SysConfig.PROTOCOL_RESERVE);
		reqt.setCommand((short) CommandType.GET_PACKAGE_REQT.getType());
		
		channel.writeAndFlush(reqt);
	}
	
	public void sendGetParamReqt(){

		GetParamReqt reqt = new GetParamReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setReserve(SysConfig.PROTOCOL_RESERVE);
		reqt.setCommand((short) CommandType.GET_PARAM_REQT.getType());
		
		channel.writeAndFlush(reqt);
	}
	
	public void sendStatusReqt1(SocketChannel socketChannel){
		StatusReqt reqt = new StatusReqt();
		reqt.setVersion(SysConfig.PROTOCOL_VERSION);
		reqt.setBackup(SysConfig.PROTOCOL_BACKUP);
		reqt.setDataFrameSeq(RWUtils.generatorDataFrameSeq());
		reqt.setCommand((short) CommandType.STATUS_REQT.getType());
		
		reqt.setEquipmentNo(equipmentNo);
		//reqt.setEquipmentNo(0x00A98AC7);
		reqt.setInstallationNo((short)0x03E7);
		reqt.setOzoneWorkingTime((byte)0x78);
		reqt.setUnitPrice((byte) 0x14);
		reqt.setHumidity((byte)0x59);
		reqt.setTemperature((byte)0x32);
		reqt.setRawWaterTds((short) 0x0000);//0x0064
		reqt.setPurifyWaterTds((short) 0x0000);//0x000F
		reqt.setWorkStatus((byte)0x01); //0000 1011
		reqt.setProduceWaterQuantity((short)0x14);
		reqt.setReserve1((byte)0x00);
		socketChannel.writeAndFlush(reqt);
	}
	
	@Override
	public void run() {
		try {
			start();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception {

		//RWClient echoClient = new RWClient("sb-server.msbapp.cn", 16080);
		//RWClient echoClient = new RWClient("127.0.0.1", 16080);
		//echoClient.start();
		
		/*System.out.println(HexUtils.hexString2Bytes("10050C121E00").length);*/
/*		BalanceProvider<ServerData> provider = new DefaultBalanceProvider("120.25.204.12:2181","/rwservers");
		for (int i = 0; i < 300; i++){
			ServerData serverData = provider.getBalanceItem();
			Thread thread = new Thread(new RWClient(serverData.getHost(), serverData.getPort(),equipmentNoSeq.incrementAndGet()));
			thread.start();
		}*/
		
		for (int i = 0; i < 1; i++){
			Thread thread = new Thread(new RWClient("127.0.0.1", 16080,equipmentNoSeq.incrementAndGet()));
			thread.start();
		}
	}


	
}

