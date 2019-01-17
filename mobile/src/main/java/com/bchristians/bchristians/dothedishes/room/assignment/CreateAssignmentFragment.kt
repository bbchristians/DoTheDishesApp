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
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class CreateAssignmentFragment: Fragment(), Observer<Room> {

    private val DATE_FORMAT = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    private var rootView: View? = null
    private var inflater: LayoutInflater? = null
    private var userInfo: UserInfo? = null

    private var users: List<String> = listOf()

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
        setupButtons()

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

    private fun setupButtons() {
        // Confirm Button
        rootView?.findViewById<Button>(R.id.confirm_button)?.setOnClickListener {
            this.getUsersAvailability()
            this.getFrequency()
        }

        // Frequency Single Select
        val singleSelectOptions = listOf(R.id.repeat_custom, R.id.repeat_single)
        singleSelectOptions.forEach { singleSelectId ->
            rootView?.findViewById<CheckBox>(singleSelectId)?.setOnClickListener { checkBox ->
                if( (checkBox as? CheckBox)?.isChecked == true ) {
                    singleSelectOptions.filter { it != singleSelectId }.forEach {
                        rootView?.findViewById<CheckBox>(it)?.isChecked = false
                    }
                }
            }
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

    fun getUsersAvailability(): Map<String, List<ScheduleAvailabilityView.DayOfTheWeek>> {
        val availabilityMap = HashMap<String, List<ScheduleAvailabilityView.DayOfTheWeek>>()
        val selectedUser = this.rootView?.findViewById<AppCompatSpinner>(R.id.field_assignment_assignee)?.selectedItem
        if( selectedUser == ASSIGNMENT_EVERYONE ) { this.users } else { listOf(selectedUser.toString()) }.forEach { userId ->
            rootView?.findViewWithTag<ScheduleAvailabilityView>(getExceptionUserTag(userId))?.let { userAvailabilityView ->
                availabilityMap.put(userId, ScheduleAvailabilityView.DayOfTheWeek.values().filter{ dotw ->
                    userAvailabilityView.userIsAvailable(dotw)
                })
            }
        }
        return availabilityMap
    }

    fun getFrequency(): AssignmentRepetition {
        var startDate: String = ""
        var endDate: String = ""
        var repeatFrequency: Int = 1
        // Case: Single Assignment
        if( rootView?.findViewById<CheckBox>(R.id.repeat_single)?.isChecked == true ) {
            startDate = rootView?.findViewById<EditText>(R.id.no_repeat_date)?.text?.toString() ?: ""
            endDate = startDate
        } else if( rootView?.findViewById<CheckBox>(R.id.repeat_custom)?.isChecked == true ) {
            startDate = "TODO"// Today
            endDate = rootView?.findViewById<EditText>(R.id.repeat_custom_end_date)?.text?.toString() ?: ""
            repeatFrequency = rootView?.findViewById<EditText>(R.id.repeat_custom_days)?.text?.toString()?.toInt() ?: 1
        }
        // Determine the year so the user doesn't need to enter it
        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
        var parsedStartDate: Date? = null
        var parsedEndDate: Date? = null
            parsedStartDate = DATE_FORMAT.parse("%s/${thisYear}".format(startDate))
            if( parsedStartDate.before(Calendar.getInstance().time) ) {
                parsedStartDate = DATE_FORMAT.parse("%s/${thisYear+1}".format(startDate))
            }
            parsedEndDate = DATE_FORMAT.parse("%s/${thisYear}".format(endDate))
            if( parsedEndDate.before(Calendar.getInstance().time) ) {
                parsedEndDate = DATE_FORMAT.parse("%s/${thisYear+1}".format(endDate))
            }
        val a = AssignmentRepetition(parsedStartDate, parsedEndDate, repeatFrequency)
        return a
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
        this.users = allUsersList
        setAssigneeAutofill(allUsersList)
        setUpUserAvailability(allUsersList)
    }

    companion object {

        private const val ASSIGNMENT_EVERYONE = "Everyone"
        private const val EXCEPTION_USER_TAG = "exception_tag_%s"

        fun getExceptionUserTag(userId: String) = EXCEPTION_USER_TAG.format(userId)
    }

}
