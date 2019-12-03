package com.github.common.util;

import com.github.common.encrypt.Encrypt;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class SignUtil {

    /** 两端约定好的 key, 可以存 db */
    private static final String DEFAULT_KEY = "af7b78e6e02249b398f9819947fdaf21";
    /** 请求时的签名 key */
    private static final String SIGN = "sign";


    /**
     * <pre>
     * 发起请求前使用默认的密钥处理一下参数
     *
     * 比如实际要发送的数据是: id=123&type=2&phone=13012345678&name=xx&desc=中文
     *
     * 计算:
     *   参数名排序后: desc=中文&id=123&name=xx&phone=13012345678&type=2
     *   约定的密钥是: af7b78e6e02249b398f9819947fdaf21
     *   全部拼起来后: desc=中文&id=123&name=xx&phone=13012345678&type=2&af7b78e6e02249b398f9819947fdaf21
     *   上面的 md5 : 27cbbf74d8e68058fca0ce99ca596555
     *
     * 想要发送的数据是: id=123&type=2&phone=13012345678&name=xx&desc=中文
     * 最后发送的参数是: id=123&type=2&phone=13012345678&name=xx&desc=中文&sign=27cbbf74d8e68058fca0ce99ca596555
     * </pre>
     */
    public static Map<String, String[]> handleSign(Map<String, String[]> parameterMap) {
        return handleSign(parameterMap, DEFAULT_KEY);
    }
    /** 发起请求前使用指定的密钥处理一下参数 */
    public static Map<String, String[]> handleSign(Map<String, String[]> parameterMap, String key) {
        if (A.isEmpty(parameterMap)) {
            return Collections.emptyMap();
        } else {
            parameterMap.put(SIGN, new String[]{sign(parameterMap, key)});
            return parameterMap;
        }
    }

    /**
     * <pre>
     * 比如实际要发送的数据是: id=123&type=2&phone=13012345678&name=xx&desc=中文
     *
     * 计算:
     *   参数名排序后: desc=中文&id=123&name=xx&phone=13012345678&type=2
     *   传入的密钥是: af7b78e6e02249b398f9819947fdaf21
     *   全部拼起来后: desc=中文&id=123&name=xx&phone=13012345678&type=2&af7b78e6e02249b398f9819947fdaf21
     *   上面的 md5 : 27cbbf74d8e68058fca0ce99ca596555
     * </pre>
     */
    private static String sign(Map<String, String[]> paramMap, String key) {
        List<String> paramList = Lists.newArrayList();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String k = entry.getKey();
            if (!SIGN.equals(k)) {
                paramList.add(k + "=" + A.toStr(entry.getValue()));
            }
        }
        Collections.sort(paramList);
        String sign = A.toStr(paramList, "&") + "&" + key;
        return Encrypt.toMd5(sign);
    }

    /**
     * <pre>
     * 收到请求时使用默认的密钥检查参数是否正常
     *
     * 比如实际要发送的数据是: id=123&type=2&phone=13012345678&name=xx&desc=中文
     *
     * 计算:
     *   参数名排序后: desc=中文&id=123&name=xx&phone=13012345678&type=2
     *   约定的密钥是: af7b78e6e02249b398f9819947fdaf21
     *   全部拼起来后: desc=中文&id=123&name=xx&phone=13012345678&type=2&af7b78e6e02249b398f9819947fdaf21
     *   上面的 md5 : 27cbbf74d8e68058fca0ce99ca596555
     *
     * 想要发送的数据是: id=123&type=2&phone=13012345678&name=xx&desc=中文
     * 最后发送的参数是: id=123&type=2&phone=13012345678&name=xx&desc=中文&sign=27cbbf74d8e68058fca0ce99ca596555
     * </pre>
     */
    public static void checkSign(Map<String, String[]> parameterMap) {
        checkSign(parameterMap, DEFAULT_KEY);
    }
    /** 收到请求时使用指定的密钥检查参数是否正确 */
    public static void checkSign(Map<String, String[]> parameterMap, String ticketValue) {
        U.assertEmpty(parameterMap, "缺少参数");

        String sign = A.first(parameterMap.get(SIGN));
        U.assertNil(sign, "缺少必要的参数");
        U.assertException(!sign(parameterMap, ticketValue).equals(sign), "参数有误");
    }
}
