package com.lucasurbas.counter.ui.explore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.lucasurbas.counter.R;
import com.lucasurbas.counter.ui.explore.model.UiHeaderItem;
import com.lucasurbas.counter.ui.explore.model.UiListItem;

import java.util.List;

public class HeaderItemAdapterDelegate extends AdapterDelegate<List<UiListItem>> {

    @Override
    public boolean isForViewType(@NonNull List<UiListItem> items, int position) {
        return items.get(position) instanceof UiHeaderItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new HeaderItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull List<UiListItem> items, int position,
            @NonNull RecyclerView.ViewHolder holder,
            @Nullable List<Object> payloads) {
        UiHeaderItem header = (UiHeaderItem) items.get(position);
        ((HeaderItemViewHolder) holder).bindUiModel(header);
    }

    static class HeaderItemViewHolder extends RecyclerView.ViewHolder {

        public HeaderItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_header, parent, false));
        }

        public void bindUiModel(UiHeaderItem uiHeaderItem) {
        }
    }
}
