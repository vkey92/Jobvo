package co.vik.jobvo.screens.scanhistory

import android.app.Activity
import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.ActivityScannedhistoryBinding
import co.vik.jobvo.pojo.OrgainizationModel
import co.vik.jobvo.pojo.ScannedModel
import co.vik.jobvo.screens.organization.OrgViewModel
import co.vik.jobvo.screens.organization.Orgainizationadb
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty

class ScannedHistory : AppCompatActivity() {
    private lateinit var binding : ActivityScannedhistoryBinding
    private lateinit var scanViewModel: ScanViewModel
    private lateinit var progressDialog : Dialog
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.navigationBarColor = resources.getColor(R.color.belowcolor)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_scannedhistory)
        init()
    }
    private fun init(){
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            goback()
        }
        scanViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ScanViewModel::class.java)

        scanViewModel.getScanData().observe(this, Observer<ScannedModel?> { model ->
            if (model != null) {
                Log.e("history response = ", model.response.toString())
                if (model.status.equals("1", ignoreCase = true)) {
                    val adb = Scannedadb(
                        applicationContext,
                        model.data as List<ScannedModel.DataItem>,
                        this@ScannedHistory
                    )
                    binding.scanhislistview.adapter = adb

                } else
                    Toasty.error(
                        applicationContext,
                        "" + model.response,
                        Toasty.LENGTH_SHORT
                    ).show()
            }
            progressDialog.dismiss()
        })

        progressDialog = Common.showProgressDialog(this@ScannedHistory)!!
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", Common.getPreferences(applicationContext, "user_id"))
        scanViewModel.getScanData(jsonObject)

    }

    private fun goback() {
        finish()
        overridePendingTransition(R.anim.enter1, R.anim.exit1)
    }

    override fun onBackPressed() {
        goback()
        super.onBackPressed()
    }
}