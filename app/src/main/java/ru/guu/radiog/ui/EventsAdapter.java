package ru.guu.radiog.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import ru.guu.radiog.R;
import ru.guu.radiog.network.model.Event;
import ru.guu.radiog.network.model.EventDate;
import ru.guu.radiog.utils.DateFormatter;
import ru.guu.radiog.utils.ServiceUtils;

public class EventsAdapter extends AbstractEndlessAdapter<Event, EventsAdapter.ViewHolder> {

    private final OnEventInteractionListener mListener;
    private final Context mContext;
    private Date mDate = Calendar.getInstance().getTime();

    public EventsAdapter(Context context, OnEventInteractionListener listener) {
        mListener = listener;
        mContext = context;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mEvent = getItem(position);
        String eventBackGroundUrl = ServiceUtils.constructEventBackgroundURL(
                holder.mEvent.getImageHorizontalUrl(),
                (int)mContext.getResources().getDimension(R.dimen.event_background_width));
        Picasso.with(mContext)
                .load(eventBackGroundUrl)
                .error(R.drawable.radiog_logo)
                .into(holder.mImage);
        holder.mTitle.setText(holder.mEvent.getTitle());
        EventDate eventDate = holder.mEvent.getCurrentDate(mDate);
        holder.mTime.setText(DateFormatter.formatEventSingleTime(eventDate.getStartDateTime(),
                eventDate.getEndDateTime()));

        holder.mButton.setOnClickListener((View v) -> {
            if (null != mListener) {
                //todo
            }
        });
    }

    public interface OnEventInteractionListener {
        void OnNotificationClicked(Event event);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImage;
        public final TextView mTitle;
        public final TextView mTime;
        public final ImageButton mButton;
        public Event mEvent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView)view.findViewById(R.id.event_item_title);
            mTime = (TextView)view.findViewById(R.id.event_item_date);
            mButton = (ImageButton)view.findViewById(R.id.notification_button);
            mImage = (ImageView)view.findViewById(R.id.event_item_image);
        }
    }
}
