package com.hammerandtongues.online.hntauction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import com.applandeo.materialcalendarview.CalendarView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.List;


public class Calender extends Activity{
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
        public static final String RESULT = "result";
        public static final String EVENT = "event";
        private static final int ADD_NOTE = 44;
        private CalendarView mCalendarView;
        private List<EventDay> mEventDays = new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calender);
            mCalendarView = (CalendarView) findViewById(R.id.calendarView);
            FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNote();
                }
            });
                    mCalendarView.setOnDayClickListener(new OnDayClickListener() {
                @Override
                public void onDayClick(EventDay eventDay) {
                    previewNote(eventDay);
                }
            });
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
                MyEventDay myEventDay = data.getParcelableExtra(RESULT);
                mCalendarView.setDate(myEventDay.getCalendar());
                mEventDays.add(myEventDay);
                mCalendarView.setEvents(mEventDays);
            }
        }
        private void addNote() {
            Intent intent = new Intent(this, Add_Note.class);
            startActivityForResult(intent, ADD_NOTE);
        }
        private void previewNote(EventDay eventDay) {
            Intent intent = new Intent(this, Note_Preview.class);
            if(eventDay instanceof MyEventDay){
                intent.putExtra(EVENT, (MyEventDay) eventDay);
            }
            startActivity(intent);
        }


}

