package irt.data.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseFieldDescription {

	private String fieldName;
	private String type;
	private boolean notNull;
	private String key;
	private String defaultValue;
	private String extra;
	private String comment;

	public DatabaseFieldDescription(ResultSet resultSet) throws SQLException {
		setFieldName(resultSet.getString("Field"));
		setType(resultSet.getString("Type"));
		setNotNull(resultSet.getString("Null").equalsIgnoreCase("NO"));
		setPrimeryKey(resultSet.getString("Key"));
		setDefaultValue(resultSet.getString("Default"));
		extra		= resultSet.getString("Extra");
	}

	public String getFieldName()	{ return fieldName;	}
	public String getType()			{ return type;		}
	public String getKey()			{ return key;		}
	public String getDefaultValue() { return defaultValue;}
	public String getExtra()		{ return extra;		}
	public String getComment()		{ return comment;	}

	public void setFieldName	(String field		) { this.fieldName = field;			}
	public void setType			(String type		) { this.type = type;				}
	public void setPrimeryKey	(String key			) { this.key = key;					}
	public void setDefaultValue	(String defaultValue) { this.defaultValue = defaultValue;}
	public void setExtra		(String extra		) { this.extra = extra;				}
	public void setComment		(String comment		) {	this.comment = comment;			}
	public void setNotNull		(boolean notNull	) { this.notNull = notNull;			}

	public boolean isNotNull() { return notNull;	}

	@Override
	public String toString() {
		return "`"+getFieldName()
				+"`"+getType()
				+ ((getKey().equalsIgnoreCase("UNI") || getKey().equalsIgnoreCase("PRI") || isNotNull()) ? " NOT NULL" : "")
				+ ((getDefaultValue()!=null) ? " DEFAULT "+ getDefaultValue() : "")
				+ (extra!=null 	 ? " "+extra : "")
				+ (comment!=null ? " COMMENT'"+comment+"'" : "");
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return fieldName!=null ? fieldName.hashCode() : super.hashCode();
	}
}
