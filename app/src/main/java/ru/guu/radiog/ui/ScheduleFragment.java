package ru.guu.radiog.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ybq.endless.Endless;

import java.util.List;

import ru.guu.radiog.R;
import ru.guu.radiog.network.model.Event;

public class ScheduleFragment extends Fragment implements EventsAdapter.OnEventInteractionListener,
        RadioContract.ScheduleView {

    private EventsAdapter.OnEventInteractionListener mListener;
    private LoadStateView mLoadStateView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public EventsAdapter mAdapter;
    private Endless mEndless;
    public RadioContract.SchedulePresenter mPresenter;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    public void setPresenter(RadioContract.SchedulePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mLoadStateView = (LoadStateView)view.findViewById(R.id.load_state);
        mAdapter = new EventsAdapter(getContext(), mListener);
        mRecyclerView.setAdapter(mAdapter);


        View loadingView = inflater.inflate(R.layout.item_progress, container, false);
        mEndless = Endless.applyTo(mRecyclerView, loadingView);
        mEndless.setLoadMoreListener((int page) -> mPresenter.loadEvents(false, page));

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mEndless.setLoadMoreAvailable(false);
            mEndless.setCurrentPage(0);
            mPresenter.loadEvents(true, 0);
        });
        mLoadStateView.setOnReloadListener(() -> mPresenter.loadEvents(true, 0));

        setEmptyCap();

        return view;
    }

    private void setEmptyCap() {
        mLoadStateView.setEmptyHeader(getString(R.string.schedule_empty_cap));
        mLoadStateView.setEmptyDescription(getString(R.string.schedule_empty_cap_description));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.stop();
    }


    @Override
    public void showEvents(List<Event> list, boolean isLast) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEndless.loadMoreComplete();
        mEndless.setLoadMoreAvailable(!isLast);
        mAdapter.add(list);
    }

    @Override
    public void reshowEvents(List<Event> list, boolean isLast) {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEndless.loadMoreComplete();
        mEndless.setLoadMoreAvailable(!isLast);
        mAdapter.set(list);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mLoadStateView.showProgress();
        } else {
            mLoadStateView.hideProgress();
        }
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void showEmptyState() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadStateView.showEmptyHint();
        mEndless.loadMoreComplete();
        mEndless.setLoadMoreAvailable(false);
    }

    @Override
    public void showError() {
        mEndless.loadMoreComplete();
        mEndless.setLoadMoreAvailable(false);
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadStateView.showErrorHint();
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void OnNotificationClicked(Event event) {

    }

}
