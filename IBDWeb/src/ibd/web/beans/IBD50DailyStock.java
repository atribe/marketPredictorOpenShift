package ibd.web.beans;

import java.util.Date;

public class IBD50DailyStock {
	private String name;
	private Date date;
	private Float Open;
	private Float High;
	private Float Low;
	private Float Close;
	private Float Volume;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Float getOpen() {
		return Open;
	}
	public void setOpen(Float open) {
		Open = open;
	}
	public Float getHigh() {
		return High;
	}
	public void setHigh(Float high) {
		High = high;
	}
	public Float getLow() {
		return Low;
	}
	public void setLow(Float low) {
		Low = low;
	}
	public Float getClose() {
		return Close;
	}
	public void setClose(Float close) {
		Close = close;
	}
	public Float getVolume() {
		return Volume;
	}
	public void setVolume(Float volume) {
		Volume = volume;
	}
	
}
