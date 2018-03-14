package top.ss007.wechatpaydemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import top.ss007.wechatpaydemo.Beans.WeChatBean;
import top.ss007.wechatpaydemo.Net.ApiException;
import top.ss007.wechatpaydemo.Net.HttpMethods;
import top.ss007.wechatpaydemo.Net.SilenceSubscriber2;

public class MainActivity extends AppCompatActivity
{
    private final static String TAG=MainActivity.class.getSimpleName();

    private final static String FinancialC_MakeChargeYmbOrder= "financialC/makeChargeYmbOrder";//充值ymb
    private final static String FinancialC_QueryOrderPayState= "financialC/queryOrderPayState";//检查订单状态

    private TextView tvDes;
    private Button btnPay;
    private Activity mAct;
    private volatile String myRechargeOrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAct=this;
        tvDes=findViewById(R.id.tv_des);
        btnPay=findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                makeChargeYmbOrder(1);
            }
        });
        registerReceiver();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try
        {
            mAct.unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void registerReceiver()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstant.BROAD_ANCTION_PAY_SUCCESS);
        mAct.registerReceiver(mBroadcastReceiver, intentFilter);
    }


    public void makeChargeYmbOrder(int rPlanId)
    {
        final Map<String,String> reqMap= getCommonPartOfParam(null);
        reqMap.put("rPlanId",rPlanId+"");
        HttpMethods.getInstance().startServerRequest(new SilenceSubscriber2<String>()
        {
            @Override
            public void onNext(String response)
            {
                super.onNext(response);
                try
                {
                    JSONObject body=new JSONObject(response);
                    JSONObject prePayInfo=body.getJSONObject("payResult");
                    WeChatBean weChatBean=new WeChatBean();
                    weChatBean.setAppId(prePayInfo.getString("appid"));
                    weChatBean.setPrepayId(prePayInfo.getString("prepayid"));
                    weChatBean.setPartnerId(prePayInfo.getString("partnerid"));
                    weChatBean.setNonceStr(prePayInfo.getString("noncestr"));
                    weChatBean.setTimeStamp(prePayInfo.getString("timestamp"));
                    weChatBean.setSign(prePayInfo.getString("sign"));
                    weChatBean.setExtData(prePayInfo.getString("extData").split("!=end!=")[0]);
                    weChatPay(weChatBean);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, FinancialC_MakeChargeYmbOrder,reqMap);
    }

    private void queryOrderPayState(String myOrderId)
    {
        final Map<String,String> reqMap= getCommonPartOfParam(null);
        reqMap.put("myOrderId",myOrderId);
        HttpMethods.getInstance().startServerRequest(new SilenceSubscriber2<String>()
        {
            @Override
            public void onHandledError(ApiException apiE)
            {
                super.onHandledError(apiE);
            }

            @Override
            public void onHandledNetError(Exception e)
            {
                super.onHandledNetError(e);
            }

            @Override
            public void onNext(String response)
            {
                super.onNext(response);
               queryOrderPaySateCallback(response);
            }
        }, FinancialC_QueryOrderPayState,reqMap);
    }

    private void weChatPay(WeChatBean weChatBean)
    {
        IWXAPI payApi = WXAPIFactory.createWXAPI(mAct, weChatBean.getAppId());
        if (payApi != null)
            payApi.registerApp(weChatBean.getAppId());

        PayReq payReq = new PayReq();
        payReq.appId = weChatBean.getAppId();
        payReq.partnerId = weChatBean.getPartnerId();
        payReq.prepayId = weChatBean.getPrepayId();
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = weChatBean.getNonceStr();
        payReq.timeStamp = weChatBean.getTimeStamp();
        payReq.sign = weChatBean.getSign();
        payReq.extData = weChatBean.getExtData();
        myRechargeOrderId = weChatBean.getExtData();
        payApi.sendReq(payReq);
    }

    public void queryOrderPaySateCallback(String str)
    {
        try
        {
            JSONObject body=new JSONObject(str);
            //Logger.t(TAG).d("check result :"+body.getString("checkResult"));
            switch (body.getString("checkResult"))
            {
                case "SUCCESS":
                case "havePayed":
                    Toast.makeText(mAct,"支付成功",Toast.LENGTH_SHORT).show();
                    tvDes.setText("感谢您慷慨的赠与");
                    break;
                default:
                    Toast.makeText(mAct,str,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        btnPay.setText("调起微信支付");
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            //Logger.t(TAG).d("接收到了广播》action：" + action);
            btnPay.setText("支付结果确认中...");
            //支付成功状态确认
            if (action.equals(AppConstant.BROAD_ANCTION_PAY_SUCCESS))
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                       queryOrderPayState(myRechargeOrderId);
                    }
                }, 1000);
            }
        }
    };

    private Map<String, String> getCommonPartOfParam(@Nullable Context context)
    {
        Map<String, String> reqParamMap = new HashMap<>();
        reqParamMap.put("token", "af27636a122e43c09c4882c915896ca6");
        reqParamMap.put("deviceId", "");
        reqParamMap.put("uId", "13");
        return reqParamMap;
    }
}
