package com.imyyq.plugin.android;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by imyyq on 16/6/13.
 */
public class ExtractStringResourceConfiguration
{
    private JPanel mPanel;

    private JTextField mPrefix; // 前缀
    private JTextField mAppId; // AppID
    private JTextField mSecurityKey; // 密钥

    public ExtractStringResourceConfiguration(String prefix, String appId, String securityKey)
    {
        mPrefix.setText(prefix);
        mAppId.setText(appId);
        mSecurityKey.setText(securityKey);
    }

    public JComponent getComponent()
    {
        return mPanel;
    }

    // 取得前缀
    public String getPrefix()
    {
        return mPrefix.getText();
    }

    // 取得AppID
    public String getAppId()
    {
        return mAppId.getText();
    }

    // 取得密钥
    public String getSecurityKey()
    {
        return mSecurityKey.getText();
    }

    public void reset()
    {
        mPrefix.setText("");
        mAppId.setText("");
        mSecurityKey.setText("");
    }
}
