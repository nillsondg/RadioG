package ru.guu.radiog.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import co.mobiwise.library.radio.RadioListener;
import co.mobiwise.library.radio.RadioManager;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import ru.guu.radiog.R;
import ru.guu.radiog.network.ApiFactory;


public class MainActivity extends AppCompatActivity implements RadioListener, RadioFragment.RadioController {
    private final String RADIO_URL = "http://listen.shoutcast.com/radio-g";
    private final String KEY_SELECTED_DATE = "key_selected_date";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private Date mSelectedDate;
    private RadioManager mRadioManager;
    private RadioFragment radioFragment;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private HorizontalCalendar mHorizontalCalendar;
    private View mCalendarView;
    private AppBarLayout mAppBarLayout;
    private AppBarLayout mCalendarFrame;
    private ScheduleFragment mScheduleFragment;

    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSelectedDate = new Date(savedInstanceState.getLong(KEY_SELECTED_DATE));
            mScheduleFragment.setPresenter(new SchedulePresenter(this, ApiFactory.getEvendateService(), mScheduleFragment));
            mScheduleFragment.mPresenter.setDate(mSelectedDate);
            mScheduleFragment.mAdapter.setDate(mSelectedDate);
        }
        setContentView(R.layout.activity_radio);

        mRadioManager = RadioManager.with(this);
        mRadioManager.registerListener(this);
        mRadioManager.setLogging(true);
        viewPager = (ViewPager)findViewById(R.id.pager);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener((@NonNull MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.action_now:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.action_schedule:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.action_about:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.action_profile:
                    viewPager.setCurrentItem(3);
                    break;
            }
            return true;
        });
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return radioFragment;
                    case 1:
                        mScheduleFragment = ScheduleFragment.newInstance();
                        mScheduleFragment.setPresenter(new SchedulePresenter(getApplicationContext(), ApiFactory.getEvendateService(), mScheduleFragment));
                        return mScheduleFragment;
                    case 2:
                        return new AboutFragment();
                    case 3:
                        return new ProfileFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        currentItem = 0;
                        bottomNavigationView.setSelectedItemId(R.id.action_now);
                        setCalendarInvisible();
                        break;
                    case 1:
                        currentItem = 1;
                        bottomNavigationView.setSelectedItemId(R.id.action_schedule);
                        setCalendarVisible();
                        break;
                    case 2:
                        currentItem = 2;
                        bottomNavigationView.setSelectedItemId(R.id.action_about);
                        setCalendarInvisible();
                        break;
                    case 3:
                        currentItem = 3;
                        bottomNavigationView.setSelectedItemId(R.id.action_profile);
                        setCalendarInvisible();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioFragment = RadioFragment.newInstance();

        mCalendarFrame = (AppBarLayout)findViewById(R.id.frame_layout);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.app_bar);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.WEEK_OF_MONTH, 2);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);
        mCalendarView = findViewById(R.id.calendarView);
        mHorizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();
        mHorizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                mSelectedDate = date;
                mScheduleFragment.mPresenter.setDate(mSelectedDate);
                mScheduleFragment.mAdapter.setDate(mSelectedDate);
                mScheduleFragment.mPresenter.loadEvents(true, 0);
            }
        });
        mCalendarFrame.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (currentItem == 1) {
                mCalendarView.setVisibility(View.VISIBLE);
            } else {
                mCalendarView.setVisibility(View.GONE);
            }
        });
    }

    private void setCalendarInvisible() {
        TransitionManager.beginDelayedTransition(mCalendarFrame);
        mCalendarView.setVisibility(View.GONE);
    }

    private void setCalendarVisible() {
        TransitionManager.beginDelayedTransition(mCalendarFrame);
        mCalendarView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRadioManager.connect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putLong(KEY_SELECTED_DATE, mSelectedDate.getTime());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRadioManager.stopRadio();
        mRadioManager.disconnect();
        mRadioManager.unregisterListener(this);
    }

    @Override
    public void onRadioConnected() {
        mRadioManager.startRadio(RADIO_URL);
    }

    @Override
    public void onRadioLoading() {
    }

    @Override
    public void onRadioStarted() {
        runOnUiThread(() -> radioFragment.onRadioStarted());
        mRadioManager.updateNotification(getString(R.string.app_name), "", R.drawable.radiog_logo, R.drawable.radiog_logo);
    }

    @Override
    public void onRadioStopped() {
        runOnUiThread(() -> radioFragment.onRadioStopped());
    }

    @Override
    public void onMetaDataReceived(String s, String s1) {
        Log.i(LOG_TAG, s + " || " + s1);
        if (s != null && s.equals("StreamTitle")) {
            runOnUiThread(() -> radioFragment.setStreamTitle(s1));
        }
    }

    @Override
    public void onError() {
        runOnUiThread(() -> radioFragment.onError());
    }

    @Override
    public void startRadio() {
        mRadioManager.startRadio(RADIO_URL);
    }

    @Override
    public void stopRadio() {
        mRadioManager.stopRadio();
    }

    @Override
    public boolean isPlaying() {
        return mRadioManager.isPlaying();
    }
}
