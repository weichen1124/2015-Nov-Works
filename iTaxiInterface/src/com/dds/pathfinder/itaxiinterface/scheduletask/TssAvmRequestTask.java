/****************************************************************************
 * 
 *					Copyright (c), 2014
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/TssAvmRequestTask.java $
 * 
 * 9/15/14, DChen, PF-16183, to create avm request.
 * 
 * 9/11/14, DChen, PF-16183, TSS shared rider AVM part.
 * 
 */
package com.dds.pathfinder.itaxiinterface.scheduletask;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dds.pathfinder.itaxiinterface.common.impl.CommonImplement.ExternalSystemId;
import com.dds.pathfinder.itaxiinterface.tss.impl.TSSBaseImplement.TSSErrorCode;
import com.dds.pathfinder.itaxiinterface.util.Debug2;


public class TssAvmRequestTask implements UpdateStatusTaskInterface{
	
	
	private static Debug2 log = Debug2.getLogger(TssAvmRequestTask.class);
	String tssServerURL = null;
	public final static int HTTP_CONNECTION_TIMEOUT = 90000; // 90 seconds
	CloseableHttpClient httpclient = null;
	private  DataSource pfDataSource;
	
	public static String XML_BODY_START_FROM = "<AVM_REQ>";
	
	public TssAvmRequestTask(String toURL,  DataSource dataSource){
		tssServerURL = toURL;
		pfDataSource = dataSource;
	}
	

	public void runUpdateStatusTask() {
		if(pfDataSource == null) return;
		
		ArrayList<TssAvmRequest> avmRequests = new ArrayList<TssAvmRequest>();
		
		
		String query = "select callsign, job_id, tss_id, badge_nr, company_id, geo_lat, geo_long, last_gps_dtm, resp_message from TABLE(aqueues.get_tss_avm_msg)";
		
		PreparedStatement stmt = null;
		ResultSet rset = null;
		Connection dbConnection = null;
		try 
		{
			if ((dbConnection = pfDataSource.getConnection()) == null){
				log.error("Failed to get db connection.....");
				return;
			}
			//log.info(query.toString()); 
			stmt = dbConnection.prepareStatement(query);
			rset = stmt.executeQuery();
			// SEND_STATUS send_status = SEND_STATUS.RESP_RC_OK;
			
			
			while (rset.next()) {
				String callsign = rset.getString("callsign");
				int jobID = rset.getInt("job_id");
				int tssID = rset.getInt("tss_id");
			    String badgeNr = rset.getString("badge_nr");
			    int companyID = rset.getInt("company_id"); 
			    double geoLatitude = rset.getDouble("geo_lat");
			    double geoLongitude = rset.getDouble("geo_long");
			    String lastGPSDtm = rset.getString("last_gps_dtm");
			    String message = rset.getString("resp_message");
			    
			    log.debug("runUpdateStatusTask: get pf message: " + callsign + ", lastGPSDTM=" + lastGPSDtm + ", message=" + message);
			    
			    if(message != null && message.length() > 0){
			    	TssAvmRequest request = new TssAvmRequest(callsign, jobID, tssID, badgeNr, companyID, geoLatitude, geoLongitude, lastGPSDtm);
			    	request.setXmlContent(trimXMLRespMessage(message));
			    	avmRequests.add(request);
			    }
			}
		}catch(SQLException se){
			log.error("Exception in TssAvmRequestTask runUpdateStatusTask()" ,se);
		}finally{
			try {if (rset != null) rset.close();} catch (SQLException ignore) {};
			try {if (stmt != null) stmt.close();} catch (SQLException ignore) {};
			try {if (dbConnection != null) dbConnection.close();} catch (SQLException ignore) {};
		}
		
		if(avmRequests != null && avmRequests.size() > 0){
			sendAvmRequests(avmRequests);
		}
	}
	
	
	
	private String trimXMLRespMessage(String message){
		if(message == null || message.trim().length() == 0) return message;
		int index = message.indexOf(XML_BODY_START_FROM);
		if(index < 0) index = message.indexOf(XML_BODY_START_FROM.toLowerCase());
		return (index >= 0) ? message.substring(index) : message;
	}

	
	private void sendAvmRequests(ArrayList<TssAvmRequest> avmRequests){
		RequestConfig requestConfig = RequestConfig.custom()
	            .setSocketTimeout(HTTP_CONNECTION_TIMEOUT)
	            .setConnectTimeout(HTTP_CONNECTION_TIMEOUT).build();
			
		httpclient = HttpClients.custom()
		            .setDefaultRequestConfig(requestConfig)
		            .build();

		HttpPost post = new HttpPost(tssServerURL);
		CloseableHttpResponse response = null;
		try{
			for(TssAvmRequest request : avmRequests){
				try{
					StringEntity entity = new StringEntity(trimXMLRespMessage(marshalAvmRequest(request)), ContentType.create("text/xml", "UTF-8"));
					entity.setContentEncoding(CharEncoding.UTF_8);
					post.setEntity(entity);
					response = httpclient.execute(post);
					if(response != null){
						int statusCode = response.getStatusLine().getStatusCode();
						if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED){
							processAvmResponse(request, response);
						}else{
							cancelPFJob(TSSErrorCode.TSS_HTTP_RESP_ERROR.getErrorCode(), TSSErrorCode.TSS_HTTP_RESP_ERROR.getErrorText() + statusCode, request);
						}
					}
				}catch(Exception e){
					log.error("http post failed", e);
				}
			}
		}finally{
			if(response != null) try{response.close();}catch(IOException ignore){};
			if(post != null) post.releaseConnection();
			if(httpclient != null) try{httpclient.close();}catch(IOException ignore){};
		}

		
	}
	
	private void processAvmResponse(TssAvmRequest request, CloseableHttpResponse response){
		HttpEntity respEntity = response.getEntity();
		
		Document doc;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(response.getEntity().getContent());
			// log.debug("TSS processAvmResponse: parse response to dom: " + getNiceLyFormattedXMLDocument(doc));
			if(doc != null){
				String status = getXMLNodeValue(doc, "status_code");
				String errText = getXMLNodeValue(doc, "err_text");
				log.debug("TSS processAvmResponse: get response.........response body status_code:" + status + ", error text:" + errText );
				cancelPFJob(status, errText, request);
			}
			EntityUtils.consume(respEntity);
			
		} catch (IllegalStateException e) {
			log.error("processAvmResponse with IllegalStateException", e);
		} catch (SAXException e) {
			log.error("processAvmResponse with SAXException", e);
		} catch (IOException e) {
			log.error("processAvmResponse with IOException", e);
		} catch (ParserConfigurationException e) {
			log.error("processAvmResponse with ParserConfigurationException", e);
		}
		

	}
	
	private String marshalAvmRequest(TssAvmRequest request) throws JAXBException {
		  Marshaller m = JAXBContext.newInstance(TssAvmRequest.class).createMarshaller();
		  m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		  StringWriter w = new StringWriter();
		  m.marshal(request, w);
		  return w.toString(); 
	}
	
	private void cancelPFJob(String status, String errText,  TssAvmRequest request){
		
		long jobID = request.getJobID();
		if(jobID <= 0){
			log.error("cancelPFJob failed with invalid job ID:" +jobID);
		}  
		   	   
		Connection con = null;
		CallableStatement cs = null;
		String cancelReason = "TSS job, switch to Share Ride";
		if(TSSErrorCode.TSS_SUCCESS.getErrorCode().equals(status)){	
		}else if(TSSErrorCode.TSS_FAILURE.getErrorCode().equals(status)){
			cancelReason = "TSS cancelled job with message:" + errText;
		}else{
			cancelReason = errText;
		}
		     
		try{
	       	con = pfDataSource.getConnection();
	       	cs = con.prepareCall("{ call despatch.cancel(?,?,?,?,?)}");
	       	cs.setLong(1, jobID);
	       	cs.setString(2, cancelReason);       	   
    	    cs.setNull(3, Types.VARCHAR);		//log_classes.log_type%TYPE DEFAULT NULL,
    	    cs.setString(4, "N");  				//p_is_redesp IN VARCHAR2 DEFAULT 'N',
    	    cs.setString(5, ExternalSystemId.SYSTEM_ID_TSS_RIDER.getLogonCode());
	       	cs.execute();
	       	log.info("cancelPFJob succeed: " + jobID + ", " + cancelReason);
		}catch(SQLException se){
			log.error("cancelPFJob failed with exception", se);
		}finally{
	       	if(cs != null) try{cs.close();}catch(SQLException ignore){};
	       	if(con != null) try{con.close();}catch(SQLException ignore){};
		}
	}
	
	public static String getNiceLyFormattedXMLDocument(Document doc) throws IOException, TransformerException {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	 
	    Writer stringWriter = new StringWriter();
	    StreamResult streamResult = new StreamResult(stringWriter);
	    transformer.transform(new DOMSource(doc), streamResult);
	    String result = stringWriter.toString();
	 
	    return result;
	}
	
	private String getXMLNodeValue(Document doc, String tagName){
		if(doc == null || tagName == null || tagName.trim().length() == 0) return null;
		NodeList nodes = doc.getElementsByTagName(tagName);
		if (nodes != null && nodes.getLength() > 0) {
        	Node aNode = nodes.item(0);
        	NodeList childNodes = aNode.getChildNodes();
        	for(int i = 0 ; i < childNodes.getLength(); i++){
        		Node node = childNodes.item(i);
        		if(node.getNodeType() == Node.TEXT_NODE) return node.getNodeValue();
        	}
        }
		return null;
	}
	
	
}
