package com.dede.tiktok

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toggle(view: View) {
        tiktok_bar.visibility =
            if (tiktok_bar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }
}
