package com.msht.retailwater.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
/**
 * 字节工具
 * 
 * @author lindaofen
 *
 */
public class ByteUtils {


	/**
	 * 
	 * <pre>
	 * 将4个byte数字组成的数组合并为一个float数.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static float byte4ToFloat(byte[] arr) {
		if (arr == null || arr.length != 4) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
		}
		int i = byte4ToInt(arr);
		return Float.intBitsToFloat(i);
	}

	/**
	 * 
	 * <pre>
	 * 将一个float数字转换为4个byte数字组成的数组.
	 * </pre>
	 * 
	 * @param f
	 * @return
	 */
	public static byte[] floatToByte4(float f) {
		int i = Float.floatToIntBits(f);
		return intToByte4(i);
	}

	/**
	 * 
	 * <pre>
	 * 将八个byte数字组成的数组转换为一个double数字.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static double byte8ToDouble(byte[] arr) {
		if (arr == null || arr.length != 8) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是8位!");
		}
		long l = byte8ToLong(arr);
		return Double.longBitsToDouble(l);
	}

	/**
	 * 
	 * <pre>
	 * 将一个double数字转换为8个byte数字组成的数组.
	 * </pre>
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] doubleToByte8(double i) {
		long j = Double.doubleToLongBits(i);
		return longToByte8(j);
	}

	/**
	 * 
	 * <pre>
	 * 将一个char字符转换为两个byte数字转换为的数组.
	 * </pre>
	 * 
	 * @param c
	 * @return
	 */
	public static byte[] charToByte2(char c) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (c >> 8);
		arr[1] = (byte) (c & 0xff);
		return arr;
	}

	/**
	 * 
	 * <pre>
	 * 将2个byte数字组成的数组转换为一个char字符.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static char byte2ToChar(byte[] arr) {
		if (arr == null || arr.length != 2) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
		}
		return (char) (((char) (arr[0] << 8)) | ((char) arr[1]));
	}

	/**
	 * 
	 * <pre>
	 * 将一个16位的short转换为长度为2的8位byte数组.
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByte2(Short s) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (s >> 8);
		arr[1] = (byte) (s & 0xff);
		return arr;
	}

	/**
	 * 
	 * <pre>
	 * 长度为2的8位byte数组转换为一个16位short数字.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static short byte2ToShort(byte[] arr) {
		if (arr != null && arr.length != 2) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
		}
		return (short) (((short) arr[0] << 8) | ((short) arr[1] & 0xff));
	}

	/**
	 * 
	 * <pre>
	 * 将short转换为长度为16的byte数组.
	 * 实际上每个8位byte只存储了一个0或1的数字
	 * 比较浪费.
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByte16(short s) {
		byte[] arr = new byte[16];
		for (int i = 15; i >= 0; i--) {
			arr[i] = (byte) (s & 1);
			s >>= 1;
		}
		return arr;
	}

	public static short byte16ToShort(byte[] arr) {
		if (arr == null || arr.length != 16) {
			throw new IllegalArgumentException("byte数组必须不为空,并且长度为16!");
		}
		short sum = 0;
		for (int i = 0; i < 16; ++i) {
			sum |= (arr[i] << (15 - i));
		}
		return sum;
	}

	/**
	 * 
	 * <pre>
	 * 将32位int转换为由四个8位byte数字.
	 * </pre>
	 * 
	 * @param sum
	 * @return
	 */
	public static byte[] intToByte4(int sum) {
		byte[] arr = new byte[4];
		arr[0] = (byte) (sum >> 24);
		arr[1] = (byte) (sum >> 16);
		arr[2] = (byte) (sum >> 8);
		arr[3] = (byte) (sum & 0xff);
		return arr;
	}

	/**
	 * <pre>
	 * 将长度为4的8位byte数组转换为32位int.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static int byte4ToInt(byte[] arr) {
		if (arr == null || arr.length != 4) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
		}
		return (int) (((arr[0] & 0xff) << 24) | ((arr[1] & 0xff) << 16) | ((arr[2] & 0xff) << 8) | ((arr[3] & 0xff)));
	}

	/**
	 * 
	 * <pre>
	 * 将长度为8的8位byte数组转换为64位long.
	 * </pre>
	 * 
	 * 0xff对应16进制,f代表1111,0xff刚好是8位 byte[]
	 * arr,byte[i]&0xff刚好满足一位byte计算,不会导致数据丢失. 如果是int计算. int[] arr,arr[i]&0xffff
	 * 
	 * @param arr
	 * @return
	 */
	public static long byte8ToLong(byte[] arr) {
		if (arr == null || arr.length != 8) {
			throw new IllegalArgumentException("byte数组必须不为空,并且是8位!");
		}
		return (long) (((long) (arr[0] & 0xff) << 56) | ((long) (arr[1] & 0xff) << 48) | ((long) (arr[2] & 0xff) << 40)
				| ((long) (arr[3] & 0xff) << 32) | ((long) (arr[4] & 0xff) << 24) | ((long) (arr[5] & 0xff) << 16)
				| ((long) (arr[6] & 0xff) << 8) | ((long) (arr[7] & 0xff)));
	}

	/**
	 * 将一个long数字转换为8个byte数组组成的数组.
	 */
	public static byte[] longToByte8(long sum) {
		byte[] arr = new byte[8];
		arr[0] = (byte) (sum >> 56);
		arr[1] = (byte) (sum >> 48);
		arr[2] = (byte) (sum >> 40);
		arr[3] = (byte) (sum >> 32);
		arr[4] = (byte) (sum >> 24);
		arr[5] = (byte) (sum >> 16);
		arr[6] = (byte) (sum >> 8);
		arr[7] = (byte) (sum & 0xff);
		return arr;
	}

	/**
	 * 
	 * <pre>
	 * 将int转换为32位byte.
	 * 实际上每个8位byte只存储了一个0或1的数字
	 * 比较浪费.
	 * </pre>
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] intToByte32(int num) {
		byte[] arr = new byte[32];
		for (int i = 31; i >= 0; i--) {
			// &1 也可以改为num&0x01,表示取最地位数字.
			arr[i] = (byte) (num & 1);
			// 右移一位.
			num >>= 1;
		}
		return arr;
	}

	/**
	 * 
	 * <pre>
	 * 将长度为32的byte数组转换为一个int类型值.
	 * 每一个8位byte都只存储了0或1的数字.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static int byte32ToInt(byte[] arr) {
		if (arr == null || arr.length != 32) {
			throw new IllegalArgumentException("byte数组必须不为空,并且长度是32!");
		}
		int sum = 0;
		for (int i = 0; i < 32; ++i) {
			sum |= (arr[i] << (31 - i));
		}
		return sum;
	}

	/**
	 * 
	 * <pre>
	 * 将长度为64的byte数组转换为一个long类型值.
	 * 每一个8位byte都只存储了0或1的数字.
	 * </pre>
	 * 
	 * @param arr
	 * @return
	 */
	public static long byte64ToLong(byte[] arr) {
		if (arr == null || arr.length != 64) {
			throw new IllegalArgumentException("byte数组必须不为空,并且长度是64!");
		}
		long sum = 0L;
		for (int i = 0; i < 64; ++i) {
			sum |= ((long) arr[i] << (63 - i));
		}
		return sum;
	}

	/**
	 * 
	 * <pre>
	 * 将一个long值转换为长度为64的8位byte数组.
	 * 每一个8位byte都只存储了0或1的数字.
	 * </pre>
	 * 
	 * @param sum
	 * @return
	 */
	public static byte[] longToByte64(long sum) {
		byte[] arr = new byte[64];
		for (int i = 63; i >= 0; i--) {
			arr[i] = (byte) (sum & 1);
			sum >>= 1;
		}
		return arr;
	}
	
	/**
     * 将一个单字节的byte转换成32位的int
     * 
     * @param byte
     * @return convert result
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * 将一个单字节的Byte转换成十六进制的数
     * 
     * @param byte
     * @return convert result
     */
    public static String byteToHex(byte b) {
        int i = b & 0xFF;
        return Integer.toHexString(i);
    }

    /**
     * 将一个4byte的数组转换成32位的int
     * 
     * @param buf  bytes buffer
     * @param byte[]中开始转换的位置
     * @return convert result
     */
    public static long unsigned4BytesToInt(byte[] buf, int pos) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = pos;
        firstByte = (0x000000FF & ((int) buf[index]));
        secondByte = (0x000000FF & ((int) buf[index + 1]));
        thirdByte = (0x000000FF & ((int) buf[index + 2]));
        fourthByte = (0x000000FF & ((int) buf[index + 3]));
        index = index + 4;
        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

    /**
     * 将16位的short转换成byte数组
     * 
     * @param s
     *            short
     * @return byte[] 长度为2
     * */
    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

/*    *//**
     * 将32位整数转换成长度为4的byte数组
     * 
     * @param s
     *            int
     * @return byte[]
     * *//*
    public static byte[] intToByteArray(int s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    	ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(4);
    	buf.writeInt(s);
    	byte[] dst = new byte[4];
    	buf.readBytes(dst);
    	return dst;
    }*/

    /**
     * long to byte[]
     * 
     * @param s
     *            long
     * @return byte[]
     * */
    public static byte[] longToByteArray(long s) {
        byte[] targets = new byte[6];
        for (int i = 0; i < 6; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }
    
    public static long bytesToLong( byte[] array )  
    {  
        return ( (((long) array[0] & 0xff) << 40)  
              | (((long) array[1] & 0xff) << 32)  
              | (((long) array[2] & 0xff) << 24)  
              | (((long) array[3] & 0xff) << 16)  
              | (((long) array[4] & 0xff) << 8)   
              | (((long) array[5] & 0xff) << 0));          
    }  
    
    public static String bytesToString(byte[] b) {  
    	  StringBuffer result = new StringBuffer("");  
    	  int length = b.length;  
    	  for (int i=0; i<length; i++) {  
    	    result.append((char)(b[i] & 0xff));  
    	  }  
    	  return result.toString();  
    }   

    /**32位int转byte[]*/
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    /**
     * 将长度为2的byte数组转换为16位int
     * 
     * @param res
     *            byte[]
     * @return int
     * */
    public static int byte2int(byte[] res) {
        // res = InversionByte(res);
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
        return targets;
    }
    
    public static void main(String[] args){
    	System.out.println(ByteUtils.unsignedByteToInt((byte)8));
    }
}
