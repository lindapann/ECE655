package com.example.sam.quizqueen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.text.NumberFormat;

/**
 * Created by Sam on 15-10-21.
 */
public class Stats extends Activity {
    Button buttonToMain,buttonReset;
    EditText etCorrect,etAttempt,etPercentage;
    Spinner spinnerAllUser;
    Adapter adapterAllUser;
    String[] allUserName;
    int totalAttmp,totalScore;
    String userN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        buttonToMain = (Button)findViewById(R.id.buttonToMainMenu);
        buttonReset = (Button)findViewById(R.id.buttonResetStats);
        etCorrect = (EditText)findViewById(R.id.editTextTotalCorr);
        etAttempt = (EditText)findViewById(R.id.editTextTotalAttmp);
        etPercentage = (EditText)findViewById(R.id.editTextRate);
        spinnerAllUser = (Spinner)findViewById(R.id.spinnerAllUserStats);


        DbHelper db1=new DbHelper(this);
        allUserName=db1.getAllUsers();

        adapterAllUser = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allUserName);
        spinnerAllUser.setAdapter((SpinnerAdapter) adapterAllUser);

        spinnerAllUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                totalScore = 0;

                userN = allUserName[position];
                DbHelper db2 = new DbHelper(Stats.this);
                final String[] corrAnswString = db2.getStatsFieldForUser(userN, "CorrectAnswer");
                final int attmpTime = corrAnswString.length;
                totalAttmp = (attmpTime * 5);

                final int[] tmpCorrAnsw = new int[attmpTime];
                for (int i = 0; i < attmpTime; i++) {
                    tmpCorrAnsw[i] = Integer.parseInt(corrAnswString[i]);
                    totalScore = totalScore + tmpCorrAnsw[i];
                }
                etCorrect.setText(String.valueOf(totalScore));
                etAttempt.setText(String.valueOf(totalAttmp));
                NumberFormat percentage = NumberFormat.getPercentInstance();
                final float per = (float) totalScore / (float) totalAttmp;
                percentage.setMinimumFractionDigits(2);
                etPercentage.setText(String.valueOf(percentage.format(per)));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DbHelper db3 = new DbHelper(Stats.this);
                db3.delUserStats(userN);
                finish();
                startActivity(getIntent());
            }
        });


        buttonToMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(Stats.this, MainActivity.class);
                startActivity(myIntent);
            }
        });





    }
}
