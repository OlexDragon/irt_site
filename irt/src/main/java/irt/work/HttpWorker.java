package irt.work;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class HttpWorker {

	public static Long[] getIds(HttpServletRequest request) {
		String componentsIdsStr = request.getParameter("ids");
		List<Long> componentsIds = new ArrayList<>();

		if(componentsIdsStr!=null)
		for (String s : componentsIdsStr.split(",")) {
			Long componentId = getComponentId(s);
			if (componentId != null)
				componentsIds.add(componentId);
		}

		return componentsIds.isEmpty() ? null : componentsIds.toArray(new Long[componentsIds.size()]);
	}

	public static Long getId(HttpServletRequest request) {
		return getComponentId(request.getParameter("id"));
	}

	private static Long getComponentId(String componentIdStr) {
		Long componentId = null;
		if(componentIdStr!=null && !(componentIdStr = componentIdStr.replaceAll("\\D", "")).isEmpty())
			componentId = Long.parseLong(componentIdStr);
		return componentId;
	}
}
