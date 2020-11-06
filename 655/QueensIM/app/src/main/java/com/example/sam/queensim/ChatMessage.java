package com.example.sam.queensim;

/**
 * Created by Sam on 2015-11-13.
 */
public class ChatMessage {

    public void sendMessage(String msg){
        String msgtype = msg.substring(0,11);
        System.out.println(msgtype);
        switch (msgtype){
            case ("!@#U_MSG!@#"):{
                new MainActivity().sendMsgOut(msg);
                break;
            }
        };

    }


}
