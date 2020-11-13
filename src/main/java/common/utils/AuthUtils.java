package common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthUtils {
    //Переедет в автотирацию
    public static String md5(String string) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //Результат совпадает с postgresql select md5('...')
        StringBuilder stringBuilder = new StringBuilder();

        //Получаем массив байт в md5 и переводим его в строку, т.к. в бд он лежит в строке
        byte[] bytes = MessageDigest.getInstance("MD5").digest(string.getBytes());

        for (int i = 0; i < bytes.length; i += 2) {
            stringBuilder.append(Integer.toHexString((bytes[i] & 0xff) << 8 | (bytes[i+1] & 0xff)));
        }

        return stringBuilder.toString();
    }
}
