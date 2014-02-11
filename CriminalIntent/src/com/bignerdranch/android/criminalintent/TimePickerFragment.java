package com.bignerdranch.android.criminalintent;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment {
	public static final String EXTRA_TIME =
			"com.bignerdranch.android.criminalintent.time";
	
	private Date mTime;
	
	public static TimePickerFragment newInstance(Date time) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TIME, time);
		
		TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	mTime = (Date)getArguments().getSerializable(EXTRA_TIME);
    	
    	// Create a Calendar to get the hour, minute and second
        // Use the current time as the default values for the picker
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(mTime);
    	int hour = calendar.get(Calendar.HOUR_OF_DAY);
    	int minute = calendar.get(Calendar.MINUTE);
    	
    	View v = getActivity().getLayoutInflater()
    			.inflate(R.layout.dialog_time, null);
    	
    	TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
    	timePicker.setCurrentHour(hour);
    	timePicker.setCurrentMinute(minute);
    	timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
             @Override
             public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                 Calendar cal = Calendar.getInstance();
                 cal.setTime(mTime);
                 cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                 cal.set(Calendar.MINUTE, minute);

                 mTime = cal.getTime();
                 getArguments().putSerializable(EXTRA_TIME, mTime);
             }
         });

        // Create a new instance of TimePickerDialog and return it
        return new AlertDialog.Builder(getActivity())
        	.setView(v)
        	.setTitle(R.string.time_picker_title)
        	.setPositiveButton(
        			android.R.string.ok, 
        			new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							sendResult(Activity.RESULT_OK);
						}
					})
        	.create();
        		
        		// new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_TIME, mTime);
		
		getTargetFragment()
			.onActivityResult(getTargetRequestCode(), resultCode, i);
	}
}

