package lib.system;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import lib.system.tools.DLog;
import lib.system.uitle.DialogUtils;
import okhttp3.Call;

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
	 
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		ActivityCollector.removeActivity(this);
	}

    

	
}

