package lib.system;

import java.io.IOException;
import java.util.List;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import lib.system.tools.DLog;
import lib.system.uitle.ActivityCollector;
import lib.system.uitle.DialogUtils;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

public class BaseActivity extends FragmentActivity {
	protected static final int ERROR_CODE = -200;
	public SharedPreferences sp;
	public static String currentUserNick = "";
	RequestCall call ;
	static String TAG="BaseActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
//		ActivityCollector.addActivity(this);
	}

	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
		           int tag=	msg.what;
		           if (tag==ERROR_CODE) {
		        	   HttpCallBackError();//一般不需要复写该方法，特殊情况可打开注释复写错误的回调
		        	   DialogUtils.closeProgressDialog();
		        	   return;
				}
					Object src=msg.obj;
					HttpCallBackSucc(tag, src);
					DialogUtils.closeProgressDialog();
		}
	};
	
	protected void HttpCallBackSucc(int tag, Object src){}
	protected void HttpCallBackError(){}
	
	protected void getHttp(String urlString,String tag){
		this.TAG=tag;
		DialogUtils.showProgressDialog(this);
		call=OkHttpUtils.get().url(urlString).tag(tag).build();
		call.execute(new StringCallback() {
			@Override
			public void onResponse(String response, int arg1) {
				DLog.d("返回数据："+response+"int"+arg1);
				Message message=new Message();
				message.what=TAG.hashCode();				
				message.obj=response;
				handler.sendMessage(message);
			}
			@Override
			public void onError(Call arg0, Exception arg1, int arg2) {
				Message message=new Message();
				message.what=ERROR_CODE;	
				handler.sendMessage(message);
				DialogUtils.showToast("网络异常", 0);
				DLog.d("网络错误：："+arg0+"Exception："+arg1+"代码号："+arg2);
			}
		});
	 }
	
	 protected <T> void getHttp(String urlString,String tag,final Class<T> cls){
		 this.TAG=tag;		 
			call=OkHttpUtils.get().url(urlString).tag(tag).build();
			call.execute(new StringCallback() {
				@Override
				public void onResponse(String response, int arg1) {
					T t = null;
					try {
						Gson gson = new Gson();
						t = gson.fromJson(response.toString(), cls);
					} catch (Exception e) {
					}
					DLog.d("返回数据："+response+"int"+arg1);
					Message message=new Message();
					message.what=TAG.hashCode();				
					message.obj=t;
					handler.sendMessage(message);
				}
				@Override
				public void onError(Call arg0, Exception arg1, int arg2) {
					DLog.d("网络错误：："+arg0+"Exception："+arg1+"代码号："+arg2);
				}
			});
	 }
	 protected <T> void getHttpList(String urlString,String tag,final Class<T> cls){
		 this.TAG=tag;		 
		 call=OkHttpUtils.get().url(urlString).tag(tag).build();
		 call.execute(new StringCallback() {
			 @Override
			 public void onResponse(String response, int arg1) {
				 List<Class<T>> cls = null;
				 try {
					 Gson gson = new Gson();
					 cls =new Gson().fromJson(response.toString(),
								new TypeToken<List<Class<T>>>() {
								}.getType());
				 } catch (Exception e) {
				 }
				 DLog.d("返回数据："+response+"int"+arg1);
				 Message message=new Message();
				 message.what=TAG.hashCode();				
				 message.obj=cls;
				 handler.sendMessage(message);
			 }
			 @Override
			 public void onError(Call arg0, Exception arg1, int arg2) {
				 DLog.d("网络错误：："+arg0+"Exception："+arg1+"代码号："+arg2);
			 }
		 });
	 }
	 
	 
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (call!=null) {
			
			call.cancel();
		}
	     DLog.d("Pause中取消OKHttp请求：TAG="+TAG);
//		ActivityCollector.removeActivity(this);
	}

    
	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}
	@Override
	protected void onStart() {
		super.onStart();
	}

	
}

