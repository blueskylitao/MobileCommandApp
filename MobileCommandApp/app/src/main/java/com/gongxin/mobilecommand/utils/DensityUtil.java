package com.gongxin.mobilecommand.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DensityUtil {

	/**
	 * 根据手机的分辨率�? dp 的单�? 转成�? px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getDPSize(Context context, int num) {
		int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, num, context.getResources()
				.getDisplayMetrics());
		return size;
	}

	public static int getScreenWidthByDP(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	public static int getScreenHeightByDP(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	// 是否大屏
	public static boolean isBigScreen(Context context) {
		return getScreenWidthByDP(context) > 720;
	}

}