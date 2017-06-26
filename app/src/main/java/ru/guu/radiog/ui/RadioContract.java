package ru.guu.radiog.ui;

import java.util.Date;
import java.util.List;

import ru.guu.radiog.network.model.Event;

/**
 * Created by dmitry on 26.06.17.
 */

public class RadioContract {

    interface ScheduleView {
        void setPresenter(SchedulePresenter presenter);

        void setLoadingIndicator(boolean active);

        void showEvents(List<Event> list, boolean isLast);

        void reshowEvents(List<Event> list, boolean isLast);

        void showEmptyState();

        void showError();

        boolean isEmpty();
    }

    interface SchedulePresenter {
        void start();

        void stop();

        void loadEvents(boolean forceLoad, int page);

        void setDate(Date date);

    }
}
