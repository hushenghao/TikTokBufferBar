package com.dede.tiktok

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progress_bar.indeterminateDrawable = TikTokBufferDrawable().apply {
            setColor(0xFF868686.toInt())
        }
    }

    fun toggle(view: View) {
        if (tiktok_bar.visibility == View.VISIBLE) {
            tiktok_bar.visibility = View.GONE
            progress_bar.visibility = View.GONE
        } else {
            tiktok_bar.visibility = View.VISIBLE
            progress_bar.visibility = View.VISIBLE
        }
    }
}
