package irt.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToDoClass {

	private static final Logger logger = LogManager.getLogger();

	public enum ToDo {
	    NO_COMMAND,
	    PRICE,
	    SEARCH,
	    PROJECT_SERARCH
	}
	private ToDo command;
	private String value;

	public ToDoClass(ToDo command, Object value){
		logger.trace("public ToDoClass({}, {})", command, value);
		this.command = command;
		this.value = value.toString();
	}

	public ToDoClass() {
		logger.trace("public ToDoClass()");
		command = ToDo.NO_COMMAND;
		value = "";
	}

	public ToDo getCommand() {
		return command;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ToDoClass parseToDoClass(String toDoClassStr){
		logger.entry(toDoClassStr);

		ToDoClass toDoClass = null;

		if(toDoClassStr!=null && toDoClassStr.startsWith(ToDoClass.class.getSimpleName())){

			try{
				int indexOf = toDoClassStr.indexOf('[');
				int lastIndexOf = toDoClassStr.lastIndexOf(']');
				if(indexOf>0 && lastIndexOf>0){
					toDoClassStr = toDoClassStr.substring(indexOf+1, lastIndexOf);

					indexOf = toDoClassStr.indexOf('{');
					lastIndexOf = toDoClassStr.lastIndexOf('}');
					if(indexOf>=0 && lastIndexOf>0){
						String value = toDoClassStr.substring(indexOf+1, lastIndexOf);
						indexOf = toDoClassStr.indexOf(',');
						if(indexOf>0){
							String[] split = toDoClassStr.substring(0, indexOf).split("=");
							if(split.length>1){
								String command = split[1];
								toDoClass = new ToDoClass(ToDo.valueOf(command), value);
								logger.trace("toDoClassStr = '{}'\n\tcommand={}\n\tvalue={}", toDoClassStr, command, value);
							}
						}
					}
				}


			}catch(StringIndexOutOfBoundsException ex){
				logger.catching(ex);
			}
		}

		if(toDoClass == null)
			toDoClass = new ToDoClass();

		return logger.exit(toDoClass);
	}

	@Override
	public String toString() {
		return ToDoClass.class.getSimpleName()+" [command=" + command + ", value={" + value + "}]";
	}
}
