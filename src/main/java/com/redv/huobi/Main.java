package com.redv.huobi;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.huobi.domain.Depth;
import com.redv.huobi.domain.Depth.Marketdepth.Data;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException {
		final String email = args[0];
		final String password = args[1];

		HUOBIClient client = new HUOBIClient(email, password);
		client.login();

		Depth depth = client.getDepth();
		log.debug("Depth: {}", depth);

		for (Data bid : depth.getBids()) {
			log.debug("Bid: {}", bid.getPrice());
		}

		for (Data ask : depth.getAsks()) {
			log.debug("Ask: {}", ask.getPrice());
		}
	}

}
