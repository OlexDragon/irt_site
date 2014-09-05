package irt.data.manufacture;

import irt.data.dao.ManufactureDAO;
import irt.data.user.UserBean;
import irt.table.OrderByService;
import irt.table.Row;
import irt.table.Table;
import irt.data.Error;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Manufacture {
	private String id;
	private String name;
	private String link;

	private OrderByService orderBy;
	private UserBean userBean;
	private Error error = new Error();
	
	public Manufacture() {
	}

	public Manufacture(String id, String name, String link) {
		setId(id!=null ? id.trim() : null);
		setName(name!=null ? name.trim() : null);
		setLink(link!=null ? link.trim() : null);
	}

	public Manufacture(ResultSet resultSet) throws SQLException {
		this((resultSet.getString("id"))
				, resultSet.getString("name")
				, resultSet.getString("link"));
	}

	public String getId() {
		return (id!=null) ? id : "";
	}

	public Manufacture setId(String id) {
		if(id!=null && id.length()==2)
			this.id = id.toUpperCase();
		else
			error.setErrorMessage("ID should have two letters"); 
		return this;
	}

	public String getName() {
		return (name!=null) ? name :"";
	}

	public Manufacture setName(String name) {
		if(name!=null && !name.isEmpty())
			this.name = name;
		else
			error.setErrorMessage("<br />Fill the \"Name\" field");
		return this;
	}

	public String getLink() {
		return (link!=null) ? link : "";
	}

	public void setLink(String link) {
		if(link!=null && link.length()>10 && link.contains("http"))
			this.link = link;
		else
			error.setErrorMessage("<br />Link \""+link+"\" is not correct");
	}

	public boolean isSet() {
		return id != null
				&& !id.isEmpty()
				&& name != null
				&& !name.isEmpty()
				&& link != null
				&& !link.isEmpty();
	}

	public String getHTML(){
		String html = "";

		if(userBean!=null && userBean.isEditing())
			html = "<form method=\"post\" action=\"/irt/manufacture-links\">" +
					"<p>"+
					"<label> ID:</label>" +
					"<input type=\"text\"" +
						" id=\"manuf_id\"" +
						" name=\"manuf_id\" size=\"2\" value=\""+getId()+"\" />"+
					" <label>Name:</label>" +
					" <input type=\"text\" id=\"manuf_name\" name=\"manuf_name\" value=\""+getName()+"\" />"+
					" <label>Link:</label>" +
					" <input type=\"text\"" +
						" id=\"manuf_link\"" +
						" name=\"manuf_link\" size=\"40\" value=\""+getLink()+"\" />"+
						" <input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Add\" />"+
						" <input type=\"submit\" name=\"bom\" id=\"bom\" value=\"get PDF\" />" +
						"</p>"+
					"</form>"+
					"<hr />";
		return html;
	}
	
	public String getTable(){
		Manufacture[] manufactures = new ManufactureDAO().getAll(orderBy);
		Table table = new Table(new String[]{"ID", "Name"}, "manufacture-links");

		for(Manufacture m:manufactures){
			Row row = new Row();
				row.add(new String[]{m.getId(),
						"<a href=\""+m.getLink()+"\" onclick=\"window.open(this.href);return false;\">"+m.getName()+"</a>"});
				table.add(row);
		}
		table.setClassName("ManufactureDAO");//for CSS

		return table!=null ? table.toString() : "";
	}

	public boolean isError(){
		return error.isError();
	}

	public String getErrorMessage(){
		return error.getErrorMessage();
	}

	@Override
	public String toString() {
		return "Manufacture [id=" + id + ", name=" + name + ", link=" + link
				+ "]";
	}

	public void setOrderBy(OrderByService orderBy) {
		this.orderBy = orderBy;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
		
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public String getOrderBy() {
		return orderBy.toString();
	}
}
