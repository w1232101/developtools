package lib.system.activity;

import java.util.List;
import java.util.Random;

import com.bumptech.glide.Glide;
import com.system.lib.R;
import com.zhy.http.okhttp.OkHttpUtils;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.ListView;
import lib.system.BaseActivity;
import lib.system.adapter.CommonAdapter;
import lib.system.bean.GankEntity;
import lib.system.bean.HttpResult;
import lib.system.uitle.DialogUtils;
import lib.system.uitle.MyCallBack;
import lib.system.uitle.MyOkHttpUtils;

public class MainActivity extends BaseActivity implements OnRefreshListener {
	private String TAG = "MainActivity";
	private long exitTime = 0;
	private List<String> datas;
	private ListView lv;
	private CommonAdapter<String> adapter;
	private int x;
	private int y;
	private SwipeRefreshLayout srl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		srl = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
		srl.setOnRefreshListener(this);
		srl.post(new Runnable() {
			
			@Override
			public void run() {
				srl.setRefreshing(true);
				
			}
		});
		onRefresh();

	}

	private void getPicByNet(int i) {
		MyOkHttpUtils.getInstance(this, new MyCallBack() {

			@Override
			public void httpCallBackSucc(int tag, Object src) {
				HttpResult result = (HttpResult) src;
				List<GankEntity> entity = result.getResults();

				Glide.with(MainActivity.this).load(entity.get(0).getUrl()).thumbnail(0.2f)
						.into((ImageView) findViewById(R.id.iv));
				srl.setRefreshing(false);
			}

			@Override
			public void httpCallBackErr(int tag, Object src) {

			}
		}).getHttp("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/1/"+i, TAG, HttpResult.class);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				DialogUtils.showToast("再按一次退出程序", 0);
				exitTime = System.currentTimeMillis();
			} else {
				super.onBackPressed();
				// finish();
				// ActivityCollector.finishAll();
				// System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(TAG);
	}

	@Override
	public void onRefresh() {
		getPicByNet(new Random().nextInt(30));
	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_MOVE:
	// int moveX = (int) event.getX();
	// int moveY = (int) event.getY();
	// int dX = moveX - x;
	// int dY = moveY - y;
	// int l = v.getLeft() + dX;
	// int b = v.getBottom() + dY;
	// int r = v.getRight() + dX;
	// int t = v.getTop() + dY;
	// v.layout(l,t,r,b);
	// v.postInvalidate();
	// break;
	// case MotionEvent.ACTION_DOWN:
	// x = (int) event.getX();
	// y = (int) event.getY();
	// break;
	// case MotionEvent.ACTION_UP:
	// x = (int) event.getX();
	// y = (int) event.getY();
	// break;
	//
	// default:
	// break;
	// }
	// return true;
	// }

}
