package com.example.himanshu.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class AddTodoActivity extends AppCompatActivity {
    public static final String TITLE_KEY = "title";
    public static final String DAY_KEY = "amount";
    public static final String DATE_KEY="01/07/2018";
    public static final String TIME_KEY="18:56";

    public static final int ADD_RESULT_CODE = 2;
    EditText TimeEditText,dayEditText,dateEditText,titleEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bundle.getString("title");

        TimeEditText = findViewById(R.id.addtodoTimeEditText);
        TimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime();
            }
        });
        dateEditText=findViewById(R.id.addtododateEditText);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate();
            }
        });



    }


    public void save(View view){


        titleEditText =findViewById(R.id.titletext);
        dayEditText=findViewById(R.id.daytext);
        dateEditText = findViewById(R.id.addtododateEditText);
        TimeEditText = findViewById(R.id.addtodoTimeEditText);


        String title = titleEditText.getText().toString();
        String day = dayEditText.getText().toString();
        String date=dateEditText.getText().toString();
        String time =TimeEditText.getText().toString();


        Intent data = new Intent();
        data.putExtra(TITLE_KEY,"Task:"+" "+title);
        data.putExtra(DAY_KEY,"Description:"+" "+day);
        data.putExtra(DATE_KEY,"Date:"+" "+date);
        data.putExtra(TIME_KEY,"Time:"+" "+time);


        setResult(ADD_RESULT_CODE,data);
        finish();



    }
    private void setTime() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTodoActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String timeExpense = hourOfDay + ":" + minute;
                TimeEditText.setText(timeExpense);

            }
        },hour,min,true);

        timePickerDialog.show();
    }
    private void setDate() {
        
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTodoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                ++month;
                String dateExpense = dayOfMonth + "/" + month + "/" + year;
                dateEditText.setText(dateExpense);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}
