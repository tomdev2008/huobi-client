package com.redv.huobi.valuereader;

import static com.redv.huobi.HUOBIClient.ENCODING;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class HTMLReader<T> implements ValueReader<T>{

	private final Logger log = LoggerFactory.getLogger(HTMLReader.class);

	@Override
	public T read(InputStream inputStream) throws IOException {
		final String content = IOUtils.toString(inputStream, ENCODING);
		log.debug("Parsing HTML: {}", content);
		try {
			return parse(content);
		} catch (Exception e) {
			String msg = String.format("Parse from \"%1$s\" failed.", content);
			throw new IOException(msg, e);
		}
	}

	protected abstract T parse(HTMLDocument document);

	private T parse(String content) throws UnsupportedEncodingException,
			IOException, SAXException {
		HTMLDocument document = toDocument(content);
		return parse(document);
	}

	private HTMLDocument toDocument(String content)
			throws UnsupportedEncodingException, IOException, SAXException {
		final InputSource inputSource;
		inputSource = new InputSource(new InputStreamReader(
				IOUtils.toInputStream(content, ENCODING), ENCODING));

		DOMParser parser = new DOMParser();
		parser.parse(inputSource);
		HTMLDocument document = (HTMLDocument) parser.getDocument();
		return document;
	}

}
