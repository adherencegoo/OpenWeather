package com.xdd.openweather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.xdd.openweather.databinding.ActivityMainBinding
import com.xdd.openweather.view.LocWeatherRecyclerAdapter
import com.xdd.openweather.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewDataBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        val viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        viewDataBinding.contentMain.weatherViewModel = viewModel

        // set up RecyclerView and Adapter
        val locWeatherAdapter = LocWeatherRecyclerAdapter()
        forecastRecycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = locWeatherAdapter
        }

        viewModel.liveForecast.observe(this, Observer {
            locWeatherAdapter.postData(it.records.locationWeatherInfoList)
        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            viewModel.updateForecasts(
                locations = viewModel.locationSelector.getSelectedEnums(),
                weatherElements = viewModel.weatherSelector.getSelectedEnums()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
