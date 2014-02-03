/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibd.web.buySellLineHistChart;

import ibd.web.classes.Data;
import ibd.web.classes.MarketAnalyzer;
import ibd.web.classes.Output;
import ibd.web.classes.VarDow;
import ibd.web.classes.VarNasdaq;
import ibd.web.classes.VarSP500;
import ibd.web.classes.Variables;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletException;

/**
 *
 * @author Aaron
 */
public class GetChartData {

	//    private String market;
	//    private String time;

	/** 
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * @param market
	 * @return
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws MalformedURLException if URL passed is not being found or contained wrong protocol header etc
	 * @throws IOException if an I/O error occurs
	 * @throws NullPointerException
	 */
	public static Vector getData(String market) {

		//	HashMap<String, Data> marketData=new HashMap<String, Data>();
		Variables var=null;
		Date[] BD=null;
		Date[] SD=null;
		Vector marketVec=new Vector();
		try {
			if(market.equals("SP500")){
				Output outputSP500 = VarSP500.currentSP500;
				BD = outputSP500.buyDates;
				SD = outputSP500.sellDates;
				var=outputSP500.var;
			} else if(market.equals("NAS")){
				Output outputNasdaq = VarNasdaq.currentNasdaq;
				BD = outputNasdaq.buyDates;
				SD = outputNasdaq.sellDates;
				var=outputNasdaq.var;
			} else if(market.equals("DOW")){
				Output outputDow = VarDow.currentDow;
				BD = outputDow.buyDates;
				SD = outputDow.sellDates;
				var=outputDow.var;
			}

			HashMap<String, Data> marketData=MarketAnalyzer.retrieveMarketData(var);
			Data data3 = marketData.get(var.list);
			Date[] dates = data3.dateData;
			float[] pricesClose=data3.priceDataClose;

			marketVec.add(dates);
			marketVec.add(pricesClose);
			marketVec.add(BD);
			marketVec.add(SD);

		} catch (Exception ex) {
			ibd.web.Constants.Constants.logger.info("Exception in GetChartData.java: "+ex);
		}
		return marketVec;
	}
}
