package com.andreassavva.waterreminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WelcomeScreenFragment4 extends Fragment {

    private ImageView cupImageInput;
    private TextView cupSizeInput, reminderInfoInput, volumeMetricInput2;
    private RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_screen4, container, false);
        cupImageInput = (ImageView) root.findViewById(R.id.cupImageView);
        cupSizeInput = (TextView) root.findViewById(R.id.cupSizeTextView);
        reminderInfoInput = (TextView) root.findViewById(R.id.reminderInfoTextView);
        volumeMetricInput2 = (TextView) root.findViewById(R.id.volumeMetricTextView);
        relativeLayout = (RelativeLayout) root.findViewById(R.id.ws4RelativeLayout);
        return root;
    }

    public ImageView getCupImageInput() {
        return cupImageInput;
    }

    public TextView getCupSizeInput() {
        return cupSizeInput;
    }

    public TextView getReminderInfoInput() {
        return reminderInfoInput;
    }

    public TextView getVolumeMetricInput2() {
        return volumeMetricInput2;
    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }
}