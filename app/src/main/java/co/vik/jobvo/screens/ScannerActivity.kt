package co.vik.jobvo.screens

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import co.vik.jobvo.R
import co.vik.jobvo.databinding.ActivityScannerBinding
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener
import kotlinx.android.synthetic.main.custom_scanner.view.*

class ScannerActivity : AppCompatActivity(), TorchListener{
    private lateinit var binding: ActivityScannerBinding
    private var isFlashLightOn = false
    private var capture: CaptureManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scanner)
        init()
    }

    private fun init() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(R.anim.enter1, R.anim.exit1)
        }

        val animBlink = AnimationUtils.loadAnimation(this, R.anim.blink)
        binding.zxingBarcodeScanner.zxing_status_view.startAnimation(animBlink)
        binding.zxingBarcodeScanner.setTorchListener(this)
        if (!hasFlash()) {
            binding.switchFlashlight.setVisibility(View.GONE)
        } else {
            binding.switchFlashlight.setOnClickListener(View.OnClickListener { switchFlashlight() })
        }
        capture = CaptureManager(this, binding.zxingBarcodeScanner)
        capture!!.decode()

    }

    private fun hasFlash(): Boolean {
        return applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    fun switchFlashlight() {
        isFlashLightOn = if (isFlashLightOn) {
            binding.zxingBarcodeScanner.setTorchOff()
            false
        } else {
            binding.zxingBarcodeScanner.setTorchOn()
            true
        }
    }

    override fun onTorchOn() {
        binding.switchFlashlight.setText(R.string.turn_off_flashlight)
    }

    override fun onTorchOff() {
        binding.switchFlashlight.setText(R.string.turn_on_flashlight)
    }

    override fun onResume() {
        super.onResume()
        capture?.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return binding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(
            keyCode,
            event
        )
    }

}