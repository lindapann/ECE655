package com.example.sam.quizqueen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Sam on 15-10-18.
 */
public class AddUser extends Activity{
    private Button buttonAddNewUser;
    EditText editTextUserName ;
    EditText editTextPassword ;
    EditText editTextConfirmPassword ;
    String userName;
    String password ;
    String confirmPassword ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);


        Button buttonMain = (Button)findViewById(R.id.buttonToMainMenu);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddUser.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        buttonAddNewUser = (Button)findViewById(R.id.buttonAddNewUser);
        editTextUserName = (EditText)findViewById(R.id.editTextUserName);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);



        buttonAddNewUser.setTextColor(0xffe7e7e7);

        buttonAddNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userName = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();
                confirmPassword = editTextConfirmPassword.getText().toString();

                if (userName.isEmpty()||userName.startsWith(" ") ) {
                    dialogErr("Error","Please input a valid Username.\n Username can not begin with space");
                }
                else if (password.isEmpty()||password.length()<6 ){
                    dialogErr("Error","Please input a valid password.\n Password must be 6 letters or more");
                }
                else if (!confirmPassword.equals(password) ){
                    dialogErr("Error","Password missmatch");
                }

                else {
                    DbHelper dbase = new DbHelper(AddUser.this);
                    dbase.addNewUser(userName,password);
                    dialogErr("User added successfully","");
                }

            }
        });


    }
    protected void dialogErr(String arg1,String arg2){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddUser.this);
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