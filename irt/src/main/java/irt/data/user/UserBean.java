package irt.data.user;

import irt.work.TextWorker;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

public class UserBean {
	
    public static final String E_MAIL		= "e_mail";
    public static final String EXTENSION	= "extension";
    public static final String PERMISSION	= "permission";
    public static final String LAST_NAME	= "lastName";
    public static final String FIRST_NAME	= "firstName";
    public static final String PASSWORD		= "password";
    public static final String USERNAME		= "username";

	public static final int WORK_ORDER		= 512;
	public static final int DEVICE_TYPE_UPDATE=1024;
	public static final int USER_EDIT		= 2048;
    public static final int SCHEMATIC_LETTER= 4096;
    public static final int ALT_PART_NUMBER	= 8192;
    public static final int EDIT_COST		= 16384;
    public static final int DEVICE_TYPE		= 32768;
    public static final int SCHEMATIC_PART	= 65536;
    public static final int STOCK			= 131072;
    public static final int EDIT_COMPANIES	= 262144;
    public static final int USER			= 524288;
	public static final int ADMIN			= 1048576;
	public static final int EDITING			= 2097152;
	public static final int SELLERS			= 4194304;
	public static final int DATABASE		= 8388608;

	private int	id;
	private String username;
    private String password;
    private String firstName;
    private String lastName;
    private int permission;
    private String extension;
    private String eMail;
	private boolean needUpdate;

    public UserBean() {
    }

	public UserBean(ResultSet resultSet) throws SQLException, GeneralSecurityException, IOException {

			setUserName(resultSet.getString(USERNAME));
			setPassword(decrypt(resultSet.getString(PASSWORD)));
			setUserBean(resultSet);
	}

	public UserBean(String username) {
		this();
		setUserName(username);
	}

	public UserBean(String userName, String firstName, String lastName,	String password, int permission) {
		setUserName(userName);
		setFirstName(firstName);
		setLastName(lastName);
		this.password = password;
		setPermission(permission);
	}

	public UserBean(UserBean userBean) {
		this(userBean.getUsername(), 
				userBean.getFirstName(),
				userBean.getLastName(),
				userBean.getPassword(),
				userBean.getPermission());
	}

	public String getUsername() {
		return username!=null ? username : "";
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
		// new UserBeanDAO().login(this);
	}

	public static final boolean ENCRYPT = true;
	public static final boolean DECRYPT = false;
	public int getPermission() {
		return permission;
	}

	public void setUserBean(String cookie) {
		if (cookie != null && !cookie.isEmpty()) {
			String[] userDatas = cookie.split(",");
			if (userDatas.length == 5)
				for(String str:userDatas){
					String[] tmpArray =  str.split("=");
					if(tmpArray.length == 2)
						set(tmpArray);
				}
		}
	}

	public static UserBean getUser(HttpServletRequest httpServletRequest){
		Cookie[] cookies = httpServletRequest.getCookies();
		UserBean user = new UserBean();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals("user")) {
					user.setUserBean(cookie.getValue());
					break;
				}
		return user;
	}

	public void setUserBean(ResultSet resultSet) throws SQLException, GeneralSecurityException, IOException {

		int id = resultSet.getInt("id");
		String username = resultSet.getString(USERNAME);

		if(this.id!=0 && this.id==id || getUsername().equalsIgnoreCase(username)){

			setId(id);
			setFirstName(resultSet.getString("firstname"));
			setLastName(resultSet.getString("lastname"));
			setExtension(resultSet.getString(EXTENSION));
			seteMail(resultSet.getString(E_MAIL));

			String pass = decrypt(resultSet.getString(PASSWORD));

			if(getPassword().equals(pass))
				setPermission(resultSet.getInt(PERMISSION));
			else if(pass.equals("?") && !getPassword().isEmpty() && !getPassword().equals("?")){
				setPermission(resultSet.getInt(PERMISSION));
				needUpdate = true;
			}
		}
	}
	
	public UserBean(int id, String firstName, String lastName, String eMail) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.eMail = eMail;
	}

	public UserBean(int userId, Map<String, String> htmlFields) {

		final String flag = "flag_";
		id = userId;
		permission = 0;

		Set<String> f = htmlFields.keySet();

		for(String key:f)
			if(key.startsWith(flag)){
				switch(key.substring(flag.length())){
				case "Edit":
					permission |=EDITING;
					break;
				case "deviceType":
					permission |= DEVICE_TYPE;
					break;
				case "deviceTypeUpdate":
					permission |= DEVICE_TYPE_UPDATE;
					break;
				case "user":
					permission |= USER;
					break;
				case "userEdit":
					permission |= USER_EDIT;
					break;
				case "stock":
					permission |= STOCK;
					break;
				case "cost":
					permission |= EDIT_COST;
					break;
				case "data":
					permission |= DATABASE;
					break;
				case "part":
					permission |= SCHEMATIC_PART;
					break;
				case "alt":
					permission |= ALT_PART_NUMBER;
					break;
				case "seller":
					permission |= SELLERS;
					break;
				case "seller_edit":
					permission |= EDIT_COMPANIES;
					break;
				case "schemLetter":
					permission |= SCHEMATIC_LETTER;
					break;
				case "workOrder":
					permission |= WORK_ORDER;
				}
			}else
				switch(key){
				case FIRST_NAME:
					setFirstName(htmlFields.get(key));
					break;
				case LAST_NAME:
					setLastName(htmlFields.get(key));
					break;
				case USERNAME:
					setUsername(htmlFields.get(key));
					break;
				case E_MAIL:
					seteMail(htmlFields.get(key));
					break;
				case EXTENSION:
					setExtension(htmlFields.get(key));
					break;
				case PASSWORD:
					setPassword(htmlFields.get(key));
				}
	}

	public UserBean(int userId) {
		this(userId, null, null, null);
	}

	private void set(String[] userData) {
		if (userData != null && userData.length == 2) {
			String s = userData[1];
			switch (userData[0]) {
			case USERNAME:
				setUserName(s);
				break;
			case PASSWORD:
				setPassword(s);
				break;
			case FIRST_NAME:
				setFirstName(s);
				break;
			case LAST_NAME:
				setLastName(s);
				break;
			case PERMISSION:
				if((s = s.replaceAll("\\D", "")).isEmpty())
					setPermission(0);
				else
					setPermission(Integer.parseInt(s));
				break;
			case EXTENSION:
				setExtension(s);
				break;
			case E_MAIL:
				seteMail(s);
			}
		}
	}

	public String getFullName() {
		String fullName = getFirstName()+" "+getLastName();
		return fullName.equals(" ") ? getUsername() : fullName;
	}

	public String getUpdates(UserBean userBean,boolean isAdmin) throws GeneralSecurityException, IOException, SQLException {
		String fieldsToUpdate = "";

		if(id == userBean.getID()){

			String username = userBean.getUsername();
			if(!getUsername().equals(username) && !username.isEmpty()){
				fieldsToUpdate = "`username`='"+username+"'";
			}

			if(!getFirstName().equals(userBean.getFirstName()) && !userBean.getFirstName().isEmpty()){
				if(!fieldsToUpdate.isEmpty())
					fieldsToUpdate += ",";	
				fieldsToUpdate = "`firstName`='"+userBean.getFirstName()+"'";
			}

			if(!getLastName().equals(userBean.getLastName()) && !userBean.getLastName().isEmpty()){
				if(!fieldsToUpdate.isEmpty())
					fieldsToUpdate += ",";	
				fieldsToUpdate += "`lastName`='"+userBean.getLastName()+"'";
			}

			if(!getPassword().equals(userBean.getPassword()) && !userBean.getPassword().isEmpty()){
				if(!fieldsToUpdate.isEmpty())
					fieldsToUpdate += ",";	
				fieldsToUpdate += "`password`='"+userBean.getPassword(true)+"'";
			}

			if(isAdmin && getPermission() != userBean.getPermission()){
				if(!fieldsToUpdate.isEmpty())
					fieldsToUpdate += ",";	
				fieldsToUpdate += "`permission`="+userBean.getPermission();
			}

			if(!getExtension().equals(userBean.getExtension()) && !userBean.getExtension().isEmpty()){
				if(!fieldsToUpdate.isEmpty())
					fieldsToUpdate += ",";	
				fieldsToUpdate += "`extension`='"+userBean.getExtension()+"'";
			}

			if(!geteMail().equals(userBean.geteMail()) && TextWorker.validateEMail(userBean.geteMail())){
				if(!fieldsToUpdate.isEmpty())
					fieldsToUpdate += ",";	
				fieldsToUpdate += "`e_mail`='"+userBean.geteMail()+"'";
			}
		}

		return fieldsToUpdate;
	}

	public String getPassword(boolean encrypt) throws GeneralSecurityException, IOException {
		String returnStr = null;
	
			returnStr = encrypt ? encrypt(getPassword())
								: decrypt(getPassword());
	
		return returnStr;
	}

	public String getValues() throws GeneralSecurityException, IOException {
		return "'"+getUsername()+"','"
				+ getPassword(true)+"',"
				+ (getFirstName().isEmpty() ? null : "'"+getFirstName()+"'")+","
				+ (getLastName().isEmpty() ? null : "'"+getLastName()+"'")+","
				+ getPermission();
	}

	public String	getFirstName()	{ return firstName!=null ? firstName : "";	}
	public String	getLastName()	{ return lastName !=null ? lastName  : "";	}
	public String	getPassword()	{ return password !=null ? password  : "";	}
	public String	getExtension()	{ return extension!=null ? extension : "";	}
	public String	geteMail()		{ return eMail!=null ? eMail : "";		}
	public int		getID()			{ return id; }

	public void setId(int id)					{ this.id = id;					}
	public void setUsername(String username)	{ this.username = username;		}
	public void setFirstName(String firstName)	{ this.firstName = firstName;	}
	public void setLastName(String lastName)	{ this.lastName = lastName;		}
	public void setExtension(String extension)	{ this.extension = extension;	}
	public void seteMail(String eMail)			{ this.eMail = eMail;			} 
	public void setPermission(int permission)	{ this.permission = permission;	}

	public boolean isValid()		{ return id!=0 && permission!=0;			}
	public boolean isSchematicPart(){ return (permission & SCHEMATIC_PART)!=0;	}
	public boolean isSchematicLetter(){ return (permission & SCHEMATIC_LETTER)!=0;	}
	public boolean isUserEdit()		{ return (permission & USER_EDIT)!=0;		}
	public boolean isUser()			{ return (permission & USER)!=0;			}
	public boolean isAdmin()		{ return (permission & ADMIN)!=0;			}
	public boolean isEditing()		{ return (permission & EDITING)!=0;			}
	public boolean isAlt()			{ return (permission & ALT_PART_NUMBER)!=0;	}

	public boolean isEditCost()		{ return (permission & EDIT_COST)!=0;		}
	public boolean isSellers()		{ return (permission & SELLERS)!=0;			}
	public boolean isStock()		{ return (permission & STOCK)!=0;			}
	public boolean isEditCompanies(){ return (permission & EDIT_COMPANIES)!=0;	}

	public boolean isDatabase() 	{ return (permission & DATABASE)!=0;		}

	public boolean isDeviceType()	{ return (permission & DEVICE_TYPE)!=0;		}
	public boolean isDeviceTypeUpdate(){return(permission& DEVICE_TYPE_UPDATE)!=0;}

	public boolean isWorkOrder()	{return(permission& WORK_ORDER)!=0;}

	// Encryption and description ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	   private static String encrypt(String property) throws GeneralSecurityException {
	        return property==null || property.equals("?")
	        		? "?"
	        				: Base64.encodeBase64String(property.getBytes());
	    }

	    private static String decrypt(String property) throws GeneralSecurityException, IOException { 
	        return property==null || property.equals("?")
	        		? "?"
	        				:  new String(Base64.decodeBase64(property)); 
	    } 

	    @Override
		public boolean equals(Object obj) {
			return obj!=null ? obj.hashCode()==hashCode() : false;
		}

		@Override
		public int hashCode() {
			return id>0 ? id : super.hashCode();
		}

		public boolean needPasswordUpdate() {
			return needUpdate;
		}

		@Override
		public String toString() {
			String str = null;
			try {
				str = "UserBean [id=" + id + "," +
								" username=" + username+ "," +
								" password=" + getPassword(true) + "," +
								" firstName=" + firstName+ "," +
								" lastName=" + lastName + "," +
								" permission=" + permission+ "," +
								" extension=" + extension + "," +
								" eMail=" + eMail+ "," +
								" needUpdate=" + needUpdate + "]";
			} catch (GeneralSecurityException | IOException e) {
				throw new RuntimeException(e);
			}
			return str;
		}

		public static UserBean parseUserBean(String userBeanStr) throws GeneralSecurityException, IOException {

			UserBean userBean = null;

			if(userBeanStr!=null && userBeanStr.startsWith("UserBean")){
				userBeanStr = userBeanStr.substring(userBeanStr.indexOf('[')+1, userBeanStr.lastIndexOf(']'));
				String[] split = userBeanStr.split(", ");
				Map<String, String> map = new HashMap<>();
				for(String s:split){
					String[] splitValue = s.split("\\=");
					if(splitValue.length==2)
						map.put(splitValue[0], splitValue[1]);
				}

				String idStr = map.get("id");
				int id;
				if(idStr!=null)
					id = Integer.parseInt(idStr);
				else
					id = 0;

				userBean = new UserBean(id, map.get("firstName"), map.get("lastName"), map.get("eMail"));
				userBean.setUsername(map.get("username"));
				userBean.setPassword(decrypt(map.get("password")));
				userBean.setPermission(Integer.parseInt(map.get("permission")));
				userBean.setExtension(map.get("extension"));
			}

			return userBean;
		}	 
}
