package com.cy.data;

public class UtilData {

	/**
	 * 十六进制 转换 byte[]
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexString2ByteArray(String hexStr) {
		if (hexStr == null)
			return null;
		hexStr = hexStr.replaceAll(" ", "");
		if (hexStr.length() % 2 != 0) {
			return null;
		}
		byte[] data = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			char hc = hexStr.charAt(2 * i);
			char lc = hexStr.charAt(2 * i + 1);
			byte hb = hexChar2Byte(hc);
			byte lb = hexChar2Byte(lc);
			if (hb < 0 || lb < 0) {
				return null;
			}
			int n = hb << 4;
			data[i] = (byte) (n + lb);
		}
		return data;
	}

	public static byte hexChar2Byte(char c) {
		if (c >= '0' && c <= '9')
			return (byte) (c - '0');
		if (c >= 'a' && c <= 'f')
			return (byte) (c - 'a' + 10);
		if (c >= 'A' && c <= 'F')
			return (byte) (c - 'A' + 10);
		return -1;
	}

	public static String byte2HexChar(byte b) {
		String tmp = Integer.toHexString(0xFF & b);
		if (tmp.length() < 2)
			tmp = "0" + tmp;
		return tmp;
	}

	/**
	 * byte[] 转 16进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	public static String byteArray2HexString(byte[] arr) {
		StringBuilder sbd = new StringBuilder();
		for (byte b : arr) {
			String tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() < 2)
				tmp = "0" + tmp;
			sbd.append(tmp);
		}
		return sbd.toString();
	}

	/**
	 * 空格分隔的hex string
	 * 
	 * @param arr
	 * @return
	 */
	public static String byteArray2HexStringWithSpace(byte[] arr) {
		StringBuilder sbd = new StringBuilder();
		for (byte b : arr) {
			String tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() < 2)
				tmp = "0" + tmp;
			sbd.append(tmp);
			sbd.append(" ");
		}
		return sbd.toString();
	}

	/**
	 * 取start到end的byte array，包含end。
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	static public byte[] getData(byte[] data, int start, int end) {
		byte[] t = new byte[end - start + 1];
		System.arraycopy(data, start, t, 0, t.length);
		return t;
	}

	/**
	 * 从data取start到end的数据，返回bcd string。end包含在取值范围。
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	static public String getBCDString(byte[] data, int start, int end) {
		byte[] t = new byte[end - start + 1];
		System.arraycopy(data, start, t, 0, t.length);
		return UtilData.byteArray2HexString(t);
	}

	/**
	 * 从data取start到end的数据，返回hex string。end包含在取值范围。
	 * 
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	static public String getHexString(byte[] data, int start, int end) {
		byte[] t = new byte[end - start + 1];
		System.arraycopy(data, start, t, 0, t.length);
		return UtilData.byteArray2HexStringWithSpace(t);
	}

    /**字符串转16进制
     * eg:strToHexStr("\"我men.{123} \\\"")
     * @param str
     * @return
     */
    public static String strToHexStr(String str){
        String hex = null;
	    try {
            hex=byteArray2HexString(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hex;
    }

    /**16进制转字符串
     * eg:hexToStr("22e688916d656e2e7b3132337d205c22")
     * @param hex
     * @return
     */
    public static String hexToStr(String hex){
        byte[] baKeyword = new byte[hex.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        hex.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String result = null;
        try {
            result = new String(baKeyword, "UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }
    
    public static String intToBytes(int i){
    	return Integer.toBinaryString(i);
	}
}