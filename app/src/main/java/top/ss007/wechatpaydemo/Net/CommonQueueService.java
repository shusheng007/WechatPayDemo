package top.ss007.wechatpaydemo.Net;


import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import top.ss007.wechatpaydemo.Beans.ResponseResult;

public interface CommonQueueService
{
    //    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("")
    Observable<ResponseResult> postRxBody(@Url String interfaceName, @Body Map<String, String> reqParamMap);

    @POST("")
    Flowable<ResponseResult> postRx2Body(@Url String interfaceName, @Body Map<String, String> reqParamMap);

    //提交一个POST表单
    @FormUrlEncoded
    @POST("")
    Observable<ResponseResult> postRxString(@FieldMap Map<String, String> reqParamMap);

    //提交一个POST表单
    @FormUrlEncoded
    @POST("")
    Flowable<ResponseResult> postRx2String(@FieldMap Map<String, String> reqParamMap);
}
