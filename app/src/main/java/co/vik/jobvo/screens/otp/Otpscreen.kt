package co.vik.jobvo.screens.otp

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.ActivityOtpscreenBinding
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.pojo.RegisterModel
import co.vik.jobvo.pojo.ResendOtpModel
import com.google.gson.JsonObject

import es.dmoral.toasty.Toasty

class Otpscreen : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOtpscreenBinding
    private lateinit var otpViewModel: OtpViewModel
    private lateinit var progressDialog: Dialog
    private var username = ""
    private var email = ""
    private var mobileno = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpscreen)
        init()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.otpbtn -> {
                progressDialog = Common.showProgressDialog(this@Otpscreen)!!
                val jsonObject = JsonObject()
                jsonObject.addProperty("mobile", mobileno)
                jsonObject.addProperty("otp", binding.pinview.value)
                otpViewModel.getOtpData(jsonObject)
            }

            R.id.resendotpbtn -> {
                progressDialog = Common.showProgressDialog(this@Otpscreen)!!
                val jsonObject = JsonObject()
                jsonObject.addProperty("mobile", mobileno)
                otpViewModel.getResendData(jsonObject)
            }

        }
    }

    private fun init() {
        otpViewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(OtpViewModel::class.java)
        binding.otpbtn.setOnClickListener(this)
        username = intent.getStringExtra("username")!!
        mobileno = intent.getStringExtra("phone")!!
        password = intent.getStringExtra("password")!!
        email = intent.getStringExtra("email")!!
        binding.otpbtn.setOnClickListener(this)
        binding.resendotpbtn.setOnClickListener(this)

        otpViewModel.getOtpData().observe(this, Observer<Otpmodel?> { otpmodel ->
            if (otpmodel != null) {
                Log.e("otp response = ", otpmodel.response.toString())
                if (otpmodel.status.equals("1", ignoreCase = true)) {
                    binding.otptxtview.setVisibility(View.GONE)
                    if (otpmodel.data?.otpVerify.equals("1")) {

                        val jsonObject = JsonObject()
                        jsonObject.addProperty("username", username)
                        jsonObject.addProperty("mobile", mobileno)
                        jsonObject.addProperty("email", email)
                        jsonObject.addProperty("password", password)
                        jsonObject.addProperty("device_id", "xyz")
                        otpViewModel.getRegisterData(jsonObject)
                    }

                } else {
                    progressDialog.dismiss()
                    Toasty.error(applicationContext, "" + otpmodel.response, Toasty.LENGTH_SHORT).show()
                }
            }

        })


        otpViewModel.getResendData().observe(this, Observer<ResendOtpModel?> { resendOtpModel ->
            if (resendOtpModel != null) {
                Log.e("reesndotp response = ", resendOtpModel.response.toString())
                binding.otptxtview.setVisibility(View.VISIBLE)
                binding.otptxtview.setText("" + resendOtpModel.response)
                progressDialog.dismiss()
            }

        })


        otpViewModel.getRegisterData().observe(this, Observer<RegisterModel?> { registermodel ->
            if (registermodel != null) {
                Log.e("register response = ", registermodel.response.toString())
                if (registermodel.status.equals("1", ignoreCase = true)) {
                    Common.SetPreferences(applicationContext, "user_id", registermodel.data?.userId)
                    Common.SetPreferences(applicationContext, "login", "true")
                    Common.SetPreferences(applicationContext, "username", username)
                    Common.SetPreferences(applicationContext, "userimage", registermodel.data?.image)
                    Common.SetPreferences(applicationContext, "name", registermodel.data?.name as String?)
                    Common.SetPreferences(applicationContext, "whatsapp_number", registermodel.data?.whatsappNumber)
                    Common.SetPreferences(applicationContext, "dashboard_main_title", registermodel.data?.dashboardMainTitle)
                    Common.SetPreferences(applicationContext, "profile_id", registermodel.data?.profileId)
                    Common.SetPreferences(applicationContext, "idcard_amount", registermodel.data?.idcardAmount)
                    Common.SetPreferences(applicationContext, "visitingcard_amount", registermodel.data?.visitingcardAmount)
                    Common.SetPreferences(applicationContext, "qr_code_amount", registermodel.data?.qrCodeAmount)

                } else
                    Toasty.error(applicationContext, "" + registermodel.response, Toasty.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        })

    }
}