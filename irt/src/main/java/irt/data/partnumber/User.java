package irt.data.partnumber;


import java.util.Date;



public class User implements Comparable<Object>{
	private String userName;
	private String fullName;
	private PartNumber partNumber;
	private long time;

//	private Logger logger = Logger.getLogger(this.getClass());

	public User(String userName, PartNumber partNumbers) {
		this.userName = userName;
		this.partNumber = partNumbers;
		this.time = new Date().getTime();
	}

	public long getTime() {
		return time;
	}

	public String getUserName() {
		return userName;
	}

	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void resetTime() {
		this.time = new Date().getTime();
	}

	@Override
	public int compareTo(Object o) {
		if(getTime() < ((User)o).getTime())
			return -1;
		else if(getTime() > ((User)o).getTime())
			return 1;
		else
			return 0;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", partNumber=" + partNumber.getClassId()
				+ ", time=" + time + "]";
	}

	public String getFullName() {
		return fullName!=null ? fullName : userName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return userName==null ? super.hashCode() : userName.hashCode();
	}
}
