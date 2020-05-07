package com.diatracker.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.diatracker.DiaTrackerDB;
import com.diatracker.DiaTrackerMain;
import com.diatracker.R;
import com.diatracker.ui.insulin.InsulinViewModel;

public class SettingsFragment extends Fragment implements OnClickListener {

    private SettingsViewModel settingsViewModel;
    private Button reset;
    private Button export;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView textView = root.findViewById(R.id.text_settings);
        reset = (Button) root.findViewById(R.id.buttonReset);
        export = (Button) root.findViewById(R.id.buttonExport);
        reset.setOnClickListener(this);
        export.setOnClickListener(this);
        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {
        DiaTrackerDB db = new DiaTrackerDB(getActivity());
        switch (v.getId()) {
            case R.id.buttonReset:
                reset(db);
                break;
            case R.id.buttonExport:
                export(db);
                break;
            default:
                break;
        }
    }
    private void reset(final DiaTrackerDB db) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.reset_message).setTitle("Database Clear");
        builder.setPositiveButton ("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast toast = Toast.makeText(getActivity(), "Database cleared",Toast.LENGTH_LONG);
                toast.show();
                db.clearDB();
            }
        });
        builder.setNegativeButton ("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void export(DiaTrackerDB db) {
        Toast toast = Toast.makeText(getActivity(), "Database exported to device",Toast.LENGTH_LONG);
        toast.show();
        DiaTrackerMain.verifyStoragePermissions(getActivity());
        db.exportDB();
    }
}
