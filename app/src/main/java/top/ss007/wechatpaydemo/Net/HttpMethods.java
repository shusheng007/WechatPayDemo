package top.ss007.wechatpaydemo.Net;

import com.google.gson.Gson;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.ss007.wechatpaydemo.Beans.ResponseResult;

/**
 * Refactor by ben on 2017/3/30
 */
public class HttpMethods
{
    private static final String TAG = HttpMethods.class.getSimpleName();

    private static final int DEFAULT_TIMEOUT = 20;
    private static String SERVER_SITE = "http://shusheng007.top:8888/sstxServer/";
    //private static String SERVER_SITE = "http://192.168.10.34:8181/";


    private Retrofit retrofit;

    //构造方法私有
    private HttpMethods()
    {
        HttpLoggingInterceptor  logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(35L, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))  //gson 转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // Rx 工厂。
                .baseUrl(SERVER_SITE)
                .build();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder
    {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance()
    {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 发起请求
     *
     * @param subscriber
     * @param interfaceName 接口名称.
     * @param reqParamMap   请求参数
     */
    public void startServerRequest(Observer<String> subscriber, String interfaceName, Map<String, String> reqParamMap)
    {
        //Logger.t(TAG).d("请求参数》"+"接口："+interfaceName+"参数："+AppController.getInstance().getGsonInstance().toJson(reqParamMap));
        CommonQueueService service = retrofit.create(CommonQueueService.class);
        Observable<String> observable = service.postRxBody(interfaceName,reqParamMap)
                .map(new ResponseResultMapper());
        toSubscribe(observable, subscriber);
    }

    public Flowable<ResponseResult> getApiService(String interfaceName, Map<String, String> reqParamMap)
    {
        CommonQueueService service = retrofit.create(CommonQueueService.class);
        return  service.postRx2Body(interfaceName, reqParamMap);

    }

    //观察者启动器
    private <T> void toSubscribe(Observable<T> o, Observer<T> s)
    {
        o.subscribeOn(Schedulers.io()) //绑定在io
                .observeOn(AndroidSchedulers.mainThread()) //返回 内容 在Android 主线程
                .subscribe(s);  //放入观察者
    }

    /**
     * 将后台返回的结果转化为需要的类型：ResponseResult
     */
    private class ResponseResultMapper implements Function<ResponseResult, String>
    {
        @Override
        public String apply(ResponseResult httpResult) throws Exception
        {
            if (httpResult == null)
            {
                throw new NullPointerException("|**************返回结果错误***************|");
            }
            //将服务器错误抛出
            if ("1".equals(httpResult.getStatus()))
            {
                throw new ApiException(httpResult.getCode(), httpResult.getBody().toString());
            }
            return httpResult.getBody().toString();
        }
    }
}
