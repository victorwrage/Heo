package com.heinsoft.heo.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.heinsoft.heo.R;


import com.heinsoft.heo.fragment.FragmentUserManager;


import com.heinsoft.heo.fragment.FragmentUserVerify;
import com.heinsoft.heo.util.Constant;
import com.heinsoft.heo.util.Utils;

import butterknife.ButterKnife;

public class UserManagerActivity extends FragmentActivity implements FragmentUserManager.IUserListtener,FragmentUserVerify.IUserVerifyListtener
{

    private static final String PAGE_1 = "page_1";
    private static final String PAGE_2 = "page_2";
    private static final String PAGE_3 = "page_3";
    private static final String PAGE_4 = "page_4";
    private static final String PAGE_5 = "page_5";


    private FragmentUserManager fragment0;

    private FragmentUserVerify fragment2;


    Utils util;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_lay);
        ButterKnife.bind(UserManagerActivity.this);
        initDate();
        initView();

    }

    private void initDate() {
        util = Utils.getInstance();
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 = new FragmentUserManager();

        fragment2 =new FragmentUserVerify();


        ft.add(R.id.fragment_container, fragment0, PAGE_1);

        ft.add(R.id.fragment_container, fragment2, PAGE_3);

        ft.show(fragment0);

        ft.hide(fragment2);

        ft.commit();

    }


    @Override
    public void finishThis() {
        finish();
    }

    @Override
    public void setFragClear() {
        setResult(Constant.FRAGMENT_CLEAR_DATE);
    }

    @Override
    public void changePhone() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        ft.hide(fragment0);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if(!fragment0.isVisible()){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.show(fragment0);

                    ft.hide(fragment2);

                    ft.commit();
                    return true;
                }else{
                    return super.onKeyDown(keyCode, event);
                }
            }
        }
        return false;
    }

    @Override
    public void verify() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment2);
        ft.hide(fragment0);
        ft.commit();
    }

    @Override
    public void changeEmail() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.hide(fragment0);
        ft.commit();
    }

    @Override
    public void changePassword() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.hide(fragment0);
        ft.commit();
    }

    private void showMain(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment0);

        ft.hide(fragment2);

        ft.commit();
    }
    @Override
    public void finishVerify() {
        showMain();
    }

}
