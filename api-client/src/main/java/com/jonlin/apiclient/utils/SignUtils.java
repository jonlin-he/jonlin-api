package com.jonlin.apiclient.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 *  签名工具
 */
public class SignUtils {

    private static final String salt = "jonlin";

    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return
     */
    public static String genSign(String body, String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body + salt + secretKey;
        return md5.digestHex(content);
    }
}
