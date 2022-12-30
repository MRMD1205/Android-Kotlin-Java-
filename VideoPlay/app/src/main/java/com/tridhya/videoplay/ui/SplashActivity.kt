package com.tridhya.videoplay.ui

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import com.tridhya.videoplay.R
import com.tridhya.videoplay.databinding.ActivitySplashBinding
import com.tridhya.videoplay.extentions.goToActivityAndClearTask
import com.tridhya.videoplay.ui.base.BaseActivity

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivAppLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_fad_in))
        Handler().postDelayed({
            val optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, binding.ivAppLogo, "appLogo")
            goToActivityAndClearTask<MainActivity>(optionsCompat.toBundle())
        }, 2500)
    }
}
