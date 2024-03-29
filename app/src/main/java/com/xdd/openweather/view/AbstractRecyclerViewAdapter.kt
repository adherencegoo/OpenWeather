package com.xdd.openweather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

abstract class AbstractRecyclerViewAdapter<BD : ViewDataBinding, T : Any>(
    lifecycleOwner: LifecycleOwner,
    private val dataList: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<AbstractRecyclerViewAdapter.ViewHolder<BD>>() {

    class ViewHolder<BD : ViewDataBinding>(internal val binding: BD) :
        RecyclerView.ViewHolder(binding.root)

    private val refLifecycleOwner = WeakReference(lifecycleOwner)

    protected val mLifecycleOwner: LifecycleOwner?
        get() = refLifecycleOwner.get()

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<BD> =
        ViewHolder(onCreateBinding(LayoutInflater.from(parent.context), parent).apply {
            lifecycleOwner = mLifecycleOwner
        })

    final override fun getItemCount(): Int = dataList.size

    final override fun onBindViewHolder(holder: ViewHolder<BD>, position: Int) =
        onBindData(holder.binding, dataList[position])

    abstract fun onBindData(binding: BD, data: T)

    abstract fun onCreateBinding(inflater: LayoutInflater, parent: ViewGroup): BD

    fun postData(list: List<T>) {
        //xdd use DiffUtils
        dataList.clear()
        dataList += list
        notifyDataSetChanged()
    }
}