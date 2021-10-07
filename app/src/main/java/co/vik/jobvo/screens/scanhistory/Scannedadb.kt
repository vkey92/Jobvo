package co.vik.jobvo.screens.scanhistory

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.vik.jobvo.R
import co.vik.jobvo.databinding.ScannedViewBinding
import co.vik.jobvo.pojo.ScannedModel
import co.vik.jobvo.screens.webview.Help

class Scannedadb(
    var context: Context,
    productlist: List<ScannedModel.DataItem>,
    parentActivity: Activity
) :
    RecyclerView.Adapter<Scannedadb.MyViewHolder>() {
    var inflater: LayoutInflater
    var productlist: List<ScannedModel.DataItem>
    var parentActivity: Activity
    private val bounce: Animation
    fun updateData(productlist: List<ScannedModel.DataItem>) {
        this.productlist = productlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ScannedViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.scanned_view, viewGroup, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.binding.setScan(productlist[position])
        viewHolder.binding.lbScantime.setText(
            productlist[position].scanTime
                .toString() + " " + productlist[position].scanDate
        )
        val status: String? = productlist[position].approvedStatus
        Log.e("username == ", productlist[position].userName.toString())
        if (status != null) {
            Log.e("status == ", status)
        }

        if (status.equals("1", ignoreCase = true))
            viewHolder.binding.imgApprov.setVisibility(View.VISIBLE) else viewHolder.binding.imgApprov.setVisibility(View.INVISIBLE)
        viewHolder.binding.btView.setOnClickListener(View.OnClickListener { view ->
            view.startAnimation(bounce)
            context.startActivity(
                Intent(context, Help::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("scanid", productlist[position].batchNumber)
                    .putExtra("click", "qrwebview")
            )
            parentActivity.overridePendingTransition(R.anim.enter, R.anim.exit)
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

    inner class MyViewHolder(binding: ScannedViewBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: ScannedViewBinding

        init {
            this.binding = binding
        }
    }

    init {
        // TODO Auto-generated constructor stub
        this.productlist = productlist
        this.parentActivity = parentActivity
        inflater = LayoutInflater.from(context)
        bounce = AnimationUtils.loadAnimation(parentActivity, R.anim.bouncing)
    }
}







