package ibd.web.DataObjects;

import org.joda.time.LocalDate;

public class IndexAnalysisRow {
	private int PVD_id;
	private LocalDate date;
	private float Open;
	private float High;
	private float Low;
	private float Close;
	private long Volume;
	private boolean isDDay;
	private int dDayCounter;
	//add more stuff as needed here

	public IndexAnalysisRow() {
		dDayCounter=0;
	}

	@Override
    public String toString() {
		return "\nid: " + getPVD_id()
				+ "\nDate: " + getDate().toString() 
				+ "\nIs D-Day: " + isDDay()
				+ "\nD-Day Count: " + getdDayCounter() + "\n";
	}
	
	/**
	 * @return the PVD_id
	 */
	public int getPVD_id() {
		return PVD_id;
	}
	/**
	 * @param id the id to set
	 */
	public void setPVD_id(int PVD_id) {
		this.PVD_id = PVD_id;
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
	 *  @return the open
	 */
	public float getOpen() {
		return Open;
	}
	/**
	 * @param open the open to set
	 */
	public void setOpen(float open) {
		Open = open;
	}
	
	/**
	 * @return the high
	 */
	public float getHigh() {
		return High;
	}
	/**
	 * @param high the high to set
	 */
	public void setHigh(float high) {
		High = high;
	}
	
	/**
	 * @return the low
	 */
	public float getLow() {
		return Low;
	}
	/**
	 * @param low the low to set
	 */
	public void setLow(float low) {
		Low = low;
	}
	
	/**
	 * @return the close
	 */
	public float getClose() {
		return Close;
	}
	/**
	 * @param close the close to set
	 */
	public void setClose(float close) {
		Close = close;
	}
	
	/**
	 * @return the volume
	 */
	public long getVolume() {
		return Volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(long volume) {
		Volume = volume;
	}
	public void setVolume(float volume) {
		Volume = Math.round(volume);
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
	public void addDDayCounter() {
		dDayCounter++;
	}
}
