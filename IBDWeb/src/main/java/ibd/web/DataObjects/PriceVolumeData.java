/**
 * 
 */
package ibd.web.DataObjects;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
/**
 * @author Allan
 *
 */
public class PriceVolumeData {
	private List<Integer>  m_id;
	private List<LocalDate>  m_date;
	private List<Float>  m_Open;
	private List<Float>  m_High;
	private List<Float>  m_Low;
	private List<Float>  m_Close;
	private List<Integer>  m_Volume;

	private int rowCount;


	/**
	 * Initializing all member variables
	 */
	public PriceVolumeData() {
		m_id = new ArrayList<Integer>();
		m_date= new ArrayList<LocalDate>();
		m_Open= new ArrayList<Float>();
		m_High= new ArrayList<Float>();
		m_Low= new ArrayList<Float>();
		m_Close= new ArrayList<Float>();
		m_Volume= new ArrayList<Integer>();
		rowCount = 0;
	}

	public List<Integer> getM_id() {
		return m_id;
	}
	public void setM_id(List<Integer> m_id) {
		this.m_id = m_id;
	}
	public void addNextId(int id){
		m_id.add(id);
	}
	public int getSingleId(int i) {
		return m_id.get(i);
	}

	public List<LocalDate> getM_date() {
		return m_date;
	}
	public void setM_date(List<LocalDate> m_date) {
		this.m_date = m_date;
	}
	public void addNextDate(LocalDate date) {
		m_date.add(date);
	}
	public LocalDate getSingleDate(int i) {
		return m_date.get(i);
	}

	public List<Float> getM_Open() {
		return m_Open;
	}
	public void setM_Open(List<Float> m_Open) {
		this.m_Open = m_Open;
	}
	public void addNextOpen(float Open) {
		m_Open.add(Open);
	}
	public float getSingleOpen(int i) {
		return m_Open.get(i);
	}

	public List<Float> getM_High() {
		return m_High;
	}
	public void setM_High(List<Float> m_High) {
		this.m_High = m_High;
	}
	public void addNextHigh(float High) {
		m_High.add(High);
	}
	public float getSingleHigh(int i) {
		return m_High.get(i);
	}

	public List<Float> getM_Low() {
		return m_Low;
	}
	public void setM_Low(List<Float> m_Low) {
		this.m_Low = m_Low;
	}
	public void addNextLow(float Low) {
		m_Low.add(Low);
	}
	public float getSingleLow(int i) {
		return m_Low.get(i);
	}

	public List<Float> getM_Close() {
		return m_Close;
	}
	public void setM_Close(List<Float> m_Close) {
		this.m_Close = m_Close;
	}
	public void addNextClose(float Close) {
		m_Close.add(Close);
	}
	public float getSingleClose(int i) {
		return m_Close.get(i);
	}

	public List<Integer> getM_Volume() {
		return m_Volume;
	}
	public void setM_Volume(List<Integer> m_Volume) {
		this.m_Volume = m_Volume;
	}
	public void addNextVolume(int Volume) {
		m_Volume.add(Volume);
	}
	public int getSingleVolume(int i) {
		return m_Volume.get(i);
	}

	public int getRowCount() {
		return m_Volume.size();
	}
}