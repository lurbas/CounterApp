package com.lucasurbas.counter.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

import com.lucasurbas.counter.ui.explore.model.UiListItem;

public class ListItemDiffCallback extends DiffUtil.Callback {
    private List<UiListItem> current;
    private List<UiListItem> next;

    public ListItemDiffCallback(List<UiListItem> current, List<UiListItem> next) {
        this.current = current;
        this.next = next;
    }

    @Override
    public int getOldListSize() {
        return current != null ? current.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return next != null ? next.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        UiListItem currentItem = current.get(oldItemPosition);
        UiListItem nextItem = next.get(newItemPosition);
        return currentItem.getId() == nextItem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        UiListItem currentItem = current.get(oldItemPosition);
        UiListItem nextItem = next.get(newItemPosition);
        return currentItem.equals(nextItem);
    }
}
