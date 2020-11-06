package com.example.sam.quizqueen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {


    public static final String startForQM = "QM starts here", startForQT = "QT starts here";
    private static final String[] operationQM ={startForQM,"User Management","Quiz Management","Statistics"};
    private Spinner spinnerQM, spinnerQT;
    private TextView textView;
    private ArrayAdapter<String> adapterQM, adapterQT;
    public String[] userName;
    public static SQLiteDatabase db;
    public Button buttonGoQM;
    public String userNameQT;
    Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerQM = (Spinner)findViewById(R.id.spinnerQuizMaster);

        adapterQM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, operationQM);
        adapterQM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerQM.setAdapter(adapterQM);
        buttonGoQM = (Button)findViewById(R.id.buttonSpinnerQM);

        spinnerQM.setVisibility(View.VISIBLE);

        spinnerQM.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this,"第"+(position +1) + "项被选择", Toast.LENGTH_LONG).show();

                if (position == 0) {
                    buttonGoQM.setTextColor(0xffe7e7e7);
                    buttonGoQM.setClickable(false);
                }
                if (position ==1){
                    buttonGoQM.setTextColor(0xff000000);
                    buttonGoQM.setClickable(true);
                    buttonGoQM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialogLogin("Login as QM",UserManagement.class);
                        }
                    });
                }
                else if (position == 2){
                    buttonGoQM.setTextColor(0xff000000);
                    buttonGoQM.setClickable(true);
                    buttonGoQM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialogLogin("Login as QM", QuizManagement.class);
                        }
                    });
                }
                else if (position == 3){
                    buttonGoQM.setTextColor(0xff000000);
                    buttonGoQM.setClickable(true);
                    buttonGoQM.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialogLogin("Login as QM", Stats.class);
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }

        );


        //display spinner for QT
        DbHelper db1=new DbHelper(this);
        userName=db1.getAllUsers();

        spinnerQT = (Spinner)findViewById(R.id.spinnerQuizTaker);
        adapterQT = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userName);
        adapterQT.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerQT.setAdapter(adapterQT);

        spinnerQT.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                userNameQT = userName[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button buttonGoQt = (Button)findViewById(R.id.buttonGoQt);
        buttonGoQt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent goForQT = new Intent(MainActivity.this,QuizTake.class);
                goForQT.putExtra("userName", userNameQT);
                startActivity(goForQT);
            }

        });
    }

    protected void dialogLogin(String title, Class page){
        final LayoutInflater loginLayout = LayoutInflater.from(MainActivity.this);
        final View v1 = loginLayout.inflate(R.layout.login_dialog, null);
        final AlertDialog.Builder login = new AlertDialog.Builder(MainActivity.this);
        //final String pg = page;
        final Class c = page;
        //login.setContentView(R.layout.login_dialog);
        login.setTitle(title);
        login.setView(v1);

        login.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edUsername = (EditText)(v1.findViewById(R.id.txtUsername));
                EditText edPassword = (EditText)v1.findViewById(R.id.txtPassword);

                if (edUsername.getText().toString().equals("root")  && edPassword.getText().toString().equals("root123")){
                    Toast.makeText(getApplicationContext(),"Welcome QuestMaster ",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Intent goForQM = new Intent(MainActivity.this,c);

                    startActivity(goForQM);
                }
                else {

                    Toast.makeText(getApplicationContext(),"Incorrect Username or Password ",Toast.LENGTH_SHORT).show();
                }


            }
        });
        login.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        login.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
