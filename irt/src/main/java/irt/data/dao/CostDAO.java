package irt.data.dao;

import irt.data.purchase.CostBean;
import irt.data.purchase.CostCompanyBean;
import irt.data.purchase.CostCompanyService;
import irt.data.purchase.CostMfrPNBean;
import irt.data.purchase.CostMfrPNService;
import irt.data.purchase.CostService;
import irt.data.purchase.CostSetUnit;
import irt.data.purchase.CostUnitBean;
import irt.data.purchase.CostUnitService;
import irt.data.purchase.ForPriceBean;
import irt.data.purchase.ForPriceService;
import irt.data.purchase.ForPriceService.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CostDAO extends DataAccessObject {

	private static final String UPDATE = "UPDATE`irt`.`cost` SET `cost`=? "
										+ "WHERE `id_components`=? and" +
												"`id_components_alternative`=? and" +
												"`id_companies`=? and" +
												"`for`=?";
	private static final String INSERT_INTO = "INSERT INTO`irt`.`cost`("+
																"`cost`," +
																"`id_components`," +
																"`id_components_alternative`," +
																"`id_companies`," +
																"`for`)" +
															"VALUES(?,?,?,?,?)";
	private static final String EXISTS = "SELECT*FROM`irt`.`cost`" +
															"WHERE `id_components`=? and" +
															"`id_components_alternative`=? and" +
															"`id_companies`=? and" +
															"`for`=?";

	public int save(List<CostUnitBean> costUnits) {
		logger.entry(costUnits);

		int executedUpdate = 0;
		try(Connection conecsion = getDataSource().getConnection();
				PreparedStatement statementInsert = conecsion.prepareStatement(INSERT_INTO);
				PreparedStatement statementUpdate = conecsion.prepareStatement(UPDATE);) {
	
			PreparedStatement statement;

			for(CostUnitBean cu:costUnits){
				logger.trace("\n\tfor(CostUnitBean {}:costUnits)", cu);
				for(CostMfrPNBean cmpn:cu.getMfrPartNumbers())
					if(CostMfrPNService.getForPrices(cmpn)!=null)
						for(ForPriceBean fp:CostMfrPNService.getForPrices(cmpn)){
							Status status = ForPriceService.getStatus(fp);
							logger.trace("\n\t"
									+ "for(ForPriceBean\t{}:CostMfrPNService.getForPrices(cmpn))\n\t"
									+ "status:\t{}",
									fp,
									status);
							if(status == Status.HAVE_TO_INSERT)
								statement = statementInsert;
							else if(status== Status.HAVE_TO_UPDATE)
								statement = statementUpdate;
							else continue;

							statement.setBigDecimal(1, fp.getPrice());
							statement.setInt(2, cu.getComponentId());
							statement.setInt(3, cmpn.getAlternativeUnit()==null ? cmpn.getId() : 0);
							statement.setInt(4, CostMfrPNService.getCompanyId(cmpn));
							statement.setInt(5, fp.getForUnits());
							executedUpdate = statement.executeUpdate();
							ForPriceService.setPrice(fp);
						}
					else
						logger.warn("\n\tCostMfrPNService.getForPrices(cmpn)==null");
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "CostDAO.save(List<CostUnitBean> costUnits)");
			throw new RuntimeException(e);
		}
		return executedUpdate;
	}

	public int save(Map<Integer, CostCompanyBean> costCompanyBeans) {
		logger.entry(costCompanyBeans);
		Set<Integer> componentIds = costCompanyBeans.keySet();
		int executedUpdate = 0;
		if(!componentIds.isEmpty()){
			try(Connection conecsion = getDataSource().getConnection();
					PreparedStatement statementExists = conecsion.prepareStatement(EXISTS);
					PreparedStatement statementInsert = conecsion.prepareStatement(INSERT_INTO);
					PreparedStatement statementUpdate = conecsion.prepareStatement(UPDATE);) {

				PreparedStatement statement;

				for(Integer componentId:componentIds){
					CostCompanyBean costCompanyBean = costCompanyBeans.get(componentId);
					if(costCompanyBean!=null)
						for (ForPriceBean forPriceBean : costCompanyBean.getForPriceBeans()) {

							statementExists.setInt(1, componentId);
							statementExists.setInt(2, 0);
							statementExists.setInt(3, costCompanyBean.getId());
							statementExists.setInt(4, forPriceBean.getForUnits());

							if (isResult(statementExists))
								statement = statementUpdate;
							else
								statement = statementInsert;

							logger.trace("\n\t{}\n\t{}", statement, forPriceBean);

							statement.setBigDecimal(1, forPriceBean.getPrice());
							statement.setInt(2, componentId);
							statement.setInt(3, 0);
							statement.setInt(4, costCompanyBean.getId());
							statement.setInt(5, forPriceBean.getForUnits());
							executedUpdate = statement.executeUpdate();
						}
				}
			} catch (SQLException e) {
				new ErrorDAO().saveError(e, "CostDAO.save(Map<Integer, CostCompanyBean> costCompanyBeans)");
				throw new RuntimeException(e);
			}
		}
		return executedUpdate;
	}

	public void saveSet(int setId, int topComponentId, List<CostUnitBean> costUnits){
		Connection conecsion = null;
		Statement statement = null;

		if(setId>0 && topComponentId>0)
		try {
			String query = "DELETE FROM `irt`.`cost_sets` WHERE `set_id`="+setId+" and`id_top_component`="+topComponentId;

			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();
			statement.executeUpdate(query);

			for(CostUnitBean cu:costUnits){
				int selectedForPrice = CostUnitService.getSelectedForPrice(cu);
				if(selectedForPrice<0)
					selectedForPrice = 0;
				if(CostUnitService.getSelectedMfrPN(cu)>0 || CostUnitService.getSelectedCompany(cu)>0 || selectedForPrice>0){
					query = "INSERT INTO`irt`.`cost_sets`(`set_id`,`id_top_component`,`id_components`,`mfr_part_number`,`vendor`,`moq`)"+
						"VALUES ("+setId+","+topComponentId+","+cu.getComponentId()+","+CostUnitService.getSelectedMfrPN(cu)+","+CostUnitService.getSelectedCompany(cu)+","+selectedForPrice+")";

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

	/**
	 * @param topComponentIdStr
	 * @return Top Level CostService
	 */
	public CostService getCostService(String topComponentIdStr){
		CostService cost = null;


		String query = "SELECT`id`," +
								"`irt`.part_number(part_number)AS`pn`," +
								"`description`" +
							"FROM`irt`.`components`" +
						"WHERE`id`=?";
		int id = -1;
		String partNumberStr = null;
		String description = null;
		try(	Connection conecsion = getDataSource().getConnection();
				PreparedStatement statement = conecsion.prepareStatement(query);) {

			statement.setString(1, topComponentIdStr);
			
			try (ResultSet resultSet = statement.executeQuery();) {

				if (resultSet.next()) {
					id = resultSet.getInt("id");
					partNumberStr = resultSet.getString("pn");
					description = resultSet.getString("description");
				}
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

				logger.trace("\n\tQuery:\t{}", query);

				try(ResultSet resultSet = statement.executeQuery(query);){

				if(resultSet.next()){

					CostBean costBean = new CostBean().setId(id).setPartnamber(partNumberStr).setDescription(description);
					cost = new CostService(costBean).setComponentId(topComponentIdStr);

					do{
						ForPriceBean forPriceBean = new ForPriceBean().setPrice(resultSet.getBigDecimal("price")).setForUnits(resultSet.getInt("for"));

						CostCompanyBean costCompanyBean = new CostCompanyBean()
																.setId(resultSet.getInt("CompanyId"));
						CostCompanyService.addForPriceBean(costCompanyBean, forPriceBean); 

						CostMfrPNBean costMfrPNBean = new CostMfrPNBean()
															.setId(resultSet.getInt("mfrPNId"))
															.setMfr(resultSet.getString("mfr"))
															.setMfrPN(resultSet.getString("mfrPN"));
						costMfrPNBean.getCostCompanyBeans().add(costCompanyBean);

						CostUnitBean costUnitBean = new CostUnitBean()
											.setComponentId(resultSet.getInt("componentId"))
											.setAlternativeComponentId(resultSet.getInt("alt_comp_id"))
											.setPartNumberStr(resultSet.getString("pn"))
											.setDescription(resultSet.getString("description"))
											.setQty(resultSet.getInt("qty"));
						costUnitBean.getMfrPartNumbers().add(costMfrPNBean);
						
						cost.add(costUnitBean);
					}while(resultSet.next());
					cost.reset();
				}
			}
			}
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "CostDAO.getCost");
			throw new RuntimeException(e);
		}
		
		return cost;
	}

	/**
	 * 
	 * @param componentId
	 * @return Component's CostService
	 */
	public CostService getCost(int componentId) {
		logger.entry(componentId);
		CostService cost = null;

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
						"WHERE`c`.`id`=?)" +

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
						"WHERE`ca`.`id_components`=?)" +
						"ORDER BY`pn`,`mfrPN`,`for`";

		logger.debug("\n\tQoery:\t{}\n\t? =\t{}", query, componentId);

		try(	Connection connection = getDataSource().getConnection();
				PreparedStatement statement = connection.prepareStatement(query);) {

			statement.setInt(1, componentId);
			statement.setInt(2, componentId);
			try(ResultSet resultSet = statement.executeQuery();){

			if(resultSet.next()){
				CostBean costBean = new CostBean()
											.setId(componentId)
											.setPartnamber(resultSet.getString("pn"))
											.setDescription(resultSet.getString("description"));
				cost = new CostService(costBean);
				do{
					int companyId = resultSet.getInt("CompanyId");

					CostUnitBean costUnitBean = new CostUnitBean()
								.setComponentId(componentId)
								.setAlternativeComponentId(resultSet.getInt("alt_comp_id"))
								.setPartNumberStr(resultSet.getString("pn"))
								.setDescription(resultSet.getString("description"))
								.setQty(resultSet.getInt("qty"));
					CostMfrPNBean costMfrPNBean = new CostMfrPNBean()
													.setId(resultSet.getInt("mfrPNId"))
													.setMfrPN(resultSet.getString("mfrPN"));
					CostCompanyBean costCompanyBean = new CostCompanyBean()
																	.setId(companyId);
					CostCompanyService.addForPriceBean(costCompanyBean, new ForPriceBean()
																				.setPrice(resultSet.getBigDecimal("cost"))
																				.setForUnits(resultSet.getInt("for")));
					costMfrPNBean.getCostCompanyBeans().add(costCompanyBean);
					costUnitBean.getMfrPartNumbers().add(costMfrPNBean);

					cost.add(costUnitBean);
				}while(resultSet.next());
			}
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "CostDAO.getCost(int componentId)");
			throw new RuntimeException(e);
		}

		return logger.exit(cost);
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
