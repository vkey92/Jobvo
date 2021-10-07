package co.vik.jobvo.screens.fragments.notification

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.vik.jobvo.R
import co.vik.jobvo.databinding.NotiViewBinding
import co.vik.jobvo.pojo.Notipojo

class Notiadb(var context: Context, productlist: List<Notipojo.NotiDataItem>, parentActivity: Activity) :
    RecyclerView.Adapter<Notiadb.MyViewHolder>() {
    var inflater: LayoutInflater
    var productlist: List<Notipojo.NotiDataItem>
    var parentActivity: Activity
    fun updateData(productlist: List<Notipojo.NotiDataItem>) {
        this.productlist = productlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val binding: NotiViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.noti_view, viewGroup, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.binding.setNoti(productlist[position])
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

    inner class MyViewHolder(binding: NotiViewBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: NotiViewBinding

        init {
            this.binding = binding
        }
    }

    init {
        // TODO Auto-generated constructor stub
        this.productlist = productlist
        this.parentActivity = parentActivity
        inflater = LayoutInflater.from(context)
    }
}
