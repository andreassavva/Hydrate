package com.andreassavva.waterreminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class WelcomeScreenFragment3 extends Fragment {

    private ImageButton editDailyIntakeInput, recalculateInput;
    private TextView dailyIntakeInput;
    private Spinner volumeMetricInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_screen3, container, false);
        editDailyIntakeInput = (ImageButton) root.findViewById(R.id.editDailyIntakeImageButton);
        recalculateInput = (ImageButton) root.findViewById(R.id.recalculateImageButton);
        dailyIntakeInput = (TextView) root.findViewById(R.id.dailyIntakeTextView);
        volumeMetricInput = (Spinner) root.findViewById(R.id.volumeMetricSpinner);
        return root;
    }

    public TextView getDailyIntakeInput() {
        return dailyIntakeInput;
    }

    public Spinner getVolumeMetricInput() {
        return volumeMetricInput;
    }

    public ImageButton getEditDailyIntakeInput() {
        return editDailyIntakeInput;
    }
}