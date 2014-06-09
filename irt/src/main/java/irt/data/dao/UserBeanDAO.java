package irt.data.dao;

import irt.data.user.UserBean;
import irt.table.OrderBy;
import irt.table.Row;
import irt.table.Table;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class UserBeanDAO extends DataAccessObject {

	public UserBean login(UserBean userBean){

		if(userBean!=null)
			try(	Connection conecsion = getDataSource().getConnection();
					Statement statement = conecsion.createStatement();) {

				

				getBy(statement, userBean);

				if(userBean.needPasswordUpdate())
						update(statement, userBean, false, userBean.getID());

			} catch (SQLException | GeneralSecurityException | IOException e) {
				new ErrorDAO().saveError(e, "UserBeanDAO.login");
				throw new RuntimeException(e);
			}

		return userBean;
	}

	public List<UserBean> getAll(){

		List<UserBean> users = new ArrayList<>();
		String query = "select*from`irt`.`users`order by 'username'";

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();) {

			while(resultSet.next())
				users.add(new UserBean(resultSet));

		} catch (SQLException | GeneralSecurityException | IOException e) {
			new ErrorDAO().saveError(e, "UserBeanDAO.getAll");
			throw new RuntimeException(e);
		}

		return users;
	}

	public UserBean getBy(String username) {

		UserBean newUserBean = null;

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();) {

			newUserBean = getBy(statement, username);

		} catch (SQLException | GeneralSecurityException | IOException e) {
			new ErrorDAO().saveError(e, "UserBeanDAO.getBy");
			throw new RuntimeException(e);
		}

		return newUserBean;
	}

	public void getBy(UserBean userBean) {

		try(Connection  conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();) {

			getBy(statement, userBean);

		} catch (SQLException | GeneralSecurityException | IOException e) {
			new ErrorDAO().saveError(e, "UserBeanDAO.getBy");
			throw new RuntimeException(e);
		}

	}

	private void getBy(Statement statement, UserBean userBean) throws SQLException, GeneralSecurityException, IOException {
		String query = null;

		int id = userBean.getID();
		if(id>0)
			query = "`id`="+id;
		else {
			String username = userBean.getUsername();
			if (!username.isEmpty())
				query = "`username`='" + username + "'";
		}

		if (query != null) {
			query = "SELECT*FROM`irt`.`users`WHERE" + query;

			try (ResultSet resultSet = statement.executeQuery(query);) {

				if (resultSet.next())
					userBean.setUserBean(resultSet);
			}
		}
	}

	private UserBean getBy(Statement statement, String username) throws SQLException, GeneralSecurityException, IOException {

		UserBean newUserBean = null;
		if (username!=null){
			String query = "SELECT*FROM`irt`.`users`WHERE`username`='"+username+"'";

			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next())
				newUserBean = new UserBean(resultSet);
		}

		return newUserBean;
	}

	public UserBean getBy(int userID) {

		UserBean newUserBean = null;

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();) {

			
			newUserBean = getBy(statement, userID);

		} catch (SQLException | GeneralSecurityException | IOException e) {
			new ErrorDAO().saveError(e, "UserBeanDAO.getBy");
			throw new RuntimeException(e);
		}

		return newUserBean;
	}

	public UserBean getBy(Statement statement, int userID) throws SQLException, GeneralSecurityException, IOException{

		UserBean newUserBean = null;
		if (userID>0){
			String query = "SELECT*FROM`irt`.`users`WHERE`ID`=" + userID;

			ResultSet resultSet = statement.executeQuery(query);

			if (resultSet.next())
				newUserBean = new UserBean(resultSet);
		}

		return newUserBean;
	}

	public boolean update(UserBean userBean, boolean isAdmin, int updateBy) {

		boolean updated;
		try(Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();) {

			
			updated = update(statement, userBean, isAdmin, updateBy)>0;

		} catch (SQLException | GeneralSecurityException | IOException e) {
			new ErrorDAO().saveError(e, "UserBeanDAO.update");
			throw new RuntimeException(e);
		}

		return updated;
	}

	private int update(Statement statement, UserBean userBean, boolean isAdmin, int updateBy)	throws GeneralSecurityException, IOException, SQLException {

		int updated = 0;

		if (userBean != null && userBean.getID()>0){
			UserBean tmpUserBean = getBy(statement, userBean.getID());
			String query = tmpUserBean.getUpdates(userBean, isAdmin);

			insertHistory(updateBy, userBean.getID(), query, statement);

			if (!query.isEmpty()) {
				query = "UPDATE `irt`.`users` SET " + query
							+ " WHERE`id`=" + userBean.getID();

				updated = statement.executeUpdate(query);
			}
		}else
			getError().setErrorMessage("Input data is not correct. <small>(E029)</small>");

		return updated;
	}

	private void insertHistory(int updatBy, int userId, String details, Statement statement) throws SQLException {
		String query = "INSERT INTO `irt`.`usets_history` (`date`, `by`, `detil`, `id_users`) VALUES (now(),"+updatBy+", \""+details+"\", "+userId+")";
		statement.executeUpdate(query);
	}

	public int insert(int insertBy, UserBean userBean) throws GeneralSecurityException, IOException {

		if (userBean != null && !userBean.getUsername().isEmpty() && !userBean.getPassword().isEmpty() && !isExist(userBean)){

				String query = "INSERT INTO `irt`.`users` "
						+ "(`username`, `password`, `firstName`, `lastName`, `permission`)"
						+ " VALUES (" + userBean.getValues() + ")";

				userBean.setId(insert(query));
				insertHistory(insertBy, userBean.getID(), userBean.toString());
		}else
			getError().setErrorMessage("Input data is not correct <small>(E030)</small>");
		return userBean.getID();
	}

	private void insertHistory(int insertBy, int userId, String details) {

		try(	Connection conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();) {
			
			insertHistory(insertBy, userId, details, statement);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "DataAccessObject.executeUpdate");
			throw new RuntimeException(e);
		}

	}

	private boolean isExist(UserBean userBean) {
		boolean isExist = false;

		if (userBean != null && !userBean.getUsername().isEmpty()){

				String query = "select`username`from`irt`.`users`where`username`='"
						+ userBean.getUsername() + "'";

				isExist = isResult(query);
		}
		return isExist;
	}

	public String getTable() {
		List<UserBean> users = getAll();
		Table table = new Table(new String[]{"Name", "Ext.", "e-mail"}, "users");

		for(UserBean u:users)
			table.add(new Row(new String[]{u.getFullName(),
											u.getExtension(),
											u.geteMail()}));

		return table.toString();
	}

	public Table getTable(boolean isAdmin, OrderBy orderBy){

		String query = "SELECT`firstName`AS`First Name`," +
								"`lastName`AS`Last Name`," +
								"`extension`AS`Ext.`," +
								"`e_mail`AS`e-Mail`" +
								(isAdmin ? ",concat('<input type=\"submit\" name=\"btn_',`id`,'\" id=\"btn_',`id`,'\" value=\"Edit\" />')AS``" : "") +
							"FROM`irt`.`users`"+
						(orderBy!=null ? orderBy : "ORDER BY`First Name`");

		return getTable(query, null);
	}

	public String getNewUsername(){
		return "irt"+getRowCount("irt","users");
	}
}
