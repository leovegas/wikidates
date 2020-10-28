package com.wikidates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public static Switch switch1;
    public Context context;
    SharedPreferences sp;
    SharedPreferences sp1,sp2;

    public static volatile ArrayList<Double> templist = new ArrayList<Double>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Button okbutton = findViewById(R.id.button);
       final EditText yearmin = (EditText) findViewById(R.id.editText3);
        final EditText yearmax = (EditText) findViewById(R.id.editText4);
         final EditText textmin = (EditText) findViewById(R.id.editText2);

        switch1 = (Switch) findViewById(R.id.switch1);
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        switch1.setVisibility(View.GONE);

// Настраиваем адаптер
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.Languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Вызываем адаптер
        spinner.setAdapter(adapter);
        if (MainActivity.language) {
            spinner.setSelection(1);
        }else           spinner.setSelection(0);


        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            switch1.setEnabled(true);
            switch1.setActivated(true);
            sp = getSharedPreferences("switchstate", MODE_PRIVATE);
            boolean sc = sp.getBoolean("switchstate", false);
            switch1.setChecked(sc);
            MainActivity.langswitch=sc;
        sp1 = getSharedPreferences("max", MODE_PRIVATE);
        int sc1 = sp1.getInt("max", 2020);
        yearmax.setText(String.valueOf(sc1));

        sp2 = getSharedPreferences("min", MODE_PRIVATE);
        int sc2 = sp2.getInt("min", 0);
        yearmin.setText(String.valueOf(sc2));




                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
if (selectedItemPosition==1) {
    switch1.setChecked(true);
    MainActivity.langswitch=true;
}
                if (selectedItemPosition==0) {
                    switch1.setChecked(false);
                    MainActivity.langswitch=false;

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


yearmin.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        MainActivity.miny = Integer.parseInt(String.valueOf(yearmin.getText()));
    }
});
        yearmax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.maxy = Integer.parseInt(String.valueOf(yearmax.getText()));

            }
        });

        okbutton.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            if (switch1.isChecked()) {
                                                sp = getSharedPreferences("switchstate", MODE_PRIVATE);
                                                sp.edit().putBoolean("switchstate",true).apply();
                                            }
                                            if (!switch1.isChecked()) {
                                                sp = getSharedPreferences("switchstate", MODE_PRIVATE);
                                                sp.edit().putBoolean("switchstate",false).apply();
                                            }
                                            sp2 = getSharedPreferences("min", MODE_PRIVATE);
                                            sp2.edit().putInt("min", Integer.parseInt(String.valueOf(yearmin.getText()))).apply();

                                            sp1 = getSharedPreferences("max", MODE_PRIVATE);
                                            sp1.edit().putInt("max", Integer.parseInt(String.valueOf(yearmax.getText()))).apply();



                                            startActivity(new Intent(SettingsActivity.this, MainActivity.class));

                                          /*  if (login.getText() != null) {ShowDialogAsyncTask.USERNAME = String.valueOf(login.getText());
                                            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                                            }
                                            if (pass.getText() != null) {ShowDialogAsyncTask.PASSWORD = String.valueOf(pass.getText());
                                                startActivity(new Intent(SettingsActivity.this, MainActivity.class));

                                            }

                                           */

                                        }
                                    });

        //getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();

    }
}