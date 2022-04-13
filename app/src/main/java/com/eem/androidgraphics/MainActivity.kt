package com.eem.androidgraphics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eem.androidgraphics.menu.MenuFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.base_container,
            MenuFragment.newInstance()
        ).commit()
    }
}