package irt.data.purchase;

import java.util.HashMap;
import java.util.Map;

public class CostCompanyBeanWrapper {

	private Map<Integer, CostCompanyBean> costCompanyBeans = new HashMap<>();

	public Map<Integer, CostCompanyBean> getCostCompanyBeans() {
		return costCompanyBeans;
	}

	public void setCostCompanyBeans(Map<Integer, CostCompanyBean> costCompanyBeans) {
		this.costCompanyBeans = costCompanyBeans;
	}

	public CostCompanyBeanWrapper set(int componentId, CostCompanyBean costCompanyBean){
		costCompanyBeans.put(componentId, costCompanyBean);
		return this;
	}
}
