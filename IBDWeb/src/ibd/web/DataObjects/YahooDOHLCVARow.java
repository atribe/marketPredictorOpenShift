/**
 * 
 */
package ibd.web.DataObjects;

import java.util.Date;

import org.joda.time.LocalDate;

/**
 * @author Allan
 * This stands for each column heading in the download for each index
 * Date, Open, High, Low, Close, Volume, Adjusted Close
 */
public class YahooDOHLCVARow {

	private String date;
	private double Open;
	private double High;
	private double Low;
	private double Close;
	private long Volume;
	private double AdjClose;
	
	private LocalDate convertedDate;
	
	@Override
    public String toString() {
		return "Date: " + getDate() 
				+ "\nOpen: " + getOpen() 
				+ "\nHigh: " + getHigh()
				+ "\nLow: " + getLow()
				+ "\nClose: " + getClose()
				+ "\nVolume: " + getVolume();
	}

	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
		setConvertedDate(date);
	}
	
	/**
	 * @return the open
	 */
	public double getOpen() {
		return Open;
	}
	/**
	 * @param open the open to set
	 */
	public void setOpen(double open) {
		Open = open;
	}
	
	/**
	 * @return the high
	 */
	public double getHigh() {
		return High;
	}
	/**
	 * @param high the high to set
	 */
	public void setHigh(double high) {
		High = high;
	}
	
	/**
	 * @return the low
	 */
	public double getLow() {
		return Low;
	}
	/**
	 * @param low the low to set
	 */
	public void setLow(double low) {
		Low = low;
	}
	
	/**
	 * @return the close
	 */
	public double getClose() {
		return Close;
	}
	/**
	 * @param close the close to set
	 */
	public void setClose(double close) {
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
	
	/**
	 * @return the adjClose
	 */
	public double getAdjClose() {
		return AdjClose;
	}
	/**
	 * @param adjClose the adjClose to set
	 */
	public void setAdjClose(double adjClose) {
		AdjClose = adjClose;
	}
	/**
	 * @return the convertedDate
	 */
	public LocalDate getConvertedDate() {
		return convertedDate;
	}
	/**
	 * @param convertedDate the convertedDate to set
	 */
	public void setConvertedDate(LocalDate convertedDate) {
		this.convertedDate = convertedDate;
	}
	
	public void setConvertedDate(String date) {
		this.convertedDate = new LocalDate(date);
	}
	
	
}
