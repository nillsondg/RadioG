package ru.guu.radiog.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for endless lists
 * Created by dmitry on 26.06.17.
 */

abstract class AbstractEndlessAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final List<T> mList = new ArrayList<>();

    public void add(final @NonNull List<T> list) {
        for (T item : list) {
            append(item);
        }
    }

    void set(final @NonNull List<T> list) {
        int size = mList.size();
        for (int index = 0; index < list.size(); index++) {
            if (index < size) {
                mList.remove(index);
                mList.add(index, list.get(index));
                update(list.get(index));
            } else {
                append(list.get(index));
            }
        }
        if (list.size() < size) {
            List<T> removing = new ArrayList<>(mList.subList(list.size(), size));
            for (T item : removing) {
                remove(item);
            }
        }
    }

    public void reset() {
        int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
    }

    boolean isEmpty() {
        return mList.size() == 0;
    }

    T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void append(T item) {
        mList.add(mList.size(), item);
        notifyItemInserted(getItemCount());
    }

    private void remove(T item) {
        int position = mList.indexOf(item);
        mList.remove(position);
        notifyItemRemoved(position);
    }

    private void update(T item) {
        int position = mList.indexOf(item);
        notifyItemChanged(position);
    }
}

