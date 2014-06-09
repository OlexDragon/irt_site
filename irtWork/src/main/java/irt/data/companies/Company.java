package irt.data.companies;

import irt.work.ComboBoxField;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Company implements ComboBoxField{

	public static final byte COMPANY	= -1;
	public static final byte ALL		= -1;
	public static final int DETAIL_NON = 0;
	public static final byte STOCK		= 1;
	public static final byte ASSEMBLED	= 3;
	public static final byte TYPE_CM	= 4;
	public static final byte TYPE_VENDOR= 5;
	public static final byte KIT 		= 6;
	public static final byte BULK 		= 7;

	private int		id;
	private String	name;
	private String	newName;
	private String	companyName;
	private String	newCompanyName;
	private String	eMail;
	private String	newEMail;
	private String	telephone;
	private String	newTelephone;
	private String	fax;
	private String	newFax;
	private String	address;
	private String	newAddress;
	private int		type;
	private boolean isActive;
	private boolean isNewActive;

	public Company(ResultSet resultSet) throws SQLException {
		this( 	resultSet.getInt( "id"), 
				resultSet.getString( "company"	), 
				resultSet.getString( "name"), 
				resultSet.getString( "e-mail"), 
				resultSet.getString( "telephone"), 
				resultSet.getString( "fax"), 
				resultSet.getString( "address"), 
				resultSet.getInt( "type"), 
				resultSet.getBoolean( "status"));
		setCompanyName();
		setName();
		setEMail();
		setTelephone();
		setFax();
		setAddress();
		setActive();

	}

	public Company(int id, String companyName, String seller_name, String e_mail,String telephone, String fax, String addtess,int type, boolean isActive) {
		this.id	= id;
		set(companyName,seller_name,e_mail,telephone,fax,addtess,type,isActive);
	}

	public Company(int companyId) {
		id = companyId;
	}

	public void set(String companyName, String seller_name, String e_mail,String telephone, String fax, String addtess,int type, boolean isActive) {
		setName(seller_name);
		setCompanyName(companyName);
		setEMail(e_mail);
		this.type		= type;
		setActive(isActive);
		setTelephone(telephone);
		setFax(fax);
		setAddress(addtess);
	}

	public String	setEMail(){
		eMail = newEMail;
		newEMail = null;
		return eMail;
	}
	public void	setEMail(String eMail){	

		if (eMail != null && !(eMail=eMail.trim()).isEmpty() && !eMail.equals(this.eMail)) {
			int index = eMail.indexOf('@');
			if (index > 0 && eMail.indexOf('.', index) > 0)
				newEMail = eMail;
		}
	}

	public String	getEMail()	{
		return newEMail!=null ? newEMail : eMail!=null	? eMail		: "";
	}

	public String setName(){
		name = newName;
		newName = null;
		return name;
	}
	public void	setName(String name){
		if(name!=null && !(name=name.trim()).isEmpty() && !name.equals(this.name))
			newName = name;
	}
	public String getName(){
		return newName!=null ? newName : name!=null	? name : "";
	}

	public String	setCompanyName(){
		companyName = newCompanyName;
		newCompanyName = null;
		return companyName;
	}

	public void	setCompanyName(String companyName){
		if(companyName!=null &&!(companyName=companyName.trim()).isEmpty() && !companyName.equals(this.companyName))
			newCompanyName = companyName;
	}
	public String getCompanyName(){
		return newCompanyName!=null ? newCompanyName : companyName!=null ? companyName : "";
	}

	public boolean	setActive(){
		isActive = isNewActive;
		return isActive;
	}
	public void	setActive(boolean isActive){
		if(isActive!=this.isActive)
			isNewActive = isActive;
	}
	public boolean isActive(){ return isNewActive;	}

	public int		getId()		{ return id;	}
	public int		getType()	{ return type;	}

	public int	setId(int id){
		return this.id = id;
	}

	public boolean isSet()	 { return  getName()!=null && getCompanyName()!=null  && getEMail()!=null;}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "id=" + id + ",name=" + name + ",companyName="
				+ companyName + ",eMail=" + eMail + ",telephone=" + telephone
				+ ",fax=" + fax + ",address=" + address + ",type=" + type
				+ ",isActive=" + isActive;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return id>0 ? id : super.hashCode();
	}

	@Override
	public String getValue() {
		return ""+id;
	}

	@Override
	public String getText() {
		return getCompanyName();
	}

	public String setFax() {
		fax = newFax;
		newFax = null;
		return fax;
	}

	public void setFax(String fax) {
		if(fax!=null){
			String replaceAll = fax.replaceAll("[\\D]", "");
			if(!replaceAll.isEmpty() && (this.fax==null || !replaceAll.equals(this.fax.replaceAll("[\\D]", ""))))
				newFax = fax;
		}
	}

	public boolean isNewFax() {
		return newFax!=null;
	}

	public String getFax() {
		return newFax==null ? fax!=null ? fax : "" : newFax;
	}

	public String setAddress() {
		address = newAddress;
		newAddress = null;
		return address;
	}

	public String getAddress() {
		return newAddress==null ? address!=null ? address : "" : newAddress;
	}

	public boolean isInsertFax(){
		return newFax!=null && fax==null;
	}

	public boolean isInsertAddress(){
		return newAddress!=null && address==null;
	}

	public boolean isUpdateFax(){
		return newFax!=null && fax!=null ;
	}

	public boolean isUpdateAddress(){
		return newAddress!=null && address!=null;
	}

	public String getTelephone() {
		return newTelephone==null ? telephone!=null ? telephone : "" : newTelephone;
	}

	public String setTelephone() {
		telephone = newTelephone;
		newTelephone = null;
		return telephone;
	}

	public void setTelephone(String telephone) {
		if(telephone!=null){
			String replaceAll = telephone.replaceAll("[\\D]", "");
			if(!replaceAll.isEmpty() && (this.telephone==null || !replaceAll.equals(this.telephone.replaceAll("[\\D]", ""))))
				newTelephone = telephone;
		}
	}

	public boolean isInsertTelephone(){
		return newTelephone!=null && telephone==null;
	}

	public boolean isUpdateTelephone(){
		return newTelephone!=null && telephone!=null;
	}

	public void setAddress(String address) {
		if(address!=null && !address.isEmpty() && !address.equals(this.address))
			newAddress = address;
	}

	public String getWhereTelephone() {
		return " WHERE `id_companies`="+id+" and`telephone`='"+telephone+"'";
	}

	public String getWhereFax() {
		return " WHERE `id_companies`="+id+" and`fax`='"+fax+"'";
	}

	public String getWhereAddress() {
		return " WHERE `id_companies`="+id+" and`address`='"+address+"'";
	}

	public void cancel(){
		newName = newCompanyName = newEMail = newTelephone = newFax = newAddress = null;
		isNewActive = isActive;
	}

	public boolean isNewFields() {
		return newName!=null || newCompanyName!=null || newEMail!=null || isActive!=isNewActive;
	}

	public boolean isNewName() {
		return newName!=null;
	}

	public boolean isNewCompanyName() {
		return newCompanyName!=null;
	}

	public boolean isNewEMail() {
		return newEMail!=null;
	}

	public boolean isNewActive() {
		return isActive!=isNewActive;
	}

	public boolean isNewTelephone() {
		return newTelephone!=null;
	}

	public boolean isNewAddress() {
		return newAddress!=null;
	}
}
