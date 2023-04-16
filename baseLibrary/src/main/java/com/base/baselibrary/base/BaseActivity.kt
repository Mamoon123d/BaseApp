package com.base.baselibrary.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


public abstract class BaseActivity<D : ViewBinding> : AppCompatActivity() {


    protected lateinit var binding: D
    protected var tag = ""
    protected lateinit var mActivity: Context
    protected var activityLauncher: BetterActivityResult<Intent, ActivityResult>? = null

    //protected  var savedInstanceState: Bundle?=null
    protected open fun setExitAnimation(animId: Int) {
        overridePendingTransition(0, animId)
    }



    /**
     *
    val i = Intent(this, AnyActivity::class.java)
    activityLauncher!!.launch(i, BetterActivityResult.OnActivityResult {
    if (it.resultCode == Activity.RESULT_OK) {
    val intent = it.data
    }
    })
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, setLayoutId())
        // if (savedInstanceState!=null) this.savedInstanceState= savedInstanceState
        mActivity = this
        activityLauncher = BetterActivityResult.registerActivityForResult(this)

        initM()
    }


    //logs...........
    protected open fun logD(msg: String, tag: String? = "") {
        Log.d("${this.localClassName} : $tag", "$msg : ")
    }

    protected open fun logE(msg: String, tag: String? = "") {
        Log.e("${this.localClassName} : $tag", "$msg : ")
    }

    protected open fun logI(msg: String, tag: String? = "") {
        Log.i("${this.localClassName} : $tag", "$msg : ")
    }

    protected open fun logW(msg: String, tag: String? = "") {
        Log.w("${this.localClassName} : $tag", "$msg : ")
    }

    protected open fun logV(msg: String, tag: String? = "") {
        Log.v("${this.localClassName} : $tag", "$msg : ")
    }
    //--------------------------------------------------------------


    protected open fun startForRegister(code_result: Int): ActivityResultLauncher<Intent> {

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                getResult(result, code_result)
                /* if (result.resultCode== Activity.RESULT_OK){
                   getResult(result,code_result)
                 }*/

            }
        return startForResult
    }

    protected open fun getResult(result: ActivityResult, code_result: Int) {}
    protected abstract fun initM()

    protected abstract fun setLayoutId(): Int

    protected open fun keepScreenOn() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    protected open fun goActivity(activityClass: Activity, bundle: Bundle? = null) {
        if (bundle != null)
            startActivity(Intent(this, activityClass::class.java).putExtras(bundle))
        else
            startActivity(Intent(this, activityClass::class.java))
    }

    protected open fun launchActivity(activityClass: Activity, code_result: Int) {
        startForRegister(code_result).launch(Intent(this, activityClass::class.java))
    }

    protected open fun goActivity(url: String) {
        if (url.isNotBlank()) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } else showMsg("url is blank")
    }

    protected open fun goActivity(
        activityClass: Activity,
        bundle: Bundle? = null,
        flag: Int? = null
    ) {
        if (bundle != null)
            startActivity(Intent(this, activityClass::class.java).putExtras(bundle))
        else
            startActivity(
                if (flag != null) Intent(
                    this,
                    activityClass::class.java
                ).setFlags(flag) else Intent(this, activityClass::class.java)
            )

    }


    /**show custom toast message-------------------------------**/
    protected open fun Activity.showMsg(message: String) {
        Toast.makeText(mActivity, "" + message, Toast.LENGTH_SHORT).show()
    }

    protected open fun Activity.showMsg(@StringRes message: Int) {
        Toast.makeText(mActivity, "" + message, Toast.LENGTH_SHORT).show()
    }

    protected open fun Fragment.showMsg(message: String) {
        Toast.makeText(mActivity, "" + message, Toast.LENGTH_SHORT).show()
    }

    protected open fun Fragment.showMsg(@StringRes message: Int) {
        Toast.makeText(mActivity, "" + message, Toast.LENGTH_SHORT).show()
    }


    /**--------------------------------------------------**/


    protected open fun setSystemBarColor(color: Int) {
        //ImmersionBar.with(this).statusBarColor(color)

        val window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        // clear FLAG_TRANSLUCENT_STATUS flag:

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // finally change the color
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, color)

    }

    protected open fun hideStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    protected class BetterActivityResult<Input, Result>
    private constructor(
        caller: ActivityResultCaller,
        contract: ActivityResultContract<Input, Result>,
        private var onActivityResult: OnActivityResult<Result>?
    ) {
        /**
         * Callback interface
         */
        interface OnActivityResult<O> {
            /**
             * Called after receiving a result from the target activity
             */
            fun onActivityResult(result: O)
        }

        private val launcher: ActivityResultLauncher<Input>

        init {
            launcher = caller.registerForActivityResult(
                contract
            ) { result: Result -> callOnActivityResult(result) }
        }

        fun setOnActivityResult(onActivityResult: OnActivityResult<Result>?) {
            this.onActivityResult = onActivityResult
        }
        /**
         * Launch activity, same as [ActivityResultLauncher.launch] except that it allows a callback
         * executed after receiving a result from the target activity.
         */
        /**
         * Same as [.launch] with last parameter set to `null`.
         */
        @JvmOverloads
        fun launch(
            input: Input,
            onActivityResult: OnActivityResult<Result>? = this.onActivityResult
        ) {
            if (onActivityResult != null) {
                this.onActivityResult = onActivityResult
            }
            launcher.launch(input)
        }

        private fun callOnActivityResult(result: Result) {
            if (onActivityResult != null) onActivityResult!!.onActivityResult(result)
        }

        companion object {
            /**
             * Register activity result using a [ActivityResultContract] and an in-place activity result callback like
             * the default approach. You can still customise callback using [.launch].
             */
            private fun <Input, Result> registerForActivityResult(
                caller: ActivityResultCaller,
                contract: ActivityResultContract<Input, Result>,
                onActivityResult: OnActivityResult<Result>?
            ): BetterActivityResult<Input, Result> {
                return BetterActivityResult(caller, contract, onActivityResult)
            }

            /**
             * Same as [.registerForActivityResult] except
             * the last argument is set to `null`.
             */
            private fun <Input, Result> registerForActivityResult(
                caller: ActivityResultCaller,
                contract: ActivityResultContract<Input, Result>
            ): BetterActivityResult<Input, Result> {
                return registerForActivityResult(caller, contract, null)
            }

            /**
             * Specialised method for launching new activities.
             */
            fun registerActivityForResult(
                caller: ActivityResultCaller
            ): BetterActivityResult<Intent, ActivityResult> {
                return registerForActivityResult(
                    caller,
                    ActivityResultContracts.StartActivityForResult()
                )
            }
        }
    }
}