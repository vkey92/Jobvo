package co.vik.jobvo.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.ActivitySplashBinding
import co.vik.jobvo.screens.dashboard.Dashboard
import co.vik.jobvo.screens.login.Login
import es.dmoral.toasty.Toasty

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var splashTread: Thread
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.window.navigationBarColor = resources.getColor(R.color.colorAccent)
        setContentView(R.layout.activity_splash)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        init()
    }

    private fun init() {
        if (Common.isInternetOn(applicationContext)) {
            splashTread = object : Thread() {
                override fun run() {
                    try {
                        var waited = 0
                        // Splash screen pause time
                        while (waited < 3500) {
                            sleep(100)
                            waited += 100
                        }
                        if (Common.getPreferences(applicationContext, "login").equals("true")
                        ) {
                            startActivity(Intent(applicationContext, Dashboard::class.java))
                            finish()
                            overridePendingTransition(R.anim.enter, R.anim.exit)
                        } else {
                            startActivity(Intent(applicationContext, Login::class.java))
                            finish()
                            overridePendingTransition(R.anim.enter, R.anim.exit)
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()

                    }
                }
            }
            splashTread.start()
        } else {
            Toasty.error(
                applicationContext,
                "Please check your internet connection",
                Toasty.LENGTH_SHORT
            ).show()
            finish()
        }
    }
}