package com.ece655.sam.MyServerSocket.main;

import java.util.Vector;

public class GroupManager {
	
	private GroupManager(){}
	private static final  GroupManager gm = new GroupManager();
	
	public static GroupManager getGroupManager(){
		return gm;
	}
	
	Vector<Vector> vectorGroup = new Vector<Vector>();
	
	
	public void addGroup(String groupName){
		Vector<String> group2Add = new Vector<>();
		group2Add.add(groupName);
		vectorGroup.add(group2Add);
		for(int i=0; i<vectorGroup.size();i++){
			System.out.println(vectorGroup.get(i));
		}
			
	}
	public String getAllGroup(){
		delVoidGroup();
		Vector<String> allGroupName = new Vector<String>();
		for(int i=0;i<vectorGroup.size();i++){
			Vector<String> groupEntry = vectorGroup.get(i);
			allGroupName.add(groupEntry.get(0));
			System.out.println("The "+Integer.toString(i)+" group is "+groupEntry.toString());
		}
		
		
		return allGroupName.toString();
	}
	public void addUser2Group(String group, String user){
		for(int i=0; i<vectorGroup.size();i++){
			Vector<String> groupEntry = vectorGroup.get(i);
			if (groupEntry.get(0).trim().equals(group.trim())){
				groupEntry.add(user.trim());
			}
		}
		//ChatManager.getChatManager().publish("system : '"+user+"'joined in this group"+"\n");
	}
	public void delUser2Group(String group, String user){
		for(int i=0; i<vectorGroup.size();i++){
			Vector<String> groupEntry = vectorGroup.get(i);
			if (groupEntry.get(0).trim().equals(group.trim())){
				for(int l=1;l<groupEntry.size();l++){
					if(groupEntry.get(l).trim().equals(user.trim())){
						groupEntry.remove(l);
					}	
				}
			}
		}
	}
	
	public void delVoidGroup(){
		for (int i=0; i<vectorGroup.size();i++){
			Vector<String> groupEntry = vectorGroup.get(i);
			if (groupEntry.size()<2){
				vectorGroup.remove(i);
			}
		}
	}
	public void updChatManager(){
		
	}
}
