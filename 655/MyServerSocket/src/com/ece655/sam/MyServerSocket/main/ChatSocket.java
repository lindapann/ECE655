package com.ece655.sam.MyServerSocket.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatSocket extends Thread {
	Socket socket;
	public ChatSocket(Socket s){
		this.socket = s;
	}
	public void out(String out){
		try {
			socket.getOutputStream().write(out.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				//ChatManager.getChatManager().publish(this, line+"\n");
				System.out.println(line);
				String msgHead =line.substring(0,11);
				
				switch (msgHead){
				case "!@#U_MSG!@#":
					ChatManager.getChatManager().publish(this, line.substring(11)+"\n");
					break;
				case "!@#A_GRP!@#":
					GroupManager.getGroupManager().addGroup(line.substring(11).trim());
					break;
				case "!@#L_GRP!@#":
					String groupNames = GroupManager.getGroupManager().getAllGroup();
					out("!@#L_GRP!@#"+groupNames+"\n");
					System.out.println("!@#L_GRP!@#"+groupNames);
					break;
				case "!@#U_GRP!@#":
					String[] toAdd = line.substring(11).split(",");
					System.out.println("Add User:"+toAdd[1]+" to Group:"+toAdd[0]);
					GroupManager.getGroupManager().addUser2Group(toAdd[0], toAdd[1]);
					ChatManager.getChatManager().updCSGroup(this, toAdd[0]);
					
					ChatManager.getChatManager().publish(this,"*****system : '"+toAdd[1]+"' has joined in group '"+toAdd[0]+"'*****\n");
					
					break;
				case "!@#D_GRP!@#":
					String[] toDel = line.substring(11).split(",");
					System.out.println("Del User:"+toDel[1]+" from Group:"+toDel[0]);
					
					ChatManager.getChatManager().publish(this,"*****system : '"+toDel[1]+"' has left group '"+toDel[0]+"'*****\n");
				
					GroupManager.getGroupManager().delUser2Group(toDel[0], toDel[1]);
					
					break;
				}
				
			}
			br.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
