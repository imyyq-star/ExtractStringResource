package com.imyyq.plugin.android.entity;

import java.util.List;

public class TransResult
{
    /**
     * from : zh
     * to : en
     * trans_result : [{"src":"高度600米","dst":"Height of 600 meters"}]
     */

    private String from;
    private String to;

    private String error_code;
    private String error_msg;

    private List<TransResultBean> trans_result;

    public String getError_code()
    {
        return error_code;
    }

    public void setError_code(String error_code)
    {
        this.error_code = error_code;
    }

    public String getError_msg()
    {
        return error_msg;
    }

    public void setError_msg(String error_msg)
    {
        this.error_msg = error_msg;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public List<TransResultBean> getTrans_result()
    {
        return trans_result;
    }

    public void setTrans_result(List<TransResultBean> trans_result)
    {
        this.trans_result = trans_result;
    }

    public static class TransResultBean
    {
        /**
         * src : 高度600米
         * dst : Height of 600 meters
         */

        private String src;
        private String dst;

        public String getSrc()
        {
            return src;
        }

        public void setSrc(String src)
        {
            this.src = src;
        }

        public String getDst()
        {
            return dst;
        }

        public void setDst(String dst)
        {
            this.dst = dst;
        }
    }
}