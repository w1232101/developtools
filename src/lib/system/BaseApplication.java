package lib.system;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import lib.system.uitle.CrashHandler;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.system.lib.BuildConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

public class BaseApplication extends Application {
	public static Context CONTEXT = null;
	protected boolean isNeedCaughtExeption = true;// 是否捕获未知异常

	public static Context applicationContext;
	private static BaseApplication instance;
	public final String PREF_USERNAME = "username";
	public static String currentUserNick = "";
	public static final String TAG = "BaseApplication";
	private static OkHttpUtils okHttpUtils;
	// baidu-map
	// private static final String strKey = "tApgS4zILa4CXiCodsOCuQsj";
	// public static String baiduMap_Version = "";
	public static final String INTENT_ACTION_LOGGED_OUT = "com.yh.educomandroid.intent.action.LOGGED_OUT";
	// location
	private static int oper_student_id = 0;

	public static int getOper_student_id() {
		return oper_student_id;
	}

	public static void setOper_student_id(int oper_student_id) {
		BaseApplication.oper_student_id = oper_student_id;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		CONTEXT = getApplicationContext();
		instance = this;

		if (isNeedCaughtExeption) {
			initCarshHandler();
		}
		OkHttpUtils.initClient(BaseApplication.getOkHttpClient());
		// ImageLoader.getInstance().init(getWholeConfig());//TODO
		// 使用UniversalImageLoader需要打开
	}

	public static BaseApplication getInstance() {
		return instance;
	}

	private void initCarshHandler() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}


	/**
	 * okHttp初始化配置
	 */
	public static OkHttpClient getOkHttpClient() {
		if (client2 != null) {
			return client2;
		}
		synchronized (BaseApplication.class) {
			if (client2 == null) {

				OkHttpClient.Builder client = new OkHttpClient.Builder();
				client.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
				client.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
				client.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
				// 设置缓存路径
				File httpCacheDirectory = new File(applicationContext.getCacheDir(), "okhttpCache");
				// 设置缓存 10M
				Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
				client.cache(cache);
				// 设置拦截器
				client.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
				client.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
				return client.build();
			}
		}
		return client2;
	}

	private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {

		@Override
		public Response intercept(Interceptor.Chain chain) throws IOException {
			// 方案一：有网和没有网都是先读缓存
			// Request request = chain.request();
			// Log.i(TAG, "request=" + request);
			// Response response = chain.proceed(request);
			// Log.i(TAG, "response=" + response);
			//
			// String cacheControl = request.cacheControl().toString();
			// if (TextUtils.isEmpty(cacheControl)) {
			// cacheControl = "public, max-age=60";
			// }
			// return response.newBuilder()
			// .header("Cache-Control", cacheControl)
			// .removeHeader("Pragma")
			// .build();

			// 方案二：无网读缓存，有网根据过期时间重新请求
			boolean netWorkConection = NetUtils.hasNetWorkConection(BaseApplication.getInstance());
			Request request = chain.request();
			if (!netWorkConection) {
				request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
			}

			Response response = chain.proceed(request);
			if (netWorkConection) {
				// 有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
				String cacheControl = request.cacheControl().toString();
				Log.i(TAG, "cacheControl:" + cacheControl);
				response.newBuilder().removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
						.header("Cache-Control", cacheControl).build();
			} else {
				int maxStale = 60 * 60 * 24 * 7;
				response.newBuilder().removeHeader("Pragma")
						.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
			}
			return response;
		}
	};
	private static OkHttpClient client2;

	/**
	 * 所有的配置参数举例
	 * 
	 * @return
	 */

	private ImageLoaderConfiguration getWholeConfig() {
		// 设置默认的配置，设置缓存，这里不设置可以到别的地方设置
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.build();
		// 设置缓存的路径
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800)
				// 即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// 解释：当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.denyCacheImageMultipleSizesInMemory()
				// 拒绝缓存多个图片。
				.memoryCache(new WeakMemoryCache())
				// 缓存策略你可以通过自己的内存缓存实现 ，这里用弱引用，缺点是太容易被回收了，不是很好！
				.memoryCacheSize(2 * 1024 * 1024)
				// 设置内存缓存的大小
				.discCacheSize(50 * 1024 * 1024)
				// 设置磁盘缓存大小 50M
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 设置图片下载和显示的工作队列排序
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.defaultDisplayImageOptions(defaultOptions)
				// 显示图片的参数，默认：DisplayImageOptions.createSimple()
				.imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)).writeDebugLogs() // 打开调试日志
				.build();// 开始构建
		return config;
	}

	/**
	 * 比较常用的配置方案
	 * 
	 * @return
	 */
	private ImageLoaderConfiguration getSimpleConfig() {
		// 设置缓存的路径
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCacheExtraOptions(480, 800) // 即保存的每个缓存文件的最大长宽
				.threadPriority(Thread.NORM_PRIORITY - 2) // 线程池中线程的个数
				.denyCacheImageMultipleSizesInMemory() // 禁止缓存多张图片
				.memoryCache(new LRULimitedMemoryCache(50 * 1024 * 1024)) // 缓存策略
				.memoryCacheSize(50 * 1024 * 1024) // 设置内存缓存的大小
				.discCacheFileNameGenerator(new Md5FileNameGenerator()) // 缓存文件名的保存方式
				.discCacheSize(200 * 1024 * 1024) // 磁盘缓存大小
				.tasksProcessingOrder(QueueProcessingType.LIFO) // 工作队列
				.discCacheFileCount(200) // 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir)) // 自定义缓存路径
				// .writeDebugLogs() // Remove for release app
				.build();
		return config;
	}

}
