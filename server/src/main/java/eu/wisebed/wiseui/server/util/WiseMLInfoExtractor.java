package eu.wisebed.wiseui.server.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eu.wisebed.wiseui.shared.SensorDetails;


public class WiseMLInfoExtractor {

	/**
	 * Extracts node information from a wiseML document. Iterates within
	 * all sensor nodes and packs information into Sensor classes
	 * 
	 * @param wiseMLnodes
	 * @return ArrayList containing sensor information
	 */
	public static ArrayList<SensorDetails> getNodeList(String wiseMLnodes){

		String urn, nodeType, description = null;
		
		ArrayList<SensorDetails> nodeList = new ArrayList<SensorDetails>();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(wiseMLnodes));

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("node");

			for (int n=0; n<nodes.getLength(); n++){
				// For all nodes "node" in wiseML iterate within attributes
				Node s = nodes.item(n);
				urn = s.getAttributes().getNamedItem("id").getNodeValue();
				Element posEl = (Element) s.getChildNodes();
				NodeList posCh = posEl.getElementsByTagName("position");
				Node pos = posCh.item(0);
				Element coordsEl = (Element) pos.getChildNodes();
				NodeList coordsCh = coordsEl.getChildNodes();
				String[] coords = new String[3];
				for (int i=0, c=0; i<coordsCh.getLength(); i++){
					// Parse coordinates
					Node coord = coordsCh.item(i);
					if (coord.getNodeType() == Node.ELEMENT_NODE){
						// For x,y,z nodes in wiseML format
						coords[c] = coord.getChildNodes().item(0).getNodeValue();
						c++;
					}
				}

				// Get rest sensor information concerning gateway, nodeType and
				// description
				//gateway = getElementValue(s, "gateway");
				nodeType = getElementValue(s, "nodeType");
				description = getElementValue(s, "description");
				nodeList.add(new SensorDetails(urn, coords, nodeType, 
						description));
			}

		} catch (ParserConfigurationException e) {
			// TODO Handle exception by returning a common 'parser' error.
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Handle exception by returning a common 'parser' error.
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Handle exception by returning a common 'parser' error.
			e.printStackTrace();
		}
		
		return nodeList;
	}
	
	/**
	 * Retrieve element value given the parent node and tag name
	 * 
	 * @param n Parent node
	 * @param tag Name of the child nodes
	 * @return String containing the node value 
	 */
	private static String getElementValue(Node n, String tag){
		Element el = (Element) n.getChildNodes();
		NodeList childs = el.getElementsByTagName(tag);
		Node node = childs.item(0);
		return node.getChildNodes().item(0).getNodeValue(); 

	}
}