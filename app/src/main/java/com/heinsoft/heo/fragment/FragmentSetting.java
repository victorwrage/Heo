package com.heinsoft.heo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.present.QueryPresent;
import com.heinsoft.heo.util.Utils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentSetting extends BaseFragment {

	@Bind(R.id.header_btn)
	ImageView header_btn;
	@Bind(R.id.header_title)
	TextView header_title;

	@Bind(R.id.setting_add_lay)
	RelativeLayout setting_add_lay;
	@Bind(R.id.setting_road_lay)
	RelativeLayout setting_road_lay;
	@Bind(R.id.setting_sound_lay)
	RelativeLayout setting_sound_lay;
	@Bind(R.id.setting_user_update_lay)
	RelativeLayout setting_user_update_lay;
	@Bind(R.id.setting_update_lay)
	RelativeLayout setting_update_lay;
	@Bind(R.id.setting_law_lay)
	RelativeLayout setting_law_lay;
	@Bind(R.id.setting_about_lay)
	RelativeLayout setting_about_lay;

	@Bind(R.id.setting_version_tv)
	TextView setting_version_tv;


	QueryPresent present;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_lay, container, false);
		ButterKnife.bind(FragmentSetting.this,view);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listener.gotoMain());
		RxView.clicks(setting_add_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> address());

		RxView.clicks(setting_user_update_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> userUpdate());
		RxView.clicks(setting_update_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> versionUpdate());
		RxView.clicks(setting_law_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> law());
		RxView.clicks(setting_about_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> about());
		header_title.setText("设置");
		setting_version_tv.setText(util.getAppVersionName(getContext()));
		return view;
	}

	private void about() {

	}

	private void law() {

	}

	private void versionUpdate() {

	}


	private void userUpdate() {

	}


	private void address() {
	}

}
