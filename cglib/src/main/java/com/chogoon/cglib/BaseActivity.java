package com.chogoon.cglib;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public abstract class BaseActivity<VDB extends ViewDataBinding, VM extends CgViewModel> extends AppCompatActivity implements ViewModelStoreOwner, SwipeRefreshLayout.OnRefreshListener, OnSingleClickListener {

//    static {
//        System.loadLibrary("native-lib");
//    }

//    public native String stringFromJNI();

    public static final String _DATA = "_DATA";
    public static final String _REFRESH = "_REFRESH";

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private ViewModelStore viewModelStore = new ViewModelStore();

    private long lastClickTime = 0L;

    protected String TAG;

    private VM viewModel;
    private VDB binding;

    private Context context;

    protected abstract void setup();

    protected abstract void onCreateView(Bundle savedInstanceState);

    protected void updateView(Object data) { }

    public static void start(Activity activity, @NonNull Class clazz) {
        start(activity, clazz, null);
    }

    public static void start(Activity activity, @NonNull Class clazz, BaseModel data) {
        Intent intent = new Intent(activity, clazz);
        if (data != null) intent.putExtra(_DATA, data);
        activity.startActivity(intent);
    }

    public static void start(Fragment fragment, @NonNull Class clazz) {
        start(fragment, clazz, null);
    }

    public static void start(Fragment fragment, @NonNull Class clazz, BaseModel data) {
        Intent intent = new Intent(fragment.getContext(), clazz);
        if (data != null) intent.putExtra(_DATA, data);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        CgLog.e(TAG, "onCreate");
        setContext(this);
        if (viewModelFactory == null)
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        setup();
        onCreateView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        CgLog.e(TAG, "onResume");
        getViewModel().error.observe(this, this::onError);
        getViewModel().done.observe(this, this::onDone);
        super.onResume();
    }

    @Override
    protected void onPause() {
        getViewModel().error.removeObservers(this);
        getViewModel().done.removeObservers(this);
        super.onPause();
    }

    public RecyclerView.Adapter getAdapter() {
        return null;
    }

    public Context getContext() {
        return context;
    }

    public BaseActivity getActivity() {
        return this;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    protected void setBinding(@LayoutRes int layoutId, @NonNull Class<VM> modelClass) {
        binding = DataBindingUtil.setContentView(getActivity(), layoutId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(modelClass);
        binding.setVariable(BR.viewModel, viewModel);
        binding.setVariable(BR.view, this);
        binding.setLifecycleOwner(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onSingleClick(View v) {
        if (v.getId() == R.id.left_) {
            finish();
        }
    }

    @Override
    public void onItemClick(View v) { }

    @Override
    public final void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - lastClickTime;
        lastClickTime = currentClickTime;
        // duplicate
        if (elapsedTime <= OnSingleClickListener.CLICK_INTERVAL) {
            return;
        }
        onSingleClick(v);
    }

    protected VDB getBinding() {
        return binding;
    }

    protected VM getViewModel() {
        return viewModel;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModelStore.clear();
    }

    protected void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void showSoftkeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    protected BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };

    protected void onDone(boolean b) {
    }

    protected void onError(final Throwable error) {
        new BaseDialog(getContext()).setText(BuildConfig.BUILD_TYPE + error.getMessage());
    }

    protected View createView(@LayoutRes int res) {
        return View.inflate(getContext(), res, null);
    }

    protected void sendRefresh() {
        Intent intent = new Intent(_REFRESH);
        intent.putExtra(_DATA, TAG);
        sendBroadcast(intent);
    }


}
