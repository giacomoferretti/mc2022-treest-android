package com.giacomoferretti.mobilecomputing2022.treest.android.ui.boardselection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.source.AppDataRepository;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.ActivityBoardSelectionBinding;
import com.giacomoferretti.mobilecomputing2022.treest.android.ui.main.MainActivity;
import com.giacomoferretti.mobilecomputing2022.treest.android.utils.NetworkUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class BoardSelectionActivity extends AppCompatActivity implements BoardSelectionNavigator {
    private static final String TAG = BoardSelectionActivity.class.getSimpleName();

    private AppDataRepository mAppDataRepository;
    private ActivityBoardSelectionBinding mBinding;
    private BoardSelectionViewModel mViewModel;

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

        mAppDataRepository = AppDataRepository.getInstance(this);

        // Skip if already selected
        String currentId = mAppDataRepository.getCurrentDirectionId();
        Line currentLine = mAppDataRepository.getCurrentLine();
        if (currentId != null && currentLine != null) {
            onSelectBoard(currentLine, currentId);
        }

        mBinding = ActivityBoardSelectionBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(BoardSelectionViewModel.class);
        mViewModel.loadLines();

        setupRecycler();
    }

    public void setupRecycler() {
        BoardAdapter boardAdapter = new BoardAdapter((line, did) -> {
            onSelectBoard(line, String.valueOf(did));
        });
        mBinding.recyclerView.setAdapter(boardAdapter);

        mViewModel.getLines().observe(this, lines -> {
            mBinding.progressBar.setVisibility(lines.size() != 0 ? View.GONE : View.VISIBLE);
            mBinding.recyclerView.setVisibility(lines.size() != 0 ? View.VISIBLE : View.GONE);
            boardAdapter.setLinesList(lines);
        });
    }

    @Override
    public void onSelectBoard(Line selectedLine, String selectedDirectionId) {
        mAppDataRepository.setCurrentDirectionId(selectedDirectionId);
        mAppDataRepository.setCurrentLine(selectedLine);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_SELECTED_BOARD_ID, selectedDirectionId);
        intent.putExtra(MainActivity.EXTRA_LINES_BUNDLE, selectedLine);
        startActivity(intent);
        finish();
    }
}