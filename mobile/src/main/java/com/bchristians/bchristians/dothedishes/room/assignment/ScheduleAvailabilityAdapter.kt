package com.bchristians.bchristians.dothedishes.room.assignment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bchristians.bchristians.dothedishes.R

class ScheduleAvailabilityAdapter(private val context: Context?): BaseAdapter() {

    var userList: List<String>? = null

    override fun getCount() = userList?.size ?: 0

    override fun getItem(position: Int) = userList?.getOrNull(position)

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val listItemView: ScheduleAvailabilityView = convertView as? ScheduleAvailabilityView ?:
            LayoutInflater.from(context).inflate(R.layout.view_availability, parent, false) as ScheduleAvailabilityView

        listItemView.assignUser(getItem(position) ?: "")
        return listItemView
    }

}