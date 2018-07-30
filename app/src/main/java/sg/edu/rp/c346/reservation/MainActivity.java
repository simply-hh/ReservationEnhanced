package sg.edu.rp.c346.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText num;
    EditText pax;
    EditText date;
    EditText time;
    CheckBox nosmoke;
    Button confirm;
    Button reset;
    int globalyear;
    int globalmonth;
    int globalday;
    int globalhour;
    int globalminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.editName);
        num = findViewById(R.id.editNum);
        pax = findViewById(R.id.editPax);
        date = findViewById(R.id.editTextDay);
        time = findViewById(R.id.editTextTime);
        nosmoke = findViewById(R.id.checkBoxNoSmoke);
        confirm = findViewById(R.id.buttonConfirm);
        reset = findViewById(R.id.buttonReset);




        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    date.setText("Date: " + dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                    globalday = dayOfMonth;
                    globalmonth = monthOfYear;
                    globalyear = year;
                    }
                };
                Calendar now = Calendar.getInstance();
                if (date.getText().toString().isEmpty()) {
                    DatePickerDialog myDateDialog = new DatePickerDialog(MainActivity.this, myDateListener, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                    myDateDialog.show();
                }
                else{
                    DatePickerDialog myDateDialog = new DatePickerDialog(MainActivity.this, myDateListener, globalyear, globalmonth, globalday);
                    myDateDialog.show();
                }
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        time.setText("Time: "+ hourOfDay + ":" + minute);
                        globalhour = hourOfDay;
                        globalminute = minute;
                    }
                };
                Calendar now = Calendar.getInstance();
                if (time.getText().toString().isEmpty()) {
                    TimePickerDialog myTimeDialog = new TimePickerDialog(MainActivity.this, myTimeListener, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
                    myTimeDialog.show();
                }
                else {
                    TimePickerDialog myTimeDialog = new TimePickerDialog(MainActivity.this, myTimeListener, globalhour, globalminute, true);
                    myTimeDialog.show();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().isEmpty() || num.getText().toString().isEmpty() || pax.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Fields must not be blank!",Toast.LENGTH_SHORT).show();
                }
                else {
                    String smoke;
                    if (nosmoke.isChecked()){
                        smoke = "Yes";
                    }
                    else{
                        smoke = "No";
                    }
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
                    myBuilder.setTitle("Confirm Your Order");
                    myBuilder.setMessage("New reservation\nName: " + name.getText().toString() + "\nNon-Smoking Area: " + smoke + "\nNo of Pax: " + pax.getText().toString() + "\n" + date.getText().toString() + "\n" + time.getText().toString());
                    myBuilder.setCancelable(false);
                    myBuilder.setPositiveButton("Confirm", null);
                    myBuilder.setNegativeButton("Cancel", null);
                    AlertDialog myDialog = myBuilder.create();
                    myDialog.show();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                num.setText("");
                pax.setText("");
                date.setText("");
                time.setText("");
                nosmoke.setChecked(false);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        String prefName = name.getText().toString();
        String prefDate = date.getText().toString();
        String prefTime = time.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("name", prefName);
        if (num.getText().toString().isEmpty()){
            prefEdit.putInt("phone", 0);
        }
        else {
            int prefPhone = Integer.valueOf(num.getText().toString());
            prefEdit.putInt("phone", prefPhone);
        }
        if (pax.getText().toString().isEmpty()){
            prefEdit.putInt("pax", 0);
        }
        else {
            int prefPax = Integer.valueOf(pax.getText().toString());
            prefEdit.putInt("pax", prefPax);
        }
        prefEdit.putString("date", prefDate);
        prefEdit.putString("time", prefTime);
        prefEdit.putBoolean("nosmoke", nosmoke.isChecked());
        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String prefName = prefs.getString("name", "");
        int prefPhone = prefs.getInt("phone", 0);
        int prefPax = prefs.getInt("pax", 0);
        String prefDate = prefs.getString("date", "");
        String prefTime = prefs.getString("time", "");
        boolean prefsmoke = prefs.getBoolean("nosmoke", false);
        name.setText(prefName);
        if (prefPhone == 0){
            num.setText("");
        }
        else {
            num.setText(String.valueOf(prefPhone));
        }
        if (prefPax == 0){
            pax.setText("");
        }
        else {
            pax.setText(String.valueOf(prefPax));
        }
        date.setText(prefDate);
        time.setText(prefTime);
        if (prefsmoke){
            nosmoke.setChecked(true);
        }
        else{
            nosmoke.setChecked(false);
        }
    }
}
