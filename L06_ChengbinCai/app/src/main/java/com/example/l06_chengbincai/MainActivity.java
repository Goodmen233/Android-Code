package com.example.l06_chengbincai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView movieLanguage;
    EditText movieName, releaseDate, boxOffice;
    Button summarize;
    AlertDialog.Builder builder;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieLanguage = findViewById(R.id.movieLanguage);
        movieName = findViewById(R.id.movieName);
        releaseDate = findViewById(R.id.releaseDate);
        boxOffice = findViewById(R.id.boxOffice);
        summarize = findViewById(R.id.button);

        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        movieLanguage.setAdapter(adapter);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date = i2 + "/" + (i1 + 1) + "/" + i;
                releaseDate.setText(date);
            }
        }, 2022, 03, 28);

        releaseDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    datePickerDialog.show();
                }
            }
        });

        summarize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Chengbin's favorite movie");
                builder.setIcon(R.drawable.ic_baseline_outlet_24);
                builder.setMessage("Movie Name: " + movieName.getText() + "\n" +
                        "Movie Language: " + movieLanguage.getText() + "\n" +
                        "Release Date: " + releaseDate.getText() + "\n" +
                        "Box Office: " + boxOffice.getText() + "\n");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("ChengbinCai", "The movie is confirmed");
                    }
                });
                builder.show();
            }
        });
    }
}