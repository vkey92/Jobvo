package co.vik.jobvo.screens.organization

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.common.SimpleDividerItemDecoration
import co.vik.jobvo.databinding.ActivityOrgainizationListBinding
import co.vik.jobvo.pojo.HistoryModel
import co.vik.jobvo.pojo.OrgainizationModel

import co.vik.jobvo.screens.history.Historyadb
import co.vik.jobvo.screens.history.HistoryviewModel
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty

class OrgainizationList : AppCompatActivity() {
    private lateinit var binding: ActivityOrgainizationListBinding
    private lateinit var orgViewModel: OrgViewModel
    private lateinit var progressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orgainization_list)
        init()
    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            goback()
        }
        orgViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(OrgViewModel::class.java)

        orgViewModel.getOrgData().observe(this, Observer<OrgainizationModel?> { model ->
            if (model != null) {
                Log.e("history response = ", model.response.toString())
                if (model.status.equals("1", ignoreCase = true)) {
                    val adb = Orgainizationadb(
                        applicationContext,
                        model.data as List<OrgainizationModel.DataItem>,
                        this@OrgainizationList
                    )
                    binding.orgailistview.adapter = adb

                } else
                    Toasty.error(
                        applicationContext,
                        "" + model.response,
                        Toasty.LENGTH_SHORT
                    ).show()
            }
            progressDialog.dismiss()
        })

        progressDialog = Common.showProgressDialog(this@OrgainizationList)!!
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", Common.getPreferences(applicationContext, "user_id"))
        orgViewModel.getOrgData(jsonObject)

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