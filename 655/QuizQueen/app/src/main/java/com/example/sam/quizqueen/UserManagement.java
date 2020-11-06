package com.example.sam.quizqueen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * Created by Sam on 15-10-16.
 */
public class UserManagement extends Activity {

    private Spinner spinnerAllUser;
    private Adapter adapterAllUser;
    private String[] allUserName, allUserPassword,userDatabaseID;
    private ListView listViewAllUser;
    private EditText etUserName,etPassword,etConfirmPassword;
    private String newUserName,newPassword;
    private Button userUpdate,userDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanagement);



        spinnerAllUser = (Spinner)findViewById(R.id.spinnerAllUser);
        spinnerAllUser.setVisibility(View.VISIBLE);
        DbHelper db1=new DbHelper(this);
        allUserName=db1.getAllUsers();
        allUserPassword = db1.getAllUserPassword();
        userDatabaseID = db1.getDatabaseID("users");
        //String[] listAllUserName = "Select a user to go" + allUserName;
        int mount = allUserName.length+1;
        final String[] listAllUserName = new String[mount];
        final String[] listAllUserPassword = new String[mount-1];
        listAllUserName[0] = "Select a User to go";
        for (int i = 1;i <= mount-1;i++){
            listAllUserName[i]=allUserName[i-1];
        }



        adapterAllUser = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAllUserName);
        spinnerAllUser.setAdapter((SpinnerAdapter) adapterAllUser);
        etUserName = (EditText)findViewById(R.id.editTextUpdateUserName);
        etPassword = (EditText)findViewById(R.id.editTextUpdatePassword);
        etConfirmPassword = (EditText)findViewById(R.id.editTextUpdateConfirmPassword);

        spinnerAllUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Button userUpdate = (Button) findViewById(R.id.buttonUserUpdate);
                Button userDelete = (Button) findViewById(R.id.buttonAddQuestion);

                if (position == 0) {
                    etUserName.setFocusable(false);
                    etUserName.setFocusableInTouchMode(false);
                    etUserName.setClickable(false);

                    etPassword.setFocusable(false);
                    etPassword.setFocusableInTouchMode(false);
                    etPassword.setClickable(false);

                    etConfirmPassword.setFocusable(false);
                    etConfirmPassword.setFocusableInTouchMode(false);
                    etConfirmPassword.setClickable(false);

                    etUserName.setText("");
                    etPassword.setText("");
                    etConfirmPassword.setText("");

                    userUpdate.setTextColor(0xffe7e7e7);
                    userUpdate.setClickable(false);
                    userDelete.setTextColor(0xffe7e7e7);
                    userDelete.setClickable(false);

                } else {//display current user names and their password

                    etPassword.setFocusable(true);
                    etPassword.setFocusableInTouchMode(true);
                    etPassword.setClickable(true);

                    etConfirmPassword.setFocusable(true);
                    etConfirmPassword.setFocusableInTouchMode(true);
                    etConfirmPassword.setClickable(true);

                    etUserName.setText(listAllUserName[position]);
                    etPassword.setText(allUserPassword[position - 1]);
                    etConfirmPassword.setText(allUserPassword[position - 1]);

                    userUpdate.setTextColor(0xff000000);
                    userUpdate.setClickable(true);
                    userDelete.setTextColor(0xff000000);
                    userDelete.setClickable(true);

                    //update user password
                    etUserName.addTextChangedListener(textWatcherUserName);
                    userUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DbHelper db = new DbHelper(UserManagement.this);
                            newPassword = etPassword.getText().toString();
                            db.updateUserInfo(etUserName.getText().toString(), newPassword);
                            dialogUserManagement("User update successfully", "");


                        }
                    });

                    //delete user
                    userDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DbHelper db = new DbHelper(UserManagement.this);
                            db.deleteDbById("users", userDatabaseID[position - 1]);
                            finish();
                            startActivity(getIntent());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button buttonMain = (Button)findViewById(R.id.buttonToMainMenu);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(UserManagement.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        Button buttonAddNewUser = (Button)findViewById(R.id.buttonAddUser);
        buttonAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(UserManagement.this, AddUser.class);
                startActivity(myIntent);
            }
        });
    }
    TextWatcher textWatcherUserName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {




            newUserName = etUserName.getText().toString();
            newPassword = etPassword.getText().toString();

        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };
    protected void dialogUserManagement(String title,String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserManagement.this);
        builder.setMessage(content);
        builder.setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                startActivity(getIntent());

            }
        });

        builder.create().show();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
