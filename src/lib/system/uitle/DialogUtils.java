package lib.system.uitle;

import lib.system.BaseApplication;
import lib.system.view.CToast;
import lib.system.view.LoadingDialog;

import com.system.lib.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;


/*
 * 弹窗类
 */
public class DialogUtils {

	static LoadingDialog progressDialog;
	private static CToast mCToast;

	public static void showToast(String msg, int time) {
		// Toast.makeText(BaseApplication.CONTEXT, msg,
		// Toast.LENGTH_SHORT).show();
		// int
		// time=TextUtils.isEmpty(mEditText.getText().toString())?CToast.LENGTH_SHORT:Integer.valueOf(mEditText.getText().toString());

		if (null != mCToast)
			mCToast.hide();
		int times = time == 0 ? 100 : CToast.LENGTH_SHORT;
		mCToast = CToast.makeText(BaseApplication.CONTEXT, msg, times);
		mCToast.setGravity( Gravity.BOTTOM, 0, 20);
		mCToast.show();
	}

	public static void showToast(int msgId, int time) {
		Res.init(BaseApplication.CONTEXT);
		if (null != mCToast)
			mCToast.hide();
		int times = time == 0 ? 100 : CToast.LENGTH_SHORT;
		String str = Res.getResources().getString(msgId);
		mCToast = CToast.makeText(BaseApplication.CONTEXT, str, times);
		mCToast.setGravity( Gravity.BOTTOM, 0, 20);
		mCToast.show();
		// Toast.makeText(BaseApplication.CONTEXT, msgId,
		// Toast.LENGTH_SHORT).show();
	}

	public static void showProgressDialog(Context context) {
		progressDialog = new LoadingDialog(context);
		progressDialog.setTitle("正在加载...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}

	public static void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	public static void showConfirmDialog(
			Context context,
			String title,
			String message,
			String positiveBtnName,
			String negativeBtnName,
			android.content.DialogInterface.OnClickListener positiveBtnListener,
			android.content.DialogInterface.OnClickListener negativeBtnListener) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		builder.create().show();
	}
}
