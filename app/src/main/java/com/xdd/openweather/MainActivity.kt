package com.xdd.openweather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
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
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)!!
        viewDataBinding.lifecycleOwner = this
        setSupportActionBar(toolbar)

        val viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        viewDataBinding.contentMain.weatherViewModel = viewModel

        // set up RecyclerView and Adapter
        val locWeatherAdapter = LocWeatherRecyclerAdapter(this)
        forecastRecycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            adapter = locWeatherAdapter
        }

        viewModel.liveForecast.observe(this, Observer {
            if (it == null) {
                viewModel.openErrorDialog(this.supportFragmentManager, RuntimeException("Null Forecast data"))
            } else {
                viewDataBinding.contentMain.forecast = it
                locWeatherAdapter.postData(it.records.locationWeatherInfoList)
            }
        })

        fab.setOnClickListener {
            viewModel.updateForecasts(
                locations = viewModel.locationSelector.getActualSelected(this),
                weatherElements = viewModel.weatherSelector.getActualSelected(this),
                failureHandler = {_, t ->
                    viewModel.openErrorDialog(this.supportFragmentManager, t)
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
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
