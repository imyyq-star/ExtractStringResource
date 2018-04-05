package com.imyyq.plugin.android.util;

import com.google.gson.Gson;
import com.intellij.openapi.ui.Messages;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by yyq on 18/4/4.
 */
public class TranslateUtil
{
    private static final String mUrl = "http://fanyi.youdao.com/openapi.do";

    public static String requestTranslate(String text)
    {
        String params = null;
        try
        {
            params = "keyfrom=testtranlate1rrrr&key=1257547986&type=data&doctype=json&version=1.1&only=translate&q=" + URLEncoder.encode(
                    text, "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        String res = HttpRequest.sendGet(mUrl, params);
        Gson gson = new Gson();
        TranslateBean bean = gson.fromJson(res, TranslateBean.class);

        if (bean.getErrorCode() == 0 && !bean.getTranslation().isEmpty())
        {
            String result = bean.getTranslation().get(0).toLowerCase().trim().replace(' ',
                    '_').replaceAll("[^A-Za-z0-9]+", "_");
            return result;
        }
        else
        {
            Messages.showMessageDialog("translate error = " + bean.getErrorCode(), "Information",
                    Messages.getInformationIcon());
        }
        return null;
    }
}
