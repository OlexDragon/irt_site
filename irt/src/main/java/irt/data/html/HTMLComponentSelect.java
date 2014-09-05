package irt.data.html;

import java.util.ArrayList;
import java.util.List;

public class HTMLComponentSelect {

	private String id;
	private String name;
	private String javaScript;
	private String firstLine;

	private List<HTMLComponentOption> htmlComponentOptions = new ArrayList<>();

	public String getId() {
		return id;
	}

	public HTMLComponentSelect setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public HTMLComponentSelect setName(String name) {
		this.name = name;
		return this;
	}

	public String getJavaScript() {
		return javaScript;
	}

	public HTMLComponentSelect setJavaScript(String javaScript) {
		this.javaScript = javaScript;
		return this;
	}

	public String getFirstLine() {
		return firstLine;
	}

	public HTMLComponentSelect setFirstLine(String firstLine) {
		this.firstLine = firstLine;
		return this;
	}

	public boolean add(HTMLComponentOption htmlComponentOption) {
		boolean added = false;
		if(!htmlComponentOptions.contains(htmlComponentOption)) {
			added = htmlComponentOptions.add(htmlComponentOption);
		}
		return added;
	}

	public List<HTMLComponentOption> getHtmlComponentOptions() {
		return htmlComponentOptions;
	}

	@Override
	public String toString() {
		StringBuilder select = new StringBuilder("<select");

		if(id!=null){
			select.append(" id='");
			select.append(id);
			select.append("'");
		}

		if(name!=null){
			select.append(" name='");
			select.append(name);
			select.append("'");
		}

		if(javaScript!=null){
			select.append(' ');
			select.append(javaScript);
		}


		select.append('>');

		for(HTMLComponentOption o:htmlComponentOptions)
			select.append(o.toString());

		select.append("</select>");

		return select.toString();
	}
}
