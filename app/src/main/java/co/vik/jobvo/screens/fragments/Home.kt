package co.vik.jobvo.screens.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import co.vik.jobvo.R
import co.vik.jobvo.common.Common
import co.vik.jobvo.databinding.FragHomeBinding
import co.vik.jobvo.screens.ScannerActivity
import co.vik.jobvo.screens.history.History
import co.vik.jobvo.screens.organization.OrgainizationList
import co.vik.jobvo.screens.profile.Profile
import co.vik.jobvo.screens.scanhistory.ScannedHistory
import co.vik.jobvo.screens.webview.Help
import com.google.zxing.integration.android.IntentIntegrator
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class Home : Fragment(), View.OnClickListener {
    private lateinit var binding: FragHomeBinding
    private lateinit var bounce: Animation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragHomeBinding>(
            inflater,
            R.layout.frag_home, container, false
        )
        init()
        return binding.root
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.mybtn -> {
                binding.mybtn.startAnimation(bounce);
                startActivity(
                    Intent(activity, Help::class.java)
                        .putExtra("click", "myac")
                )
                activity?.overridePendingTransition(R.anim.enter, R.anim.exit)

            }

            R.id.scanqrbtn -> {
                binding.scanqrbtn.startAnimation(bounce)
                requestCameraPermission()

            }

            R.id.myidbtn -> {
                binding.myidbtn.startAnimation(bounce);
                requestexternalstorage()
            }


            R.id.myhisbtn -> {
                binding.myhisbtn.startAnimation(bounce);
                startActivity(Intent(activity, History::class.java))
                activity?.overridePendingTransition(R.anim.enter, R.anim.exit)

            }

            R.id.btnvisitingcard -> {
                binding.btnvisitingcard.startAnimation(bounce);
                startActivity(
                    Intent(activity, Help::class.java)
                        .putExtra("click", "visitingcard")
                )
                activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
            }

            R.id.btnapplyqr -> {
                binding.btnapplyqr.startAnimation(bounce);
                startActivity(Intent(activity, OrgainizationList::class.java))
                activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
            }

            R.id.profilebtn -> {
                binding.profilebtn.startAnimation(bounce);
                startActivity(Intent(activity, Profile::class.java))
                activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
            }


            R.id.bt_scanhistory -> {
                binding.btScanhistory.startAnimation(bounce);
                startActivity(Intent(activity, ScannedHistory::class.java))
                activity?.overridePendingTransition(R.anim.enter, R.anim.exit)
            }

        }
    }

    private fun init() {
        binding.lbTitle.setText(getActivity()?.let {
            Common.getPreferences(
                it.applicationContext,
                "dashboard_main_title"
            )
        })
        binding.lbProfileid.text = "Profile Id : " + getActivity()?.let {
            Common.getPreferences(
                it.applicationContext,
                "profile_id"
            )
        }
        bounce = AnimationUtils.loadAnimation(activity, R.anim.bouncing)
        binding.scanqrbtn.setOnClickListener(this)
        binding.btScanhistory.setOnClickListener(this)
        binding.profilebtn.setOnClickListener(this)
        binding.mybtn.setOnClickListener(this)
        binding.myhisbtn.setOnClickListener(this)
        binding.myidbtn.setOnClickListener(this)
        binding.btnvisitingcard.setOnClickListener(this)
        binding.btnapplyqr.setOnClickListener(this)
    }

    private fun requestCameraPermission() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted
                    IntentIntegrator(activity).setCaptureActivity(ScannerActivity::class.java)
                        .initiateScan()
                    activity!!.overridePendingTransition(R.anim.enter, R.anim.exit)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        Common.showSettingsDialog(activity!!)
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

    private fun requestexternalstorage() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    // permission is granted
                    startActivity(
                        Intent(activity, Help::class.java)
                            .putExtra("click", "myid")
                    )
                    activity!!.overridePendingTransition(R.anim.enter, R.anim.exit)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied()) {
                        Common.showSettingsDialog(activity!!)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }
}