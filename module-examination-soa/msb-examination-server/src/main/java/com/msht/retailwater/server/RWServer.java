package com.msht.retailwater.server;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.msht.framework.cluster.zk.ServerData;
import com.msht.framework.cluster.zk.server.DefaultBalanceUpdateProvider;
import com.msht.framework.cluster.zk.server.ServerRegister;
import com.msht.retailwater.codec.RWDecoder;
import com.msht.retailwater.codec.RWEncoder;
import com.msht.retailwater.common.SysConfig;
import com.msht.retailwater.handler.RWServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @author lindaofen
 *
 */
@Component
public class RWServer {
	private Logger logger = LoggerFactory.getLogger(RWServer.class);

	@Autowired
	private RWServerHandler handler;
	
	@Value("${server.host}")
	private String host = "0.0.0.0";
 	@Value("${server.port}")
	private int port = 16080;
 	
 	@Value("${server.boss.thread.count}")
 	private int bossThreadCount = 1;

 	@Value("${server.worker.thread.count}")
 	private int workerThreadCount = 1;
 	
 	@Value("${server.so.keepalive}")
 	private boolean keepalive = true;
 	
 	@Value("${server.so.backlog}")
 	private int backlog = 1024;
 	
 	@Value("${server.channel.idle.state.handle.time}")
 	private int idleStateHandleTime = 30;
 	
 	private EventLoopGroup bossGroup;
 	
 	private EventLoopGroup workerGroup;
 	
 	private ChannelFuture future;

	@PostConstruct
	public void start() throws Exception {
	    bossGroup = new NioEventLoopGroup(this.bossThreadCount); // (1)
	    workerGroup = new NioEventLoopGroup(this.workerThreadCount);
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
					.option(ChannelOption.SO_BACKLOG, this.backlog)
					.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childOption(ChannelOption.SO_KEEPALIVE, this.keepalive)
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)//关键是这句
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
						@Override
						public void initChannel(SocketChannel ch) throws Exception {

							ChannelPipeline p = ch.pipeline();
							p.addLast(new IdleStateHandler(0, 0, idleStateHandleTime));
					        p.addLast(new RWDecoder());
	                        p.addLast(new RWEncoder());
							p.addLast(handler);
						}
					});

			// Bind and start to accept incoming connections.
			InetSocketAddress address = new InetSocketAddress(host, port);
			future = b.bind(address).sync(); // (7)
			System.out.println("Server listening port: " + address.toString());

			//向zookeeper注册
			//zkRegister();
			
			// Wait until the server socket is closed.
			future.channel().closeFuture().sync();
		} finally {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	@PreDestroy
	public void stop() throws Exception {
		// Shut down all event loops to terminate all threads.
		
		if (bossGroup != null){
			bossGroup.shutdownGracefully();
		}
		
		if (workerGroup != null){
			workerGroup.shutdownGracefully();
		}

	}
	
	/*
	 * 向zookeeper注册 
	 * @throws Exception
	 */
	private void zkRegister() throws Exception{
		ServerData serverData = new ServerData();
		serverData.setBalance(0);
		serverData.setHost(SysConfig.ZK_SERVER_IP);
		serverData.setPort(port);
		ServerRegister register = new ServerRegister(SysConfig.ZK_ADDRESS, SysConfig.ZK_ROOT_PATH, serverData);
		register.register();
		handler.setBalanceUpdater(new DefaultBalanceUpdateProvider(register.getCurrentServerPath(), register.getZkClient()));
	}


	public static void main(String[] args) throws Exception {
		new RWServer().start();
	}
}
