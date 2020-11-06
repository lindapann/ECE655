package com.ece655.sam.MyServerSocket.main;

import java.util.Vector;

public class ChatManager {
	
	private ChatManager(){}
	private static final ChatManager cm = new ChatManager();
	public static ChatManager getChatManager(){
		return cm;
	}
	
	Vector<ChatSocket> vectorCS = new Vector<ChatSocket>();
	Vector<String> vectorGroup = new Vector<String>();
	
	public void add(ChatSocket cs){
		vectorCS.add(cs);
		vectorGroup.add("");
	}
	public void del(ChatSocket cs){
		for (int i=0; i<vectorCS.size();i++){
			if (cs == vectorCS.get(i)){
				vectorCS.remove(i);
				vectorGroup.remove(i);
			}
		}
	}
	public void updCSGroup(ChatSocket cs, String group){
		int index_ = vectorCS.indexOf(cs);
		vectorGroup.set(index_, group.trim());
		
	}
	public void publish(ChatSocket cs,String out) {
		
		int index_ = vectorCS.indexOf(cs);
		String group = vectorGroup.get(index_).trim();
		
		for (int i=0; i<vectorGroup.size();i++){
			if(vectorGroup.get(i).trim().equals(group)){
				vectorCS.get(i).out("!@#U_MSG!@#"+out);
			}
		}
		
		
		
		
	}
	
}
