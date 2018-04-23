/**
 * 
 */
package com.msht.retailwater.common;

import io.netty.buffer.ByteBuf;

/**
 * @author lindaofen
 *
 */
public class RWByteBuf {

	private int length;
	
	private ByteBuf buf;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public ByteBuf getBuf() {
		return buf;
	}

	public void setBuf(ByteBuf buf) {
		this.buf = buf;
	}
	
	
}
