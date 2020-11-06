package com.example.sam.quizqueen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Sam on 15-10-21.
 */
public class QuizResult extends Activity {
    RatingBar ratingBar;
    TextView txtResult;
    Button buttonRetake,buttonToMain;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar1);
        txtResult = (TextView)findViewById(R.id.textResult);
        buttonRetake = (Button)findViewById(R.id.buttonRetake);
        buttonToMain = (Button)findViewById(R.id.buttonToMain);

        userName = getIntent().getStringExtra("userName");


        String resultCorr = getIntent().getStringExtra("correctAnswer");
        txtResult.setText(resultCorr);
        ratingBar.setNumStars(5);
        ratingBar.setRating(Integer.parseInt(resultCorr));

        buttonRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResult.this, QuizTake.class);
                intent.putExtra("userName",userName);
                startActivity(intent);

            }
        });

        buttonToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResult.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }
}
