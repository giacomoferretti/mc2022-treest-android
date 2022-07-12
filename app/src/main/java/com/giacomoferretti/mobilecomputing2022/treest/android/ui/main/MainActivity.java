package com.giacomoferretti.mobilecomputing2022.treest.android.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.giacomoferretti.mobilecomputing2022.treest.android.R;
import com.giacomoferretti.mobilecomputing2022.treest.android.data.model.Line;
import com.giacomoferretti.mobilecomputing2022.treest.android.databinding.ActivityMainBinding;
import com.giacomoferretti.mobilecomputing2022.treest.android.utils.NetworkUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_SELECTED_BOARD_ID = "SELECTED_BOARD_ID";
    public static final String EXTRA_LINES_BUNDLE = "LINES_BUNDLE";

    private ActivityMainBinding mBinding;
    private MainViewModel mViewModel;

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

        // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.setDirectionId(getIntent().getStringExtra(EXTRA_SELECTED_BOARD_ID));

        Line line = getIntent().getParcelableExtra(EXTRA_LINES_BUNDLE);

        // Setup fragment
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, BoardFeedFragment.newInstance(line, getIntent().getStringExtra(EXTRA_SELECTED_BOARD_ID)), null)
                .commit();

        // Bottom navigation setup
        mBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tab0) {
                Log.d(TAG, "Switching to FEED");
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, BoardFeedFragment.newInstance(line, getIntent().getStringExtra(EXTRA_SELECTED_BOARD_ID)), null)
                        .commit();
                return true;
            }

            if (item.getItemId() == R.id.tab1) {
                Log.d(TAG, "Switching to NEW");
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, NewPostFragment.class, null)
                        .commit();
                return true;
            }

            if (item.getItemId() == R.id.tab2) {
                Log.d(TAG, "Switching to PROFILE");
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, UserProfileFragment.class, null)
                        .commit();
                return true;
            }

            return false;
        });

        /*mViewModel.getOpenBoardEvent().observe(this, event -> {
            String directionId = event.getContentIfNotHandled();
            if (directionId != null) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, BoardFeedFragment.newInstance(directionId), null)
                        .commit();
            }
        });*/
    }

    private BoardFeedFragment findOrCreateViewFragment() {
        Line line = getIntent().getParcelableExtra(EXTRA_LINES_BUNDLE);
        String directionId = getIntent().getStringExtra(EXTRA_SELECTED_BOARD_ID);

        BoardFeedFragment boardFeedFragment = (BoardFeedFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (boardFeedFragment == null) {
            boardFeedFragment = BoardFeedFragment.newInstance(line, directionId);
        }

        return boardFeedFragment;
    }
}