package irt.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class ToDoClass {

	private static final Logger logger = (Logger) LogManager.getLogger();

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

	public static ToDoClass parseToDoClass(String toDoClassStr){
		logger.entry(toDoClassStr);

		ToDoClass toDoClass;

		if(toDoClassStr!=null && toDoClassStr.startsWith(ToDoClass.class.getSimpleName())){

			try{
				toDoClassStr = toDoClassStr.substring(toDoClassStr.indexOf('[')+1, toDoClassStr.lastIndexOf(']'));
				String value = toDoClassStr.substring(toDoClassStr.indexOf('{')+1, toDoClassStr.lastIndexOf('}'));
				String command = toDoClassStr.substring(0, toDoClassStr.indexOf(',')).split("=")[1];
				toDoClass = new ToDoClass(ToDo.valueOf(command), value);

				logger.trace("toDoClassStr = '{}'\n\tcommand={}\n\tvalue={}", toDoClassStr, command, value);

			}catch(StringIndexOutOfBoundsException ex){
				toDoClass = new ToDoClass();
				logger.catching(ex);
			}
//			Map<String, String> map = new HashMap<>();
//			for(String s:toDoClassStr.split(", ")){
//				String[] split = s.split("=");
//				map.put(split[0], split[1]);
//				logger.trace("s={}; split={}", s, split);
//			}
//
//			logger.trace("{}", map);

		}else
			toDoClass = new ToDoClass();

		return logger.exit(toDoClass);
	}

	@Override
	public String toString() {
		return ToDoClass.class.getSimpleName()+" [command=" + command + ", value={" + value + "}]";
	}
}
