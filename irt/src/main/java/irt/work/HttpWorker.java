package irt.work;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class HttpWorker {

	public static Integer[] getIds(HttpServletRequest request) {
		String componentsIdsStr = request.getParameter("ids");
		List<Integer> componentsIds = new ArrayList<>();

		if(componentsIdsStr!=null)
		for (String s : componentsIdsStr.split(",")) {
			Integer componentId = getComponentId(s);
			if (componentId != null)
				componentsIds.add(componentId);
		}

		return componentsIds.isEmpty() ? null : componentsIds.toArray(new Integer[componentsIds.size()]);
	}

	public static Integer getId(HttpServletRequest request) {
		return getComponentId(request.getParameter("id"));
	}

	private static Integer getComponentId(String componentIdStr) {
		Integer componentId = null;
		if(componentIdStr!=null && !(componentIdStr = componentIdStr.replaceAll("\\D", "")).isEmpty())
			componentId = Integer.parseInt(componentIdStr);
		return componentId;
	}
}
