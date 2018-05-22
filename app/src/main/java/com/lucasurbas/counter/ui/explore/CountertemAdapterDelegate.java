package com.lucasurbas.counter.ui.explore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.lucasurbas.counter.R;
import com.lucasurbas.counter.ui.explore.model.UiCounterItem;
import com.lucasurbas.counter.ui.explore.model.UiListItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountertemAdapterDelegate extends AdapterDelegate<List<UiListItem>> {

    private final OnCounterItemClickListener counterItemClickListener;

    public CountertemAdapterDelegate(OnCounterItemClickListener counterItemClickListener) {
        this.counterItemClickListener = counterItemClickListener;
    }

    @Override
    public boolean isForViewType(@NonNull List<UiListItem> items, int position) {
        return items.get(position) instanceof UiCounterItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new CounterItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull List<UiListItem> items, int position,
            @NonNull RecyclerView.ViewHolder holder,
            @Nullable List<Object> payloads) {
        UiCounterItem counterItem = (UiCounterItem) items.get(position);
        ((CounterItemViewHolder) holder).bindUiModel(counterItem, counterItemClickListener);
    }

    static class CounterItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.value_view)
        TextView valueView;

        public CounterItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_counter, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void bindUiModel(UiCounterItem uiCounterItem, OnCounterItemClickListener listener) {
            valueView.setText(uiCounterItem.getStringValue());

            itemView.setOnClickListener(view -> listener.onCounterClick(uiCounterItem.getId()));
        }
    }

    public interface OnCounterItemClickListener {

        void onCounterClick(int counterId);
    }
}
