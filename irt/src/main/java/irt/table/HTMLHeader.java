package irt.table;

public class HTMLHeader {

	private String text;
	private String htmlClassName;
	private int size = 1;

	public String getText() {
		return text;
	}

	public HTMLHeader(String text, String htmlClassName, int size) {
		super();
		setText(text);
		this.htmlClassName = htmlClassName;
		this.size = size;
	}

	public void setText(String text) {
		if(text.contains("&"))
			this.text = text.replace("&", "&amp;");
		else
			this.text = text;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return text!=null ?  "<h"+size
						+ (htmlClassName!=null ? " class=\""+htmlClassName+"\"" : "")
						+" >"+text+"</h"+size+">" : "";
	}
}
