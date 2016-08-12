package lib.system.uitle;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
/**隐藏键盘*/
public class ScreenUtils {
	public static void hideInput(Context context, View editView) {
		((InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(editView.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
    /** 
     * 获取屏幕宽度 
     */  
    public static int getScreenWidthPX(Context context) {  
    	return context.getResources().getDisplayMetrics().widthPixels;
    }  
    /** 
     * 获取屏幕高度 
     */  
    public static int getScreenHeightPX(Context context) {  
    	return context.getResources().getDisplayMetrics().heightPixels;
    }  

    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */
  //转换dip为px 
    public static int convertDipOrPx(Context context, int dip) { 
        float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dip*scale + 0.5f*(dip>=0?1:-1)); 
    } 
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    //转换px为dip 
    public static int convertPxOrDip(Context context, int px) { 
        float scale = context.getResources().getDisplayMetrics().density; 
       return (int)(px/scale + 0.5f*(px>=0?1:-1)); 
   } 
    /** 
     * 根据手机的分辨率从 sp(像素) 的单位 转成为 px
     */  
    public static int sp2px(Context context, float spValue) {
          float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
          return (int) (spValue * fontScale + 0.5f);
      }
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp 
     */  
      public static int px2sp(Context context, float pxValue) {
          float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
          return (int) (pxValue / fontScale + 0.5f);
      }
}