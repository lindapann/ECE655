package com.example.sam.quizqueen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sam on 15-10-16.
 */
public class QuizManagement extends Activity {
    private Spinner spinnerAllQuestion;
    private Adapter adapterAllQuestion;
    private String[] allQuestion, allQuestionID, questionField;
    private TextView questionTitle, questionOptA,questionOptB,questionOptC,questionOptD,questionTimer;
    private RadioGroup rgAnswer;
    private RadioButton radioButtonA,radioButtonB,radioButtonC,radioButtonD;
    private Button buttonDel, buttonUpd;
    private String updAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizmanagement);

        spinnerAllQuestion = (Spinner) findViewById(R.id.spinnerAllQuestion);
        questionTitle = (EditText)findViewById(R.id.editTextQuestionTitle);
        questionOptA = (EditText)findViewById(R.id.editTextOptA);
        questionOptB = (EditText)findViewById(R.id.editTextOptB);
        questionOptC = (EditText)findViewById(R.id.editTextOptC);
        questionOptD = (EditText)findViewById(R.id.editTextOptD);
        questionTimer = (EditText)findViewById(R.id.editTextTimer);

        rgAnswer = (RadioGroup)findViewById(R.id.rgAnswer);
        radioButtonA = (RadioButton)findViewById(R.id.rbA);
        radioButtonB = (RadioButton)findViewById(R.id.rbB);
        radioButtonC = (RadioButton)findViewById(R.id.rbC);
        radioButtonD = (RadioButton)findViewById(R.id.rbD);


        DbHelper db = new DbHelper(this);
        allQuestion = db.getAllQuestion();
        int questionAmount = allQuestion.length;
        allQuestionID = db.getAllQuestionId();

        adapterAllQuestion = new ArrayAdapter<String >(this, android.R.layout.simple_spinner_item, allQuestion );
        spinnerAllQuestion.setAdapter((SpinnerAdapter) adapterAllQuestion);




        spinnerAllQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                final String queryId = allQuestionID[position];
                String[] questionField = new String[8];
                DbHelper db1 = new DbHelper(QuizManagement.this);
                questionField = db1.getQuestionById(queryId);

                questionTitle.setText(questionField[1]);
                questionOptA.setText(questionField[2]);
                questionOptB.setText(questionField[3]);
                questionOptC.setText(questionField[4]);
                questionOptD.setText(questionField[5]);
                questionTimer.setText(questionField[7]);

                if (questionField[6].equals("A") == true) {
                    radioButtonA.setChecked(true);
                } else if (questionField[6].equals("B") == true) {
                    radioButtonB.setChecked(true);
                } else if (questionField[6].equals("C") == true) {
                    radioButtonC.setChecked(true);
                } else if (questionField[6].equals("D") == true) {
                    radioButtonD.setChecked(true);
                }

                rgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId){
                        if (checkedId == radioButtonA.getId()) {
                            updAnswer = "A";
                        } else if (checkedId == radioButtonB.getId()) {
                            updAnswer = "B";
                        } else if (checkedId == radioButtonC.getId()) {
                            updAnswer = "C";
                        } else if (checkedId == radioButtonD.getId()) {
                            updAnswer = "D";
                        }
                    }
                });
                Button buttonUpd = (Button)findViewById(R.id.buttonUpdQuestion);
                buttonUpd.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        String newQuestionTitle = questionTitle.getText().toString();
                        String newOptA = questionOptA.getText().toString();
                        String newOptB = questionOptB.getText().toString();
                        String newOptC = questionOptC.getText().toString();
                        String newOptD = questionOptD.getText().toString();
                        String newtimer = questionTimer.getText().toString();

                        DbHelper dbUpd = new DbHelper(QuizManagement.this);
                        dbUpd.updateQuestion(queryId,newQuestionTitle,newOptA,newOptB,newOptC,newOptD,updAnswer,newtimer);
                        finish();
                        startActivity(getIntent());

                    }
                });

                Button buttonDel = (Button)findViewById(R.id.buttonDelQuestion);
                buttonDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DbHelper dbDel = new DbHelper(QuizManagement.this);
                        dbDel.deleteDbById("question",queryId);
                        finish();
                        startActivity(getIntent());

                    }
                });


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        Button buttonAddNewUser = (Button)findViewById(R.id.buttonAddUser);
        buttonAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(QuizManagement.this, AddQuestion.class);
                startActivity(myIntent);
            }
        });
        Button buttonMain = (Button)findViewById(R.id.buttonToMainMenu);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(QuizManagement.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

    }




    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
