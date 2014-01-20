package ibd.web.DataObjects;

import org.joda.time.LocalDate;

public class IndexAnalysisRow {
	private int id;
	private LocalDate date;
	private boolean isDDay;
	private int dDayCounter;
	//add more stuff as needed here

	public IndexAnalysisRow() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setDate(java.sql.Date date) {
		this.date = new LocalDate(date);
	}
	public void setDate(String date) {
		this.date = new LocalDate(date);
	}

	/**
	 * @return the isDDay
	 */
	public boolean isDDay() {
		return isDDay;
	}
	/**
	 * @param isDDay the isDDay to set
	 */
	public void setDDay(boolean isDDay) {
		this.isDDay = isDDay;
	}

	/**
	 * @return the dDayCounter
	 */
	public int getdDayCounter() {
		return dDayCounter;
	}
	/**
	 * @param dDayCounter the dDayCounter to set
	 */
	public void setdDayCounter(int dDayCounter) {
		this.dDayCounter = dDayCounter;
	}

}
