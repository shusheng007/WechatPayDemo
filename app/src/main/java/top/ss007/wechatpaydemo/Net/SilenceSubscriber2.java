package top.ss007.wechatpaydemo.Net;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by ben on 2017/3/30.
 */

public abstract class SilenceSubscriber2<T> implements Observer<T>
{
    public final static String TAG = SilenceSubscriber2.class.getSimpleName();

    @Override
    public void onComplete()
    {

    }

    @Override
    public void onSubscribe(Disposable d)
    {

    }
    //*************特殊的错误码需要在外部拦截处理，这里只负责外部没有处理的错误码***********
    @Override
    public void onError(Throwable e)
    {
        if (e instanceof SocketTimeoutException)
        {
            onHandledNetError((SocketTimeoutException)e);
            e.printStackTrace();
        }
        else if (e instanceof SocketException)
        {
            onHandledNetError((SocketException)e);
            e.printStackTrace();
        }
        else if (e instanceof ApiException)
        {
            String errCode=((ApiException)e).getErrorCode();
//            if (ErrorCodeTable.handleErrorCode(errCode, AppController.getInstance()))
//                return;
            onHandledError((ApiException) e);
        }
        else
        {
            e.printStackTrace();
        }
    }
    //要处理特殊的错误码，重写这个函数，不要重写onError了
    public void  onHandledError(ApiException apiE)
    {
//        String errMsg=ErrorCodeTable.parseErrorCode(apiE.getErrorCode());
//        if (!TextUtils.isEmpty(errMsg)&&!errMsg.equals("TOKEN_FAILURE"))
//            ToastUtils.showShort(errMsg);
    }

    public void  onHandledNetError(Exception e)
    {
//        Logger.t(TAG).d("onHandledNetError");
    }
    @Override
    public void onNext(T response)
    {
//        Logger.t(TAG).d("onNext>"+response);
    }
}
