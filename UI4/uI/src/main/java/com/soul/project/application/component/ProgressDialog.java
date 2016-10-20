/**
 * custom progress dialog
 */
package com.soul.project.application.component;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.soul.project.story.activity.R;

/**
 * 进度条
 *
 */
public class ProgressDialog {
	
	private static Dialog dialog;
	
	/**
	 * show
	 * @param context
	 * @param info
	 */
	public static void showProgressDialog(Context context, String info) {
		
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.cmp_progress_dialog, (ViewGroup) ((Activity) context).findViewById(R.id.dialog_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.progress_info);
        text.setText(info);
        dialog = new Dialog(context, R.style.SimpleProgressDialogStyle);
		dialog.setContentView(layout);
		dialog.show();
	}
	
	/**
	 * dismiss
	 */
	public static void dismissProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
