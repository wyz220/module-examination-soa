/**
 * 
 */
package com.msht.examination.vo;

import java.util.List;

/**
 * 销售额以及充值金额走势统计
 * @author lindaofen
 *
 */
public class StatAmountTrendVo {

	private String title;
	
	private List<String> series;
	
	private List<AmountTrend> rows;

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

	public List<AmountTrend> getRows() {
		return rows;
	}

	public void setRows(List<AmountTrend> rows) {
		this.rows = rows;
	}
	
}
