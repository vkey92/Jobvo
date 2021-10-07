package co.vik.jobvo.screens.history

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.common.DownloadTask
import co.vik.jobvo.common.SimpleDividerItemDecoration
import co.vik.jobvo.databinding.ActivityHistoryBinding
import co.vik.jobvo.pojo.HistoryModel
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty

class History : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyviewModel: HistoryviewModel
    private lateinit var progressDialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        init()
    }

    private fun init(){
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.enter1, R.anim.exit1)
        }

        historyviewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(HistoryviewModel::class.java)

        historyviewModel.getHistoryData().observe(this, Observer<HistoryModel?> { historymodel ->
            if (historymodel != null) {
                Log.e("history response = ", historymodel.response.toString())
                if (historymodel.status.equals("1", ignoreCase = true)) {

                    binding.hislistview.addItemDecoration(
                        SimpleDividerItemDecoration(
                            this@History,
                            resources.getColor(R.color.list_divider),
                            1f
                        )
                    )
                    val historyadb = Historyadb(
                        this@History,
                        historymodel.data as List<HistoryModel.DataItem>, this@History
                    )
                    binding.hislistview.adapter = historyadb
                } else
                    Toasty.error(
                        applicationContext,
                        "" + historymodel.response,
                        Toasty.LENGTH_SHORT
                    ).show()
            }
            progressDialog.dismiss()
        })

        progressDialog = Common.showProgressDialog(this@History)!!
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", Common.getPreferences(applicationContext, "user_id"))
        historyviewModel.getHistoryData(jsonObject)
    }

    fun downloadfile(URL: String?, type: String?) {
        DownloadTask(this@History, URL, type)
    }
}