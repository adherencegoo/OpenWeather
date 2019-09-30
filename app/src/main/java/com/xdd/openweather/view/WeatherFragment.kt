package com.xdd.openweather.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xdd.openweather.R
import com.xdd.openweather.databinding.WeatherFragmentBinding
import com.xdd.openweather.viewmodel.WeatherViewModel

class WeatherFragment : Fragment() {
    companion object {
        val TAG = WeatherFragment::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentActivity = requireActivity()
        val viewModel = ViewModelProviders.of(fragmentActivity).get(WeatherViewModel::class.java)
        val locWeatherAdapter = LocWeatherRecyclerAdapter(this)

        val binding = WeatherFragmentBinding.inflate(inflater, container, false)!!.apply {
            lifecycleOwner = fragmentActivity
            weatherViewModel = viewModel
            forecastRecycler!!.apply {
                addItemDecoration(DividerItemDecoration(fragmentActivity, DividerItemDecoration.VERTICAL))
                layoutManager = LinearLayoutManager(fragmentActivity)
                adapter = locWeatherAdapter
            }
        }

        viewModel.liveForecast.observe(this, Observer {
            binding.forecast = it
            locWeatherAdapter.postData(it?.records?.locationWeatherInfoList ?: emptyList())
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.menu_main, menu)
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