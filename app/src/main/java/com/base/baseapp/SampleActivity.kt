package com.base.baseapp

import com.base.baseapp.databinding.SampleActivityBinding
import com.base.baselibrary.base.BaseActivity

class SampleActivity : BaseActivity<SampleActivityBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.sample_activity
    }

    override fun initM() {

    }
}