package com.example.sam.queensim;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    public TextView connectionStatus;
    Button buttonSend,btnListGroupUser,btnJoinGroup;
    String serverIp = "10.20.174.194",status="";
    Integer portNo = 22345;
    public Socket socket= null;
    String userId,groupId;
    TextView userName, groupName,tvMessage;
    EditText edMsg;
    public BufferedWriter writer = null;
    public BufferedReader reader = null;
    Integer timeout = 10000;
    String[] groupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionStatus = (TextView)findViewById(R.id.textViewConnectionStatus);
        userName = (TextView)findViewById(R.id.textViewUsername);
        groupName = (TextView)findViewById(R.id.textViewGroupName);

        btnListGroupUser = (Button)findViewById(R.id.buttonListGroupUsers);
        btnJoinGroup = (Button)findViewById(R.id.buttonJoinGroup);
        buttonSend = (Button)findViewById(R.id.buttonSend);

        edMsg = (EditText)findViewById(R.id.editTextMsg);
        tvMessage = (TextView)findViewById(R.id.textViewReceive);

        buttonSend.setTextColor(0xffffffff);
        buttonSend.setClickable(false);
        btnListGroupUser.setTextColor(0xffffffff);
        btnListGroupUser.setClickable(false);
        btnJoinGroup.setTextColor(0xffffffff);
        btnJoinGroup.setClickable(false);

        inputUsername();

        connectSocketServer();

    }

    public void connectSocketServer() {
        AsyncTask<Void,String,Void> read = new AsyncTask<Void, String, Void>() {
            //Socket socket;
            @Override
            public Void doInBackground(Void... params) {
            //connect socket server and monitor the status
                try {
                    socket = new Socket(serverIp, portNo);
                    publishProgress("UP","");
                    String socketStats = "UP";
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String line;
                    while ((line = reader.readLine())!= null){
                        publishProgress("UP",line+"\n");
                    }
                    while (socketStats.equals("UP")) {
                        try {
                            Thread.sleep(timeout);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket.sendUrgentData(0xFF);
                        } catch (IOException e) {
                            socket.close();
                            publishProgress("DOWN");
                            socketStats = "DOWN";
                            connectSocketServer();
                        }
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    publishProgress("DOWN");
                    connectSocketServer();
                } catch (IOException e) {
                    e.printStackTrace();
                    publishProgress("DOWN");
                    connectSocketServer();
                }
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                //update UI Connection icon
                connectionStatus.setText(values[0]);
                if (values[0].equals("UP")){
                    connectionStatus.setBackgroundColor(0xFF01FF15);

                    btnJoinGroup.setClickable(true);
                    btnJoinGroup.setTextColor(0xff000000);
                    buttonSend.setTextColor(0xffffffff);
                    buttonSend.setClickable(false);

                    //display chat message
                    if (!values[1].equals("")){
                        String msgHead = values[1].substring(0,11);
                        switch (msgHead){
                            case "!@#U_MSG!@#" :
                                tvMessage.append(values[1].substring(11));
                                break;
                            case "!@#L_GRP!@#" :
                                //Toast.makeText(MainActivity.this, "Groups are: " + values[1], Toast.LENGTH_LONG).show();
                                listGroupsDialog(values[1].substring(11).replace("[","").replace("]","").trim());

                        }
                    }

                    //region Group Operations JoinGroup Button
                    btnJoinGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listGroup();
                        }
                    });
                    //endregion


                    //after join a group
                    if (isInAGroup()){

                        //enable message sending
                        buttonSend.setTextColor(0xff000000);
                        buttonSend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendUserMessage(edMsg.getText().toString());
                            }
                        });
                        //endregion

                        //region function: list users in this group
                        btnListGroupUser.setTextColor(0xff000000);
                        btnListGroupUser.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                listGroupUser();
                            }
                        });
                        //endregion

                    }


                }else {
                    connectionStatus.setBackgroundColor(0xffff0d07);
                    buttonSend.setClickable(false);
                    btnListGroupUser.setClickable(false);
                    btnJoinGroup.setClickable(false);
                    buttonSend.setTextColor(0xffffffff);
                    btnListGroupUser.setTextColor(0xffffffff);
                    tvMessage.setText("");
                    groupName.setText("*Not in a group*");
                }

                super.onProgressUpdate(values);
            }
        };

        read.execute();
    }
    public void listGroup(){

        try {
            writer.write("!@#L_GRP!@#list"+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addGroup(String groupName){
        try {
            writer.write("!@#A_GRP!@#"+groupName.trim()+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addAUser2Group(groupName,userId);
    }
    public void addAUser2Group(String group,String user){
        try {
            writer.write("!@#U_GRP!@#"+group+","+user+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void delAUser2Group(String group,String user){
        try {
            writer.write("!@#D_GRP!@#"+group+","+user+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inputUsername(){
        final String[] username = new String[1];
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);

        final EditText userInput = (EditText)dialogView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Please input your name:")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                username[0] = userInput.getText().toString();
                                userName.setText(username[0]);
                                userId = username[0];
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    public void listGroupsDialog(String groups){
        final String[] groupNames = groups.split(",");
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);

        final EditText userInput = (EditText)dialogView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(true)
                .setTitle("Please choose a group")
                .setItems(groupNames,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!groupName.getText().toString().equals(groupNames[which])) {
                                    delAUser2Group(groupId, userId);
                                    groupId = groupNames[which].trim();
                                    groupName.setText(groupId);
                                    tvMessage.setText("");
                                    addAUser2Group(groupId, userId);
                                    //buttonSend.setClickable(true);
                                    //buttonSend.setTextColor(0xff000000);
                                    btnListGroupUser.setClickable(true);
                                    btnListGroupUser.setTextColor(0xff000000);
                                }
                            }
                        })

                .setPositiveButton("Create a group",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String groupToAdd = userInput.getText().toString();
                                if (!groupToAdd.equals("")) {
                                    delAUser2Group(groupId, userId);
                                    addGroup(groupToAdd);
                                    groupId = groupToAdd;
                                    groupName.setText(groupId);
                                    tvMessage.setText("");
                                    //buttonSend.setClickable(true);
                                    buttonSend.setTextColor(0xff000000);
                                    btnListGroupUser.setClickable(true);
                                    btnListGroupUser.setTextColor(0xff000000);
                                } else {
                                    Toast.makeText(MainActivity.this, "Please input group name", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void sendUserMessage(String m){
        if (!m.equals("")){
            String userMsg = "!@#U_MSG!@#" + userName.getText().toString() + " : " + m+"\n";
            try {
                writer.write(userMsg);
                writer.flush();
                edMsg.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void listGroupUser(){
        try {
            writer.write("!@#L_USR!@#"+groupName.getText().toString().trim()+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isInAGroup(){
        boolean inGroup;
        if (groupName.getText().toString().equals("*Not in a group*")){
            inGroup = false;
        }else {
            inGroup= true;
        }
        return inGroup;
    }
}
