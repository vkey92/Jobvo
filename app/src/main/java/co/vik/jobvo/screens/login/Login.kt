package co.vik.jobvo.screens.login

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
import co.vik.jobvo.databinding.ActivityLoginBinding
import co.vik.jobvo.pojo.Loginmodel
import co.vik.jobvo.screens.dashboard.Dashboard
import co.vik.jobvo.screens.forgotpass.Forgot
import co.vik.jobvo.screens.signup.SignUp
import com.example.qdischyd.activity.iqc.LoginViewModle
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty
import java.util.*

class Login : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModle: LoginViewModle
    private lateinit var progressDialog: Dialog
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.belowcolor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        init()
    }

    private fun init() {
        loginViewModle = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(LoginViewModle::class.java)

        binding.loginbtn.setOnClickListener(this)
        binding.forgotpass.setOnClickListener(this)
        binding.newregisterbtn.setOnClickListener(this)
        loginViewModle.getLoginData().observe(this, Observer<Loginmodel?> { loginmodel ->
            if (loginmodel != null) {
                Log.e("login response = ", loginmodel.data.toString())
                if (loginmodel.status.equals("1", ignoreCase = true)) {
                    Common.SetPreferences(applicationContext, "user_id", loginmodel.data?.userId)
                    Common.SetPreferences(applicationContext, "login", "true")
                    Common.SetPreferences(applicationContext, "username", binding.phoneeditview.text.toString().trim())
                    Common.SetPreferences(applicationContext, "userimage", loginmodel.data?.image)
                    Common.SetPreferences(applicationContext, "name", loginmodel.data?.name)
                    Common.SetPreferences(applicationContext, "email", loginmodel.data?.email)
                    Common.SetPreferences(applicationContext, "whatsapp_number", loginmodel.data?.whatsappNumber)
                    Common.SetPreferences(applicationContext, "dashboard_main_title", loginmodel.data?.dashboardMainTitle)
                    Common.SetPreferences(applicationContext, "profile_id", loginmodel.data?.profileId)
                    Common.SetPreferences(applicationContext, "idcard_amount", loginmodel.data?.idcardAmount)
                    Common.SetPreferences(applicationContext, "visitingcard_amount", loginmodel.data?.visitingcardAmount)
                    Common.SetPreferences(applicationContext, "qr_code_amount", loginmodel.data?.qrCodeAmount)
                    startActivity(Intent(applicationContext, Dashboard::class.java))
                    finish()
                    overridePendingTransition(R.anim.enter, R.anim.exit)

                } else
                    Toasty.error(applicationContext, "" + loginmodel.response, Toasty.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        })

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginbtn -> {
                var uservalue = binding.phoneeditview.text.toString().trim()
                var passvalue = binding.passeditview.text.toString().trim()
                if (uservalue.isEmpty() || uservalue.length < 10)
                    Toasty.error(applicationContext, "Please enter registered mobile number.", Toasty.LENGTH_SHORT).show()
                else if (passvalue.isEmpty())
                    Toasty.error(applicationContext, "Please enter your password.", Toasty.LENGTH_SHORT).show();
                else {
                    progressDialog = Common.showProgressDialog(this@Login)!!
                    val jsonObject = JsonObject()
                    jsonObject.addProperty("mobile", uservalue)
                    jsonObject.addProperty("password", passvalue)
                    loginViewModle.getLoginData(jsonObject)
                }
            }

            R.id.forgotpass -> {
                startActivity(Intent(applicationContext, Forgot::class.java))
                overridePendingTransition(R.anim.enter, R.anim.exit)
            }

            R.id.newregisterbtn -> {
                startActivity(Intent(applicationContext, SignUp::class.java))
                overridePendingTransition(R.anim.enter, R.anim.exit)
            }


        }
    }


}