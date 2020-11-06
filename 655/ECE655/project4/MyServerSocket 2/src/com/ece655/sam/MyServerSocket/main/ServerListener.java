package com.ece655.sam.MyServerSocket.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerListener extends Thread {
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(22345);
			while (true) {
				Socket socket = serverSocket.accept();
				//JOptionPane.showMessageDialog(null, "Socket link setup");
				System.out.println("Socket link setup");
				
				ChatSocket cs =new ChatSocket(socket);
				cs.start();
				ChatManager.getChatManager().add(cs);
				
				
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
