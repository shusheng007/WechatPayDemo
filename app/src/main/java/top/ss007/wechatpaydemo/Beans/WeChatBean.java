package top.ss007.wechatpaydemo.Beans;

/**
 * Copyright (C) 2018 科技发展有限公司
 * 完全享有此软件的著作权，违者必究
 *
 * @author ben
 * @version 1.0
 * @modifier
 * @createDate 2018/1/15 16:32
 * @description
 */

public class WeChatBean
{
    private String appId;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timeStamp;
    private String sign;
    private String extData;
    private String signType;

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getPartnerId()
    {
        return partnerId;
    }

    public void setPartnerId(String partnerId)
    {
        this.partnerId = partnerId;
    }

    public String getPrepayId()
    {
        return prepayId;
    }

    public void setPrepayId(String prepayId)
    {
        this.prepayId = prepayId;
    }

    public String getNonceStr()
    {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr)
    {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getExtData()
    {
        return extData;
    }

    public void setExtData(String extData)
    {
        this.extData = extData;
    }

    public String getSignType()
    {
        return signType;
    }

    public void setSignType(String signType)
    {
        this.signType = signType;
    }

    @Override
    public String toString()
    {
        return "WeChatBean{" +
                "appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", sign='" + sign + '\'' +
                ", extData='" + extData + '\'' +
                ", signType='" + signType + '\'' +
                '}';
    }
}
