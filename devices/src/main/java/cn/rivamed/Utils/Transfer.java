package cn.rivamed.Utils;


import android.util.Base64;

public class Transfer {
    public static String Byte2String(byte[] buf) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < buf.length; i++) {
            int v = buf[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String Byte2StringAscII(byte[] buf) {
        if (buf == null) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : buf) {
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString().toUpperCase();
    }

    //base64字符串转byte[]
    public static byte[] base64String2ByteFun(String base64Str) {
        return Base64.decode(base64Str,Base64.DEFAULT);


    }

    //byte[]转base64
    public static String byte2Base64StringFun(byte[] b) {
        return Base64.encodeToString(b,Base64.DEFAULT);
    }
}