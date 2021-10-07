package co.vik.jobvo.screens.history

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.HistoryViewBinding
import co.vik.jobvo.pojo.HistoryModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import es.dmoral.toasty.Toasty

class Historyadb(
    var context: Context,
    productlist: List<HistoryModel.DataItem>,
    parentActivity: Activity
) : RecyclerView.Adapter<Historyadb.MyViewHolder>() {
    var inflater: LayoutInflater
    var productlist: List<HistoryModel.DataItem>
    var parentActivity: Activity
    fun updateData(productlist: List<HistoryModel.DataItem>) {
        this.productlist = productlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val binding: HistoryViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.history_view, viewGroup, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        if (position > 0) {
            viewHolder.binding.headerlayout.setVisibility(View.GONE)
            viewHolder.binding.belowview.setVisibility(View.GONE)
        }
        viewHolder.binding.setHistory(productlist[position])
        val srno = position + 1
        viewHolder.binding.lbSerial.setText("" + srno)
        viewHolder.binding.btDownload.setOnClickListener(View.OnClickListener {
            if (productlist[position].idCardGanrateStatus.equals("1", ignoreCase = false))
                requestPermission(position)
            else
                Toasty.error(
                    parentActivity,
                    productlist[position].idCardMsg.toString(),
                    Toasty.LENGTH_SHORT
                ).show()
        })
    }

    override fun getItemCount(): Int {
        return productlist.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(binding: HistoryViewBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: HistoryViewBinding

        init {
            this.binding = binding
        }
    }

    private fun requestPermission(pos: Int) {
        Dexter.withActivity(parentActivity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted
                     requestrightpermission(pos)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        Common.showSettingsDialog(parentActivity)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

     private fun requestrightpermission(pos: Int) {
         Dexter.withActivity(parentActivity)
             .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
             .withListener(object : PermissionListener {
                 override fun onPermissionGranted(response: PermissionGrantedResponse) {
                     // permission is granted
                     val filename = "jobvoo$pos"
                     // ((History)parentActivity).downloadfile(productlist.get(pos).getDownloadPath(), filename, ".pdf");
                     Log.e("download path = ", productlist[pos].downloadPath.toString())
                     val type: String = productlist[pos].downloadType.toString()
                     (parentActivity as History).downloadfile(
                         productlist[pos].downloadPath.toString(),
                         type
                     )
                 }

                 override fun onPermissionDenied(response: PermissionDeniedResponse) {
                     // check for permanent denial of permission
                     if (response.isPermanentlyDenied) {
                         Common.showSettingsDialog(parentActivity)
                     }
                 }

                 override fun onPermissionRationaleShouldBeShown(
                     permission: PermissionRequest,
                     token: PermissionToken
                 ) {
                     token.continuePermissionRequest()
                 }
             }).check()
     }

    init {
        // TODO Auto-generated constructor stub
        this.productlist = productlist
        this.parentActivity = parentActivity
        inflater = LayoutInflater.from(context)
    }
}


