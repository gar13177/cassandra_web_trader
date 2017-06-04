package com.cassandrawebtrader.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cassandrawebtrader.domain.Quote;
import com.cassandrawebtrader.repository.QuoteRepository;

@Service
public class YahooFeedService {
	
	private static Logger logger = LoggerFactory.getLogger(YahooFeedService.class);

	@Autowired
	private QuoteRepository quoteRepository;
	
	public static BufferedReader getStock(String symbol, int fromMonth,
			int fromDay, int fromYear, int toMonth, int toDay, int toYear) {

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date from_date = formatter.parse(String.format("%d/%d/%d", fromDay, fromMonth, fromYear));
			Date to_date = formatter.parse(String.format("%d/%d/%d", toDay, toMonth, toYear));
			
			
			SimpleDateFormat url_formatter = new SimpleDateFormat("MMM d, yyyy");
			
			String from_date_url = url_formatter.format(from_date).replaceAll(" ","+");
			String to_date_url = url_formatter.format(to_date).replaceAll(" ","+");
			
			
			// Retrieve CSV stream
			URI yahoo_pre = new URI("http://www.google.com/finance/historical?q="
					+ symbol.toUpperCase() + "&startdate="
					+ from_date_url + "&enddate="
					+ to_date_url + "&output=csv");
			URL yahoo = yahoo_pre.toURL();
			logger.info(yahoo.toString());
			URLConnection connection = yahoo.openConnection();
			InputStreamReader is = new InputStreamReader(
					connection.getInputStream());
			// return the BufferedReader
			return new BufferedReader(is);

		} catch (IOException e) {
			logger.error(e.toString(), e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.toString(), e);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e.toString(), e);
		}
		return null;
	}
	
	public static Quote parseQuote(String symbol, String[] feed) throws java.text.ParseException {
		Date date = null;
		float low = 0;
		float high = 0;
		float open = 0;
		float close = 0;
		double volume = 0;
		
		date = new SimpleDateFormat("dd-MMM-yy").parse(feed[0]);
		open = Float.parseFloat(feed[1]);
		high = Float.parseFloat(feed[2]);
		low = Float.parseFloat(feed[3]);
		close = Float.parseFloat(feed[4]);
		volume = Double.parseDouble(feed[5]);

		// create a Quote POJO
		return new Quote(symbol.toUpperCase(), date, open, high, low, close,
				volume);
	}
	
	public void getData(final String symbol) {
		final Calendar cal = Calendar.getInstance();

		// today is the default end date for fetching stock quote data
		final Date today = new Date();
		cal.setTime(today);
		final int toDay = cal.get(Calendar.DAY_OF_MONTH);
		final int toMonth = cal.get(Calendar.MONTH);
		final int toYear = cal.get(Calendar.YEAR);

		// get the last date of stock quote data of a stock
		// if none is found, use the default 1-JAN-2000 as the start date
		// otherwise, use the next day of the last date in Cassandra
		Quote lastQuote = quoteRepository.findLastBySymbol(symbol);
		Date lastQuoteDate = null;
		if (lastQuote != null)
			lastQuoteDate = lastQuote.getDate();
		int fromDay = 1;
		int fromMonth = 1;
		int fromYear = 2010;
		if (lastQuoteDate != null) {
			cal.setTime(lastQuoteDate);
			cal.add(Calendar.DATE, 1);
			fromDay = cal.get(Calendar.DAY_OF_MONTH);
			fromMonth = cal.get(Calendar.MONTH);
			fromYear = cal.get(Calendar.YEAR);
		}

		// retrieve stock quote data from Yahoo! Finance
		final BufferedReader br = getStock(symbol, fromMonth, fromDay,
				fromYear, toMonth, toDay, toYear);
		
		if (br != null) {
			try {				
				// process each line of stock quote data
				for (String line = br.readLine(); line != null; line = br
						.readLine()) {
					String[] feed = line.split(",");
					// skip the header line
					try{
						new SimpleDateFormat("dd-MMM-yy").parse(feed[0]);
						// extract each line into the Quote POJO
						Quote q = parseQuote(symbol, feed);
						// persist the Quote POJO into Cassandra
						quoteRepository.save(q);
					} catch (Exception e){
						logger.info("no se encontro una fecha");
					}
				}
				logger.info(symbol + " is saved.");
			} catch (IOException e) {
				logger.error(e.toString(), e);
			} 
		}
	}
	
}
