/**
 * 
 */
package com.msht.retailwater.protocol;

import com.msht.retailwater.common.RWByteBuf;

import io.netty.buffer.ByteBuf;

/**
 * @author lindaofen
 *
 */
public abstract interface CommonMsg{

	  public abstract short getCommand();
	  
	  public abstract void build(ByteBuf buf);
	  
	  public abstract void mergeFrom(RWByteBuf buf);
	  
	  public abstract String getCheckCode();
	  
	  public abstract void setCheckCode(String checkCode);
	  
	  public abstract int getLength();
	  
	  public abstract void setLength(int length);
}
