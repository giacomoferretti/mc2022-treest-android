package com.giacomoferretti.mobilecomputing2022.treest.android.ui.boardselection;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.EntryBoardRowDoubleBinding;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    @Nullable
    private final DirectionClickCallback mDirectionClickCallback;

    List<? extends Line> mLinesList;

    public BoardAdapter(@Nullable DirectionClickCallback directionClickCallback) {
        this.mDirectionClickCallback = directionClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EntryBoardRowDoubleBinding binding =
                EntryBoardRowDoubleBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.firstItem.textViewLineName.setText(mLinesList.get(position).getLineName());
        holder.binding.secondItem.textViewLineName.setText(mLinesList.get(position).getInversedLineName());

        if (mDirectionClickCallback != null) {
            holder.binding.firstItem.getRoot().setOnClickListener(view -> {
                mDirectionClickCallback.onDirectionSelected(mLinesList.get(position),
                        mLinesList.get(position).getTerminus1().getDirectionId());
            });
            holder.binding.secondItem.getRoot().setOnClickListener(view -> {
                mDirectionClickCallback.onDirectionSelected(mLinesList.get(position),
                        mLinesList.get(position).getTerminus2().getDirectionId());
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLinesList == null ? 0 : mLinesList.size();
    }

    public void setLinesList(final List<? extends Line> linesList) {
        if (mLinesList == null) {
            mLinesList = linesList;
            notifyItemRangeInserted(0, linesList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mLinesList.size();
                }

                @Override
                public int getNewListSize() {
                    return linesList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mLinesList.get(oldItemPosition).getLineName().equals(linesList.get(newItemPosition).getLineName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Line newLine = linesList.get(newItemPosition);
                    Line oldLine = mLinesList.get(oldItemPosition);
                    return TextUtils.equals(newLine.getLineName(), oldLine.getLineName());
                }
            });
            mLinesList = linesList;
            result.dispatchUpdatesTo(this);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final EntryBoardRowDoubleBinding binding;

        public ViewHolder(EntryBoardRowDoubleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
