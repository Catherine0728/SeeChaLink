package com.home.view;

import org.eclipse.paho.android.service.Constants;

import com.home.listener.CommanTitle_Right_Listener;
import com.home.mainactivity.MainActivity;
import com.home.mainactivity.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.constants.Configer;

/**
 * 头部视图
 * 
 * @author Catherine
 * 
 * 
 *         共用头部，那么就在是否需要显示头部的两个按钮进行判断
 * 
 * 
 *         作为在配置页面：显示左边按钮，如果连接成功，那么我们就可以显示左右两个键了。且左边的的图标换成个人详细信息的按钮。右边的换成添加设备，
 *         添加监控
 * 
 */
public class CommonTitleView extends LinearLayout implements
		View.OnClickListener {
	CommanTitle_Right_Listener right_Listener = null;
	private Context mContext;
	private View view;
	public static CommonTitleView instance;

	public CommonTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = Configer.getView(context, R.layout.comman_title, this);
		mContext = context;
		instance = this;

		// initData(mContext);
	}

	Button main_head_left, main_head_right;
	TextView text_Title, right_Text;
	FrameLayout layout_right;

	/**
	 * 设置左侧图片隐藏
	 */
	public void setLeftImgHide() {
		main_head_left.setVisibility(View.GONE);
	}

	/**
	 * 设置右侧图片隐藏
	 */
	public void setRightImgHide() {
		main_head_right.setVisibility(View.GONE);
	}

	Activity ac = null;

	public void initData(Activity mContext,
			CommanTitle_Right_Listener listener, String title) {
		right_Listener = listener;
		ac = mContext;
		main_head_left = (Button) view.findViewById(R.id.main_head_back);
		main_head_right = (Button) view.findViewById(R.id.main_head_pre_right);
		text_Title = (TextView) view.findViewById(R.id.title);
		text_Title.setText(title);
		right_Text = (TextView) view.findViewById(R.id.main_right_text);
		right_Text.setOnClickListener(this);
		layout_right = (FrameLayout) view.findViewById(R.id.layout_right);
		main_head_left.setOnClickListener(this);
		main_head_right.setOnClickListener(this);
		if (Configer.PAGER == 0) {
			main_head_left.setVisibility(View.INVISIBLE);
			main_head_right.setVisibility(View.INVISIBLE);
			layout_right.setVisibility(View.INVISIBLE);
		} else if (Configer.PAGER == 1) {
			main_head_left.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.main_title_left_normal, 0, 0, 0);

			main_head_right.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.home_right_add, 0);
			layout_right.setVisibility(View.VISIBLE);
			right_Text.setVisibility(View.GONE);
			main_head_right.setVisibility(View.VISIBLE);
			main_head_left.setVisibility(View.VISIBLE);
		} else {
			main_head_left.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.main_header_back, 0, 0, 0);
			main_head_left.setVisibility(View.VISIBLE);
			if (Configer.PAGER == 3) {
				layout_right.setVisibility(View.VISIBLE);
				right_Text.setVisibility(view.VISIBLE);
				right_Text.setText("完成");

			} else if (Configer.PAGER == 2) {
				layout_right.setVisibility(View.VISIBLE);
				right_Text.setVisibility(view.VISIBLE);
				right_Text.setText("编辑");
			} else {

				layout_right.setVisibility(View.GONE);
			}
			main_head_right.setVisibility(View.GONE);
		}
	}

	public interface OnClickListener {
		public void onClick(View v);
	}

	public OnClickListener onClick;

	public void setOnClickListener(OnClickListener onclick) {
		this.onClick = onclick;
	}

	boolean isRight = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_head_back:
			if (Configer.PAGER == 0) {
				// 代表无显示
			} else if (Configer.PAGER == 1) {
				// 显示用户详情的按钮
				// Intent it1 = new Intent(mContext, MainActivity.class);
				// mContext.startActivity(it1);
				right_Listener.DotLeft(true);

			}
			// else if (Configer.PAGER == -1) {
			// // 应该是跳回首页，关闭当前页面
			// Intent intent = new Intent(mContext, MainActivity.class);
			// mContext.startActivity(intent);
			// ac.finish();
			// }
			else {
				Intent intent = new Intent(mContext, MainActivity.class);
				mContext.startActivity(intent);
				ac.finish();
			}

			break;
		case R.id.main_head_pre_right:
			right_Listener.DotRight(true);
			break;
		case R.id.main_right_text:

			String rightText = right_Text.getText().toString();
			if (rightText.equals("完成")) {
				right_Listener.DotRightFinish(true);
			} else if (rightText.equals("编辑")) {
				right_Text.setText("完成");
				right_Listener.DotRightEdit(true);
			}
			break;
		}

	}

}
