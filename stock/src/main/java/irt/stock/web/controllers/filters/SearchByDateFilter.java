package irt.stock.web.controllers.filters;
public class SearchByDateFilter {

	public SearchByDateFilter(SearchStyle createdType, String createdYear, String createdMonth, String createdDay) {
	}

	public boolean isUseful() {
		return false;
	}

	public enum SearchStyle{
		BEFORE,
		EXACT,
		AFRER,
		BETWEEN
	}
}
