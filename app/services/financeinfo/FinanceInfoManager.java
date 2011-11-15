package services.financeinfo;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import play.Logger;

public class FinanceInfoManager {

	
	public static final String YAHOO_BASE_URl = "http://query.yahooapis.com/v1/public/yql?q="; 
	
	public static final String YAHOO_STOCK_YQL_TABLE = "select%20*%20from%20yahoo.finance.stocks%20where%20"; 
	
	public static final String YAHOO_QUOTE_YQL_TABLE = "select%20*%20from%20yahoo.finance.quotes%20where%20"; 
	
	private static Document getXmlFromHttpGet2(String base_url){
		try {
			URL url = new URL(base_url);
			
			// Process response
			Document XMLresponse = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(url.openStream());

			return XMLresponse;
		} catch (MalformedURLException e) {

			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");
		} catch (IOException e) {
			
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");
		} catch (SAXException e) {
			
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");
		}
		return null;
	}
	/*
	private static Document getXmlFromHttpGet(String url){
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("User-Agent", "xxxx");
		
		try {
			HttpResponse response = client.execute(httpget);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {

				Logger.error("Get failed : " + response.getStatusLine());
			} else {
				response.getEntity().getContentType();
				response.getEntity().getContentEncoding();

				HttpEntity entity = response.getEntity();

				InputStream rstream = entity.getContent();

				// Process response
				Document XMLresponse = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder().parse(rstream);

				return XMLresponse;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");

		} catch (IOException e) {
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");
		} catch (SAXException e) {
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");
		}
		return null;
	}
	*/
	public static FinanceInfo getFinanceInfo(String symbol) {

		Document XMLresponse = getXmlFromHttpGet2(YAHOO_BASE_URl + YAHOO_STOCK_YQL_TABLE 
						+ "symbol%20in%20(%22"
						+ symbol.toUpperCase() + "%22)"
						+ "&env=http%3A%2F%2Fdatatables.org%2Falltables.env");

		return parseFinanceInfoFromXML(XMLresponse);
	}

	
	public static Map<String,QuoteInfo> getQuoteInfo(String[] symbols) {
		
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < symbols.length; i++) {
			if(i != 0){
				builder.append("%2C");
			}
			
			builder.append("%22"+symbols[i].toUpperCase()+"%22");

		}
		
		
		Document XMLresponse = getXmlFromHttpGet2(YAHOO_BASE_URl + YAHOO_QUOTE_YQL_TABLE 
				+ "symbol%20in%20("+builder.toString()+")"
						+ "&env=http%3A%2F%2Fdatatables.org%2Falltables.env");
		
		return parseQuoteInfoFromXML(XMLresponse);
		
	}
	
	public static QuoteInfo getQuoteInfo(final String symbol) {

		String symbolToSearch = symbol.toUpperCase();
		
		Document XMLresponse = getXmlFromHttpGet2(YAHOO_BASE_URl + YAHOO_QUOTE_YQL_TABLE 
				+ "symbol%20in%20(%22"
						+ symbolToSearch + "%22)"
						+ "&env=http%3A%2F%2Fdatatables.org%2Falltables.env");
		
		Map<String, QuoteInfo> result = parseQuoteInfoFromXML(XMLresponse);
		return result.get(symbolToSearch);
	}

	private static Map<String,QuoteInfo> parseQuoteInfoFromXML(Document xMLresponse) {
		Map<String,QuoteInfo> result = new HashMap<String, QuoteInfo>();

		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		NodeList nodes;
		try {
			nodes = (NodeList) xPath.evaluate("query/results/quote",
					xMLresponse, XPathConstants.NODESET);

		//	int nodeCount = nodes.getLength();
			
			for (int i = 0; i < nodes.getLength(); i++) {
				String symbol = (String) xPath.evaluate("@symbol",
						nodes.item(i), XPathConstants.STRING);

				String error = (String) xPath.evaluate("ErrorIndicationreturnedforsymbolchangedinvalid",
						nodes.item(i), XPathConstants.STRING);
				
				if(StringUtils.isEmpty(error)){
					QuoteInfo quoteInfo = new QuoteInfo(symbol);
					quoteInfo.setCompanyName((String) xPath.evaluate("Name",
							nodes.item(i), XPathConstants.STRING));

					
					quoteInfo.setLastTradePriceOnly(new BigDecimal((String) xPath.evaluate("LastTradePriceOnly",
							nodes.item(i), XPathConstants.STRING)));
					quoteInfo.setPreviousClose(new BigDecimal((String) xPath.evaluate("PreviousClose",
							nodes.item(i), XPathConstants.STRING)));
					quoteInfo.setChangeinPercent((String) xPath.evaluate("ChangeinPercent",
							nodes.item(i), XPathConstants.STRING));
					quoteInfo.setMarketCapitalization((String) xPath.evaluate("MarketCapitalization",
							nodes.item(i), XPathConstants.STRING));
					quoteInfo.setVolume(new BigDecimal((String) xPath.evaluate("Volume",
							nodes.item(i), XPathConstants.STRING)));
					
					result.put(quoteInfo.getSymbol(), quoteInfo);
					
			}

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");

		}
		return result;
		
	}
	
	private static FinanceInfo parseFinanceInfoFromXML(Document xMLresponse) {
		FinanceInfo result = null;

		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		NodeList nodes;
		try {
			nodes = (NodeList) xPath.evaluate("query/results/stock",
					xMLresponse, XPathConstants.NODESET);

			int nodeCount = nodes.getLength();
			if (nodeCount != 1) {
				Logger.error("To many result : " + nodeCount);
			} else {

				String symbol = (String) xPath.evaluate("@symbol",
						nodes.item(0), XPathConstants.STRING);

				String companyName = (String) xPath.evaluate("CompanyName",
						nodes.item(0), XPathConstants.STRING);

				if(!StringUtils.isEmpty(companyName)){
					result = new FinanceInfo(symbol);
					result.setCompanyName(companyName);
					return result;
				}

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			Logger.error(e, "error occured in FinanceInfoManager");

		}
		return result;
	}
}
