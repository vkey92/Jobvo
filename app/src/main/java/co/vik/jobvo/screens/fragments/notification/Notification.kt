package co.vik.jobvo.screens.fragments.notification

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.common.SimpleDividerItemDecoration
import co.vik.jobvo.databinding.NotificationfragBinding
import co.vik.jobvo.pojo.Notipojo
import com.google.gson.JsonObject
import es.dmoral.toasty.Toasty

class Notification : Fragment() {
    private lateinit var binding: NotificationfragBinding
    private lateinit var notiViewModel: NotiViewModel
    private lateinit var progressDialog: Dialog
    private lateinit var mLayoutManager: LinearLayoutManager
    private var startpage = 0
    private var recordsTotal = 0
    private var isLoading = false
    private var notilist = ArrayList<Notipojo.NotiDataItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<NotificationfragBinding>(
            inflater,
            R.layout.notificationfrag, container, false
        )
        init()
        return binding.root
    }

    private fun init() {
        notilist = ArrayList()
        notilist.clear()
        mLayoutManager =
            LinearLayoutManager::class.java.cast(binding.notificationlistview.layoutManager)

        binding.notificationlistview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isLoading) {
                        startpage += 1
                        getnotification()
                        isLoading = false
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount: Int = mLayoutManager.getItemCount()
                val lastVisible: Int = mLayoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible + 5 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached) {
                    Log.e("this item = ", "last itme")
                }
            }
        })

        notiViewModel = ViewModelProvider(this).get(NotiViewModel::class.java)
        notiViewModel.getNotiData().observe(
            getViewLifecycleOwner(),
            Observer<Notipojo?> { notipojo ->
                if (notipojo != null) {
                    Log.e("noti response = ", notipojo.response.toString())
                    recordsTotal = notipojo.totalRecord

                    if (notilist.size < recordsTotal)
                        isLoading = true
                    else
                        isLoading = false

                    if (notipojo.status.equals("1", ignoreCase = true)) {
                        notilist.addAll(notipojo.data!!)
                        val recyclerViewState: Parcelable?
                        recyclerViewState =
                            binding.notificationlistview.layoutManager!!.onSaveInstanceState()
                        binding.notificationlistview.addItemDecoration(
                            SimpleDividerItemDecoration(
                                requireActivity(),
                                resources.getColor(R.color.white),
                                5f
                            )
                        )
                        val notiadb = Notiadb(
                            requireActivity(),
                            notilist,
                            requireActivity()
                        )
                        binding.notificationlistview.adapter = notiadb
                        binding.notificationlistview.layoutManager!!.onRestoreInstanceState(
                            recyclerViewState
                        )

                    } else
                        Toasty.error(
                            requireActivity(),
                            "" + notipojo.response,
                            Toasty.LENGTH_SHORT
                        )
                            .show()

                }
                progressDialog.dismiss()
            })

        getnotification()

    }

    private fun getnotification() {
        progressDialog = Common.showProgressDialog(activity)!!
        val jsonObject = JsonObject()
        jsonObject.addProperty("user_id", Common.getPreferences(requireActivity(), "user_id"))
        jsonObject.addProperty("count_number", "" + startpage)
        notiViewModel.getNotiData(jsonObject)
    }
}