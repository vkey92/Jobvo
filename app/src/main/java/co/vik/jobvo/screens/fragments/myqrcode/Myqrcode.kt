package co.vik.jobvo.screens.fragments.myqrcode

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.MyQrcodeBinding
import co.vik.jobvo.pojo.MyqrModel
import com.google.gson.JsonObject
import com.razorpay.Checkout
import es.dmoral.toasty.Toasty
import org.json.JSONObject

class Myqrcode : Fragment(),View.OnClickListener{
    private lateinit var binding: MyQrcodeBinding
    private lateinit var myqrViewModel: MyqrViewModel
    private lateinit var progressDialog : Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<MyQrcodeBinding>(
            inflater,
            R.layout.my_qrcode, container, false
        )
        init()
        return binding.root
    }

    private fun init(){
        binding.paynowbtn.setOnClickListener(this)
        myqrViewModel = ViewModelProvider(this).get(MyqrViewModel::class.java)
        myqrViewModel.getQrinfoData().observe(
            getViewLifecycleOwner(),
            Observer<MyqrModel?> { myqrmodel ->
                if (myqrmodel != null) {
                    Log.e("getinfo response = ", myqrmodel.response.toString())
                    if (myqrmodel.status.equals("1", ignoreCase = true))
                        binding.info = myqrmodel
                    else
                        Toasty.error(
                            requireActivity(),
                            "" + myqrmodel.response,
                            Toasty.LENGTH_SHORT
                        )
                            .show()

                }
                progressDialog.dismiss()
            })

        progressDialog = Common.showProgressDialog(activity)!!
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", Common.getPreferences(requireActivity(), "user_id"))
        myqrViewModel.getQrinfoData(jsonObject)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.paynowbtn -> {
                startPayment(Common.getPreferences(requireActivity(), "qr_code_amount")!!)
            }
        }
    }

    private fun startPayment(amount: String) {
        val amountvalue = amount.toInt() * 100
        Log.e("amoutn is = ", "" + amountvalue)
        val activity: Activity? = activity
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
            preFill.put("email", Common.getPreferences(activity!!, "email"))
            preFill.put("contact", Common.getPreferences(activity!!, "username"))
            options.put("prefill", preFill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toasty.error(activity!!, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }
}