package com.giacomoferretti.mobilecomputing2022.treest.android.ui.firsttime;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.giacomoferretti.mobilecomputing2022.treest.android.core.Status;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.ActivityFirstTimeBinding;
import com.giacomoferretti.mobilecomputing2022.treest.android.ui.boardselection.BoardSelectionActivity;
import com.giacomoferretti.mobilecomputing2022.treest.android.utils.NetworkUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class FirstTimeActivity extends AppCompatActivity implements FirstTimeNavigator {

    private ActivityFirstTimeBinding mBinding;
    private FirstTimeViewModel mViewModel;

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isNetworkConnected(this)) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Nessuna connessione ad Internet")
                    .setMessage("Quest'app ha bisogno di una connessione ad Internet per funzionare. Connettiti ad Internet e riprova.")
                    .setCancelable(false)
                    .setPositiveButton("Riprova", (dialogInterface, i) -> {
                        recreate();
                    })
                    .setNegativeButton("Esci", (dialogInterface, i) -> {
                        finish();
                    })
                    .show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDataRepository repository = AppDataRepository.getInstance(this);

        // Initialize session id
        if (repository.getSessionId() == null) {
            repository.register(result -> {
                if (result.getStatus() == Status.SUCCESS) {
                    repository.setSessionId(result.getData());
                } else {
                    // TODO: Show dialog when error
                }
            });
        }

        // Check if already displayed
        if (AppDataRepository.getInstance(this).shouldSkipFirstTime()) {
            onSetProfile();
        }

        mBinding = ActivityFirstTimeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(FirstTimeViewModel.class);

        mBinding.nameInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mBinding.submitButton.setOnClickListener(view -> {
            mViewModel.setProfile();
        });

        mViewModel.getIsEnabled().observe(this,
                isEnabled -> mBinding.submitButton.setEnabled(isEnabled));

        mViewModel.getSubmitCommand().observe(this, event -> {
            Boolean success = event.getContentIfNotHandled();
            if (success != null) {
                if (success) {
                    // Snackbar.make(mBinding.getRoot(), "SUCCESS", Snackbar.LENGTH_SHORT).show();
                    onSetProfile();
                } else {
                    Snackbar.make(mBinding.getRoot(), "C'è stato un errore. Riprova.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onSetProfile() {
        Intent intent = new Intent(this, BoardSelectionActivity.class);
        startActivity(intent);
        finish();
    }
}