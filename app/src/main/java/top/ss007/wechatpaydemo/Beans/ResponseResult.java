package top.ss007.wechatpaydemo.Beans;

import com.google.gson.JsonElement;

public class ResponseResult
{
/*"{    "body":{\"face\":\"988608\",\"balance\":\"6216.00\",\"meal\":\"185016\"},
        "code":"0",
        "status":"0"}"*/

    private JsonElement body;
    private String code;
    private String status;

    public JsonElement getBody()
    {
        return body;
    }

    public void setBody(JsonElement body)
    {
        this.body = body;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ResponseResult{" +
                "body='" + body + '\'' +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
