package us.supercheng.utils;

import java.util.List;

public class PagedResult {
	
	private int page;			// curr page number
	private int total;			// total pages
	private long records;		// total record counts
	private List<?> rows;		// data collection

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public long getRecords() {
		return records;
	}
	public void setRecords(long records) {
		this.records = records;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
}
