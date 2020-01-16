package com.chogoon.cgbase.ui.main;

import android.os.Bundle;

import com.chogoon.cgbase.R;
import com.chogoon.cgbase.databinding.ActivityMainBinding;
import com.chogoon.cglib.BaseActivity;
import com.chogoon.cglib.CgLog;
import com.chogoon.cglib.KCgUtils;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    protected void setup() {
        setBinding(R.layout.activity_main, MainViewModel.class);
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {

        String encrypt = KMainActivity.encryptBase("chogoonAce,,");
        getBinding().sample1.setText(encrypt);
        getBinding().sample2.setText(KMainActivity.decryptBase(encrypt));

        CgLog.e(KCgUtils.INSTANCE.commaFormat(12345));
        KCgUtils.INSTANCE.getSHA256("wrewrew");
        KCgUtils.INSTANCE.getTag(this.getClass());

    }



}
