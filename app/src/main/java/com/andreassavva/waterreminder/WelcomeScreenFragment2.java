package com.andreassavva.waterreminder;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class WelcomeScreenFragment2 extends Fragment {

    private String wdStartTime = null;
    private String wdEndTime = null;
    private String weStartTime = null;
    private String weEndTime = null;

    private TextView wdStartInput, wdEndInput, weStartInput, weEndInput;
    private View.OnClickListener textViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            int minutes = 0;
            int hours = 0;
            switch (v.getId()) {
                case R.id.wdStartTextView:
                    wdStartTime = wdStartInput.getText().toString().trim();
                    String[] brokenString1 = wdStartTime.split(":");
                    hours = Integer.parseInt(brokenString1[0]);
                    minutes = Integer.parseInt(brokenString1[1]);
                    break;
                case R.id.wdEndTextView:
                    wdEndTime = wdEndInput.getText().toString().trim();
                    String[] brokenString2 = wdEndTime.split(":");
                    hours = Integer.parseInt(brokenString2[0]);
                    minutes = Integer.parseInt(brokenString2[1]);
                    break;
                case R.id.weStartTextView:
                    weStartTime = weStartInput.getText().toString().trim();
                    String[] brokenString3 = weStartTime.split(":");
                    hours = Integer.parseInt(brokenString3[0]);
                    minutes = Integer.parseInt(brokenString3[1]);
                    break;
                case R.id.weEndTextView:
                    weEndTime = weEndInput.getText().toString().trim();
                    String[] brokenString4 = weEndTime.split(":");
                    hours = Integer.parseInt(brokenString4[0]);
                    minutes = Integer.parseInt(brokenString4[1]);
                    break;
            }
            new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    switch (v.getId()) {
                        case R.id.wdStartTextView:
                            wdStartTime = String.format("%02d", hourOfDay) + ":" +String.format("%02d",  minute);
                            wdStartInput.setText(wdStartTime);
                            break;
                        case R.id.wdEndTextView:
                            wdEndTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                            wdEndInput.setText(wdEndTime);
                            break;
                        case R.id.weStartTextView:
                            weStartTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                            weStartInput.setText(weStartTime);
                            break;
                        case R.id.weEndTextView:
                            weEndTime = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
                            weEndInput.setText(weEndTime);
                            break;
                    }
                }
            }, hours, minutes, true).show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_screen2, container, false);
        wdStartInput = (TextView) root.findViewById(R.id.wdStartTextView);
        wdEndInput = (TextView) root.findViewById(R.id.wdEndTextView);
        weStartInput = (TextView) root.findViewById(R.id.weStartTextView);
        weEndInput = (TextView) root.findViewById(R.id.weEndTextView);

        wdStartInput.setOnClickListener(textViewOnClickListener);
        wdEndInput.setOnClickListener(textViewOnClickListener);
        weStartInput.setOnClickListener(textViewOnClickListener);
        weEndInput.setOnClickListener(textViewOnClickListener);

        return root;
    }

    public TextView getWdStartInput() {
        return wdStartInput;
    }

    public TextView getWdEndInput() {
        return wdEndInput;
    }

    public TextView getWeStartInput() {
        return weStartInput;
    }

    public TextView getWeEndInput() {
        return weEndInput;
    }
}