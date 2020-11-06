package com.example.sam.quizqueen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sam on 15-10-21.
 */
public class QuizTake extends Activity {

    RadioGroup rgQuizTakeAnswer;
    RadioButton rbQuizTakeOptA, rbQuizTakeOptB, rbQuizTakeOptC, rbQuizTakeOptD;
    Button buttonNextQuestion;
    TextView textViewQuestionTitle,textViewCurrentRate;
    String[] allQuestionId,questionIdForUser,currentQuestion,userAnswer,rightAnswer;
    int questionAmount, currentNum=0,correctAnswered,rightNum = 0;;
    String userName;

    private TextView time;
    private int totalTime;
    private int i1;
    private Timer timer = null;
    private TimerTask task = null;

    private Animation shakeAnimation; // animation for incorrect guess

    TextView incorrect;
    TextView correct;
    TextView timesout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        initView();
        startTime();
        correctAnswered = 0;
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");

        userAnswer = new String[5];
        rightAnswer = new String[5];

        rbQuizTakeOptA = (RadioButton) findViewById(R.id.radio0);
        rbQuizTakeOptB = (RadioButton) findViewById(R.id.radio1);
        rbQuizTakeOptC = (RadioButton) findViewById(R.id.radio2);
        rbQuizTakeOptD = (RadioButton) findViewById(R.id.radio3);
        buttonNextQuestion = (Button) findViewById(R.id.buttonNextQuestion);
        rgQuizTakeAnswer = (RadioGroup) findViewById(R.id.radioGroupQuestionTake);
        textViewCurrentRate =(TextView)findViewById(R.id.textViewCurrentRate);

        shakeAnimation =
                AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3); // animation repeats 3 times

        /*rbQuizTakeOptA.setChecked(false);
        rbQuizTakeOptB.setChecked(false);
        rbQuizTakeOptC.setChecked(false);
        rbQuizTakeOptD.setChecked(false);*/

        textViewQuestionTitle = (TextView) findViewById(R.id.tvQuestionTitle);


        DbHelper db = new DbHelper(this);
        allQuestionId = db.getAllQuestionId();
        questionAmount = allQuestionId.length;
        int[] randNum = randomCommon(0,questionAmount,5);

        questionIdForUser = new String[5];
        questionIdForUser[0] = allQuestionId[randNum[0]];
        questionIdForUser[1] = allQuestionId[randNum[1]];
        questionIdForUser[2] = allQuestionId[randNum[2]];
        questionIdForUser[3] = allQuestionId[randNum[3]];
        questionIdForUser[4] = allQuestionId[randNum[4]];

        currentQuestion = new String[7];
        currentQuestion = db.getQuestionById(questionIdForUser[0]);

        rightAnswer[0] = currentQuestion[6];
        textViewQuestionTitle.setText(currentQuestion[1] + "     Right Answer is " +rightAnswer[0]);
        rbQuizTakeOptA.setText(currentQuestion[2]);
        rbQuizTakeOptB.setText(currentQuestion[3]);
        rbQuizTakeOptC.setText(currentQuestion[4]);
        rbQuizTakeOptD.setText(currentQuestion[5]);

        totalTime = Integer.valueOf(currentQuestion[7]);



        userAnswer[0] = " ";
        rgQuizTakeAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (i1>0){
                    if (rbQuizTakeOptA.isChecked()) {
                        userAnswer[0] = "A";
                    } else if (rbQuizTakeOptB.isChecked()) {
                        userAnswer[0] = "B";
                    } else if (rbQuizTakeOptC.isChecked()) {
                        userAnswer[0] = "C";
                    } else if (rbQuizTakeOptD.isChecked()) {
                        userAnswer[0] = "D";
                    }
                }else userAnswer[0]="";

            }

        });

        textViewCurrentRate.setText("Current: 0/0");

        buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                rgQuizTakeAnswer.clearCheck();
                if(userAnswer[currentNum].equals(rightAnswer[currentNum])){
                    rightNum++;
                }
                textViewCurrentRate.setText("Current: "+String.valueOf(rightNum)+"/"+String.valueOf(currentNum+1));

                if(rightAnswer[currentNum].equals(userAnswer[currentNum])&&i1>0)

                {

                    correct.setText("correct");
                    correct.setTextSize(30);
                    correct.setTextColor(0xff0f8a06);
                }
                else if(i1==0) {
                    rgQuizTakeAnswer.setClickable(false);
                    if (rbQuizTakeOptA.getText().equals(rightAnswer[currentNum])) {
                        rbQuizTakeOptA.startAnimation(shakeAnimation);
                        rbQuizTakeOptA.setTextColor(0xff0f8a06);
                    } else if (rbQuizTakeOptB.getText().equals(rightAnswer[currentNum])) {
                        rbQuizTakeOptB.startAnimation(shakeAnimation);
                        rbQuizTakeOptB.setTextColor(0xff0f8a06);
                    } else if (rbQuizTakeOptC.getText().equals(rightAnswer[currentNum])) {
                        rbQuizTakeOptC.startAnimation(shakeAnimation);
                        rbQuizTakeOptC.setTextColor(0xff0f8a06);
                    } else if (rbQuizTakeOptD.getText().equals(rightAnswer[currentNum])) {
                        rbQuizTakeOptD.startAnimation(shakeAnimation);
                        rbQuizTakeOptD.setTextColor(0xff0f8a06);
                    }
                    timesout.setText("time out");
                    timesout.setTextColor(0xffd8cd17);
                    timesout.setTextSize(30);


                }
                else

                {

                    if (rbQuizTakeOptA.isChecked()) {
                        rbQuizTakeOptA.setTextColor(0xffff0000);

                    } else if (rbQuizTakeOptB.isChecked()) {
                        rbQuizTakeOptB.setTextColor(0xffff0000);

                    } else if (rbQuizTakeOptC.isChecked()) {
                        rbQuizTakeOptC.setTextColor(0xffff0000);

                    } else if (rbQuizTakeOptD.isChecked()) {
                        rbQuizTakeOptD.setTextColor(0xffff0000);

                    }


                    if (rbQuizTakeOptA.getText().equals(rightAnswer[currentNum])) {
                        rbQuizTakeOptA.startAnimation(shakeAnimation);
                        rbQuizTakeOptA.setTextColor(0xff0f8a06);
                    } else if (rbQuizTakeOptB.getText().equals(rightAnswer[currentNum])) {
                        rbQuizTakeOptB.startAnimation(shakeAnimation);
                        rbQuizTakeOptB.setTextColor(0xff0f8a06);
                    } else if (rbQuizTakeOptC.getText().equals(rightAnswer[currentNum])) {
                        rbQuizTakeOptC.startAnimation(shakeAnimation);
                        rbQuizTakeOptC.setTextColor(0xff0f8a06);
                    } else if (rbQuizTakeOptD.getText().equals(rightAnswer[currentNum])) {
                        rbQuizTakeOptD.startAnimation(shakeAnimation);
                        rbQuizTakeOptD.setTextColor(0xff0f8a06);
                    }

                    incorrect.setText("incorrect");
                    incorrect.setTextSize(30);
                    incorrect.setTextColor(0xffff0000);
                }




                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                if (currentNum <4) {
                    currentNum = currentNum + 1;

                    DbHelper db1 = new DbHelper(QuizTake.this);
                    currentQuestion = db1.getQuestionById(questionIdForUser[currentNum]);
                    rightAnswer[currentNum] = currentQuestion[6];
                    textViewQuestionTitle.setText(currentQuestion[1] + "     Right Answer is " + rightAnswer[currentNum]);
                    rbQuizTakeOptA.setText(currentQuestion[2]);
                    rbQuizTakeOptB.setText(currentQuestion[3]);
                    rbQuizTakeOptC.setText(currentQuestion[4]);
                    rbQuizTakeOptD.setText(currentQuestion[5]);
                    rbQuizTakeOptA.setTextColor(0xff000000);
                    rbQuizTakeOptB.setTextColor(0xff000000);
                    rbQuizTakeOptC.setTextColor(0xff000000);
                    rbQuizTakeOptD.setTextColor(0xff000000);
                    correct.setText("");
                    incorrect.setText("");
                    timesout.setText("");

                    rgQuizTakeAnswer.clearCheck();
                    totalTime = Integer.valueOf(currentQuestion[7]);
                    userAnswer[currentNum] = " ";
                    rgQuizTakeAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == rbQuizTakeOptA.getId()) {
                                userAnswer[currentNum] = "A";
                            } else if (checkedId == rbQuizTakeOptB.getId()) {
                                userAnswer[currentNum] = "B";
                            } else if (checkedId == rbQuizTakeOptC.getId()) {
                                userAnswer[currentNum] = "C";
                            } else if (checkedId == rbQuizTakeOptD.getId()) {
                                userAnswer[currentNum] = "D";
                            }

                        }
                    });
                    if(currentNum == 4){
                        buttonNextQuestion.setText("Finish");
                    }
                }
                else {

                    for(int i =0; i<5;i++){
                        if (userAnswer[i].equals(rightAnswer[i])){
                            correctAnswered++;
                        }
                    }
                    DbHelper db2 = new DbHelper(QuizTake.this);
                    db2.insertStats(userName, correctAnswered, questionIdForUser[0], questionIdForUser[1], questionIdForUser[2], questionIdForUser[3],questionIdForUser[4]);


                    Intent intent = new Intent(QuizTake.this,QuizResult.class);
                    intent.putExtra("correctAnswer",Integer.toString(correctAnswered));
                    intent.putExtra("userName",userName);
                    startActivity(intent);

                }
                       }
                },1000);

            }

        });


    }
    private void initView(){
        time = (TextView) findViewById(R.id.time);
        incorrect = (TextView) findViewById(R.id.incorrect);
        correct = (TextView) findViewById(R.id.correct);
        timesout = (TextView) findViewById(R.id.timesout);
    }
    //Countdown Timer
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            time.setText("seconds remaining: " +msg.arg1+"");
            time.setTextColor(0xff000000);
            time.setTextSize(20);
            startTime();
        };
    };

    public void startTime(){
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (totalTime>0){
                    totalTime--;
                    Message  message = mHandler.obtainMessage();
                    message.arg1 = totalTime;
                    mHandler.sendMessage(message);


                }else{
                    Message message = mHandler.obtainMessage();
                    message.arg1 = totalTime;
                    mHandler.sendMessage(message);
                }
                i1 = totalTime;
            }
        };

        timer.schedule(task, 1000);
    }



    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }
}