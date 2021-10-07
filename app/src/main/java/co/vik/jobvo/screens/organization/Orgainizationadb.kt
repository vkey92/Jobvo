package co.vik.jobvo.screens.organization

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.vik.jobvo.R
import co.vik.jobvo.databinding.OrgainizationViewBinding
import co.vik.jobvo.pojo.OrgainizationModel

import co.vik.jobvo.screens.webview.Help

class Orgainizationadb(
    var context: Context,
    productlist: List<OrgainizationModel.DataItem>,
    parentActivity: Activity) :
    RecyclerView.Adapter<Orgainizationadb.MyViewHolder>() {
    var inflater: LayoutInflater
    var productlist: List<OrgainizationModel.DataItem>
    var parentActivity: Activity
    private val bounce: Animation
    fun updateData(productlist: List<OrgainizationModel.DataItem>) {
        this.productlist = productlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val binding: OrgainizationViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.orgainization_view, viewGroup, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.binding.setOrgainization(productlist[position])
        viewHolder.binding.mainlayout.setOnClickListener(View.OnClickListener { view ->
            view.startAnimation(bounce)
            context.startActivity(
                Intent(context, Help::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("click", "applycard")
                    .putExtra("orgid", productlist[position].orgUserId)
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

    inner class MyViewHolder(binding: OrgainizationViewBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: OrgainizationViewBinding

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
