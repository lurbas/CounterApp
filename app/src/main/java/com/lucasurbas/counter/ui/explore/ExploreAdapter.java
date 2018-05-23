package com.lucasurbas.counter.ui.explore;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;
import com.lucasurbas.counter.ui.explore.model.UiListItem;
import com.lucasurbas.counter.utils.ListItemDiffCallback;

import java.util.ArrayList;
import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_COUNTER = 2;

    private AdapterDelegatesManager<List<UiListItem>> delegatesManager;
    private List<UiListItem> itemList = new ArrayList<>();

    public ExploreAdapter(CountertemAdapterDelegate.OnCounterItemClickListener counterItemClickListener) {
        delegatesManager = new AdapterDelegatesManager<>();
        delegatesManager.addDelegate(TYPE_HEADER, new HeaderItemAdapterDelegate())
                .addDelegate(TYPE_COUNTER, new CountertemAdapterDelegate(counterItemClickListener));
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(itemList, position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(itemList, position, holder);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        delegatesManager.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        delegatesManager.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).getId();
    }

    public int getSpanSize(int position, int spanCount) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                return spanCount;
            default:
                return 1;
        }
    }

    public void updateItemList(@Nullable List<UiListItem> newItemList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ListItemDiffCallback(itemList, newItemList));
        itemList.clear();
        itemList.addAll(newItemList);
        diffResult.dispatchUpdatesTo(this);
    }
}