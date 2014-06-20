package irt.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Link {

	private int id;
	private String link;
	
	public Link() {
		link = "";
	}

	public Link(int id, String link) {
		this.id = id;
		this.link = link;
	}

	public Link(ResultSet resultSet) throws SQLException {
		id = resultSet.getInt("id");
		link = resultSet.getString("link");
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLink() {
		return (link!=null)
				? link
						: "";
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public String getHTML() {
		return (getLink()!=null && !getLink().isEmpty())
				? "<a href=\""+getLink()+"\" onclick=\"window.open(this.href);return false;\" >detail</a>"
				: "";
	}
	
	public boolean isSet(){
		return !getLink().isEmpty();
	}
}
