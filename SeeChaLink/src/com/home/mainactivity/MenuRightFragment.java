package com.home.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MenuRightFragment extends Fragment implements OnClickListener {
	View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == view) {
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.menu_layout_right, null);
		}
		initView();
		return view;
	}

	LinearLayout layout_tjsb, layout_tjcj, layout_tjyk;

	private void initView() {
		// layout_sblb = (LinearLayout) view.findViewById(R.id.layout_sblb);
		layout_tjcj = (LinearLayout) view.findViewById(R.id.layout_tjcj);
		layout_tjyk = (LinearLayout) view.findViewById(R.id.layout_tjyk);
		layout_tjsb = (LinearLayout) view.findViewById(R.id.layout_tjsb);
		// layout_sblb.setOnClickListener(this);
		layout_tjcj.setOnClickListener(this);
		layout_tjyk.setOnClickListener(this);
		layout_tjsb.setOnClickListener(this);
	}

	Intent intent = null;

	@Override
	public void onClick(View v) {
		intent = new Intent();
		switch (v.getId()) {
		case R.id.layout_tjcj:
			intent.setClass(getActivity(), AddSceneActivity.class);
			break;
		case R.id.layout_tjsb:
			intent.setClass(getActivity(), ConfigActivity.class);

			break;
		case R.id.layout_tjyk:

			intent.setClass(getActivity(), AddRemoteControll.class);
			intent.putExtra("fromwhere", "添加遥控");
			break;

		default:
			break;
		}
		startActivity(intent);

	}
}
