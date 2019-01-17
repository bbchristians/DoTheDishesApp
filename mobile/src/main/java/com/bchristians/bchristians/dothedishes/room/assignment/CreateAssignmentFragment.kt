package com.bchristians.bchristians.dothedishes.room.assignment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatSpinner
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bchristians.bchristians.dothedishes.DishesApplication
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.injection.responses.Room
import com.bchristians.bchristians.dothedishes.room.RoomViewModel
import com.bchristians.bchristians.dothedishes.user.UserInfo
import javax.inject.Inject
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.*


class CreateAssignmentFragment: Fragment(), Observer<Room> {

    private var rootView: View? = null
    private var inflater: LayoutInflater? = null
    private var userInfo: UserInfo? = null

    @Inject
    lateinit var roomViewModel: RoomViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.rootView = inflater.inflate(R.layout.fragment_create_assignment, container, false)
        this.inflater = inflater

        this.userInfo = this.arguments?.getParcelable(this.context?.getString(R.string.user_info_id_key))

        DishesApplication.getApplication().injectorComponent.inject(this)

        this.userInfo?.roomId?.let {
            roomViewModel.getRoomLiveData(it).observe(this, this)
            roomViewModel.makeRoomLiveDataRequest(it)
        }

        setupFields()

        return this.rootView
    }

    private fun setupFields() {
        // Edit Text for assignment task name
        val nameEditText = rootView?.findViewById<EditText>(R.id.field_assignment_name)
        nameEditText?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }

        // Spinner for assignee
        rootView?.findViewById<AppCompatSpinner>(R.id.field_assignment_assignee)?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position) ?: return
                (0 until parent.count).map { index ->
                    parent.getItemAtPosition(index).toString()
                }.forEach { userId ->
                    rootView?.findViewWithTag<View>(getExceptionUserTag(userId))?.visibility =
                            if( userId == selectedItem || selectedItem == ASSIGNMENT_EVERYONE) {
                                View.VISIBLE
                            } else {
                                View.GONE
                            }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { /* Intentionally Blank */ }
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = this.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setAssigneeAutofill(assignees: List<String>) {
        this.context?.let { context ->
            this.rootView?.findViewById<AppCompatSpinner>(R.id.field_assignment_assignee)?.let { spinner ->
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, assignees)
                spinner.adapter = adapter
            }
        }
    }

    private fun setUpUserAvailability(users: List<String>) {
        val exceptionHolder = rootView?.findViewById<LinearLayout>(R.id.availability_list) ?: return
        exceptionHolder.removeAllViews()
        users.forEach { userId ->
            val newView = inflater?.inflate(R.layout.view_availability, exceptionHolder, false) as? ScheduleAvailabilityView
            newView?.assignUser(userId)
            if( userId == ASSIGNMENT_EVERYONE ) {
                newView?.onButtonToggleListener = object: OnButtonToggleListener() {
                    override fun onToggle(
                        dotw: ScheduleAvailabilityView.DayOfTheWeek,
                        toggleValue: Boolean,
                        userId: String
                    ) {
                        updateAllUserAvailability(users, dotw, toggleValue)
                    }
                }
            }
            newView?.tag = getExceptionUserTag(userId)
            exceptionHolder.addView(newView)
        }
    }

    fun updateAllUserAvailability(users: List<String>, dotw: ScheduleAvailabilityView.DayOfTheWeek, toggleValue: Boolean) {
        users.forEach { userId ->
            rootView?.findViewWithTag<ViewGroup>(getExceptionUserTag(userId))
                ?.findViewById<ToggleButton>(dotw.buttonId)
                ?.isChecked = toggleValue
        }
    }

    override fun onChanged(t: Room?) {
        t ?: return
        val allUsersList = listOf(ASSIGNMENT_EVERYONE)
            .plus(
                t.registeredUsers
                    ?.asSequence()
                    ?.map { it.userId }
                    ?.sorted()
                    ?.asIterable() ?: listOf()
            )
        setAssigneeAutofill(allUsersList)
        setUpUserAvailability(allUsersList)
    }

    companion object {

        private const val ASSIGNMENT_EVERYONE = "Everyone"
        private const val EXCEPTION_USER_TAG = "exception_tag_%s"

        fun getExceptionUserTag(userId: String) = EXCEPTION_USER_TAG.format(userId)
    }

}
