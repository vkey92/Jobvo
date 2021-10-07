package co.vik.jobvo.screens.forgotpass

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.ActivityForgotBinding
import co.vik.jobvo.pojo.Forgotmodel
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty

class Forgot : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityForgotBinding
    private lateinit var progressDialog: Dialog
    private lateinit var forgotViewModel: ForgotViewModel
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.belowcolor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot)
        init()
    }

    fun init() {

        binding.forgotsavebtn.setOnClickListener(this)
        forgotViewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ForgotViewModel::class.java)

        forgotViewModel.getForgotData().observe(this, Observer<Forgotmodel?> { forgotmodel ->
            if (forgotmodel != null) {
                if(forgotmodel.status.equals("1",ignoreCase = true)) {
                    Log.e("forgot response = ", forgotmodel.toString())
                    Toasty.success(
                        applicationContext,
                        forgotmodel.response.toString(),
                        Toasty.LENGTH_SHORT
                    ).show()
                    finish()
                }else
                    Toasty.error(applicationContext,""+forgotmodel.response,Toasty.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.forgotsavebtn -> {
                val forgotphone = binding.phoneeditview.text.toString().trim()
                val newpass = binding.newpasseditview.text.toString().trim()
                val confirmpass = binding.confirmpasseditview.text.toString().trim()

                if (forgotphone.isEmpty() || forgotphone.length < 10)
                    binding.phoneeditview.error = "Enter your Registered mobile no."
                else if (newpass.isEmpty() || newpass.length < 3) binding.newpasseditview.error = "Password must be greater than or equal to 3."
                else if (!newpass.equals(confirmpass, ignoreCase = true))
                    binding.confirmpasseditview.error = "Confirm password not match."
                else{
                    progressDialog = Common.showProgressDialog(this@Forgot)!!
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("mobile", forgotphone)
                    jsonObject.addProperty("password", newpass)
                    forgotViewModel.getForgotData(jsonObject)
                }


            }

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.enter1, R.anim.exit1)
    }
}