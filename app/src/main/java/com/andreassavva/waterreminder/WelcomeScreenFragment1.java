package com.andreassavva.waterreminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class WelcomeScreenFragment1 extends Fragment {

    private EditText nameInput, weightInput;
    private Spinner weightMetricInput, activityInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_screen1, container, false);
        nameInput = (EditText) root.findViewById(R.id.nameEditText);
        weightInput = (EditText) root.findViewById(R.id.weightEditText);
        weightMetricInput = (Spinner) root.findViewById(R.id.weightMetricSpinner);
        activityInput = (Spinner) root.findViewById(R.id.activitySpinner);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.person_activity, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityInput.setAdapter(activityAdapter);
        activityInput.setSelection(2);

        ArrayAdapter<CharSequence> weightMetricsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.weight_metrics, android.R.layout.simple_spinner_item);
        weightMetricsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightMetricInput.setAdapter(weightMetricsAdapter);

        return root;
    }

    public EditText getNameInput() {
        return nameInput;
    }

    public EditText getWeightInput() {
        return weightInput;
    }

    public Spinner getWeightMetricInput() {
        return weightMetricInput;
    }

    public Spinner getActivityInput() {
        return activityInput;
    }
}
