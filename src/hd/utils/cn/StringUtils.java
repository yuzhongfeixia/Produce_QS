
package hd.utils.cn;

import org.ksoap2.serialization.SoapObject;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String encode(String str) {
        if (str == null) {
            return Constants.STR_EMPTY;
        } else {
            if (str.contains("/")) {
                str = str.replaceAll("/", "-");
            }
            if (str.contains(":")) {
                str = str.replaceAll(":", "-");
            }
            if (str.contains("<")) {
                str = str.replaceAll("<", "-");
            }
            if (str.contains(">")) {
                str = str.replaceAll(">", "-");
            }
            if (str.contains("?")) {
                str = str.replace("?", "-");
            }
            if (str.contains("&")) {
                str = str.replace("&", "-");
            }
            if (str.contains("#")) {
                str = str.replace("#", "-");
            }
            if (str.contains("\"")) {
                str = str.replace("\"", "-");
            }
            return str;
        }
    }

    public static String getMD5(String content) {
        if (content != null && !"".equals(content)) {
            try {
                MessageDigest digest = MessageDigest.getInstance(Constants.STR_MD5);
                digest.update(content.getBytes());
                return getHashString(digest);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return Constants.STR_EMPTY;
        }
        return "";
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder(Constants.STR_EMPTY);
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    public static boolean isEmpty(String content) {
        if (content == null || Constants.STR_EMPTY.equals(content)) {
            return true;
        }
        return false;
    }

    public static String getImageName(String url) {
        if (isEmpty(url)) {
            return Constants.STR_EMPTY;
        } else {
            String imageName = Constants.STR_EMPTY;
            if (url.contains("/")) {
                String[] strs = url.split("/");
                imageName = strs[strs.length - 1];
            }
            if (imageName.contains("?")) {
                String[] strs = imageName.split("[?]");
                imageName = strs[0];
            }
            return imageName;
        }
    }

    public static String markStr(String str) {
        if (isEmpty(str)) {
            str = Constants.STR_EMPTY;
        }
        return "\"" + str + "\"";
    }

    public static boolean emailCheck(String str) {
        String check = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*"
                + "@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*"
                + "((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    public static boolean phoneCheck(String str) {
        String check = "^1[3|4|5|8][0-9]" + "\\" + "d{4,8}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    public static boolean checkFull(String str) {
        String checkFull = "[\uFF00-\uFFFF]";
        Pattern regexFull = Pattern.compile(checkFull);
        Matcher matcherFull = regexFull.matcher(str);
        return matcherFull.matches();
    }

    public static boolean checkChinese(String str) {
        String checkChinese = "[\u4e00-\u9fa5]";
        Pattern regexChinese = Pattern.compile(checkChinese);
        Matcher matcherChinese = regexChinese.matcher(str);
        return matcherChinese.matches();
    }

    public static String splitStr(String str, String subStr) {
        if (str.startsWith(subStr) && str.length() > subStr.length()) {
            str = str.substring(subStr.length() + 1);
        }
        return str;
    }

    public static String getStrProperty(SoapObject soapObject, String key) {
        if (soapObject.hasProperty(key)) {
            return soapObject.getProperty(key).toString();
        }
        return Constants.STR_EMPTY;
    }

    public static Integer getIntProperty(SoapObject soapObject, String key) {
        if (soapObject.hasProperty(key)) {
            String result = getStrProperty(soapObject, key);
            if (TextUtils.isEmpty(result)) {
                return 0;
            } else {
                return Integer.valueOf(result);
            }
        }
        return 0;
    }

    public static Object getProperty(SoapObject soapObject, String key) {
        if (soapObject.hasProperty(key)) {
            return soapObject.getProperty(key);
        }
        return null;
    }
}
