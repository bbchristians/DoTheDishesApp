package com.bchristians.bchristians.dothedishes.room.assignment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatSpinner
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bchristians.bchristians.dothedishes.DishesApplication
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.injection.responses.Room
import com.bchristians.bchristians.dothedishes.room.RoomViewModel
import com.bchristians.bchristians.dothedishes.user.UserInfo
import javax.inject.Inject

class CreateAssignmentFragment: Fragment(), Observer<Room> {

    private var rootView: View? = null
    private var userInfo: UserInfo? = null

    @Inject
    lateinit var roomViewModel: RoomViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.rootView = inflater.inflate(R.layout.fragment_create_assignment, container, false)

        this.userInfo = this.arguments?.getParcelable(this.context?.getString(R.string.user_info_id_key))

        DishesApplication.getApplication().injectorComponent.inject(this)

        this.userInfo?.roomId?.let {
            roomViewModel.getRoomLiveData(it).observe(this, this)
            roomViewModel.makeRoomLiveDataRequest(it)
        }

        return this.rootView
    }

    private fun setAssigneeAutofill(assignees: List<String>) {
        this.context?.let { context ->
            this.rootView?.findViewById<AppCompatSpinner>(R.id.field_assignment_assignee)?.let { actv ->
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, assignees)
                actv.adapter = adapter
            }
        }
    }

    override fun onChanged(t: Room?) {
        t ?: return
        setAssigneeAutofill(t.registeredUsers?.asSequence()?.map { it.userId }?.sorted()?.toList() ?: listOf())
    }

}
