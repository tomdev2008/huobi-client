package com.redv.huobi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.huobi.domain.Depth;
import com.redv.huobi.domain.LoginResult;
import com.redv.huobi.valuereader.LoginResultReader;
import com.redv.huobi.valuereader.VoidValueReader;

public class HUOBIClient {

	public static final String ENCODING = "UTF-8";

	private static final URI HTTPS_BASE = URI.create("https://www.huobi.com/");

	private static final URI LOGIN_URI = URIUtils.resolve(HTTPS_BASE, "account/login.php");

	private static final URI DEPTH_URI = URI.create("https://market.huobi.com/market/depth.php");

	private final Logger log = LoggerFactory.getLogger(HUOBIClient.class);

	private final HttpClient httpClient;

	private final String email;

	private final String password;

	public HUOBIClient() {
		this(null, null);
	}

	public HUOBIClient(String email, String password) {
		httpClient = new HttpClient();
		this.email = email;
		this.password = password;
	}

	public void login() throws IOException {
		initLoginPage();

		LoginResult loginResult = httpClient.post(
				LOGIN_URI,
				new LoginResultReader(),
				new BasicNameValuePair("email", email),
				new BasicNameValuePair("password", password));
		log.debug("Login result: {}", loginResult);
	}

	public Depth getDepth() throws IOException {
		final URI depthUri;
		try {
			depthUri = new URIBuilder(DEPTH_URI)
				.setParameter("a", "marketdepth")
				.setParameter("random", String.valueOf(Math.random()))
				.build();
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

		return httpClient.get(depthUri, Depth.class);
	}

	/**
	 * Calls this method before doing login post is required.
	 */
	private void initLoginPage() throws IOException {
		httpClient.get(HTTPS_BASE, VoidValueReader.getInstance());
	}

}
