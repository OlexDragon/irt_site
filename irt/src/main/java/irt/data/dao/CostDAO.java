package irt.data.dao;

import irt.data.purchase.Cost;
import irt.data.purchase.CostCompany;
import irt.data.purchase.CostMfrPN;
import irt.data.purchase.CostSetUnit;
import irt.data.purchase.CostUnit;
import irt.data.purchase.ForPrice;
import irt.data.purchase.Price;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class CostDAO extends DataAccessObject {

	public void save(List<CostUnit> costUnits) {
		Connection conecsion = null;
		Statement statement = null;
		String query;

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
	
			for(CostUnit cu:costUnits){
				for(CostMfrPN cmpn:cu.getMfrPartNumbers())
					if(cmpn.getForPrices()!=null)
						for(ForPrice fp:cmpn.getForPrices()){
							int status = fp.getStatus();
							if(status == ForPrice.INSERT)
								query = "INSERT INTO`irt`.`cost`(" +
											"`id_components`," +
														"`id_components_alternative`," +
																	"`id_companies`," +
																				"`for`," +
																							"`cost`)" +
										"VALUES("+
											cu.getComponentId(cmpn)+","+
														(cmpn.getAlternativeUnit()==null ? cmpn.getId() : 0)+","+
																	cmpn.getCompanyId()+","+
																				fp.getForUnits()+","+
																							fp.getPrice().getValueLong()+")";
							else if(status== ForPrice.UPDATE)
								query = "UPDATE `irt`.`cost` SET `cost`="+fp.getPrice().getValueLong()+
										" WHERE `id_components`="+cu.getComponentId(cmpn)+" and" +
												"`id_components_alternative`="+(cmpn.getAlternativeUnit()==null ? cmpn.getId() : 0)+" and" +
												"`id_companies`="+cmpn.getCompanyId()+" and" +
												"`for`="+fp.getForUnits();
							else continue;

							statement.executeUpdate(query);
							fp.setPrice();
						}
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "CostDAO.save");
			throw new RuntimeException(e);
		} finally {
			close(null, statement, conecsion);
		}
	}

	public void saveSet(int setId, int topComponentId, List<CostUnit> costUnits){
		Connection conecsion = null;
		Statement statement = null;

		if(setId>0 && topComponentId>0)
		try {
			String query = "DELETE FROM `irt`.`cost_sets` WHERE `set_id`="+setId+" and`id_top_component`="+topComponentId;

			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			statement.executeUpdate(query);

			for(CostUnit cu:costUnits){
				int selectedForPrice = cu.getSelectedForPrice();
				if(selectedForPrice<0)
					selectedForPrice = 0;
				if(cu.getSelectedMfrPN()>0 || cu.getSelectedCompany()>0 || selectedForPrice>0){
					query = "INSERT INTO`irt`.`cost_sets`(`set_id`,`id_top_component`,`id_components`,`mfr_part_number`,`vendor`,`moq`)"+
						"VALUES ("+setId+","+topComponentId+","+cu.getComponentId()+","+cu.getSelectedMfrPN()+","+cu.getSelectedCompany()+","+selectedForPrice+")";

//					irt.work.Error.setErrorMessage(query);
					statement.executeUpdate(query);
				}
			}

			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "CostDAO.saveSet");
				throw new RuntimeException(e);
			} finally {
				close(null, statement, conecsion);
			}
	}

	public Cost getCost(String topComponentIdStr){
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Cost cost = null;


		String query = "SELECT`id`," +
								"`irt`.part_number(part_number)AS`pn`," +
								"`description`" +
							"FROM`irt`.`components`" +
						"WHERE`id`="+topComponentIdStr;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			int id = -1;
			String partNumberStr = null;
			String description = null;
			if(resultSet.next()){
				id = resultSet.getInt("id");
				partNumberStr = resultSet.getString("pn");
				description = resultSet.getString("description");
			}

			if(id>0){
				query = "(SELECT `c`.`id`AS`componentId`," +
									"0 AS`alt_comp_id`," +
									"concat('<a href=\"/irt/part-numbers?pn=',`part_number`,'\">',`irt`.part_number(`part_number`),'</a>')AS`pn`," +
									"`c`.`description`," +
									"`m`.`name`AS`Mfr`," +
									"`manuf_part_number`AS`mfrPN`," +
									"`irt`.component_qty(`ref`)AS`Qty`," +
									"`co`.`id_components_alternative`AS`mfrPNId`," +
									"`co`.`for`," +
									"`co`.`cost`AS`price`," +
									"`cm`.`id`AS`CompanyId`," +
									"IF(`cm`.`company`IS NULL AND`co`.`cost`IS NOT NULL, `m`.`name`, `cm`.`company`)AS`companyName`" +
								"FROM`irt`.`bom_ref`AS`br`" +
							"JOIN`irt`.`bom`ON`bom`.`id_bom_ref`=`br`.`id`" +
							"JOIN`irt`.`components`AS`c`ON`c`.`id`=`bom`.`id_components`" +
							"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`c`.`manuf_id`" +
							"LEFT JOIN`irt`.`cost`AS`co`ON`co`.`id_components`=`c`.`id`AND`co`.`id_components_alternative`=0 " +
							"LEFT JOIN`irt`.`companies`AS`cm`ON`cm`.`id`=`co`.`id_companies`" +
							"WHERE`bom`.`id_top_comp`="+id+")" +

							"UNION" +

							"(SELECT `ca`.`id_components`AS`componentId`," +
										"`alt`.`id`AS`alt_comp_id`," +
										"IF(`alt`.`id`IS NULL," +
											"concat('<a href=\"/irt/part-numbers?pn=',`c`.`part_number`,'\">',`irt`.part_number(`c`.`part_number`),'</a>')," +
												"concat('<a href=\"/irt/part-numbers?pn=',`alt`.`part_number`,'\">',`irt`.part_number(`alt`.`part_number`),'</a>'))" +
													"AS`pn`," +
										"IF(`alt`.`id`IS NULL,`c`.`description`,`alt`.`description`)AS`description`," +
										"`m`.`name`AS`Mfr`," +
										"`alt_mfr_part_number`AS`mfrPN`," +
										"`irt`.component_qty(`ref`)AS`Qty`,`" +
										"ca`.`id`AS`mfrPNId`," +
										"`co`.`for`," +
										"`co`.`cost`AS`price`," +
										"`cm`.`id`AS`CompanyId`," +
										"IF(`cm`.`company`IS NULL AND`co`.`cost`IS NOT NULL, `m`.`name`, `cm`.`company`)AS`companyName`" +
									"FROM`irt`.`bom_ref`AS`br`" +
								"JOIN`irt`.`bom`ON`bom`.`id_bom_ref`=`br`.`id`" +
								"JOIN`irt`.`components_alternative`AS`ca`ON`ca`.`id_components`=`bom`.`id_components`" +
								"LEFT JOIN`irt`.`components`AS`c`ON`c`.`id`=`ca`.`id_components`" +
								"LEFT JOIN`irt`.`components`AS`alt`ON`alt`.`manuf_part_number`=`alt_mfr_part_number`" +
								"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`ca`.`manuf_id`" +
								"LEFT JOIN`irt`.`cost`AS`co`ON`co`.`id_components`=`ca`.`id_components`AND`co`.`id_components_alternative`=`ca`.`id`OR((`co`.`id_components_alternative`=0 AND`c`.`id`!=`alt`.`id`AND`co`.`id_components`=`alt`.`id`))" +
								"LEFT JOIN`irt`.`companies`AS`cm`ON`cm`.`id`=`co`.`id_companies`" +
								"WHERE`bom`.`id_top_comp`="+id+")" +
							"ORDER BY`pn`,`mfrPN`,`for`";
				resultSet = statement.executeQuery(query);

				if(resultSet.next()){
					cost = new Cost(id,partNumberStr,description);
					do
						cost.add(new CostUnit(resultSet.getInt("componentId"),
												resultSet.getInt("alt_comp_id"),
												resultSet.getString("pn"),
												resultSet.getString("description"),
												new CostMfrPN(resultSet.getInt("mfrPNId"),
																resultSet.getString("mfr"),
																resultSet.getString("mfrPN"),
																new CostCompany(resultSet.getInt("CompanyId"),
																				resultSet.getString("companyName"),
																				new ForPrice(new Price(resultSet.getLong("price")),
																						resultSet.getInt("for")))),
												resultSet.getInt("qty")));
					while(resultSet.next());
					cost.reset();
				}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "CostDAO.getCost");
			throw new RuntimeException(e);
		} finally {
				close(resultSet, statement, conecsion);
		}
		
		return cost;
	}

	public Cost getCost(int componentId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Cost cost = null;

		String query = "(SELECT`irt`.part_number(`part_number`)AS`pn`," +
								"0 AS`alt_comp_id`," +
								"`description`," +
								"`m`.`name`AS`mfr`," +
								"`c`.`manuf_part_number`AS`mfrPN`," +
								"`c`.`qty`AS`qty`," +
								"0 AS`mfrPNId`," +
								"`co`.`for`," +
								"`co`.`cost`," +
								"`cm`.`id`AS`CompanyId`," +
								"`cm`.`company`AS`companyName`" +
							"FROM`irt`.`components`AS`c`" +
						"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`c`.`manuf_id`" +
						"LEFT JOIN`irt`.`cost`AS`co`ON`co`.`id_components`=`c`.`id`AND`co`.`id_components_alternative`=0 " +
						"LEFT JOIN`irt`.`companies`AS`cm`ON`cm`.`id`=`co`.`id_companies`" +
						"WHERE`c`.`id`="+componentId+")" +

						"UNION" +

						"(SELECT IF(`alt`.`id`IS NULL,`irt`.part_number(`c`.`part_number`),`irt`.part_number(`alt`.`part_number`))AS`pn`," +
								"`alt`.`id`AS`alt_comp_id`," +
								"IF(`alt`.`id`IS NULL,`c`.`description`,`alt`.`description`)," +
								"`m`.`name`AS`mfr`," +
								"`alt_mfr_part_number`AS`mfrPN`," +
								"IF(`alt`.`id`IS NULL,`c`.`qty`,`alt`.`qty`)AS`qty`," +
								"`ca`.`id`AS`mfrPNId`," +
								"`co`.`for`," +
								"`co`.`cost`," +
								"`cm`.`id`AS`CompanyId`," +
								"`cm`.`company`AS`companyName`" +
							"FROM`irt`.`components`AS`alt`" +
						"RIGHT JOIN`irt`.`components_alternative`AS`ca`ON`ca`.`alt_mfr_part_number`=`alt`.`manuf_part_number`" +
						"LEFT JOIN`irt`.`components`AS`c`ON`c`.`id`=`ca`.`id_components`" +
						"LEFT JOIN`irt`.`manufacture`AS`m`ON`m`.`id`=`ca`.`manuf_id`" +
						"LEFT JOIN`irt`.`cost`AS`co`ON`co`.`id_components`=`ca`.`id_components`AND`co`.`id_components_alternative`=`ca`.`id`OR((`co`.`id_components_alternative`=0 AND`c`.`id`!=`alt`.`id`AND`co`.`id_components`=`alt`.`id`))" +
						"LEFT JOIN`irt`.`companies`AS`cm`ON`cm`.`id`=`co`.`id_companies`" +
						"WHERE`ca`.`id_components`="+componentId+")" +
						"ORDER BY`pn`,`mfrPN`,`for`";
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			if(resultSet.next()){
				cost = new Cost(componentId, resultSet.getString("pn"), resultSet.getString("description"));
				do{
					String mfr = resultSet.getString("mfr");
					int companyId = resultSet.getInt("CompanyId");
					cost.add(new CostUnit(componentId,
							resultSet.getInt("alt_comp_id"),
							resultSet.getString("pn"),
							resultSet.getString("description"),
							new CostMfrPN(resultSet.getInt("mfrPNId"),
											mfr,
											resultSet.getString("mfrPN"),
											new CostCompany(companyId,
													companyId!=0 ? resultSet.getString("companyName") : mfr,
															new ForPrice(new Price(resultSet.getLong("cost")),
																	resultSet.getInt("for")))),
							resultSet.getInt("qty")));
				}while(resultSet.next());
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "CostDAO.getCost 2");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return cost;
	}

	public boolean hasCost(int componentId) {

		String query = "SELECT*FROM`irt`.`cost`WHERE`id_components`="+componentId;
		return isResult(query);
	}

	public CostSetUnit[] getCostSetUnits(int topComponentId, int setId) {
		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;
		CostSetUnit[] csu = null;

		String query = "SELECT*FROM`irt`.`cost_sets`WHERE`set_id`="+setId+" AND`id_top_component`="+topComponentId;
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			resultSet = statement.executeQuery(query);

			if(resultSet.last()){
				int row = resultSet.getRow();
				csu = new CostSetUnit[row];
				do
					csu[--row]= new CostSetUnit(resultSet.getInt("id_components"), resultSet.getInt("mfr_part_number"), resultSet.getInt("vendor"), resultSet.getInt("moq"));
				while(resultSet.previous());
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "CostDAO.getCostSetUnits");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return csu;
	}

	public boolean hasSet(int setId, int top_componentId) {

		String query = "SELECT*FROM`irt`.`cost_sets`WHERE`set_id`="+setId+" AND`id_top_component`="+top_componentId+" LIMIT 1";
		return isResult(query);
	}
}
