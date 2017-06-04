package com.cassandrawebtrader.repository;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.cassandrawebtrader.domain.Watchlist;

public interface WatchlistRepository extends CassandraRepository<Watchlist> {
	
	
	@Query("select * from watchlist where watchlistcode='HK001' and symbol=?0")
	public Watchlist findBySymbol(String symbol);

	@Query("select * from watchlist where watchlistcode='HK001' order by symbol")
	public List<Watchlist> findByWatchlistCode(String watchlistcode);
	
}