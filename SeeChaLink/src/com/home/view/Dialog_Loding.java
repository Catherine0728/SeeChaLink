package com.home.view;

import com.home.mainactivity.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 
 * 自定义一个dialog来显示加载
 * 
 * @author Catherine
 * */
public class Dialog_Loding extends ProgressDialog {
	private Context mContext = null;
	private TextView text_display = null;

	public Dialog_Loding(Context context, String text) {
		super(context);
		this.mContext = context;
		// text_display.setText(text);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		text_display = (TextView) findViewById(R.id.text_display);

	}

	public static Dialog_Loding show(Context context) {
		Dialog_Loding dialog = new Dialog_Loding(context, "Loading...");
		dialog.show();
		return dialog;
	}

}
