package irt.web.view;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ComponentMovementView {

	private Long from;
	private String fromDetailsTableName;
	private Long to;
	private String toDetailsTableName;

	public ComponentMovementView() { }

	public ComponentMovementView(ComponentMovementView movementView) {
		from = movementView.getFrom();
		fromDetailsTableName = movementView.getFromDetailsTableName();
		to = movementView.getTo();
		toDetailsTableName = movementView.getToDetailsTableName();
	}

	public Long getFrom() {
		return from;
	}
	public void setFrom(Long id) {
		this.from = id;
	}
	public Long getTo() {
		return to;
	}
	public String getFromDetailsTableName() {
		return fromDetailsTableName;
	}

	public void setFromDetailsTableName(String fromDetailsTableName) {
		this.fromDetailsTableName = fromDetailsTableName;
	}

	public String getToDetailsTableName() {
		return toDetailsTableName;
	}

	public void setToDetailsTableName(String toDetailsTableName) {
		this.toDetailsTableName = toDetailsTableName;
	}

	public void setTo(Long to) {
		this.to = to;
	}
	@Override
	public String toString() {
		return "\n\tComponentMovementView [\n\t\t"
				+ "from=" + from + ",\n\t\t"
				+ "fromDetils=" + fromDetailsTableName + ",\n\t\t"
				+ "to=" + to + ",\n\t\t"
				+ "toDetils=" + toDetailsTableName + ",\n\t\t"
				+ "]";
	}
}
