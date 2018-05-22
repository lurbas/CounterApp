package com.lucasurbas.counter.ui.explore;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.lucasurbas.counter.R;
import com.lucasurbas.counter.app.GlideApp;
import com.lucasurbas.counter.ui.explore.model.UiListItem;
import com.lucasurbas.counter.ui.explore.model.UiPostItem;

public class PostItemAdapterDelegate extends AdapterDelegate<List<UiListItem>> {

    private final OnPostItemClickListener postItemClickListener;
    private ColorMatrixColorFilter colorFilter;

    public PostItemAdapterDelegate(OnPostItemClickListener postItemClickListener) {
        this.postItemClickListener = postItemClickListener;
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        this.colorFilter = new ColorMatrixColorFilter(matrix);
    }

    @Override
    public boolean isForViewType(@NonNull List<UiListItem> items, int position) {
        return items.get(position) instanceof UiPostItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PostItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull List<UiListItem> items, int position,
            @NonNull RecyclerView.ViewHolder holder,
            @Nullable List<Object> payloads) {
        UiPostItem post = (UiPostItem) items.get(position);
        ((PostItemViewHolder) holder).bindUiModel(post, postItemClickListener, colorFilter);
    }

    static class PostItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_view)
        TextView titleView;
        @BindView(R.id.image_view)
        ImageView imageView;

        public PostItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item_post, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void bindUiModel(UiPostItem uiPostItem, OnPostItemClickListener listener, ColorFilter colorFilter) {
            titleView.setText(uiPostItem.getTitle());

            GlideApp.with(itemView.getContext())
                    .load(uiPostItem.getImageUrl())
                    .centerCrop()
                    .transition(withCrossFade())
                    .into(imageView);

            imageView.setAlpha(uiPostItem.getIsRead() ? 0.5f : 1.0f);
            imageView.setColorFilter(uiPostItem.getIsRead() ? colorFilter : null);

            itemView.setOnClickListener(view -> listener.onPostClick(uiPostItem.getId()));
        }
    }

    public interface OnPostItemClickListener {

        void onPostClick(int postId);
    }
}
