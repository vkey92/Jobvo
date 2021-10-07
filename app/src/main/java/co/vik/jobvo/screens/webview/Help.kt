package co.vik.jobvo.screens.webview


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.ActivityHelpBinding
import co.vik.jobvo.pojo.ThankuModel
import com.google.gson.JsonObject
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import es.dmoral.toasty.Toasty
import org.json.JSONObject


class Help : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityHelpBinding
    private var webSettings: WebSettings? = null
    private var checkclick = ""
    private var userid = ""
    private var scanid = ""
    private var idcardtype = ""
    private var amount = ""
    private var isLoaded: Boolean = false
    private var webUrl = ""
    private lateinit var helpViewModle: HelpViewModle
    private lateinit var progressDialog: Dialog
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.colorAccent)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help)
        init()

    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            goback()
        }
        webSettings = binding.webview.settings
    /*    webSettings!!.setAppCacheEnabled(true)
        webSettings!!.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        webSettings!!.setJavaScriptEnabled(true)
        webSettings!!.setLoadWithOverviewMode(true)
        webSettings!!.setAllowFileAccess(true)*/

       // this is the scope function apply fro concise writing and easy to understand
        webSettings?.apply {
            setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
            javaScriptEnabled = true
            setLoadWithOverviewMode(true)
            setAllowFileAccess(true)
        }

        //if SDK version is greater of 19 then activate hardware acceleration otherwise activate software acceleration


        //if SDK version is greater of 19 then activate hardware acceleration otherwise activate software acceleration
        if (Build.VERSION.SDK_INT >= 19) {
            binding.webview.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 19) {
            binding.webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        checkclick = intent.getStringExtra("click").toString()
        binding.webview.clearHistory()
        binding.webview.clearCache(true)
        CookieSyncManager.createInstance(this)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
        binding.webview.settings.setJavaScriptEnabled(true)

        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    binding.progressBar.visibility = View.GONE
                    binding.webview.visibility = View.VISIBLE
                    view?.loadUrl(url)
                }
                return true
            }
        }
        userid = Common.getPreferences(applicationContext, "user_id").toString()
        Log.e("userid = ", userid)
        if (checkclick.equals("qrwebview", ignoreCase = true)) {
            supportActionBar!!.title = "ID Card"
            scanid = intent.getStringExtra("scanid").toString()
            // binding.webview.loadUrl(Common.weburl + "viewidcard.php?user_id=" + scanid)
            webUrl = Common.weburl + "viewidcard.php?user_id=" + scanid
        } else if (checkclick.equals("contactus", ignoreCase = true)) {
            supportActionBar!!.title = "Contact Us"
            // binding.webview.loadUrl(Common.weburl + "contact_us.html")
            webUrl = Common.weburl + "contact_us.html"
        } else if (checkclick.equals("aboutus", ignoreCase = true)) {
            supportActionBar!!.title = "About Us"
            //binding.webview.loadUrl(Common.weburl + "about.html")
            webUrl = Common.weburl + "about.html"
        } else if (checkclick.equals("terms", ignoreCase = true)) {
            supportActionBar!!.title = "Privacy Policy"
            //binding.webview.loadUrl(Common.weburl+ "privacy_policy.html")
            webUrl = Common.weburl + "privacy_policy.html"
        } else if (checkclick.equals("history", ignoreCase = true)) {
            supportActionBar!!.title = "History"
            // binding.webview.loadUrl(Common.weburl + "myorderhistory.php?user_id=" + userid)
            webUrl = Common.weburl + "myorderhistory.php?user_id=" + userid
        } else if (checkclick.equals("visitingcard", ignoreCase = true)) {
            idcardtype = "2"
            amount = Common.getPreferences(applicationContext, "visitingcard_amount").toString()
            supportActionBar!!.title = "Visiting Card"
            //   binding.webview.loadUrl(Common.weburl + "visiting_card.php?user_id=" + userid)
            webUrl = Common.weburl + "visiting_card.php?user_id=" + userid
        } else if (checkclick.equals("applycard", ignoreCase = true)) {
            supportActionBar!!.title = "Apply ID Card"
            val orgid = intent.getStringExtra("orgid")
            // binding.webview.loadUrl(Common.weburl + "applyicard.php?org_id=" + orgid + "," + userid)
            webUrl = Common.weburl + "applyicard.php?org_id=" + orgid + "," + userid
        } else if (checkclick.equals("myac", ignoreCase = true)) {
            supportActionBar!!.setTitle("My Account")
            //  binding.webview.loadUrl(Common.weburl + "myaccount.php?user_id=" + userid)
            webUrl = Common.weburl + "myaccount.php?user_id=" + userid
        } else if (checkclick.equals("myid", ignoreCase = true)) {
            idcardtype = "1"
            amount = Common.getPreferences(applicationContext, "idcard_amount").toString()
            supportActionBar!!.setTitle("My ID")
            // binding.webview.loadUrl(Common.weburl + "myidcard.php?user_id=" + userid)
            webUrl = Common.weburl + "myidcard.php?user_id=" + userid
        } else if (checkclick.equals("pdf", ignoreCase = true)) {
            Log.e("url is  = ", intent.getStringExtra("pdfurl")!!)
            supportActionBar!!.setTitle("Document")
            // binding.webview.loadUrl(intent.getStringExtra("pdfurl")!!)
            webUrl = intent.getStringExtra("pdfurl")!!
        }
        loadWebView(webUrl)

      // click event on paymentnow button
        binding.paynowbtn.setOnClickListener {
            startPayment(amount)
        }

       // initialization of viewmodel
        helpViewModle = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(HelpViewModle::class.java)

       // get data from thankyou order api
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


    private fun loadWebView(url: String) {

        binding.webview.loadUrl(url)
        binding.webview.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.progressBar.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                isLoaded = true
                binding.progressBar.visibility = View.GONE
                binding.webview.visibility = View.VISIBLE
                if (checkclick.equals("myid", ignoreCase = true) || checkclick.equals(
                        "visitingcard",
                        ignoreCase = true
                    )
                )
                    binding.paynowbtn.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                isLoaded = false
                val errorMessage = "Got Error! $error"
                binding.progressBar.visibility = View.GONE

                super.onReceivedError(view, request, error)
            }
        }
    }

    private fun successdialog(msg: String?) {
        val builder: AlertDialog.Builder
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(this@Help, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(this@Help)
        }
        builder.setCancelable(false)
        builder.setTitle("Jobvoo")
            .setMessage(msg)
            .setPositiveButton(
                android.R.string.yes
            ) { dialog, which ->
                dialog.dismiss()
                //startActivity(Intent(applicationContext, History::class.java))
                //overridePendingTransition(R.anim.enter, R.anim.exit)
                //onBrowseClick(url);
            }
            .setIcon(R.drawable.check)
            .show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (binding.webview.canGoBack()) {
                    binding.webview.goBack()
                } else {
                    goback()
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun startPayment(amount: String?) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        //int amountvalue = Integer.parseInt(amount)*100;
        val amountvalue = "1".toInt() * 100
        Log.e("amoutn is = ", "" + amountvalue)
        val activity: Activity = this
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", "Jobvoo")
            options.put("description", "Id charges")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", "" + amountvalue)
            val preFill = JSONObject()
            preFill.put("email", Common.getPreferences(applicationContext, "email"))
            preFill.put("contact", Common.getPreferences(applicationContext, "username"))
            options.put("prefill", preFill)
            co.open(activity, options)
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        try {
            progressDialog = Common.showProgressDialog(this@Help)!!
            val jsonObject = JsonObject()
            jsonObject.addProperty("user_id", userid)
            jsonObject.addProperty("idcardtype", idcardtype)
            jsonObject.addProperty("razorpay_order_id", razorpayPaymentID)
            jsonObject.addProperty("payamount", amount)
            Log.e("jsonstring = ", jsonObject.toString())
            helpViewModle.getThankuData(jsonObject)
            //Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (e: Exception) {
            Log.e("WebView = ", "Exception in onPaymentSuccess", e)
        }
    }


    override fun onPaymentError(code: Int, response: String) {
        try {
            Toasty.error(this, "Payment failed: $code $response", Toasty.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("WebView", "Exception in onPaymentError", e)
        }
    }


    private fun goback() {
        finish()
        overridePendingTransition(R.anim.enter1, R.anim.exit1)
    }


}