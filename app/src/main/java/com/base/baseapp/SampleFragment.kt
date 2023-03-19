package com.base.baseapp

import com.base.baseapp.databinding.SampleFragmentBinding
import com.base.baselibrary.base.BaseFragment

class SampleFragment : BaseFragment<SampleFragmentBinding>() {
    override fun setLayoutId(): Int {
        return R.layout.sample_fragment
    }

    override fun initM() {
        //showMsg()
    }
}