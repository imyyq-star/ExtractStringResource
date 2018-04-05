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
    private JLabel mLable;
    private JTextField mPrefix;
    private JTextPane mBefore;
    private JTextPane mAfter;

    private String stringContext = ".getString(R.string.xxx);\r\n";

    private String mPrefixString = "";

    public ExtractStringResourceConfiguration(String prefix){

        mPrefixString = prefix;

        mPrefix.setText(prefix);

        mAfter.setText(mPrefixString + stringContext);

        mPrefix.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mPrefixString = mPrefix.getText();
                mAfter.setText(mPrefixString + stringContext);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mPrefixString = mPrefix.getText();
                mAfter.setText(mPrefixString + stringContext);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mPrefixString = mPrefix.getText();
                mAfter.setText(mPrefixString + stringContext);
            }
        });

    }

    public JComponent getComponent(){
        return mPanel;
    }

    public String getPrefix(){
        return mPrefixString;
    }

    public void reset(){
        mPrefixString = "";
        mPrefix.setText(mPrefixString);
    }
}
