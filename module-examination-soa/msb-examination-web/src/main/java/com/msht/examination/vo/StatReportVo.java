/**
 * 
 */
package com.msht.examination.vo;

import java.util.List;

/**
 * @author lindaofen
 *
 */
public class StatReportVo {

	private String title;
	
	private List<String> series;
	
	private List<StatTotal> rows;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getSeries() {
		return series;
	}

	public void setSeries(List<String> series) {
		this.series = series;
	}

	public List<StatTotal> getRows() {
		return rows;
	}

	public void setRows(List<StatTotal> rows) {
		this.rows = rows;
	}
	
	
}
