package irt.data;

import irt.work.TextWorker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
	public static final int REVISION	= 0;
	public static final int BY_CLASS_ID = 1;
	public static final int OPTION		= 2;	

	private Map<Integer,String> idsMap;
	private List<String> ids;
	private List<String> descriptions;
	
	public Menu(ResultSet resultSet) throws SQLException{
		
		ids = new ArrayList<>();
		descriptions = new ArrayList<>();
		
		while(resultSet.next()){
			ids.add(resultSet.getString("id"));
			descriptions.add(resultSet.getString("description"));
		}
	}

	public Menu(ResultSet rs, int choice) throws SQLException {
		this(rs);

		String str = "";
		switch(choice){
		case REVISION:
			str = "R";
		case OPTION:
			str += String.format("%2s", descriptions.size()+1).replace(' ', '0');
			ids.add(str);
			descriptions.add(str);
			break;
		case BY_CLASS_ID:
			for(int i=0; i<descriptions.size(); i++)
				descriptions.set(i, TextWorker.getPartNumber(descriptions.get(i), 3, 9, 13, 16));
		}
	}

	public Menu() {
		idsMap = new HashMap<>();
	}

	public int size(){
		return ids.size();
	}
	
	public String[] getKeys(){
		return (String[]) ids.toArray(new String[ids.size()]);
	}

	public boolean containsKey(String key) {
		return ids.contains(key);
	}

	public String[] getDescriptions(){
		return (String[]) descriptions.toArray(new String[descriptions.size()]);
	}

	public String getDescription(int classId) {
		String description = "Description not faund";
		String str = ""+classId;
		if(idsMap!=null)
			description = idsMap.get(classId);
		else
			for(int i=0; i<ids.size(); i++)
				if(ids.get(i).equals(str)){
					description = descriptions.get(i);
					break;
				}
		return description;
	}

	public void add(Menu menu) {
		for(String s:menu.getKeys())
			ids.add(s);
		for(String s:menu.getDescriptions())
			descriptions.add(s);
	}

	public void add(int classId, String description) {
		idsMap.put(classId, description);
	}

	public int getIntKey(String description) {
		int key = -1;
		if(description!=null)
			for(int i=0; i<descriptions.size(); i++)
				if(descriptions.get(i).equals(description) && !(description = ids.get(i).replaceAll("[^\\d]", "")).isEmpty()){
					key = Integer.parseInt(description);
					break;
				}
		return key;
	}

	@Override
	public String toString() {
		return "Menu [idsMap=" + idsMap + ", ids=" + ids + ", descriptions="
				+ descriptions + "]";
	}

	public void add(String id, String description) {
		ids.add(id);
		descriptions.add(description);
	}
}
