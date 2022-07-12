package com.giacomoferretti.mobilecomputing2022.treest.android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.FragmentBoardFeedBinding;
import com.giacomoferretti.mobilecomputing2022.treest.android.ui.boardselection.BoardSelectionActivity;
import com.giacomoferretti.mobilecomputing2022.treest.android.ui.mapdetails.MapDetailsActivity;

public class BoardFeedFragment extends Fragment {
    private static final String TAG = BoardFeedFragment.class.getSimpleName();

    public static final String ARGUMENT_LINE = "LINE";
    public static final String ARGUMENT_DIRECTION_ID = "DIRECTION_ID";

    private Line mLine;
    private String mDirectionId;

    private FragmentBoardFeedBinding mBinding;
    private MainViewModel mViewModel;

    public static BoardFeedFragment newInstance(Line line, String directionId) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_LINE, line);
        arguments.putString(ARGUMENT_DIRECTION_ID, directionId);
        BoardFeedFragment fragment = new BoardFeedFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLine = getArguments().getParcelable(ARGUMENT_LINE);
            mDirectionId = getArguments().getString(ARGUMENT_DIRECTION_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentBoardFeedBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated");

        PostAdapter mPostAdapter = new PostAdapter((authorId, shouldFollow) -> {
            mViewModel.followUser(authorId, shouldFollow);

        });
        mBinding.recyclerView.setAdapter(mPostAdapter);

        mBinding.firstItem.setText(mLine.getTerminus1().getStationName());
        mBinding.secondItem.setText(mLine.getTerminus2().getStationName());

        if (Integer.parseInt(mViewModel.getDirectionId().getValue()) == mLine.getTerminus1().getDirectionId()) {
            mBinding.toggleButton.check(mBinding.firstItem.getId());
            mViewModel.setTerminusName(mLine.getTerminus1().getStationName());
        } else if (Integer.parseInt(mViewModel.getDirectionId().getValue()) == mLine.getTerminus2().getDirectionId()) {
            mBinding.toggleButton.check(mBinding.secondItem.getId());
            mViewModel.setTerminusName(mLine.getTerminus2().getStationName());
        }

        mBinding.firstItem.setOnClickListener(button -> {
            Log.d(TAG, "onClick: " + mBinding.toggleButton.getCheckedButtonId() + " - " + button.getId());
            mViewModel.setDirectionId(String.valueOf(mLine.getTerminus1().getDirectionId()));
            mViewModel.setTerminusName(mLine.getTerminus1().getStationName());
        });

        mBinding.secondItem.setOnClickListener(button -> {
            Log.d(TAG, "onClick: " + mBinding.toggleButton.getCheckedButtonId() + " - " + button.getId());
            mViewModel.setDirectionId(String.valueOf(mLine.getTerminus2().getDirectionId()));
            mViewModel.setTerminusName(mLine.getTerminus2().getStationName());
        });

        mViewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            mBinding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            // mBinding.recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        mBinding.mapButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireActivity(), MapDetailsActivity.class);
            intent.putExtra(MapDetailsActivity.EXTRA_DIRECTION_ID, mViewModel.getDirectionId().getValue());
            startActivity(intent);
        });

        mBinding.changeLineButton.setOnClickListener(view1 -> {
            AppDataRepository appDataRepository = AppDataRepository.getInstance(requireContext());
            appDataRepository.setCurrentDirectionId(null);
            appDataRepository.setCurrentLine(null);
            Intent intent = new Intent(requireActivity(), BoardSelectionActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        mViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            Log.d(TAG, "posts");
            mPostAdapter.setPostsList(posts);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
