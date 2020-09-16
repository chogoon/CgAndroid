package com.chogoon.cglib

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.chogoon.cglib.data.BaseModel

abstract class KBaseActivity<VDB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() , ViewModelStoreOwner, OnRefreshListener, OnSingleClickListener {

    companion object{
        const val _DATA = "_DATA"
        const val _REFRESH = "_REFRESH"

        fun start(context: Activity, clazz: Class<*>) {
            start(context, clazz, null)
        }

        fun start(context: Activity, clazz: Class<*>, data: BaseModel?) {
            val intent = Intent(context, clazz)
            if (data != null) intent.putExtra(BaseActivity._DATA, data)
            context.startActivity(intent)
        }

        fun start(context: Fragment, clazz: Class<*>) {
            start(context, clazz, null)
        }

        fun start(context: Fragment, clazz: Class<*>, data: BaseModel?) {
            val intent = Intent(context.context, clazz)
            if (data != null) intent.putExtra(BaseActivity._DATA, data)
            context.startActivity(intent)
        }
    }

    lateinit var viewModelFactory : ViewModelProvider.AndroidViewModelFactory
    private val viewModelStore = ViewModelStore()
    private var lastClickTime = 0L
    private var TAG: String? = null
    lateinit var viewModel: VM
    lateinit var binding: VDB

    lateinit var context: Context

    protected abstract fun setup()
    protected abstract fun onCreateView(savedInstanceState: Bundle?)

    protected open fun updateView(data: Any?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = this.javaClass.simpleName
        CgLog.e(TAG, "onCreate")
        context = this
        viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        setup()
        onCreateView(savedInstanceState)
    }

    override fun onResume() {
        viewModel.error?.observe(this, Observer { onError(it) })
        viewModel.done?.observe(this, Observer { onDone(it) })
        super.onResume()
    }

    override fun onPause() {
        viewModel.error?.removeObservers(this)
        viewModel.done?.removeObservers(this)
        super.onPause()
    }

    open fun getAdapter(): Adapter<RecyclerView.ViewHolder>? {
        return null
    }

    open fun getActivity(): KBaseActivity<VDB, VM> {
        return this
    }

    protected fun serBinding(@LayoutRes layoutRes: Int, modelClass : Class<VM>){
        binding = DataBindingUtil.setContentView(getActivity(), layoutRes)
        viewModel = ViewModelProvider(this, viewModelFactory).get(modelClass)
        binding.setVariable(BR.viewModel, viewModel)
        binding.setVariable(BR.view, this)
        binding.lifecycleOwner = this
    }

    override fun onRefresh() { }

    override fun onItemClick(v: View) { }

    override fun onSingleClick(v: View) {
        if(v.id == R.id.left_){
            finish()
        }
    }

    override fun onClick(v: View?) {
        val currentClickTime  = SystemClock.uptimeMillis()
        val elapsedTime  = currentClickTime - lastClickTime
        lastClickTime = currentClickTime
        //duplicate
        if(elapsedTime <= OnSingleClickListener.CLICK_INTERVAL){
            return
        }
        onSingleClick(v!!)

    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    open fun onError(throwable: Throwable){
        BaseDialog(context).setText(BuildConfig.BUILD_TYPE + throwable.localizedMessage)
    }

    open fun onDone(b: Boolean) {}

}