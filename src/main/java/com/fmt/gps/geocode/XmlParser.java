package com.fmt.gps.geocode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlParser {

	private final String TAG = getClass().getSimpleName();

	public GeoCodeResult parseXmlResponse(Document doc) {

		NodeList resultNodes = doc.getElementsByTagName("result");            
		//Node resultNode = resultNodes.item(0);            
		

		GeoCodeResult result = new GeoCodeResult();

		for(int c= 0; c < resultNodes.getLength(); c++) {
			Node resultNode = resultNodes.item(c); 
			NodeList attrsList = resultNode.getChildNodes();
			for (int i=0; i < attrsList.getLength(); i++) {
	
				Node node = attrsList.item(i);                
				Node firstChild = node.getFirstChild();
	
				if ("name".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.placename = firstChild.getNodeValue();
				}                
				if ("vicinity".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.street = firstChild.getNodeValue();
				}
				if ("type".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.type = firstChild.getNodeValue();
				}
				if ("rating".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.rating = Float.parseFloat(firstChild.getNodeValue());
				}                
				/*if ("lng".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.lng = firstChild.getNodeValue();
				}
				if ("adminCode1".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.adminCode1 = firstChild.getNodeValue();
				}
				if ("adminName1".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.adminName1 = firstChild.getNodeValue();
				}
				if ("streetNumber".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.streetNumber = firstChild.getNodeValue();
				}
				if ("postalcode".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
					result.postalcode = firstChild.getNodeValue();
				}*/
				
				if(result.rating > 0.0F) {
					return result;
				}
				
				if(null != result.street) {
					String[] streetToks= result.street.split(" ");
	
					try {
						final String streetNumber= ""+ Integer.parseInt(streetToks[0]);
						
						if(streetToks.length > 2)	return result;	//if includes street number
					} catch (NumberFormatException e) {
						//e.printStackTrace();
					}
				}
			}
		}
		//Log.d(TAG, result.toString());

		return result;



		//return null;

	}

	public GeoCodeResult parseXmlResponse(String response) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is= new InputSource(new StringReader(response));
			Document doc = db.parse(is);

			return parseXmlResponse(doc);
		} catch (ParserConfigurationException e) {
			System.err.println("ParserConfigurationException");
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println("SAXException");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException");
			e.printStackTrace();
		}

		return null;
	}

	public GeoCodeResult parseXmlResponse(File response) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			FileInputStream fis = new FileInputStream(response);
			Document dom = builder.parse(fis);

			return parseXmlResponse(dom);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public GeoCodeResult parseOlderXmlResponse(Document doc) {



		NodeList resultNodes = doc.getElementsByTagName("address");            
		Node resultNode = resultNodes.item(0);            
		NodeList attrsList = resultNode.getChildNodes();

		GeoCodeResult result = new GeoCodeResult();

		for (int i=0; i < attrsList.getLength(); i++) {

			Node node = attrsList.item(i);                
			Node firstChild = node.getFirstChild();

			/*if ("adminCode1".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
				result.adminCode1 = firstChild.getNodeValue();
			}
			if ("adminName1".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
				result.adminName1 = firstChild.getNodeValue();
			}
			if ("streetNumber".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
				result.streetNumber = firstChild.getNodeValue();
			}
			if ("postalcode".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
				result.postalcode = firstChild.getNodeValue();
			}
			if ("placename".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
				result.placename = firstChild.getNodeValue();
			}                
			if ("street".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
				result.street = firstChild.getNodeValue();
			}
			if ("lat".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
				result.lat = firstChild.getNodeValue();
			}                
			if ("lng".equalsIgnoreCase(node.getNodeName()) && firstChild!=null) {
				result.lng = firstChild.getNodeValue();
			}*/
		}

		//Log.d(TAG, result.toString());

		return result;



		//return null;

	}
}
