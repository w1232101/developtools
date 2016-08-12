package lib.system.activity;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.system.lib.R;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import lib.system.BaseActivity;
import lib.system.MyOkHttpUtils;
import lib.system.bean.GankEntity;
import lib.system.bean.HttpResult;
import lib.system.uitle.ActivityCollector;
import lib.system.uitle.DialogUtils;
import lib.system.uitle.MyCallBack;

public class MainActivity extends BaseActivity {
	
	private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		MyOkHttpUtils.getInstance(this, new MyCallBack() {
			@Override
			public void httpCallBackSucc(int tag, Object src) {
				Log.i("wjx", "succ!");
				HttpResult result = (HttpResult) src;
				List<GankEntity> entity = result.getResults();
				Log.i("wjx", "entity:" + entity.toString());

				  Glide
	                .with(MainActivity.this)
	                .load(entity.get(0).getUrl())
	                .thumbnail(0.2f)
	                .into((ImageView) findViewById(R.id.iv));
			}

			@Override
			public void httpCallBackErr(int tag, Object src) {

			}
		}).getHttp("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/1/8", "search", HttpResult.class);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 3000) {
				DialogUtils.showToast("再按一次退出程序", 0);
				exitTime = System.currentTimeMillis();
			} else {
				super.onBackPressed();
//				finish();
//				ActivityCollector.finishAll();
//				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
