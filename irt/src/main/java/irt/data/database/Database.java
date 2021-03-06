package irt.data.database;

import irt.data.dao.DatabaseDAO;
import irt.work.TextWorker;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Database implements Runnable {

	private boolean running = true;
	private String databaseName;
	private DatabaseDAO databaseDAO = new DatabaseDAO();

	public Database(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public void run() {
		while(running){
			synchronized (this) {

				Calendar calendar = Calendar.getInstance();
				File file = new File("c:"+File.separator+"db"+File.separator+databaseName+File.separator
						+ calendar.get(Calendar.YEAR)
						+ TextWorker.addZeroInFront(""+(calendar.get(Calendar.MONTH)+1),2)
						+ TextWorker.addZeroInFront(""+calendar.get(Calendar.DATE),2)
						+ ".sql");

				file.getParentFile().mkdirs();

				try {

					if(file.createNewFile()) {
						databaseDAO.writeToFile(file, databaseName);
					}

				} catch (IOException e1) { e1.printStackTrace(); }

				try { wait(1000*60*60*24); /* 24 hours */ } catch (InterruptedException e) {  e.printStackTrace(); }
			}
		}
	}

}
