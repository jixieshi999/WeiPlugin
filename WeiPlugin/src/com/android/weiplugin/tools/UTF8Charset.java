package com.android.weiplugin.tools;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.text.GetChars;

import com.android.weiplugin.log.LogTools;

/**
 * <p>To judge whether the byte array is a UTF-8 encoding
 * @author w00217005 2012-6-14
 * @see
 * @since 1.0
 */
public class UTF8Charset {
	private final static String charset="UTF-8";
	private final static byte[] oneByte={(byte)0x00};
	private final static byte[] twoBytes={(byte)0xC0,(byte)0x80};
	private final static byte[] threeBytes={(byte)0xE0,(byte)0x80,(byte)0x80};
	private final static byte[] fourBytes={(byte)0xF0,(byte)0x80,(byte)0x80,(byte)0x80};
	private final static List<byte[]> utf8Standard=new ArrayList<byte[]>();
	
	static {
		utf8Standard.add(oneByte);
		utf8Standard.add(twoBytes);
		utf8Standard.add(threeBytes);
	}
	
	private byte[] targetByteArray;
	private int count=200;//Maximum check the number of bytes
	
	public UTF8Charset(byte[] byteArray, int count){
		this.targetByteArray=byteArray;
		this.count=count<targetByteArray.length?count:targetByteArray.length;
	}
	
	public UTF8Charset(byte[] byteArray){
		this.targetByteArray=byteArray;
		this.count=this.count<targetByteArray.length?this.count:targetByteArray.length;
	}
	   /**
     * @param buffer 
     * @param start. the start position of buffer that will be decoded.
     * @return
     * if the byte array encoding is UTF-8, decode the bytes with UTF-8.
     */
    public static String bytesToUTF8(byte[] buffer,int start){
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = cs.decode(ByteBuffer.wrap(buffer, start,
                buffer.length - start));
        int len = cb.length();
        char[] c = cb.array();
        return new String(c, 0, len);
    }
    private static String ByteArrayToString(byte[] ba, boolean swap) {
        if (ba == null) {
            return null;
        }

        char[] c = new char[ba.length / 2 - 1];
        if (swap) {
            for (int i = 2; i < ba.length; i += 2) {
                int tmp = 0;
                tmp = (ba[i + 1] & 0xFF) << 8 | (ba[i] & 0xFF);
                c[i / 2 - 1] = (char) (tmp & 0xFFFF);
            }
        } else {
            for (int i = 2; i < ba.length; i += 2) {
                int tmp = 0;
                tmp = (ba[i] & 0xFF) << 8 | (ba[i + 1] & 0xFF);
                c[i / 2 - 1] = (char) (tmp & 0xFFFF);
            }
        }

        return new String(c);
    }

    public static String getStrings(byte[] buffer) {

        String str="";
        try {
            if (buffer.length > 3 && buffer[0] == (byte) 0xEF
                    && buffer[1] == (byte) 0xBB && buffer[2] == (byte) 0xBF) {
                // UTF-8
                /*
                 * DTS2012052307795 wangshuang 20120618 for decoding the bytes
                 * with UTF-8 begin
                 */
                // Charset cs = Charset.forName("UTF-8");
                // CharBuffer cb = cs.decode(ByteBuffer.wrap(buffer, 3,
                // buffer.length - 3));
                // int len = cb.length();
                // char[] c = cb.array();
                // lrc = new String(c, 0, len);
                str = bytesToUTF8(buffer, 3);
                /*
                 * DTS2012052307795 wangshuang 20120618 for decoding the bytes
                 * with UTF-8 end
                 */
            } else if (buffer.length > 2 && buffer[0] == (byte) 0xFE
                    && buffer[1] == (byte) 0xFF) {
                // UTF-16/UCS-2, big endian
                str = ByteArrayToString(buffer, false);
            } else if (buffer.length > 2 && buffer[0] == (byte) 0xFF
                    && buffer[1] == (byte) 0xFE) {
                // UTF-16/UCS-2, little endian
                str = ByteArrayToString(buffer, true);
            } else {
                /*
                 * DTS2012052307795 wangshuang 20120618 for decoding the bytes
                 * with UTF-8 begin
                 */
                if (new UTF8Charset(buffer).getCharset(0) == null)// is not
                                                                  // UTF-8
                                                                  // encoding
                    str = new String(buffer, "GBK");
                else {
                    str = bytesToUTF8(buffer, 0);
                }
                /*
                 * DTS2012052307795 wangshuang 20120618 for decoding the bytes
                 * with UTF-8 end
                 */
            }
        } catch (UnsupportedEncodingException e) {
            LogTools.logToFile("UTF8Charset", e);
        }
        return str;
    }
	/**
	 * @param stardard
	 * @param target
	 * @return
	 * if the target match standard,return true; else retunr false;
	 */
	public boolean matchStandard(byte[] standard, byte[] target){
		for(int i=0;i<standard.length;i++){
			if((target[i] & ((standard[i]>>>1) | (byte)0x80))!=standard[i])
				return false;
		}
		return true;
	}
	
	/**
	 * @param index
	 * @return
	 * get charset. If array is not UTF-8,return null;else,return 'UTF-8';
	 */
	public String getCharset(int index){
		if(count-index<utf8Standard.get(utf8Standard.size()-1).length)
			return null;
		
		for(int i=0;i<utf8Standard.size();i++){
			byte[] standard=utf8Standard.get(i);
			byte[] target=new byte[standard.length];
			System.arraycopy(targetByteArray,index,target,0,standard.length);
                    
			if(matchStandard(standard,target)){
				if(i==utf8Standard.size()-1)
					return charset;
				else
					return getCharset(index+standard.length); 
			}
		}
		
		return null;
	}
}