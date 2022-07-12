package com.giacomoferretti.mobilecomputing2022.treest.android.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.giacomoferretti.mobilecomputing2022.treest.android.R;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Delay;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Status;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.FragmentNewPostBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class NewPostFragment extends Fragment {
    private static final String TAG = NewPostFragment.class.getSimpleName();
    private FragmentNewPostBinding mBinding;
    private MainViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentNewPostBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.boardSelectionSubtitle.setText(getString(R.string.publishing_in, mViewModel.getTerminusName().getValue()));
        mViewModel.getTerminusName().observe(getViewLifecycleOwner(), terminus -> {
            mBinding.boardSelectionSubtitle.setText(getString(R.string.publishing_in, terminus));
        });

        mViewModel.getAddPostEvent().observe(getViewLifecycleOwner(), event -> {
            Boolean success = event.getContentIfNotHandled();
            if (success != null) {
                mBinding.submitButton.setEnabled(true);

                if (success) {
                    Snackbar.make(mBinding.getRoot(), "SUCCESS", Snackbar.LENGTH_SHORT).show();
                    mBinding.commentInput.getEditText().clearFocus();
                    mBinding.delaySelect.getEditText().clearFocus();
                    mBinding.statusSelect.getEditText().clearFocus();
                    mBinding.commentInput.getEditText().getText().clear();
                    mBinding.delaySelect.getEditText().getText().clear();
                    mBinding.statusSelect.getEditText().getText().clear();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mBinding.getRoot().getWindowToken(), 0);
                } else {
                    Snackbar.make(mBinding.getRoot(), "ERROR", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        String[] delayItems = getResources().getStringArray(R.array.enum_delay_array);
        String[] statusItems = getResources().getStringArray(R.array.enum_status_array);

        ArrayAdapter<String> delayAdapter = new ArrayAdapter<>(requireContext(),
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                delayItems);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(),
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                statusItems);

        ((AutoCompleteTextView) mBinding.delaySelect.getEditText()).setAdapter(delayAdapter);
        ((AutoCompleteTextView) mBinding.statusSelect.getEditText()).setAdapter(statusAdapter);

        mBinding.submitButton.setOnClickListener(button -> {
            String comment = mBinding.commentInput.getEditText().getText().toString().trim();
            String delay = mBinding.delaySelect.getEditText().getText().toString().trim();
            String status = mBinding.statusSelect.getEditText().getText().toString().trim();

            if (comment.length() == 0 && delay.length() == 0 && status.length() == 0) {
                Snackbar.make(button, "Devi compilare almeno un campo.", Snackbar.LENGTH_SHORT).show();
                return;
            }

            button.setEnabled(false);

            Delay delayEnum = delay.length() == 0
                              ? null
                              : Delay.fromInteger(Arrays.asList(delayItems).indexOf(delay));

            Status statusEnum = status.length() == 0
                              ? null
                              : Status.fromInteger(Arrays.asList(statusItems).indexOf(status));

            Log.d(TAG, "comment: " + comment);
            Log.d(TAG, "delay: " + delay);
            Log.d(TAG, "delayEnum: " + delayEnum);
            Log.d(TAG, "status: " + status);
            Log.d(TAG, "statusEnum: " + statusEnum);

            mViewModel.publish(comment, delayEnum, statusEnum);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
