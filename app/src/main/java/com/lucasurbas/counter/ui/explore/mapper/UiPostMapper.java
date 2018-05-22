package com.lucasurbas.counter.ui.explore.mapper;

import android.net.Uri;

import com.annimon.stream.Optional;

import javax.inject.Inject;

import com.lucasurbas.counter.data.model.Post;
import com.lucasurbas.counter.ui.explore.model.UiPostItem;

public class UiPostMapper {

    @Inject
    public UiPostMapper() {
    }

    public Optional<UiPostItem> toUiPostItem(Post post) {
        String title = getTitle(post.getLinkUrl());
        if(title == null){
            return Optional.empty();
        }
        return Optional.of(UiPostItem.builder()
                .id(post.getId())
                .imageUrl(post.getImageUrl())
                .title(title)
                .isRead(post.getIsRead())
                .build());
    }

    private String getTitle(String linkUrl) {
        return Uri.parse(linkUrl).getHost();
    }
}
