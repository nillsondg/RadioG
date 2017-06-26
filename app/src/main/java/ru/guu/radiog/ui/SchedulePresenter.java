package ru.guu.radiog.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.guu.radiog.R;
import ru.guu.radiog.network.ApiService;
import ru.guu.radiog.network.model.Event;
import ru.guu.radiog.utils.ServiceUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by dmitry on 26.06.17.
 */

public class SchedulePresenter implements RadioContract.SchedulePresenter {
    private static final int LENGTH = 10;
    private final static int ORGANIZATION_ID = 189;
    private static final String LOG_TAG = SchedulePresenter.class.getSimpleName();
    private final ApiService mApiService;
    private Disposable mDisposable;
    private final RadioContract.ScheduleView mView;
    private Date mDate = Calendar.getInstance().getTime();
    private Context mContext;


    SchedulePresenter(Context context, @NonNull ApiService apiService,
                      @NonNull RadioContract.ScheduleView view) {
        mApiService = checkNotNull(apiService);
        mView = checkNotNull(view);
        mView.setPresenter(this);
        mContext = context;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Override
    public void start() {
        loadEvents(true, 0);
    }

    @Override
    public void stop() {
        if (mDisposable != null)
            mDisposable.dispose();
    }

    @Override
    public void loadEvents(boolean forceLoad, int page) {
        mView.setLoadingIndicator(forceLoad);
        mDisposable = mApiService.getEvents(mContext.getString(R.string.evendate_token), ORGANIZATION_ID,
                ServiceUtils.formatDateRequestNotUtc(mDate), Event.FIELDS_LIST, Event.ORDER_BY, page, LENGTH)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            boolean isLast = result.getData().size() < LENGTH;
                            boolean isEmpty = result.getData().size() == 1;
                            if (result.isOk()) {
                                if (isEmpty && mView.isEmpty()) {
                                    mView.showEmptyState();
                                } else if (forceLoad) {
                                    mView.reshowEvents(result.getData(), isLast);
                                } else {
                                    mView.showEvents(result.getData(), isLast);
                                }
                            } else {
                                mView.showError();
                            }
                        },
                        this::onError,
                        () -> mView.setLoadingIndicator(false)
                );
    }

    private void onError(Throwable error) {
        Log.e(LOG_TAG, "" + error.getMessage());
        mView.showError();
    }
}
