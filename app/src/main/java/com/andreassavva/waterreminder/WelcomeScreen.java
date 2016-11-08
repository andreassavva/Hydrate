package com.andreassavva.waterreminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WelcomeScreen extends AppCompatActivity {

    public static final String TAG = "AKS";
    private static final int TOTAL_PAGES = 5;
    private static final String PREFS_NAME = "WaterPrefsFile";

    private ArrayList<Fragment> fragmentList;

    private boolean measureInKg = true, measureInMl = true;
    private int weight;
    private int dailyWaterInMl;
    private int levelOfActivity;
    private String wdStart, wdEnd, weStart, weEnd;
    private String[] cupSizesMl = {"150 ml", "250 ml", "350 ml", "500 ml", "650 ml", "750 ml", "1000 ml"};
    private String[] cupSizesOz = {"5 oz", "9 oz", "12 oz", "17 oz", "22 oz", "26 oz", "34 oz"};
    private int[] cupImages = {R.drawable.ic_edit, R.drawable.ic_edit, R.drawable.ic_edit, R.drawable.ic_edit, R.drawable.ic_edit, R.drawable.ic_edit, R.drawable.ic_edit};

    private ViewPager pager;
    private LinearLayout circles;
    private Button btnBack;
    private Button btnDone;
    private Button btnNext;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_layout);

        fragmentList = new ArrayList<>();
        final WelcomeScreenFragment1 welcomeScreenFragment1 = new WelcomeScreenFragment1();
        final WelcomeScreenFragment2 welcomeScreenFragment2 = new WelcomeScreenFragment2();
        final WelcomeScreenFragment3 welcomeScreenFragment3 = new WelcomeScreenFragment3();
        final WelcomeScreenFragment4 welcomeScreenFragment4 = new WelcomeScreenFragment4();
        final WelcomeScreenFragment5 welcomeScreenFragment5 = new WelcomeScreenFragment5();
        fragmentList.add(welcomeScreenFragment1);
        fragmentList.add(welcomeScreenFragment2);
        fragmentList.add(welcomeScreenFragment3);
        fragmentList.add(welcomeScreenFragment4);
        fragmentList.add(welcomeScreenFragment5);

        btnBack = Button.class.cast(findViewById(R.id.btn_back));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(pager.getCurrentItem() - 1, true);
            }
        });

        btnNext = Button.class.cast(findViewById(R.id.btn_next));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (everythingIsOK(pager.getCurrentItem())) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                } else {
                    switch (pager.getCurrentItem()) {
                        case 0:
                            if (welcomeScreenFragment1.getNameInput().getText().toString().matches("")) {
                                Toast.makeText(WelcomeScreen.this, "Please fill in your name", Toast.LENGTH_SHORT).show();
                                welcomeScreenFragment1.getNameInput().requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(welcomeScreenFragment1.getNameInput(), InputMethodManager.SHOW_IMPLICIT);
                            } else if (welcomeScreenFragment1.getWeightInput().getText().toString().matches("")) {
                                Toast.makeText(WelcomeScreen.this, "Please fill in your weight", Toast.LENGTH_SHORT).show();
                                welcomeScreenFragment1.getWeightInput().requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(welcomeScreenFragment1.getWeightInput(), InputMethodManager.SHOW_IMPLICIT);
                            }
                            break;
                        case 1:
                            Toast.makeText(WelcomeScreen.this, "Start time must be before finish time", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(WelcomeScreen.this, "Daily water intake must be higher than 0", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            // No controls yet.
                            break;
                    }
                }
            }
        });

        btnDone = Button.class.cast(findViewById(R.id.done));
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endIntroduction();
            }
        });

        pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new ScreenSlideAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);
                if (position == 0) {
                    btnBack.setVisibility(View.INVISIBLE);
                    btnDone.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);

                } else if (position == 1) {
                    btnBack.setVisibility(View.VISIBLE);
                    if (!everythingIsOK(position - 1)) {
                        pager.setCurrentItem(0, true);
                        if (welcomeScreenFragment1.getNameInput().getText().toString().matches("")) {
                            Toast.makeText(WelcomeScreen.this, "Please fill in your name", Toast.LENGTH_SHORT).show();
                            welcomeScreenFragment1.getNameInput().requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(welcomeScreenFragment1.getNameInput(), InputMethodManager.SHOW_IMPLICIT);
                        } else if (welcomeScreenFragment1.getWeightInput().getText().toString().matches("")) {
                            Toast.makeText(WelcomeScreen.this, "Please fill in your weight", Toast.LENGTH_SHORT).show();
                            welcomeScreenFragment1.getWeightInput().requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(welcomeScreenFragment1.getWeightInput(), InputMethodManager.SHOW_IMPLICIT);
                        }
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(pager.getWindowToken(), 0);

                        if (wdStart != null)
                            welcomeScreenFragment2.getWdStartInput().setText(wdStart);
                        if (wdEnd != null) welcomeScreenFragment2.getWdEndInput().setText(wdEnd);
                        if (weStart != null)
                            welcomeScreenFragment2.getWeStartInput().setText(weStart);
                        if (weEnd != null) welcomeScreenFragment2.getWeEndInput().setText(weEnd);
                    }
                } else if (position == 2) {
                    if (everythingIsOK(position - 1)) {
                        btnNext.setVisibility(View.VISIBLE);
                        btnBack.setVisibility(View.VISIBLE);
                        btnDone.setVisibility(View.GONE);

                        wdStart = welcomeScreenFragment2.getWdStartInput().getText().toString().trim();
                        wdEnd = welcomeScreenFragment2.getWdEndInput().getText().toString().trim();
                        weStart = welcomeScreenFragment2.getWeStartInput().getText().toString().trim();
                        weEnd = welcomeScreenFragment2.getWeEndInput().getText().toString().trim();

                        if (welcomeScreenFragment3.getDailyIntakeInput().getText().toString().matches("")) {
                            calculateDailyWaterIntake();
                        }

                        welcomeScreenFragment3.getEditDailyIntakeInput().setOnClickListener(null);
                        welcomeScreenFragment3.getEditDailyIntakeInput().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final View dialog2 = getLayoutInflater().inflate(R.layout.edit_daily_water_intake_dialog, null);
                                final EditText newDailyWaterIntakeInput = (EditText) dialog2.findViewById(R.id.dailyWaterDialogEditText);


                                newDailyWaterIntakeInput.append(welcomeScreenFragment3.getDailyIntakeInput().getText().toString().trim());
                                newDailyWaterIntakeInput.setSelectAllOnFocus(true);

                                final AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeScreen.this);

//                                Handler delayedRun = new Handler();
//                                delayedRun.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        newDailyWaterIntakeInput.requestFocus();
//                                        InputMethodManager mgr = (InputMethodManager) builder.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                        mgr.showSoftInput(newDailyWaterIntakeInput, InputMethodManager.SHOW_IMPLICIT);
//                                    }
//                                });

                                builder.setMessage(R.string.new_daily_water_intake)
                                        .setView(dialog2)
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (newDailyWaterIntakeInput.getText().toString().matches("") ||
                                                        newDailyWaterIntakeInput.getText().toString().matches("0")) {
                                                    Toast.makeText(getApplicationContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    welcomeScreenFragment3.getDailyIntakeInput().setText(newDailyWaterIntakeInput.getText().toString());
                                                    dailyWaterInMl = Integer.parseInt(newDailyWaterIntakeInput.getText().toString());
                                                }
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(pager.getWindowToken(), 0);
                                            }
                                        }).show();

                            }
                        });

                        welcomeScreenFragment3.getVolumeMetricInput().setOnItemSelectedListener(null);
                        welcomeScreenFragment3.getVolumeMetricInput().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    measureInMl = true;
                                    welcomeScreenFragment3.getDailyIntakeInput().setText(String.valueOf((Math.round(dailyWaterInMl))));
                                } else {
                                    measureInMl = false;
                                    int dailyWaterTemp = (int) Math.round(dailyWaterInMl * 0.033814);
                                    welcomeScreenFragment3.getDailyIntakeInput().setText(String.valueOf(dailyWaterTemp));
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                } else if (position == TOTAL_PAGES - 2) {
                    if (everythingIsOK(position - 1)) {
                        btnNext.setVisibility(View.GONE);
                        btnBack.setVisibility(View.VISIBLE);
                        btnDone.setVisibility(View.VISIBLE);

                        if (welcomeScreenFragment3.getVolumeMetricInput().getSelectedItemPosition() == 1) {
                            welcomeScreenFragment4.getVolumeMetricInput2().setText("oz");
                            welcomeScreenFragment4.getCupSizeInput().setText("12");
                        } else {
                            welcomeScreenFragment4.getVolumeMetricInput2().setText("ml");
                            welcomeScreenFragment4.getCupSizeInput().setText("350");
                        }

                        welcomeScreenFragment4.getRelativeLayout().setOnClickListener(null);
                        welcomeScreenFragment4.getRelativeLayout().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ListAdapter adapter;
                                if (measureInMl) {
                                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.cup_choose_row, cupSizesMl) {

                                        ViewHolder holder;
                                        Drawable icon;

                                        class ViewHolder {
                                            ImageView icon;
                                            TextView title;
                                        }

                                        public View getView(int position, View convertView,
                                                            ViewGroup parent) {
                                            final LayoutInflater inflater = getLayoutInflater();

                                            if (convertView == null) {
                                                convertView = inflater.inflate(
                                                        R.layout.cup_choose_row, null);

                                                holder = new ViewHolder();
                                                holder.icon = (ImageView) convertView
                                                        .findViewById(R.id.cupChooseImageView);
                                                holder.title = (TextView) convertView
                                                        .findViewById(R.id.cupChooseTextView);
                                                convertView.setTag(holder);
                                            } else {
                                                holder = (ViewHolder) convertView.getTag();
                                            }

                                            Drawable image = ContextCompat.getDrawable(getApplicationContext(), cupImages[position]);

                                            holder.title.setText(cupSizesMl[position]);
                                            holder.icon.setImageDrawable(image);

                                            return convertView;
                                        }
                                    };
                                } else {
                                    adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.cup_choose_row, cupSizesOz) {

                                        ViewHolder holder;
                                        Drawable icon;

                                        class ViewHolder {
                                            ImageView icon;
                                            TextView title;
                                        }

                                        public View getView(int position, View convertView, ViewGroup parent) {
                                            final LayoutInflater inflater = getLayoutInflater();

                                            if (convertView == null) {
                                                convertView = inflater.inflate(R.layout.cup_choose_row, null);

                                                holder = new ViewHolder();
                                                holder.icon = (ImageView) convertView.findViewById(R.id.cupChooseImageView);
                                                holder.title = (TextView) convertView.findViewById(R.id.cupChooseTextView);
                                                convertView.setTag(holder);
                                            } else {
                                                // view already defined, retrieve view holder
                                                holder = (ViewHolder) convertView.getTag();
                                            }

                                            Drawable image = ContextCompat.getDrawable(getApplicationContext(), cupImages[position]);
                                            holder.title.setText(cupSizesOz[position]);
                                            holder.icon.setImageDrawable(image);

                                            return convertView;
                                        }
                                    };
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeScreen.this);
                                builder.setTitle("Cup sizes");
                                builder.setAdapter(adapter,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int item) {
                                                if (measureInMl) {
                                                    String[] brokenStringTemp = cupSizesMl[item].split(" ");
                                                    welcomeScreenFragment4.getCupSizeInput().setText(brokenStringTemp[0].trim());
                                                } else {
                                                    String[] brokenStringTemp = cupSizesOz[item].split(" ");
                                                    welcomeScreenFragment4.getCupSizeInput().setText(brokenStringTemp[0].trim());
                                                }
                                                dialog.dismiss();
                                                calculateReminders(welcomeScreenFragment2, welcomeScreenFragment3, welcomeScreenFragment4);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        });

                        calculateReminders(welcomeScreenFragment2, welcomeScreenFragment3, welcomeScreenFragment4);

                    } else {
                        pager.setCurrentItem(2, true);
                        Toast.makeText(WelcomeScreen.this, "Daily water intake must be higher than 0", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == TOTAL_PAGES - 1) {
                    if (everythingIsOK(position - 1)) {
                        endIntroduction();
                    } else {
                        pager.setCurrentItem(3, true);
                        // I don't have anything to control here.
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        buildCircles();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }

    private void buildCircles() {
        circles = LinearLayout.class.cast(findViewById(R.id.circles));
        for (int i = 0; i < TOTAL_PAGES - 1; i++) {
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setFilterBitmap(true);
            p.setDither(true);
            p.setColor(Color.GRAY);
            p.setStrokeWidth(3.75F);
            p.setStyle(Paint.Style.FILL);
            Bitmap bmp1 = Bitmap.createBitmap(36, 36, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp1);
            canvas.drawCircle(18.0F, 18.0F, 10.0F, p);

            ImageView circle = new ImageView(this);
            circle.setImageBitmap(bmp1);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circles.addView(circle);
        }
        setIndicator(0);
    }

    private void setIndicator(int index) {
        if (index < TOTAL_PAGES) {
            for (int i = 0; i < TOTAL_PAGES - 1; i++) {
                ImageView circle = (ImageView) circles.getChildAt(i);
                if (i == index) {
                    Paint p = new Paint();
                    p.setAntiAlias(true);
                    p.setFilterBitmap(true);
                    p.setDither(true);
                    p.setColor(Color.GRAY);
                    p.setStrokeWidth(3.75F);
                    p.setStyle(Paint.Style.FILL);
                    Bitmap bmp1 = Bitmap.createBitmap(72, 72, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bmp1);
                    canvas.drawCircle(36.0F, 36.0F, 20.0F, p);
                    circle.setImageBitmap(bmp1);
                    circle.setColorFilter(Color.WHITE);

                } else {
                    Paint p = new Paint();
                    p.setAntiAlias(true);
                    p.setFilterBitmap(true);
                    p.setDither(true);
                    p.setColor(Color.GRAY);
                    p.setStrokeWidth(3.75F);
                    p.setStyle(Paint.Style.FILL);
                    Bitmap bmp1 = Bitmap.createBitmap(36, 36, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bmp1);
                    canvas.drawCircle(18.0F, 18.0F, 10.0F, p);
                    circle.setImageBitmap(bmp1);
                    circle.setColorFilter(Color.TRANSPARENT);
                }
            }
        }
    }

    private void endIntroduction() {
        //////////////////////////////// SAVE PREFERENCES //////////////////////////////////////
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();

        WelcomeScreenFragment1 welcomeScreenFragment1 = (WelcomeScreenFragment1) fragmentList.get(0);
        WelcomeScreenFragment4 welcomeScreenFragment4 = (WelcomeScreenFragment4) fragmentList.get(3);

        // From welcomeScreenFragment1
        String name = welcomeScreenFragment1.getNameInput().getText().toString().trim();
        weight = Integer.parseInt(welcomeScreenFragment1.getWeightInput().getText().toString());
        levelOfActivity = welcomeScreenFragment1.getActivityInput().getSelectedItemPosition();

        editor.putString("name", name);
        editor.putInt("weight", weight);
        editor.putBoolean("measureInKg", measureInKg);
        editor.putInt("levelOfActivity", levelOfActivity);

        // From welcomeScreenFragment2
        editor.putString("wdStart", wdStart);
        editor.putString("wdEnd", wdEnd);
        editor.putString("weStart", weStart);
        editor.putString("weEnd", weEnd);

        // From welcomeScreenFragment3
        if (measureInMl) {
            editor.putInt("dailyWaterInMl", dailyWaterInMl);
        } else {
            editor.putInt("dailyWaterInMl", (int) Math.round(dailyWaterInMl * 0.033814));
        }
        editor.putBoolean("measureInMl", measureInMl);

        // From welcomeScreenFragment4
        int cupSize = Integer.parseInt(welcomeScreenFragment4.getCupSizeInput().getText().toString());

        editor.putInt("cupSize", cupSize);
        editor.putBoolean("firstrun", false).commit();

        editor.commit();

        Intent intent = new Intent(WelcomeScreen.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    public boolean everythingIsOK(int position) {
        boolean everythingFilledIn = true;
        switch (position) {
            case 0:
                WelcomeScreenFragment1 welcomeScreenFragment1 = (WelcomeScreenFragment1) fragmentList.get(0);
                if (welcomeScreenFragment1.getNameInput().getText().toString().matches("") ||
                        welcomeScreenFragment1.getWeightInput().getText().toString().matches("")) {
                    everythingFilledIn = false;
                }
                break;
            case 1:
                // Nothing to check.
                break;
            case 2:
                // Nothing to check.
                break;
            case 3:
                // Nothing to check.
                break;
        }
        return everythingFilledIn;
    }

    public void recalculateDailyWater(View view) {
        calculateDailyWaterIntake();
    }

    public void calculateDailyWaterIntake() {
        WelcomeScreenFragment1 welcomeScreenFragment1 = (WelcomeScreenFragment1) fragmentList.get(0);
        WelcomeScreenFragment3 welcomeScreenFragment3 = (WelcomeScreenFragment3) fragmentList.get(2);

        weight = Integer.parseInt(welcomeScreenFragment1.getWeightInput().getText().toString());
        if (!welcomeScreenFragment1.getWeightMetricInput().getSelectedItem().toString().matches("kg")) {
            measureInKg = false;
        }
        levelOfActivity = welcomeScreenFragment1.getActivityInput().getSelectedItemPosition();

        dailyWaterInMl = (int) ((weight * 1.1) * 29.5735);
        switch (levelOfActivity) {
            case 0:
                dailyWaterInMl -= (dailyWaterInMl * 0.24);
                break;
            case 1:
                dailyWaterInMl -= (dailyWaterInMl * 0.12);
                break;
            case 2:
                // Do nothing
                break;
            case 3:
                dailyWaterInMl += (dailyWaterInMl * 0.12);
                break;
            case 4:
                dailyWaterInMl += (dailyWaterInMl * 0.24);
                break;
        }
        if (measureInMl) {
            welcomeScreenFragment3.getDailyIntakeInput().setText(String.valueOf(Math.round(dailyWaterInMl)));
        } else {
            welcomeScreenFragment3.getDailyIntakeInput().setText(String.valueOf(Math.round(dailyWaterInMl * 0.033814)));
        }
    }

    public int calculateTimeDifferenceInMinutes(String start, String end) {
        String[] startBrokenString = start.split(":");
        String[] endBrokenString = end.split(":");

        if (Integer.parseInt(startBrokenString[0]) > Integer.parseInt(endBrokenString[0]) ||
                (Integer.parseInt(startBrokenString[0]) == Integer.parseInt(endBrokenString[0]) &&
                        Integer.parseInt(startBrokenString[1].trim()) >= Integer.parseInt(endBrokenString[1].trim()))) {
            endBrokenString[0] = String.valueOf(Integer.parseInt(endBrokenString[0])+24);
        }

        int minutesStart = (Integer.parseInt(startBrokenString[0]) * 60) + Integer.parseInt(startBrokenString[1].trim());
        int minutesEnd = (Integer.parseInt(endBrokenString[0]) * 60) + Integer.parseInt(startBrokenString[1].trim());

        return minutesEnd - minutesStart;
    }

    public void calculateReminders(WelcomeScreenFragment2 welcomeScreenFragment2, WelcomeScreenFragment3 welcomeScreenFragment3, WelcomeScreenFragment4 welcomeScreenFragment4) {
        int wdMinuteDifference = calculateTimeDifferenceInMinutes(
                welcomeScreenFragment2.getWdStartInput().getText().toString(),
                welcomeScreenFragment2.getWdEndInput().getText().toString());
        int weMinuteDifference = calculateTimeDifferenceInMinutes(
                welcomeScreenFragment2.getWeStartInput().getText().toString(),
                welcomeScreenFragment2.getWeEndInput().getText().toString());

        int amountToDrinkTemp = Integer.parseInt(welcomeScreenFragment3.getDailyIntakeInput().getText().toString().trim());
        int cupSizeTemp = Integer.parseInt(welcomeScreenFragment4.getCupSizeInput().getText().toString().trim());

        int wdhowManyTimesDrink = (int) Math.ceil((double) amountToDrinkTemp / cupSizeTemp);
        int wehowManyTimesDrink = (int) Math.ceil((double) amountToDrinkTemp / cupSizeTemp);

        int wdTimeUntilNextRem = wdMinuteDifference / wdhowManyTimesDrink;
        int weTimeUntilNextRem = weMinuteDifference / wehowManyTimesDrink;

        int wdHoursUntilNextRem = wdTimeUntilNextRem / 60;
        int wdMinutesUntilNextRem = wdTimeUntilNextRem % 60;

        int weHoursUntilNextRem = weTimeUntilNextRem / 60;
        int weMinutesUntilNextRem = weTimeUntilNextRem % 60;

        welcomeScreenFragment4.getReminderInfoInput().setText("Reminders:\nWeekdays: " + wdhowManyTimesDrink + " times a day, every "
                + wdHoursUntilNextRem + "h" + String.format("%02d", wdMinutesUntilNextRem) + "m.\nWeekends: " + wehowManyTimesDrink + " times a day, every "
                + weHoursUntilNextRem + "h" + String.format("%02d", weMinutesUntilNextRem) + "m.");
    }

    public class ScreenSlideAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;

        public ScreenSlideAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES - 1;
        }
    }
}
