package com.heinsoft.heo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heinsoft.heo.R;
import com.heinsoft.heo.activity.SettingActivity;
import com.heinsoft.heo.util.Constant;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 侧滑栏界面
 *
 * @author xiaoyl
 * @date 2013-07-16
 */
public class ListFragmentMenuSliding extends ListFragment implements OnItemClickListener {
    private final String COOKIE_KEY = "cookie";
    protected static final String PAGE_9 = "page_9";
    @Bind(R.id.setting_header_lay)
    LinearLayout setting_header_lay;
    @Bind(R.id.setting_phone_tv)
    TextView setting_phone_tv;
    @Bind(R.id.setting_logout_tv)
    TextView setting_logout_tv;
    MenuAdapter adapter;
    IFragmentSlindingListtener listtener;
    private String menus[] = {"实名认证"};
    private int icons[] = {R.drawable.verify_ico};
    SharedPreferences sp;

    @Override
    public void onAttach(Context context) {
        try {
            listtener = (IFragmentSlindingListtener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        super.onAttach(context);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = getActivity().getSharedPreferences(COOKIE_KEY, 0);
        adapter = new MenuAdapter(getActivity());
        adapter.add(new SlidingMenuItem(menus[0], icons[0]));
//      adapter.add(new SlidingMenuItem(menus[1], icons[1]));
//      adapter.add(new SlidingMenuItem(menus[2], icons[2]));
//        adapter.add(new SlidingMenuItem(menus[3], icons[3]));
//        adapter.add(new SlidingMenuItem(menus[4], icons[4]));
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_lay, null);
        ButterKnife.bind(ListFragmentMenuSliding.this, view);

        RxView.clicks(setting_header_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> userManager());
        RxView.clicks(setting_logout_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> logoff());
        return view;
    }

    private void logoff() {
        //   sp.edit().clear().commit();
        setting_logout_tv.setVisibility(View.GONE);
        setting_phone_tv.setText("未实名");
        listtener.clearDateFlag();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.user_info == null) {
            setting_phone_tv.setText("未实名");
        } else {
            setting_logout_tv.setVisibility(View.VISIBLE);
            setting_phone_tv.setText(Constant.user_info.get(Constant.NAME));
        }
    }

    protected void userManager() {
        if(Constant.user_info==null){
            listtener.showVerify();
        }
        /*if (sp.getString(Constant.USER_INFO_NAME, "").equals("")) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            FragmentRegisterLogin fragment = new FragmentRegisterLogin();
            ft.add(R.id.fragment_container, fragment, PAGE_9);
            ft.show(fragment);
            ft.commit();
        } else {
            // startActivityForResult(new Intent(getContext(), UserManagerActivity.class), 0);
        }*/
    }

    public void initView() {
        KLog.v("initView");
        if (setting_phone_tv != null) {
            setting_phone_tv.setText(Constant.user_info == null ? "请实名认证" : Constant.user_info.get(Constant.USER_INFO_USER_NAME));
            switch (Integer.parseInt(Constant.user_info == null ? "0" : Constant.user_info.get(Constant.USER_INFO_ISAUTH))) {
                case 0:
                    menus[0] = "实名认证";
                    break;
                case 1:
                    menus[0] = "查看认证";
                    break;
                case 2:
                    menus[0] = "实名认证";
                    break;
                case 3:
                    menus[0] = "更新认证资料";
                    break;
            }
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg2) {
            case 0:
                //  startCredit();
                listtener.verify();
                break;
            case 1:

                // startPakage();
                break;
            case 2:
                startFeedBack();
                break;
            case 3:
                //    startSetting();
                break;
            case 4:
                //  startEvaluate();
                break;
        }

    }

    private void startEvaluate() {
        //startActivity(new Intent(getContext(), EvaluateActivity.class));
    }

    private void startPakage() {
        // startActivity(new Intent(getContext(), PakageActivity.class));
    }

    private void startCredit() {
        // startActivity(new Intent(getContext(), CreditActivity.class));
    }

    private void startSetting() {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }


    private void startPreference() {


    }

    private void startFeedBack() {
        //startActivity(new Intent(getContext(), FeedBackActivity.class));
    }


    private class MenuAdapter extends ArrayAdapter<SlidingMenuItem> {

        public MenuAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.behind_row_lay, null);
            }
            ImageView img = (ImageView) convertView.findViewById(R.id.row_icon);
            img.setImageResource(getItem(position).getIcon());
            TextView txv = (TextView) convertView.findViewById(R.id.row_title);
            txv.setText(getItem(position).getName());
            return convertView;
        }
    }

    private class SlidingMenuItem {
        private String name;
        private int icon;

        public SlidingMenuItem(String name, int icon_res) {
            this.name = name;
            this.icon = icon_res;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }


    }


    /**
     *
     */
    public interface IFragmentSlindingListtener {
        /**
         */
        void clearDateFlag();

        void verify();

        void showVerify();
    }
}
