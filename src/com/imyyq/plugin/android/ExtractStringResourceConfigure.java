package com.imyyq.plugin.android;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by imyyq on 16/6/9.
 */
public class ExtractStringResourceConfigure
        implements Configurable {

    // 存储
    public final static String KEY_PREFIX = "KEY_PREFIX";
    public final static String KEY_APP_ID = "KEY_APP_ID";
    public final static String KEY_SECURITY_KEY = "KEY_SECURITY_KEY";

    private String mPrefix = "";
    private String mAppId = "";
    private String mSecurityKey = "";

    private ExtractStringResourceConfiguration configuration = null;

    private PropertiesComponent mPersistent = null;

    private boolean mIsModified = false;

    public ExtractStringResourceConfigure() {
        mPersistent = PropertiesComponent.getInstance();
        if(mPersistent != null){
            mPrefix = mPersistent.getValue(KEY_PREFIX, "");
            mAppId = mPersistent.getValue(KEY_APP_ID, "");
            mSecurityKey = mPersistent.getValue(KEY_SECURITY_KEY, "");
        }
    }

    // 显示在设置中的条目名称
    @Nls
    @Override
    public String getDisplayName() {
        return "Auto extract string resource";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    // 创建面板
    @Nullable
    @Override
    public JComponent createComponent() {
        if(configuration == null){
            configuration = new ExtractStringResourceConfiguration(mPrefix, mAppId, mSecurityKey);
        }
        return configuration.getComponent();
    }

    @Override
    public boolean isModified() {
        if(configuration == null){
            return false;
        }
        mIsModified = !mPrefix.contentEquals(configuration.getPrefix()) ||
        !mAppId.contentEquals(configuration.getAppId()) ||
        !mSecurityKey.contentEquals(configuration.getSecurityKey());
        return mIsModified;
    }

    // 生效
    @Override
    public void apply() throws ConfigurationException {
        if(configuration != null){
            mPrefix = configuration.getPrefix();
            mAppId = configuration.getAppId();
            mSecurityKey = configuration.getSecurityKey();
            if(mPersistent != null) {
                mPersistent.setValue(KEY_PREFIX, mPrefix);
                mPersistent.setValue(KEY_APP_ID, mAppId);
                mPersistent.setValue(KEY_SECURITY_KEY, mSecurityKey);
            }
        }
    }

    // 重置
    @Override
    public void reset() {
        if(mIsModified) {
            if (configuration != null) {
                mPrefix = "";
                mAppId = "";
                mSecurityKey = "";
                configuration.reset();
            }
        }
    }

    @Override
    public void disposeUIResources() {
        configuration = null;
    }

}
