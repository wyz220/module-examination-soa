/**
 * 
 */
package com.msht.retailwater.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.msht.framework.common.utils.CurrencyUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

/**
 * @author lindaofen
 *
 */
public class RWUtils {
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	public static int dataFrameSeqInit = 0;
	public static AtomicInteger dataFrameSeq = new AtomicInteger(dataFrameSeqInit);
	
	private static int CRC16(byte[] Buf, int Len) {
        int CRC;
        int i, Temp;
        CRC = 0xffff;
        for (i = 0; i < Len; i++) {
            CRC = CRC ^ byteToInteger(Buf[i]);
            for (Temp = 0; Temp < 8; Temp++) {
                if ((CRC & 0x01) == 1)
                    CRC = (CRC >> 1) ^ 0x8408;//0xA001
                else
                    CRC = CRC >> 1;
            }
        }
        return ~CRC;
    }
	
	private static int byteToInteger(byte b) {
        int value;
        value = b & 0xff;
        return value;
    }

    /**
     * 计算CRC 返回两个字节的字节数组
     *
     * @param length
     * @param address
     * @return
     */
    public static byte[] crcCalculateByte(int length, byte[] address) {
        int s = CRC16(address, length);
        return unsignedShortToByte2(s);
    }
    
    public static byte[] unsignedShortToByte2(int s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (s >> 8 & 0xFF);
        targets[1] = (byte) (s & 0xFF);
        return targets;
    }
	
	public static String convertToBinaryString(byte data){
		int value = ByteUtils.unsignedByteToInt(data);
		String temp = Integer.toBinaryString(value);
		if (temp.length() < 8){
			int len = 8 - temp.length();
			for (int i = 0; i < len; i++){
				temp = "0" + temp;
			}
		}
		return temp;
	}
	
	public static int generatorDataFrameSeq(){
/*		try{
			lock.lock();
			if (dataFrameSeq.get() >= Integer.MAX_VALUE){
				dataFrameSeq = new AtomicInteger(dataFrameSeqInit);
			}
			return dataFrameSeq.incrementAndGet();
		} finally{
			if (lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}*/
		
		if (dataFrameSeq.get() >= Integer.MAX_VALUE){
			dataFrameSeq = new AtomicInteger(dataFrameSeqInit);
		}
		return dataFrameSeq.incrementAndGet();

	}

	public static void main(String[] args) throws Exception {

		byte[] bb = HexUtils.hexString2Bytes("002e1000000000000000010504c4e439000822460000000000004b0000000000000000000000000000000000");
		int CRC = CRC16(bb, bb.length);
		System.out.println(CRC);
		System.out.println(HexUtils.bytes2HexString(unsignedShortToByte2(CRC)));
		
		byte[] cc = HexUtils.hexString2Bytes("000e10000000002100000205");
		int CRC2 = CRC16(cc, cc.length);
		System.out.println(CRC2);
		System.out.println(HexUtils.bytes2HexString(unsignedShortToByte2(CRC2)));
		

		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
/*		String cardNumber = "99998881";
		int multiple = cardNumber.length() / 2;
		int remainder = cardNumber.length() % 2;
		int total = multiple + remainder;
		int temp = 6 - total;
		if (temp > 0){
			for (int i = 0; i < temp; i++){
				buf.writeByte((byte) 0x00);
			}
		}
		int start = 0;
		for (int i = 0; i < total; i++){
			int end = start + 2;
			if (end > cardNumber.length()){
				end = cardNumber.length();
			}
			buf.writeByte(Byte.valueOf(cardNumber.substring(start, end)));
			start += 2;
		}
		
		byte[] aaData = new byte[6];
		buf.readBytes(aaData);
		System.out.println(HexUtils.bytes2HexString(aaData));
		byte[] dst = ByteUtils.longToByteArray(99998881l);
		System.out.println(buf.readCharSequence(6, Charset.defaultCharset()));
		System.out.println(HexUtils.bytes2HexString(dst));
		System.out.println(ByteUtils.bytesToLong(dst));
*/

		buf.writeLong(18999998888l);
		byte[] dst = new byte[8];
		buf.readBytes(dst);
		System.out.println(dst);
		System.out.println(ByteBufUtil.hexDump(dst));
		long dd = 1710251023132465l;
		System.out.println(dd);
		
	    double payBalance = CurrencyUtils.sub(1.5,4.0);
	    double giveBalance = 1.5;
	    if (payBalance < 0.00){
	    	giveBalance = CurrencyUtils.add(giveBalance, payBalance);
	    	payBalance = 0.00;
	    	giveBalance = giveBalance >= 0.00 ? giveBalance : 0.00;
	    }
	    System.out.println("payBalance=" + payBalance + ", giveBalance=" + giveBalance);
	    
	    int dataFrameSeq = generatorDataFrameSeq();
	    System.out.println(HexUtils.encodeHex(ByteUtils.intToByte4(dataFrameSeq)));
	    System.out.println(generatorDataFrameSeq());
	    System.out.println(generatorDataFrameSeq());
	    
		System.out.println(HexUtils.appendPrefixHex(ByteUtils.shortToByteArray((short) 70000)));
		System.out.println(HexUtils.appendPrefixHex(ByteUtils.intToByte4(70000)));
		
		Map<String, String> map = new ConcurrentHashMap<String,String>();
		map.put("80011235_1", "80011235_1");
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()){
			String key = it.next();
			if (key.startsWith("80011235_")){
				System.out.println(map.get(key));
			}
		}
		
		//System.out.println(CurrencyUtils.div(7.5, 0.8));
	//	System.out.println(CurrencyUtils.div(9.38, 30));
		
		
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		System.out.println(generatorDataFrameSeq());
		
	}
}
