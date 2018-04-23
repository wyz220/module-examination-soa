/**
 * 
 */
package com.msht.retailwater.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lindaofen
 *
 */
@Component
public class ChannelMap {

    public Map<String,SocketChannel> map=new ConcurrentHashMap<String, SocketChannel>();
    public void add(String clientId,SocketChannel socketChannel){
        map.put(clientId,socketChannel);
    }
    public Channel get(String clientId){
       return map.get(clientId);
    }
    
    public String get(SocketChannel socketChannel){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==socketChannel){
                return (String) entry.getKey();
            }
        }
        return null;
    }
    public void remove(SocketChannel socketChannel){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==socketChannel){
                map.remove(entry.getKey());
            }
        }
    }
    
	@Override
	public String toString() {
		return "ChannelMap: " + map.toString();
	}
    
    
}
