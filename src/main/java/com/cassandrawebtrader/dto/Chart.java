package com.cassandrawebtrader.dto;

public class Chart {
	
	private String symbol;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Chart(String symbol) {
		super();
		this.symbol = symbol;
	}

	public Chart() {
		super();
		// TODO Auto-generated constructor stub
	}

}
