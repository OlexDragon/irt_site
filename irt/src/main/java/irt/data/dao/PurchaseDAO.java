package irt.data.dao;

import irt.data.companies.Company;
import irt.data.manufacture.ManufacturePartNumber;
import irt.data.purchase.Price;
import irt.data.purchase.PurchaseOrder;
import irt.data.purchase.PurchaseOrderUnit;
import irt.data.user.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO extends DataAccessObject {

	public boolean createNewPO(PurchaseOrder purchaseOrder, int status, int userId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		boolean isCreated = false;

		if (purchaseOrder!=null && purchaseOrder.isSet()) {
			try {
				conecsion = getDataSource().getConnection();
				statement = conecsion.createStatement();

				int index = 1;
				String query;

				if(purchaseOrder.getPONumber()==null){
					query = "SELECT`po_number`" +
								"FROM`irt`.`purchase`" +
							"WHERE LEFT(`po_number`,2)=RIGHT(YEAR(CURDATE()),2)AND " +
									"SUBSTRING(`po_number`,3,2)=MONTH(CURDATE())AND " +
									"LENGTH(`po_number`)=7";
					resultSet = statement.executeQuery(query);
					if(resultSet.last())
						index = resultSet.getRow()+1;
					resultSet.close();
				}

				int taxIndex = getPurchaseTaxId(purchaseOrder.getTaxes(), statement);
				int extraIndex = getPurchaseTaxId(purchaseOrder.getExtra(), statement);

				query = "INSERT INTO`irt`.`purchase`(`po_number`,`id_users`,`id_companies`, `tax`,`extra`,`comments`,`date`,`status`)" +
						" VALUES ('" +
							(purchaseOrder.getPONumber()==null ?
									purchaseOrder.setPONumber(index) :
										purchaseOrder.getPONumber())+"'," +
							userId+","+
							purchaseOrder.getSellerId()+"," +
							(taxIndex <0 ? null : taxIndex)+"," +
							(extraIndex <0 ? null : extraIndex)+"," +
							(purchaseOrder.getComments()!=null ? "'" +purchaseOrder.getComments() + "'" : null) + "," +
							"'"+purchaseOrder.getDateTime()+"'," +
							status+")";

				statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				resultSet = statement.getGeneratedKeys();
				if(resultSet.next())
					purchaseOrder.setId(resultSet.getInt(1));

				insertPurchaseComponents(purchaseOrder, statement);

				isCreated = true;

			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "PurchaseDAO.createNewPO");
				throw new RuntimeException(e);
			} finally {
				close(resultSet, statement, conecsion);
			}
		}
		return isCreated;
	}

	protected void insertPurchaseComponents(PurchaseOrder purchaseOrder, Statement statement) throws SQLException {
		String query;
		List<PurchaseOrderUnit> poList = purchaseOrder.getPurchaseOrderUnits();
		if(poList!=null && !poList.isEmpty()){
			query = "INSERT INTO`irt`.`purchase_components`(`id_purchase`,`id_components`,`id_components_alternative`,`price`,`qty`)" +
					"VALUES";
			char comma = 0;
			for(PurchaseOrderUnit pou:poList){
				if(comma==0)
					comma=',';
				else
					query += comma;
				query += "(" +purchaseOrder.getId()+","+
												pou.getComponentId()+","+
												pou.getMfrPNIndex()+","+
												pou.getPriceLong()+"," +
												pou.getOrderQuantity()+")";
			}
			statement.executeUpdate(query);
		}
	}

	private int getPurchaseTaxId(String fieldTax, Statement statement) throws SQLException {
		int id = -1;
		if(fieldTax!=null){
			String query = "SELECT`id`FROM`irt`.`purchase_tax`WHERE`tax`='"+fieldTax+"'";
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet.next())
				id = resultSet.getInt(1);
			resultSet.close();

			if(id<0){
				query = "INSERT INTO`irt`.`purchase_tax`(`tax`)VALUES('"+fieldTax+"')";
				statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				resultSet = statement.getGeneratedKeys();
				id = resultSet.next() ? resultSet.getInt(1) : -1;
				resultSet.close();
			}
		}
		return id;
	}

	public String[] getAllPONumbers(){

		String query = "SELECT `po_number`FROM`irt`.`purchase`ORDER BY`po_number`DESC";
		return getStringArray(query);
	}

	public String[] getPONumbers(int statusToShow){

		String query = "SELECT `po_number`FROM`irt`.`purchase`" +(statusToShow<0 ? "" : "WHERE`status`="+statusToShow)+" ORDER BY`po_number`DESC";
		return getStringArray(query);
	}

	public PurchaseOrder getPurchaseOrder(String poNumber) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		PurchaseOrder purchaseOrder = null;

		String query = "SELECT`p`.`id`AS`purchase_id`,"+
								"`p`.`po_number`,"+
								"`p`.`date`,"+
								"`p`.`id_companies`AS`id`,"+
								"`pt`.`tax`,"+
								"`p`.`extra`," +
								"`p`.`comments`," +
								"`p`.`status`AS`po_status`," +
								"`u`.`id`AS`user_id`," +
								"`u`.`firstName`," +
								"`u`.`lastName`," +
								"`u`.`e_mail`," +
								"`co`.*," +
								"`ct`.`telephone`," +
								"`cf`.`fax`," +
								"`ca`.`address`," +
								"`l`.`link`"+
							"FROM`irt`.`purchase`AS`p`" +
						"JOIN`irt`.`users`AS`u`ON`u`.`id`=`p`.`id_users`" +
						"JOIN`irt`.`companies`AS`co`ON`co`.`id`=`p`.`id_companies`" +
						"LEFT JOIN`irt`.`companies_telephone`AS`ct`ON`ct`.`id_companies`=`co`.`id`" +
						"LEFT JOIN`irt`.`companies_fax`AS`cf`ON`cf`.`id_companies`=`co`.`id`" +
						"LEFT JOIN`irt`.`companies_address`AS`ca`ON`ca`.`id_companies`=`co`.`id`" +
						"LEFT JOIN`irt`.`purchase_tax`AS`pt`ON`pt`.`id`=`p`.`tax`OR`pt`.`id`=`p`.`extra`" +
						"LEFT JOIN`irt`.`links`AS`l`ON`l`.`id`=`p`.`invoice`" +
						"WHERE`p`.`po_number`='"+poNumber+"'";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);
	
			if(resultSet.next()){
				ArrayList<PurchaseOrderUnit> purchaseOrderUnits = new ArrayList<PurchaseOrderUnit>();
				purchaseOrder = new PurchaseOrder(purchaseOrderUnits);
				purchaseOrder.setId(resultSet.getInt("purchase_id"));
				purchaseOrder.setPONumber(resultSet.getString("po_number"));
				purchaseOrder.setDateTime(resultSet.getTimestamp("date"));
				purchaseOrder.setCompany(new Company(resultSet));
				purchaseOrder.setTaxes(resultSet.getString("tax"));
				purchaseOrder.setExtra(resultSet.getString("extra"));
				purchaseOrder.setUser(new UserBean(resultSet.getInt("user_id"), resultSet.getString("firstName"), resultSet.getString("lastName"), resultSet.getString("e_mail")));
				purchaseOrder.setComments(resultSet.getString("comments"));
				purchaseOrder.setInvoiceLink(resultSet.getString("link"));
				purchaseOrder.setStatus(resultSet.getInt("po_status"));
				purchaseOrder.setComments();
				purchaseOrder.setUser();
				purchaseOrder.setCompany();
				purchaseOrder.setVendor();

				resultSet.close();
				query = "SELECT`c`.`id`," +
								"`irt`.part_number(`part_number`)AS`Part Number`," +
								"`c`.`description`," +
								"`c`.`manuf_part_number`AS`Mfr P/N`," +
								"`m`.`id`AS`mfrID`," +
								"`m`.`name`AS`mfr`," +
								"`pc`.`id_components_alternative`AS`mfrPNId`," +
								"`pc`.`price`," +
								"`pc`.`qty`AS`SQty`" +
							"FROM`irt`.`purchase_components`AS`pc`" +
						"JOIN`irt`.`components`AS`c`ON`c`.`id`=`pc`.`id_components`" +
						"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`c`.`manuf_id`" +
						"WHERE`id_purchase`="+purchaseOrder.getId();
//irt.work.Error.setErrorMessage(query);
				resultSet = statement.executeQuery(query);

				while(resultSet.next()){
					PurchaseOrderUnit purchaseOrderUnit = new PurchaseOrderUnit(resultSet.getInt("id"),
																resultSet.getString("Part Number"),
																resultSet.getString("description"),
																new ManufacturePartNumber(0, resultSet.getString("mfrID"), resultSet.getString("Mfr P/N"), resultSet.getString("mfr")));
					purchaseOrderUnit.setPrice(new Price(resultSet.getLong("price")));
					purchaseOrderUnit.setOrderQuantity(resultSet.getInt("SQty"));
					purchaseOrderUnit.setMfrPNIndex(resultSet.getInt("mfrPNId"));
					purchaseOrderUnit.setPrice();
					purchaseOrderUnit.setOrderQuantity();
					purchaseOrderUnits.add(purchaseOrderUnit);
				}
				resultSet.close();

				query = "SELECT`ca`.`id`," +
								"`alt_mfr_part_number`AS`mfrPN`," +
								"`m`.`name`AS`mfr`" +
							"FROM`irt`.`components_alternative`AS`ca`" +
						"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`ca`.`manuf_id`" +
						"WHERE`id_components`=";
				for(PurchaseOrderUnit pou:purchaseOrderUnits){
					resultSet = statement.executeQuery(query+pou.getComponentId());
					while(resultSet.next())
						pou.addMfrPM(resultSet.getInt("id"), resultSet.getString("mfrPN"), resultSet.getString("mfr"));
				}
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "PurchaseDAO.getPurchaseOrder");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return purchaseOrder;
	}

	public void delete(String poNumber) {
		// TODO Auto-generated method stub
		
	}

	public String getUpdatePONumber(String poNumber) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		if(poNumber.length()>7)
			poNumber = poNumber.substring(0,7);

		String query = "SELECT`po_number`FROM`irt`.`purchase`WHERE`po_number`LIKE'"+poNumber+"%'AND LENGTH(`po_number`)>7";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			int count = 1;
			if(resultSet.last()){
				count = resultSet.getRow()+1;
			}

			poNumber += "R"+count;
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "PurchaseDAO.getUpdatePONumber");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}
		return poNumber;
	}

	public void resavePO(PurchaseOrder purchaseOrder, int status, int id) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		if (purchaseOrder.getId() > 0) {
			try {
				conecsion = getDataSource().getConnection();
				statement = conecsion.createStatement();

				String query;
				if(status==PurchaseOrder.OPEN || purchaseOrder.isNewComments()){
					query= "UPDATE`irt`.`purchase`" +
								"SET " +
									(purchaseOrder.isNewCompany() ? "`id_companies`="+purchaseOrder.setCompany().getId()+"," : "" )+
									(purchaseOrder.isNewUser() ? "`id_users`="+purchaseOrder.getUser().getID()+"," : "" )+
									(purchaseOrder.isNewComments() ? "`comments`='"+purchaseOrder.setComments()+"'," : "") +
									"`date`='" +purchaseOrder.getDateTime()+"'"+
							"WHERE`id`="+purchaseOrder.getId();
					statement.executeUpdate(query);
				}

				query = "DELETE FROM`irt`.`purchase_components`WHERE`id_purchase`="+purchaseOrder.getId();
				statement.executeUpdate(query);
				insertPurchaseComponents(purchaseOrder, statement);
				purchaseOrder.cancel();

			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "PurchaseDAO.resavePO");
				throw new RuntimeException(e);
			} finally {
				close(resultSet, statement, conecsion);
			}
		}
	}

	public boolean addLink(int userId, String purchaseOrderNumber, int linkId) {

		boolean isDon = false;

		String query= "SELECT`id`FROM`irt`.`purchase`WHERE`po_number`=?";
		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query)) {

			statement.setString(1, purchaseOrderNumber);

			try(ResultSet resultSet = statement.executeQuery()){

				if (resultSet.next()) {

					int poId = resultSet.getInt(1);

					if (new HistoryDAO().setHistory(statement, userId, "`irt`.`purchase`", poId, "`invoice`", "" + linkId)) {
						if (linkId > 0 && purchaseOrderNumber != null) {

							query = "UPDATE`irt`.`purchase`SET`invoice`=" + linkId + " WHERE`id`=" + poId;
							isDon = statement.executeUpdate(query) > 0;
						}
					}
				}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "PurchaseDAO.addLink");
			throw new RuntimeException(e);
		}
		return isDon;
	}

	public boolean setStatus(String purchaseOrderNumber, int active) {
		boolean isDon = false;

		if (active > 0 && purchaseOrderNumber!=null) {

				String query= "UPDATE`irt`.`purchase`SET`status`="+active+" WHERE`po_number`='"+purchaseOrderNumber+"'";
				isDon = executeUpdate(query)>0;

		}
		return isDon;
	}
}
