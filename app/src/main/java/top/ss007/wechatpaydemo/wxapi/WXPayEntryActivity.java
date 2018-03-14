package top.ss007.wechatpaydemo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import top.ss007.wechatpaydemo.AppConstant;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
    private Activity mAct;
	// TODO: 2018/3/14 配置上你的APPID
	private final static String WE_CHAT_APP_ID=" ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);
        mAct=this;
    	api = WXAPIFactory.createWXAPI(this, WE_CHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
    	if (req instanceof PayReq)
		{
			//Logger.t(TAG).d("是子类");
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		//Logger.t(TAG).d("onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
		{
			switch (resp.errCode) {
				case 0:
					mAct.sendBroadcast(new Intent(AppConstant.BROAD_ANCTION_PAY_SUCCESS));
					break;
				case -1:
					Toast.makeText(mAct,"支付失败",Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(mAct,"支付取消",Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(mAct,"支付失败",Toast.LENGTH_SHORT).show();
					break;
			}
			finish();
		}
	}
}