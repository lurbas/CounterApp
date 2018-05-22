package com.lucasurbas.counter.remote.mapper;

import javax.inject.Inject;

import com.lucasurbas.counter.data.model.Post;
import com.lucasurbas.counter.remote.model.PostJson;

public class ApiPostMapper {

    @Inject
    public ApiPostMapper(){
    }

    public Post toPost(PostJson postJson){
        return Post.builder()
                .id(postJson.getId())
                .imageUrl(postJson.getThumbnailUrl())
                .linkUrl(postJson.getLinkUrl())
                .build();
    }
}
