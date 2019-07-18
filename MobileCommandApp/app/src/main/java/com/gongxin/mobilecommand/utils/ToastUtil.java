package com.gongxin.mobilecommand.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast 显示工具�?
 * 
 * 
 */
public class ToastUtil {
	Context context;

	/**
	 * 弹出短时间toast
	 * 
	 * @param str
	 *            显示的内�?
	 */
	public static void shortToast(Context context, String str) {
		if (str.contains("数据空")) {
			return;
		}
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	private static Toast toast;

	/**
	 * 连续的吐司
	 * 
	 * @param ctx
	 * @param str
	 */
	public static void showToast(Context ctx, String str) {
		if (toast == null) {
			toast = Toast.makeText(ctx, str, Toast.LENGTH_SHORT);
		}

		toast.setText(str);
		toast.show();
	}

	/**
	 * 弹出长时间的toast
	 * 
	 * @param str
	 *            显示的内�?
	 */
	public static void longToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
}
