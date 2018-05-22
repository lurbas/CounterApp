package com.lucasurbas.counter.ui.explore.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UiHeaderItem implements UiListItem {

    private static final int ITEM_ID = 11223344;

    @Override
    public int getId() {
        return ITEM_ID;
    }

    public static UiHeaderItem newInstance() {
        return new AutoValue_UiHeaderItem();
    }
}
