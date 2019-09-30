package com.xdd.openweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.xdd.openweather.databinding.ActivityMainBinding
import com.xdd.openweather.view.WeatherFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDataBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)!!
        viewDataBinding.lifecycleOwner = this
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, WeatherFragment(), WeatherFragment.TAG)
            .commit()
    }
}
