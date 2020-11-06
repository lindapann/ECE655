package com.example.sam.quizqueen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Sam on 15-10-20.
 */
public class AddQuestion extends Activity{
    private Button buttonAddQuestion;
    EditText etQuestionTitle;
    EditText etQuestionOptA ,etQuestionOptB,etQuestionOptC,etQuestionOptD ,etQuestionTimer;
    RadioGroup rgQuestionAnswer;
    RadioButton rbOptA,rbOptB,rbOptC,rbOptD;
    String questionTitle;
    String questionOptA, questionOptB,questionOptC,questionOptD,questionTimer;
    String questionAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestion);

        buttonAddQuestion = (Button)findViewById(R.id.buttonAddQuestion);
        etQuestionTitle = (EditText)findViewById(R.id.editTextAddQuestionTitle);

        etQuestionOptA = (EditText)findViewById(R.id.editTextAddOptA);
        etQuestionOptB = (EditText)findViewById(R.id.editTextAddOptB);
        etQuestionOptC = (EditText)findViewById(R.id.editTextAddOptC);
        etQuestionOptD = (EditText)findViewById(R.id.editTextAddOptD);
        etQuestionTimer = (EditText)findViewById(R.id.editTextQuestionTimer);

        rgQuestionAnswer = (RadioGroup)findViewById(R.id.rgQuestionAnswer);
        rbOptA = (RadioButton)findViewById(R.id.radioButtonA);
        rbOptB = (RadioButton)findViewById(R.id.radioButtonB);
        rbOptC = (RadioButton)findViewById(R.id.radioButtonC);
        rbOptD = (RadioButton)findViewById(R.id.radioButtonD);



        rgQuestionAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbOptA.getId()) {
                    questionAnswer = "A";
                } else if (checkedId == rbOptB.getId()) {
                    questionAnswer = "B";
                } else if (checkedId == rbOptC.getId()) {
                    questionAnswer = "C";
                } else if (checkedId == rbOptD.getId()) {
                    questionAnswer = "D";
                } else {
                    questionAnswer = "";
                }
            }
        });



        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionTitle = etQuestionTitle.getText().toString();
                questionOptA = etQuestionOptA.getText().toString();
                questionOptB = etQuestionOptB.getText().toString();
                questionOptC = etQuestionOptC.getText().toString();
                questionOptD = etQuestionOptD.getText().toString();
                questionTimer = etQuestionTimer.getText().toString();

                DbHelper dbase = new DbHelper(AddQuestion.this);
                dbase.addNewQuestion(questionTitle,questionOptA,questionOptB,questionOptC,questionOptD,questionAnswer,questionTimer);

                Intent intent = new Intent(AddQuestion.this,QuizManagement.class);
                startActivity(intent);
            }
        });








        Button buttonMain = (Button)findViewById(R.id.buttonToMainMenu);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddQuestion.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    protected void dialogErr(String arg1,String arg2){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddQuestion.this);
        builder.setMessage(arg2);
        builder.setTitle(arg1);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.create().show();
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

