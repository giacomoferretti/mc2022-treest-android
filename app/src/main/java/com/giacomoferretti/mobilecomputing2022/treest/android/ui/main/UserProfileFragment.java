package com.giacomoferretti.mobilecomputing2022.treest.android.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.giacomoferretti.mobilecomputing2022.treest.android.R;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.FragmentUserProfileBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding mBinding;
    private MainViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.setNewProfilePicture(null);

        mViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            mBinding.submitButton.setEnabled(true);
            mBinding.nameInput.getEditText().setText(profile.getName());

            if (profile.getPicture() != null) {
                byte[] decodedData = Base64.decode(profile.getPicture(), Base64.DEFAULT);

                mBinding.profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(decodedData,
                        0, decodedData.length));
            }
        });

        ActivityResultLauncher<String> mGetContent =
                registerForActivityResult(new ActivityResultContracts.GetContent(),
                        uri -> {
                            try {
                                // Read bitmap
                                Bitmap image =
                                        BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(uri));

                                // Crop to square
                                int dimension = Math.min(image.getWidth(), image.getHeight());
                                image = ThumbnailUtils.extractThumbnail(image, dimension,
                                        dimension);

                                // Convert to Base64
                                ByteArrayOutputStream byteArrayOutputStream =
                                        new ByteArrayOutputStream();
                                image.compress(Bitmap.CompressFormat.WEBP, 80,
                                        byteArrayOutputStream);
                                String result =
                                        Base64.encodeToString(byteArrayOutputStream.toByteArray(),
                                                Base64.DEFAULT);

                                // Check for requirements
                                if (result.length() > 137000) {
                                    Snackbar.make(mBinding.getRoot(), getString(R.string.image_too_big),
                                            Snackbar.LENGTH_SHORT).show();
                                    return;
                                }

                                // Set image
                                mViewModel.setNewProfilePicture(result);
                                mBinding.profilePicture.setImageBitmap(image);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        });

        mBinding.profilePicture.setOnClickListener(view1 -> {
            mGetContent.launch("image/*");
        });

        mBinding.submitButton.setOnClickListener(view1 -> {
            view1.setEnabled(false);
            mViewModel.setProfile(mBinding.nameInput.getEditText().getText().toString(),
                    mViewModel.getNewProfilePicture().getValue());
        });

        mViewModel.getUpdateProfileEvent().observe(getViewLifecycleOwner(), event -> {
            Boolean success = event.getContentIfNotHandled();
            if (success != null) {
                mBinding.submitButton.setEnabled(true);

                if (success) {
                    mViewModel.setNewProfilePicture(null);
                    Snackbar.make(mBinding.getRoot(), getString(R.string.profile_updated),
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mBinding.getRoot(), getString(R.string.retry_error),
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}