package com.chogoon.cglib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.chogoon.cglib.OnSingleClickListener;

import static com.chogoon.cglib.BaseActivity._REFRESH;


public abstract class BaseFragment<VDB extends ViewDataBinding, VM extends CgViewModel<?>> extends Fragment implements ViewModelStoreOwner, OnRefreshListener, OnSingleClickListener {
    private static String TAG = BaseFragment.class.getSimpleName();
    private long lastClickTime = 0L;

    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;
    private ViewModelStore viewModelStore = new ViewModelStore();
    private Context context;
    private VDB binding;
    private VM viewmodel;

    protected void updateView(Object data) {

    }

    protected void onDone(boolean b) {
    }

    protected View setContentView(LayoutInflater inflater, ViewGroup container, int res) {
        return inflater.inflate(res, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = CgUtils.getTag(this.getClass());
        setContext(getActivity());
        if (viewModelFactory == null)
            viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        CgLog.e(TAG, "onCreate");
    }

    @Override
    public void onResume() {
        getViewModel().error.observe(this, this::onError);
        getViewModel().done.observe(this, this::onDone);
        super.onResume();
    }

    @Override
    public void onPause() {
        getViewModel().error.removeObservers(this);
        getViewModel().done.removeObservers(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModelStore.clear();
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @Nullable
    protected final View findViewById(@IdRes int id) {
        if (binding == null) return null;
        return binding.getRoot().findViewById(id);
    }

    protected View setBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @LayoutRes int layoutId, @NonNull Class<VM> modelClass) {
        binding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        binding.setLifecycleOwner(this);
        viewmodel = new ViewModelProvider(this, viewModelFactory).get(modelClass);
        binding.setVariable(BR.viewModel, viewmodel);
        binding.setVariable(BR.view, this);
        return getView();
    }

    protected VDB getBinding() {
        return binding;
    }

    @Override
    public View getView() {
        return binding.getRoot();
    }

    protected VM getViewModel() {
        return viewmodel;
    }

    public void setLoading(boolean flag) {
        if (viewmodel != null) {
            viewmodel.loading.set(flag);
        }
    }

    @Override
    public void onSingleClick(View v) {
        if (v.getId() == R.id.left_) {
            if (getActivity() != null) getActivity().finish();
        }
    }

    @Override
    public void onItemClick(View v) {

    }

    public RecyclerView.Adapter getAdapter() {
        return null;
    }

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

    @Override
    public void onRefresh() {

    }

    protected BaseFragment getFragment() {
        return this;
    }

    @NonNull
    public Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }


    void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    protected BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };

    protected View createView(@LayoutRes int res) {
        return View.inflate(getContext(), res, null);
    }

    protected void onError(Throwable error) {
        new BaseDialog(getContext()).setText(BuildConfig.BUILD_TYPE + error.getMessage());
    }

    void sendRefresh() {
        getContext().sendBroadcast(new Intent(_REFRESH));
    }
}