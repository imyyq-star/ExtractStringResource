package com.imyyq.plugin.android.util;

import com.google.gson.Gson;
import com.imyyq.plugin.android.entity.TransResult;

import java.util.HashMap;
import java.util.Map;

public class TransApi
{
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static final Gson GSON = new Gson();

    public static TransResult getTransResult(String appid, String securityKey, String query, String from,
                                        String to)
    {
        Map<String, String> params = buildParams(appid, securityKey, to, query, from);
        String result = HttpGet.get(TRANS_API_HOST, params);
        if (result != null)
        {
            return GSON.fromJson(result, TransResult.class);
        }
        return null;
    }

    private static Map<String, String> buildParams(String appid, String securityKey, String to,
                                                   String query, String from)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

}
