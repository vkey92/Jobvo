package co.vik.jobvo.screens.dashboard

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.ActivityDashboardBinding
import co.vik.jobvo.databinding.NavHeaderMainBinding
import co.vik.jobvo.pojo.ThankuModel
import co.vik.jobvo.screens.fragments.Home
import co.vik.jobvo.screens.fragments.myqrcode.Myqrcode
import co.vik.jobvo.screens.fragments.notification.Notification
import co.vik.jobvo.screens.history.History
import co.vik.jobvo.screens.login.Login
import co.vik.jobvo.screens.webview.Help
import co.vik.jobvo.screens.webview.HelpViewModle
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import com.razorpay.PaymentResultListener
import es.dmoral.toasty.Toasty
import org.json.JSONObject

class Dashboard : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var fragmentTransaction: FragmentTransaction
    private var doubleBackToExitPressedOnce = false
    private var fragment: Fragment? = null
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var helpViewModle: HelpViewModle
    private var scanid = ""
    private lateinit var progressDialog: Dialog
    private var whatsappno = ""
    companion object {
        lateinit var headerMainBinding: NavHeaderMainBinding
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.colorAccent)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        init()
    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.bottomNavigation.inflateMenu(R.menu.bottom_nav)
        val hView: View = binding.navView.getHeaderView(0)
        headerMainBinding = NavHeaderMainBinding.bind(hView)
        whatsappno = Common.getPreferences(applicationContext,"whatsapp_number").toString()
        Glide.with(applicationContext).load(Common.getPreferences(applicationContext, "userimage"))
            .placeholder(R.mipmap.ic_launcher)
            .dontAnimate().into(headerMainBinding.profileImage)
        Log.e("username = ", Common.getPreferences(applicationContext, "username")!!)
        headerMainBinding.userName.setText(Common.getPreferences(applicationContext, "username"))
        headerMainBinding.phoneno.setText(Common.getPreferences(applicationContext, "name"))
        toggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                supportInvalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                supportInvalidateOptionsMenu()
            }
        }
        binding.drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        if (Common.isInternetOn(applicationContext)) {
            binding.bottomNavigation.setSelectedItemId(R.id.home)
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragment = Home()
            fragmentTransaction.replace(R.id.main_container, fragment as Home)
            fragmentTransaction.commit()
        } else {
            Toasty.error(
                applicationContext,
                "Please check your internet connection.",
                Toasty.LENGTH_SHORT
            ).show()
            finish()
        }

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            binding.navView.clearFocus()
            binding.navView.requestLayout()
            when (id) {
                R.id.contact -> {
                    startActivity(
                        Intent(applicationContext, Help::class.java)
                            .putExtra("click", "contactus")
                    )
                    overridePendingTransition(R.anim.enter, R.anim.exit)
                }

                R.id.aboutus -> {
                    startActivity(
                        Intent(applicationContext, Help::class.java)
                            .putExtra("click", "aboutus")
                    )
                    overridePendingTransition(R.anim.enter, R.anim.exit)
                }
                R.id.privacypolicy -> {
                    startActivity(
                        Intent(applicationContext, Help::class.java)
                            .putExtra("click", "terms")
                    )
                    overridePendingTransition(R.anim.enter, R.anim.exit)
                }
                R.id.logouticon -> {
                    val alertDialog = android.app.AlertDialog.Builder(this@Dashboard).create()
                    alertDialog.setTitle("Logout")
                    alertDialog.setMessage("Are you sure you want to logout!")

                    alertDialog.setButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        alertDialog.dismiss()
                        Common.SetPreferences(applicationContext, "login", "false")
                        Common.SetPreferences(applicationContext, "username", "")
                        //OneSignal.setSubscription(false)
                        startActivity(Intent(applicationContext, Login::class.java))
                        finish()
                        overridePendingTransition(R.anim.enter1, R.anim.exit1)
                        return@OnClickListener
                    })
                    alertDialog.show()
                }
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        })


        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.bottomhome -> {
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                    }
                    fragment = Home()
                    binding.dashlogo.setText(R.string.app_name)
                }
                  R.id.bottomnoti -> {
                      if (supportFragmentManager.backStackEntryCount > 0) {
                          supportFragmentManager.popBackStack()
                      }
                      fragment = Notification()
                      binding.dashlogo.setText("Notification")
                  }
                R.id.bottomchat -> {
                    if (Common.isInternetOn(applicationContext))
                        if (!whatsappno.equals("0", ignoreCase = true) && !whatsappno.isEmpty())
                            Common.openWhatsApp(whatsappno,this@Dashboard)
                        else
                            Toasty.error(
                                applicationContext,
                                "Chatting number is not avilalbe right now.",
                                Toasty.LENGTH_SHORT
                            ).show()
                    else
                        Toasty.error(
                            applicationContext,
                            resources.getString(R.string.checkinternet),
                            Toasty.LENGTH_SHORT
                        ).show()
                }


                R.id.bottomqrcode -> {
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                    }
                    fragment = Myqrcode()
                    binding.dashlogo.setText("Personal QR Code")
                }
            }
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragment?.let { fragmentTransaction.replace(R.id.main_container, it) }
            fragmentTransaction.commit()
            true
        })

        dashboardViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(DashboardViewModel::class.java)

        helpViewModle = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(HelpViewModle::class.java)

        dashboardViewModel.getScanData().observe(this, Observer<JsonObject?> { jsonobject ->
            if (jsonobject != null) {
                val jsonObject = JSONObject(jsonobject.toString())
                if (jsonObject.optString("status").equals("1", ignoreCase = true)) {
                    startActivity(
                        Intent(applicationContext, Help::class.java)
                            .putExtra("scanid", scanid)
                            .putExtra("click", "qrwebview")
                    )
                    overridePendingTransition(R.anim.enter, R.anim.exit)

                } else {
                    var checkhttp = false
                    if (scanid.contains("http"))
                        checkhttp = true
                    else
                        checkhttp = false
                    Common.showcommondialog1(
                        this@Dashboard,
                        "Scanned result",
                        scanid,
                        "",
                        "",
                        "",
                        checkhttp
                    )
                }
            }
            progressDialog.dismiss()
        })


        helpViewModle.getThankuData().observe(this, Observer<ThankuModel?> { thankumodel ->
            if (thankumodel != null) {
                Log.e("thanku response = ", thankumodel.response.toString())
                if (thankumodel.status.equals("1", ignoreCase = true)) {
                    successdialog(thankumodel.response)
                } else
                    Toasty.error(applicationContext, "" + thankumodel.response, Toasty.LENGTH_SHORT)
                        .show()
            }
            progressDialog.dismiss()
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //We will get scan results here
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        //check for null
        if (result != null) {
            if (result.contents == null) {
                Toasty.error(applicationContext, "Scan Cancelled", Toasty.LENGTH_SHORT).show()
            } else {
                Log.e("scan value = ", result.contents)
                scanid = result.contents
                progressDialog = Common.showProgressDialog(this@Dashboard)!!
                val jsonObject = JsonObject()
                jsonObject.addProperty(
                    "user_id", Common.getPreferences(
                        applicationContext,
                        "user_id"
                    )
                )
                jsonObject.addProperty("scan_user_id", scanid)
                jsonObject.addProperty("scanner_device_id", "xyz")
                dashboardViewModel.getScanData(jsonObject)
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        try {
            progressDialog = Common.showProgressDialog(this@Dashboard)!!
            val jsonObject = JsonObject()
            jsonObject.addProperty("user_id", Common.getPreferences(applicationContext, "user_id"))
            jsonObject.addProperty("idcardtype", "3")
            jsonObject.addProperty("razorpay_order_id", razorpayPaymentID)
            jsonObject.addProperty(
                "payamount",
                Common.getPreferences(applicationContext, "qr_code_amount")
            )
            Log.e("jsonstring = ", jsonObject.toString())
            helpViewModle.getThankuData(jsonObject)
            //Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (e: Exception) {
            Log.e("Dashboard", "Exception in onPaymentSuccess", e)
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    override fun onPaymentError(code: Int, response: String) {
        try {
            Toasty.error(this@Dashboard, "Payment failed: $code $response", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Log.e("Dashboard", "Exception in onPaymentError", e)
        }
    }


    fun successdialog(msg: String?) {
        val builder: AlertDialog.Builder
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(this@Dashboard, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(this@Dashboard)
        }
        builder.setCancelable(false)
        builder.setTitle("Jobvoo")
            .setMessage(msg)
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                dialog.dismiss()
                //onBrowseClick(url);
                startActivity(Intent(applicationContext, History::class.java))
                overridePendingTransition(R.anim.enter, R.anim.exit)
            }
            .setIcon(R.drawable.check)
            .show()
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                overridePendingTransition(R.anim.enter1, R.anim.exit1)
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }


}