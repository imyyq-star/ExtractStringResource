package com.imyyq.plugin.android.entity;

import com.imyyq.plugin.android.util.TransApi;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.xml.XmlFile;

/**
 * Created by imyyq on 2017/4/16.
 */
public class WriteObj
{

    private String translationName;
    private String value;

    private String prefix;

    private PsiFile psiFile;

    public WriteObj(String appid, String securityKey, PsiFile psiFile, String prefix,
                    String value, String translationName)
    {
        this.psiFile = psiFile;
        this.prefix = prefix;

        this.value = value;
        this.translationName = translationName;
    }

    public String getValue()
    {
        return value;
    }

    public String getTranslationName()
    {
        return translationName;
    }

    public void setTranslationName(String translationName)
    {
        this.translationName = translationName;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getWriteToStringsXMLStr()
    {
        return "<string name=\"" + translationName + "\">" + value + "</string>";
    }

    public String getReplaceWriteString()
    {
        String s = getWriteToJavaStr();
        return s == null
                ? getWriteToXMLStr()
                : s;
    }

    private String getWriteToJavaStr()
    {
        if (psiFile instanceof PsiJavaFile)
        {
            return prefix + ".getString(R.string." + translationName + ")";
        }
        return null;
    }

    private String getWriteToXMLStr()
    {
        if (psiFile instanceof XmlFile)
        {
            return "@string/" + translationName;
        }
        return null;
    }
}