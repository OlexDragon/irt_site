package irt.web;

import irt.data.dao.DataAccessObject;
import irt.data.database.Database;
import irt.data.user.UserBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;



public class Init implements ServletContextListener{

//    private Logger logger = Logger.getLogger(this.getClass());

	public static final int MSIE	= 1;//Internet Explorer
	public static final int CHROME	= 2;
	public static final int FIREFOX = 3;

   @Override
	public void contextInitialized(ServletContextEvent sce) {
    	
//		logger.debug("Initialization ...");
        InitialContext initialContext;
        
		try {
			initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:comp/env");
			DataAccessObject.setDataSource((DataSource) context.lookup("datasource"));

			Thread t = new Thread(new Database("irt"));
			int priority = t.getPriority()-1;
			t.setPriority(priority>Thread.MIN_PRIORITY ? priority : Thread.MIN_PRIORITY);
			t.start();

		} catch (Exception e) {
//			logger.debug("Initialization error.");
			throw new RuntimeException(e);
		}
//		logger.debug("Initialization succeeded.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}

	public static UserBean getUserBean(HttpServletRequest request) throws IOException {

		HttpSession session = request.getSession(false);
		UserBean userBean = null;

		if(session!=null)
			userBean = (UserBean)session.getAttribute("user");

		return userBean;
	}

	public static List<String> getFieldsNames(HttpServletRequest request, String startWith) {
		ArrayList<String> fieldsNames = new ArrayList<>();
		String str;
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements())
			if((str = names.nextElement()).startsWith(startWith))
				fieldsNames.add(str);

		return fieldsNames;
	}

	public static String getFieldName(HttpServletRequest request, String contains) {
		String name = null;
		Enumeration<String> names = request.getParameterNames();
		String n;
		while(names.hasMoreElements())
			if((n = names.nextElement()).contains(contains)){
				name = n;
				break;
			}
		return name;
	}

	public static String getSubmitButton(HttpServletRequest request) {
		String str = "";
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements())
			if((str = names.nextElement()).contains("submit"))
				break;
		return str;
	}

	public static String getFieldNameWithValue(HttpServletRequest request, String nameContains, String value) {
		String name = null;
		List<String> names = getFieldsNames(request, nameContains);

		for(String n:names)
			if(request.getParameter(n).equals(value)){
				name = n;
				break;
			}
		return name;
	}
}
