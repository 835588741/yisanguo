/**
 * custom alert dialog and used
 */
package com.soul.project.application.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.soul.project.story.activity.R;

/**
 * @author Mentago
 * 
 *         调用： AlertDialog.showSimpleAlertDialog(SplashActivity.this,
 *         R.drawable.dialog_alert_icon, getString(R.string.alert),
 *         getString(R.string.sd_unavailable), getString(R.string.confirm), new
 *         View.OnClickListener(){
 * @Override public void onClick(View v) { AlertDialog.dialog.dismiss(); } },
 *           getString(R.string.cancel), new View.OnClickListener() {
 * @Override public void onClick(View v) { AlertDialog.dialog.dismiss(); } });
 * 
 */
public class AlertDialog {

	public static Dialog dialog;

	/**
	 * 弹出显示一个只有两个按钮，文本内容的简单的alert对话框 titleIcon -> int
	 * 
	 * @param context
	 * @param titleIcon
	 *            对话框图标 -1为null
	 * @param titleInfo
	 *            对话框title
	 * @param content
	 *            对话框内容
	 * @param positiveButtonText
	 *            对话框“确定”按钮文字
	 * @param positiveListener
	 *            对话框“确定”按钮鼠标事件
	 * @param negativeButtonText
	 *            对话框“取消”按钮文字
	 * @param negativeListener
	 *            对话框“取消”按钮鼠标事件
	 */
	public static void showSimpleAlertDialog(Context context, int titleIcon, String titleInfo, String content, String positiveButtonText, final View.OnClickListener positiveListener, String negativeButtonText, View.OnClickListener negativeListener) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dialog = new Dialog(context, R.style.alertDialogStyle);

		View layout = inflater.inflate(R.layout.cmp_alert_dialog, (ViewGroup) (((Activity) context).findViewById(R.id.alert_dialog_frame_root)));
		ImageView titleIconView = (ImageView) layout.findViewById(R.id.alert_dialog_frame_icon);
		if (titleIcon != -1) {
			titleIconView.setImageResource(titleIcon);
		}

		TextView titleInfoView = (TextView) layout.findViewById(R.id.alert_dialog_frame_title);
		if ((titleInfo != null) && (!(titleInfo.equals("")))) {
			titleInfoView.setText(titleInfo);
		}

		LinearLayout viewGroup = (LinearLayout) layout.findViewById(R.id.alert_dialog_bottom_viewgroup);
		View contentLayout = inflater.inflate(R.layout.alert_dialog_simple_content, (ViewGroup) (((Activity) context).findViewById(R.id.RelativeLayout01)));
		viewGroup.addView(contentLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		TextView contentView = (TextView) contentLayout.findViewById(R.id.alert_dialog_msg_id);
		if (content == null || content.equals("")) {
			content = "";
		}
		contentView.setText(content);

		Button positiveBtn = (Button) contentLayout.findViewById(R.id.alert_dialog_ok_btn_id);
		if ((positiveButtonText != null) && (!(positiveButtonText.equals("")))) {
			positiveBtn.setText(positiveButtonText);
		}
		if (positiveListener != null) {
			positiveBtn.setOnClickListener(positiveListener);
		}
		
		Button negativeBtn = (Button) contentLayout.findViewById(R.id.alert_dialog_cancel_btn_id);
		if ((negativeButtonText != null) && (!(negativeButtonText.equals("")))) {
			negativeBtn.setText(negativeButtonText);
		}

		if (negativeListener != null) {
			negativeBtn.setOnClickListener(negativeListener);
		}

		dialog.setContentView(layout);
		dialog.show();
	}

	/**
	 * 一个按钮的对话框
	 * @param context
	 * @param titleIcon
	 * @param titleInfo
	 * @param content
	 * @param positiveButtonText
	 * @param positiveListener
	 */
	public static void showSimpleAlertDialog(Context context, int titleIcon, String titleInfo, String content, String positiveButtonText, final View.OnClickListener positiveListener) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dialog = new Dialog(context, R.style.alertDialogStyle);

		View layout = inflater.inflate(R.layout.cmp_alert_dialog, (ViewGroup) (((Activity) context).findViewById(R.id.alert_dialog_frame_root)));
		ImageView titleIconView = (ImageView) layout.findViewById(R.id.alert_dialog_frame_icon);
		if (titleIcon != -1) {
			titleIconView.setImageResource(titleIcon);
		}

		TextView titleInfoView = (TextView) layout.findViewById(R.id.alert_dialog_frame_title);
		if ((titleInfo != null) && (!(titleInfo.equals("")))) {
			titleInfoView.setText(titleInfo);
		}

		LinearLayout viewGroup = (LinearLayout) layout.findViewById(R.id.alert_dialog_bottom_viewgroup);
		View contentLayout = inflater.inflate(R.layout.alert_dialog_simple_one_button_content, (ViewGroup) (((Activity) context).findViewById(R.id.RelativeLayout01)));
		viewGroup.addView(contentLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		TextView contentView = (TextView) contentLayout.findViewById(R.id.alert_dialog_msg_id);
		if (content == null || content.equals("")) {
			content = "";
		}
		contentView.setText(content);

		Button positiveBtn = (Button) contentLayout.findViewById(R.id.alert_dialog_ok_btn_id);
		if ((positiveButtonText != null) && (!(positiveButtonText.equals("")))) {
			positiveBtn.setText(positiveButtonText);
		}
		if (positiveListener != null) {
			positiveBtn.setOnClickListener(positiveListener);
		}

		dialog.setContentView(layout);
		dialog.show();
	}

}
