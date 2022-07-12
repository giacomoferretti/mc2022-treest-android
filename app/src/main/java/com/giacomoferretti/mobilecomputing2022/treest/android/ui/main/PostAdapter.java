package com.giacomoferretti.mobilecomputing2022.treest.android.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.AppExecutors;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Delay;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Post;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Status;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.EntryPostBinding;
import com.giacomoferretti.mobilecomputing2022.treest.android.di.AppContainer;
import com.giacomoferretti.mobilecomputing2022.treest.android.utils.AvatarGenerator;

import java.util.List;
import java.util.Objects;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    @Nullable
    private final PostFollowCallback mPostFollowCallback;

    List<? extends Post> mPostsList;
    private EntryPostBinding mBinding;

    public PostAdapter(@Nullable PostFollowCallback postFollowCallback) {
        this.mPostFollowCallback = postFollowCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = EntryPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context viewContext = holder.mBinding.getRoot().getContext();
        AppExecutors appExecutors = AppContainer.getInstance(viewContext).getAppExecutors();
        AppDataRepository appDataRepository = AppDataRepository.getInstance(viewContext);

        String authorName = mPostsList.get(position).getAuthorName();
        /*String publishDate =
                DateUtils.getRelativeTimeSpanString(mPostsList.get(position).getDatetime()
                .getTime(), new Date().getTime(), 0).toString();*/
        String publishDate = DateFormat.format("hh:mm:ss dd/MM/yyyy",
                mPostsList.get(position).getDatetime()).toString();
        boolean isFollowingAuthor = mPostsList.get(position).isFollowingAuthor();
        Integer delay = mPostsList.get(position).getDelay();
        Integer status = mPostsList.get(position).getStatus();
        String comment = mPostsList.get(position).getComment();
        String authorId = mPostsList.get(position).getAuthorId();
        String pictureVersion = mPostsList.get(position).getPictureVersion();

        appExecutors.diskIO().execute(() -> {
            appDataRepository.getUserPicture(authorId, pictureVersion, result -> {
                if (result.getData() != null) {
                    try {
                        byte[] decodedData = Base64.decode(result.getData(), Base64.DEFAULT);

                        appExecutors.mainThread().execute(() -> {
                            holder.mBinding.profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(decodedData, 0, decodedData.length));
                        });
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Bitmap placeholder = new AvatarGenerator(viewContext)
                            .setSize(128)
                            .setTextSize(24)
                            .setLabel(authorName)
                            .build();

                    appExecutors.mainThread().execute(() -> {
                        holder.mBinding.profilePicture.setImageBitmap(placeholder);
                    });
                }
            });
        });

        if (isFollowingAuthor) {
            TypedValue value = new TypedValue();
            viewContext.getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondaryContainer, value, true);
            holder.mBinding.getRoot().setCardBackgroundColor(value.data);
        } else {
            holder.mBinding.getRoot().setCardBackgroundColor(Color.TRANSPARENT);
        }

        holder.mBinding.authorName.setText(authorName);
        holder.mBinding.publishDate.setText(publishDate);
        holder.mBinding.comment.setVisibility(comment == null ? View.GONE : View.VISIBLE);
        if (comment != null) {
            holder.mBinding.comment.setText(mPostsList.get(position).getComment());
        }
        holder.mBinding.delay.setVisibility(delay == null ? View.GONE : View.VISIBLE);
        if (delay != null) {
            Delay delayEnum = Delay.fromInteger(mPostsList.get(position).getDelay());
            holder.mBinding.delay.setText(delayEnum.getLabel(viewContext));
        }
        holder.mBinding.status.setVisibility(status == null ? View.GONE : View.VISIBLE);
        if (status != null) {
            Status statusEnum = Status.fromInteger(mPostsList.get(position).getStatus());
            holder.mBinding.status.setText(statusEnum.getLabel(viewContext));
        }
        holder.mBinding.followButton.setChecked(isFollowingAuthor);

        // Setup callback
        if (mPostFollowCallback != null) {
            holder.mBinding.followButton.setOnClickListener(view -> {
                mPostFollowCallback.onFollow(mPostsList.get(position).getAuthorId(),
                        !mPostsList.get(position).isFollowingAuthor());
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPostsList == null ? 0 : mPostsList.size();
    }

    public void setPostsList(final List<? extends Post> postsList) {
        if (mPostsList == null) {
            mPostsList = postsList;
            notifyItemRangeInserted(0, postsList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mPostsList.size();
                }

                @Override
                public int getNewListSize() {
                    return postsList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mPostsList.get(oldItemPosition).equals(postsList.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Post newPost = postsList.get(newItemPosition);
                    Post oldPost = mPostsList.get(oldItemPosition);
                    return TextUtils.equals(newPost.getComment(), oldPost.getComment()) &&
                            Objects.equals(newPost.getDelay(), oldPost.getDelay()) &&
                            Objects.equals(newPost.getStatus(), oldPost.getStatus());
                }
            });
            mPostsList = postsList;
            result.dispatchUpdatesTo(this);
        }

        // FIXME: Not good for UX
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final EntryPostBinding mBinding;

        public ViewHolder(EntryPostBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }
}