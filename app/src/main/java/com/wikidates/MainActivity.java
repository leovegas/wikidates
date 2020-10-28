package com.wikidates;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "my app";
    public static TextView textView;
    public static EditText editText;
    public static Button button;
    public static Button button2;
    public static Button button1;
    public static Button button3;
    public static Button button4;
    public static Button button5;
    public static ProgressBar progressBar;

    public static boolean butclick=false;
    public static int yearR,yearSave;
   public static int monthR,monthSave;
   public static int dayR,daySave;
    public static DatePicker dp;
    public static DatePickerDialog dpd;
    public static int b1counter=0;
    public static boolean toaster=false;
    static Context context;
    public static boolean language;//rus
    public static String messtext="Введите дату в указанном формате:";
    public static String openurl="http://wikipedia.org";
    public static boolean langswitch;
    public static boolean textYN;


    public static int maxy= 2020;
    int maxm= 12;
    int maxd= 31;
    public static int miny=1900;
    int minm=1;
    int mind = 1;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year;
            int month;
            int day;
            if (b1counter==0) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } else {
               year = yearSave;
               month = monthSave;
               day = daySave;
            }

            // Create a new instance of DatePickerDialog and return it
DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.MySpinnerDatePickerStyle, this, year, month, day);
               return dpd ;

        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            yearSave=year;
            monthSave=month;
            daySave=day;
            yearR=year;
            monthR=month+1;
            dayR=day;
            editText.setText(dayR+"/"+(monthR)+"/"+yearR);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.set:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText2);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button = (Button) findViewById(R.id.button);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);

        textView.isLongClickable();
        textView.setEnabled(true);
        textView.setFocusable(true);
        textView.setTextIsSelectable(true);


        if (Locale.getDefault().getLanguage().equals("Русский")||Locale.getDefault().getLanguage().equals("русский") || !langswitch)
        {
            language=false;
        }else language=true;

        if (language) {
button1.setText("Choose date");
button2.setText("Random sel");
button3.setText("Enter date");
button.setText("Get information");
messtext="Enter date";

        }


        createNotificationChannel();
       Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(openurl)));

            }
        });


        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dayR++;
                if (dayR<=31&&dayR>0){
                    editText.setText(dayR+"/"+monthR+"/"+yearR);
                } else dayR--;

            }
                                  });
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 dayR--;
                if (dayR<=31&&dayR>0){
                    editText.setText(dayR+"/"+monthR+"/"+yearR);
                } else dayR++;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

    //     NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
    //     notificationManager.notify(1, builder.build());

                if (toaster) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Пожалуйста подождите...", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
                textView.setText(null);
                new MyTask().execute();
                toaster=false;


            }
        });


        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText txtUrl = new EditText(MainActivity.this);
                txtUrl.setCursorVisible(false);

                final Calendar c = Calendar.getInstance();
                toaster=false;

// Set the default text to a link of the Queen
                txtUrl.setHint("dd/mm/yyyy");

                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(messtext)
                        .setView(txtUrl)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                editText.setText(String.valueOf(txtUrl.getText()));
                                String text = String.valueOf(editText.getText());

                                for (int i =0;i<text.length();i++){
                                    if (text.charAt(i)=='/') {
                                        dayR=Integer.parseInt(text.substring(0,i));
                                        text=text.substring(i+1);
                                    }
                                }

                                for (int i =0;i<text.length();i++){
                                    if (text.charAt(i)=='/') {
                                        monthR=Integer.parseInt(text.substring(0,i));
                                        yearR=Integer.parseInt(text.substring(i+1));
                                    }
                                }
                                editText.setText(dayR+"/"+monthR+"/"+yearR);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();

            }
        });

button2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        toaster=false;
        yearR = miny + (int)(Math.random() * ((maxy - miny) + 1));
        monthR =  minm + (int)(Math.random() * ((maxm - minm) + 1));
        dayR = mind + (int)(Math.random() * ((maxd - mind) + 1));
        if (yearR<1700) {
            if (language) editText.setText("Year "+String.valueOf(yearR)); else
            editText.setText(String.valueOf(yearR)+" год");
        }else
        editText.setText(dayR+" / "+monthR+" / "+yearR);

    }
});

       button1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //0123456789
               //dd/mm/yyyy
               toaster=false;
               b1counter++;
               Toast toast = Toast.makeText(getApplicationContext(),
                       "Выберите дату!", Toast.LENGTH_LONG);
               toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
               toast.show();

               DialogFragment newFragment = new DatePickerFragment();
               newFragment.show(getSupportFragmentManager(), "datePicker");


           }
       });

    }
}
