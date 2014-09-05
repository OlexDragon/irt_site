package irt.data.dao;

import irt.data.companies.Company;
import irt.data.purchase.Purchase;
import irt.data.user.UserBean;
import irt.table.Row;
import irt.table.Table;
import irt.work.ComboBoxField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CompanyDAO extends DataAccessObject{

	private static final int ADD_COMPANY 	= 1;
	private static final int UPDATE_SELLER	= 2;

	public List<Company> getAllSellers(){
		return getCompanies(Company.TYPE_VENDOR, true);
	}

	public List<Company> getAllSellers(boolean showAll) {
		return getCompanies(Company.TYPE_VENDOR, showAll);
	}

	public List<Company> getAllSuppliers(){
		return getCompanies(Company.TYPE_CM, true);
	}

	public List<Company> getAllSuppliers(boolean showAll) {
		return getCompanies(Company.TYPE_CM, showAll);
	}


	public Table getCompanysTable(int type,boolean isEdit, boolean showAll) {
		Table table = null;

		String query = "SELECT`id`FROM`irt`.`companies`WHERE`status`=1 and`type`="+type+" LIMIT 1";
		try(	Connection  conecsion = getDataSource().getConnection();
				Statement statement = conecsion.createStatement();
				) {
			try(ResultSet resultSet = statement.executeQuery(query)){
				if (!resultSet.next())
					showAll = true;
			}

			query = "SELECT	 `Company`," +
							"`Name`," +
							"`E-Mail`," +
							"`irt`.phone(`telephone`)AS`Telephone`," +
							"`irt`.phone(`fax`)AS`Fax`," +
							"replace(`address`,'\r','<br />')AS`Address`"+
							(isEdit ? 	",if(`status`,'Active','Not Active')AS`Status`" +
										", concat('<input type=\"submit\" name=\"submit_edit_',`id`,'\" id=\"submit_edit_',`id`,'\" value=\"Edit\" />')AS''" : "")+
						"FROM`irt`.`companies`AS`co`" +
						"LEFT JOIN`irt`.`companies_telephone`AS`ct`ON`ct`.`id_companies`=`co`.`id`" +
						"LEFT JOIN`irt`.`companies_fax`AS`cf`ON`cf`.`id_companies`=`co`.`id`" +
						"LEFT JOIN`irt`.`companies_address`AS`ca`ON`ca`.`id_companies`=`co`.`id`" +
					"WHERE`type`="+type+(showAll ? " " : " and`status`!=0 ")+"order by`name`";

			try(ResultSet resultSet = statement.executeQuery(query)){
				table = getTable(resultSet, null);
			}

			if(table!=null){
				int colCount = table.getColumnCount()-1;
				table.getRows().get(0).getRow().get(colCount).setValue("<input type=\"checkbox\" id=\"show_all\" name=\"show_all\" " +
																							(showAll ? "checked=\"checked\"" : "")+
																			" onclick=\"oneClick('submit')\" onmouseover=\"style.cursor='hand'\" />" +
																		"<label class=\"c1em\" for=\"show_all\" onmouseover=\"style.cursor='hand'\" >All</label>");
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return table;
	}

	private List<Company> getCompanies(byte type, boolean showAll) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		LinkedList<Company> companies = new LinkedList<>();

		try {
				String query = "SELECT`co`.*," +
										"`irt`.phone(`telephone`)AS`Telephone`," +
										"`irt`.phone(`fax`)AS`Fax`," +
										"`address`" +
									"FROM`irt`.`companies`AS`co`" +
								"LEFT JOIN`irt`.`companies_telephone`AS`ct`ON`ct`.`id_companies`=`co`.`id`" +
								"LEFT JOIN`irt`.`companies_fax`AS`cf`ON`cf`.`id_companies`=`co`.`id`" +
								"LEFT JOIN`irt`.`companies_address`AS`ca`ON`ca`.`id_companies`=`co`.`id`" +
								"WHERE`type`="+type+(showAll ? " " : " and`status`!=0 ")+"order by`name`";

				conecsion = getDataSource().getConnection();
				statement = conecsion.createStatement();
				resultSet = statement.executeQuery(query);

				while (resultSet.next()) {
					companies.add(new Company(resultSet));
				}
			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "BomDAO.getCompanies");
				throw new RuntimeException(e);
			} finally {
				close(resultSet, statement, conecsion);
			}
		return companies;
	}

	public List<String> getCompaniesName(byte type){
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		LinkedList<String> companies = new LinkedList<>();

		String query = "select`company`from`irt`.`companies`"+(type>0 ? "where`type`="+type : "")+" order by`company`";

		try {
				conecsion = getDataSource().getConnection();
				statement = conecsion.prepareStatement(query);
				resultSet = statement.executeQuery();

				while (resultSet.next()) {
					companies.add(resultSet.getString(1));
				}
			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "BomDAO.getCompaniesName");
				throw new RuntimeException(e);
			} finally {
				close(resultSet, statement, conecsion);
			}

		return companies;
	}

	public boolean addCompany(Company company, UserBean userBean) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		boolean isAdded = false;

		if(company.isSet() && company.isNewFields())
		try {
			String query = "INSERT IGNORE INTO`irt`.`companies`(`Company`,`Name`,`E-mail`,`type`,`status`)" +
							"VALUES('"+company.setCompanyName()+"','"+company.setName()+"','"+company.setEMail()+"',"+company.getType()+","+company.setActive()+")";

			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			resultSet = statement.getGeneratedKeys();
			
			if(resultSet.next())
				if(company.setId(resultSet.getInt(1))>0){
					if(company.isInsertTelephone()){
						query = "INSERT INTO `irt`.`companies_telephone` (`id_companies`, `telephone`) VALUES (" +company.getId() +", '"+company.setTelephone().replaceAll("[\\D]", "") +"')";
						statement.executeUpdate(query);
					}
					if(company.isInsertFax()){
						query = "INSERT INTO `irt`.`companies_fax` (`id_companies`, `fax`) VALUES (" +company.getId() +", '"+company.setFax().replaceAll("[\\D]", "") +"')";
						statement.executeUpdate(query);
					}
					if(company.isInsertAddress()){
						query = "INSERT INTO `irt`.`companies_address` (`id_companies`, `address`) VALUES (" +company.getId() +", '"+company.setAddress() +"')";
						statement.executeUpdate(query);
					}
				
					isAdded = true;
					addCompanyActivity(userBean, statement, company, ADD_COMPANY);
				}else
					Purchase.setErrorMessage("This seller already exist.");

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "BomDAO.addCompany");
			throw new RuntimeException(e);
		}finally {
			close(resultSet, statement, conecsion);
		}
	
		return isAdded;
	}

	private boolean addCompanyActivity(UserBean userBean, Statement statement, Company company, int activity) throws SQLException {

		String query = "INSERT INTO`irt`.`companies_activity`(`user_id`,`seller_fields`,`activity`,`date`)VALUES("+userBean.getID()+",'"+company+"',"+activity+",NOW())";

		statement.executeUpdate(query);

		return true;
	}

	public String getHtmlSelect(byte type) {
		List<Company> cs = getCompanies(type, false);
		String str = "<select>";
		for(Company c:cs)
			str += "<option>"+c.getName()+" from "+c.getCompanyName()+"</option>";
		
		return str += "</select>";
	}

	public boolean update(Company company, UserBean userBean) {
		Connection conecsion = null;
		Statement statement = null;
		boolean isUpdated = false;


		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			String query;
			if(company.isNewFields()){
				List<String> sets = new ArrayList<>();
				if(company.isNewName())
					sets.add("`name`='"+company.setName()+"'");
				if(company.isNewCompanyName())
					sets.add("`company`='"+company.setCompanyName()+"'");
				if(company.isNewEMail())
					sets.add("`e-mail`='"+company.setEMail()+"'");
				if(company.isNewActive())
					sets.add("`status`="+company.setActive());
				String set = sets.get(0);
				for(int i=1; i<sets.size(); i++)
					set += ","+sets.get(i);
				query = "UPDATE`irt`.`companies`SET"+set+" WHERE`id`="+company.getId();
				statement.executeUpdate(query);
				getError().setErrorMessage("<br />Query1: "+query);
			}

			if(company.isNewTelephone()){
				if(company.isInsertTelephone())
					query = "INSERT INTO `irt`.`companies_telephone` (`id_companies`, `telephone`) VALUES (" +company.getId() +", '"+company.setTelephone().replaceAll("[\\D]", "") +"')";
				else
					query = "UPDATE `irt`.`companies_telephone` SET `telephone`='"+company.setTelephone().replaceAll("[\\D]", "")+"'"+company.getWhereTelephone();
				statement.executeUpdate(query);
				getError().setErrorMessage("<br />Query2: "+query);
			}
			if(company.isNewFax()){
				if(company.isInsertFax())
					query = "INSERT INTO `irt`.`companies_fax` (`id_companies`, `fax`) VALUES (" +company.getId() +", '"+company.setFax() +"')";
				else
					query = "UPDATE `irt`.`companies_fax` SET `fax`='"+company.setFax().replaceAll("[\\D]", "")+"'"+company.getWhereFax();
				statement.executeUpdate(query);
				getError().setErrorMessage("<br />Query3: "+query);
			}
			if(company.isNewAddress()){
				if(company.isInsertAddress())
					query = "INSERT INTO `irt`.`companies_address` (`id_companies`, `address`) VALUES (" +company.getId() +", '"+company.setAddress() +"')";
				else
					query = "UPDATE `irt`.`companies_address` SET `address`='"+company.setAddress()+"'"+company.getWhereAddress();
				getError().setErrorMessage("<br />Query4: "+query);
				statement.executeUpdate(query);
			}

			isUpdated = addCompanyActivity(userBean, statement, company, UPDATE_SELLER);

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "BomDAO.update");
			throw new RuntimeException(e);
		}finally {
			close(null, statement, conecsion);
		}
	
		return isUpdated;
	}

	public Company getCompany(String companyIdStr) {
		return companyIdStr!=null && !(companyIdStr = companyIdStr.replaceAll("\\D", "")).isEmpty() ? getCompany(Integer.parseInt(companyIdStr)) : null;
	}

	public Company getCompany(int companyId) {

		Company company= null;

		String query = "SELECT`co`.*," +
								"`irt`.phone(`telephone`)AS`Telephone`," +
								"`irt`.phone(`fax`)AS`Fax`," +
								"`address`" +
							"FROM`irt`.`companies`AS`co`" +
						"LEFT JOIN`irt`.`companies_telephone`AS`ct`ON`ct`.`id_companies`=`co`.`id`" +
						"LEFT JOIN`irt`.`companies_fax`AS`cf`ON`cf`.`id_companies`=`co`.`id`" +
						"LEFT JOIN`irt`.`companies_address`AS`ca`ON`ca`.`id_companies`=`co`.`id`" +
						"WHERE`id`=?";
		logger.trace("\n\tqueru:\t{}\n\tcompany id:\t{}", query, companyId);

		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query)) {

			statement.setInt(1, companyId);

				try(ResultSet resultSet = statement.executeQuery();){

					if(resultSet.next()) 
						company = new Company(resultSet);
				}
			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "BomDAO.getCompany");
				throw new RuntimeException(e);
			}

		return company;
	}

	public LinkedList<Row> getRows(String tableName, int componyType) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		LinkedList<Row> rowList = new LinkedList<>();

		String query = "select*from`irt`.`"+tableName+"`"+( componyType>0 ? "where`type`="+componyType : "");
		try {

			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			while(resultSet.next())
				rowList.add(new Row(resultSet));

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "BomDAO.getRows");
			throw new RuntimeException(e);
		}finally {
			close(resultSet, statement, conecsion);
		}

		return rowList;
	}

	public ComboBoxField[] getComboBoxFields(int type){
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		ComboBoxField[] companies = null;

		String query = "SELECT`id`," +
								"`company`" +
							"FROM`irt`.`companies`" +
						"WHERE"+(type>0 ? "`type`="+type+" AND" : "") +
								"`status`=1";//status=1 - active
		try {

			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);
			if(resultSet.last()){
				int index = resultSet.getRow();
				companies = new ComboBoxField[index];
				do{
					companies[--index] = new Company(resultSet.getInt("id"), resultSet.getString("company"), null, null, null, null, null, type, true);
				}while(resultSet.previous());
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "BomDAO.getComboBoxFields");
			throw new RuntimeException(e);
		}finally {
			close(resultSet, statement, conecsion);
		}

		return companies;
	}
}
