package co.vik.jobvo.screens.signup

import android.app.Dialog
import android.content.Intent
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
import co.vik.jobvo.databinding.ActivitySignUpBinding
import co.vik.jobvo.pojo.Otpmodel
import co.vik.jobvo.screens.otp.Otpscreen
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty

class SignUp : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var progressDialog: Dialog
    private var username = ""
    private var phonenumber = ""
    private var email = ""
    private var password = ""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.belowcolor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        init()
    }

    private fun init() {
        signupViewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(SignupViewModel::class.java)
        binding.registerbtn.setOnClickListener(this)

        signupViewModel.getOtpData().observe(this, Observer<Otpmodel?> { otpmodel ->
            if (otpmodel != null) {
                Log.e("otp response = ", otpmodel.response.toString())
                if (otpmodel.status.equals("1", ignoreCase = true)) {
                    if (otpmodel.data?.otpVerify.equals("0")) {
                        startActivity(Intent(applicationContext, Otpscreen::class.java)
                                .putExtra("username", binding.usernameeditview.text.toString().trim())
                                .putExtra("phone", binding.mobilenoeditview.text.toString().trim())
                                .putExtra("password", binding.passwordeditview.text.toString().trim())
                                .putExtra("email", binding.emaileditview.text.toString().trim()))
                        overridePendingTransition(R.anim.enter, R.anim.exit)
                    }

                } else
                    Toasty.error(applicationContext, "" + otpmodel.response, Toasty.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.registerbtn -> {
                username = binding.usernameeditview.text.toString().trim()
                phonenumber = binding.mobilenoeditview.text.toString().trim()
                email = binding.emaileditview.text.toString().trim()
                password = binding.passwordeditview.text.toString().trim()
                if (validate()) {
                    progressDialog = Common.showProgressDialog(this@SignUp)!!
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("mobile", phonenumber)
                    jsonObject.addProperty("otp", "")
                    signupViewModel.getOtpData(jsonObject)
                }
            }
        }
    }

    fun validate(): Boolean {
        var valid = true
        binding.usernameeditview.error = null
        binding.mobilenoeditview.error = null
        binding.passwordeditview.error = null
        binding.emaileditview.error = null
        if (username.isEmpty()) {
            binding.usernameeditview.error = "Enter your user name."
            valid = false
        } else if (phonenumber.isEmpty() || phonenumber.length < 10) {
            binding.mobilenoeditview.error = "Enter your valid mobile number."
            valid = false
        } else if (email.isEmpty() || !Common.emailValidator(email)) {
            binding.emaileditview.error = "Enter your valid email."
            valid = false
        } else if (password.length < 3 ){
            binding.passwordeditview.error = "Password length must be 3 or equal"
            valid = false
        }
        return valid
    }
}