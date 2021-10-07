package co.vik.jobvo.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class Loadimage {
    companion object {
       @JvmStatic @BindingAdapter("bind:imageUrl")
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context).load(imageUrl) //.placeholder(R.mipmap.ic_launcher)
                .dontAnimate().into(view)
        }
    }
}