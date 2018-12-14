package link.app.byunm.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.webkit.WebView;

import java.io.File;
import java.util.HashMap;

public final class UIUtil {
	
	/**
	 * 현재버전 가져오기
	 */
	public static int getAppVersion(Context context) {
		
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
		return pi.versionCode;
	}
	
	/**
	 * 현재버전이름 가져오기
	 */
	public static String getAppVersionName(Context context) {
		
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "1.0.0";
		}
		return pi.versionName;
	}
	
	/**
	 * 화면상태 꺼져 있는지 아닌지 확인
	 * @param context
	 * @return
	 */
	public static boolean isScreenOn(Context context) {
		return ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
	}
	
	/**
	 * apk이름 있으면 삭제
	 */
	public static void deleteApkFiles(){
		File files = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
		
		if(!files.exists()) return;

		if(files.listFiles().length>0){
			for(File file : files.listFiles()){
				if(file.getName().startsWith("SmartMouMouMom")){
					file.delete();
				}
			}
		}
	}
	
	/**
     * dp => pixel 로 변환
     * @param number 	dp
     * @param context	해당 뷰
     * @return			pixel
     */
    public static int dpToPixel(float number, Context context) {

        int num = (int) TypedValue.applyDimension(  TypedValue.COMPLEX_UNIT_DIP
								                    , number
								                    , context.getResources().getDisplayMetrics());
        
        return num;
    }
    
    public static HashMap<Float, Integer> dispToPxList = new HashMap<Float, Integer>();
    
    /**
     * dip값을 px로 변환
     * @param number 	dp
     * @param context	해당 뷰
     * @return			px
     */
    public static int dipToPx(float number, Context context)
    {
    	int num = -1;
    	if( dispToPxList == null ) {
    		dispToPxList = new HashMap<Float, Integer>();
    	}
    	
    	if( dispToPxList.containsKey(number) ) {
    		num = dispToPxList.get(number);
    	}
    	else {
    		num = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, number, context.getResources().getDisplayMetrics());
    		dispToPxList.put(number, num);
    	}
        
        return num;
    }
    
    private static int m_screenWidthPixcels = -1;
    private static int m_screenHeightPixcels = -1;
    
    /**
     * 폰의 가로 with Pixels
     * @param ctx
     * @return
     */
    public static int getScreenWidth(Context ctx)
    {
    	if(  m_screenWidthPixcels == -1 ) {
	    	DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
	    	m_screenWidthPixcels = metrics.widthPixels;
    	}
    	
    	return m_screenWidthPixcels;
    }
    
    /**
     * 폰의 Height Pixels
     * @param ctx
     * @return
     */
    public static int getScreenHeight(Context ctx)
    {
    	if(  m_screenHeightPixcels == -1 ) {
	    	DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
	    	m_screenHeightPixcels = metrics.heightPixels;
    	}
    	
    	return m_screenHeightPixcels;
    }   
    

    /**
     * 테블릿인지
     */
    public static boolean isTablet(Activity activity) {
		int portrait_width_pixel = Math.min(activity.getResources().getDisplayMetrics().widthPixels, activity.getResources().getDisplayMetrics().heightPixels);
		int dots_per_virtual_inch = activity.getResources().getDisplayMetrics().densityDpi;
		float virutal_width_inch = portrait_width_pixel / dots_per_virtual_inch;

		return (virutal_width_inch > 2);
	}

	public static boolean checkTabletDeviceWithUserAgent(Context context) {
		WebView webView = new WebView(context);
		String ua=webView.getSettings().getUserAgentString();
		webView = null;
		if(ua.contains("Mobile Safari")){
			return false;
		}else{
			return true;
		}
	}
}
