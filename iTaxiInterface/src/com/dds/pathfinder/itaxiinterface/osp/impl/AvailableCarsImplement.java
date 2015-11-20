/****************************************************************************
 *
 *                            Copyright (c), 2015
 *                            All rights reserved
 *
 *                            DIGITAL DISPATCH SYSTEMS, INC
 *                            RICHMOND, BRITISH COLUMBIA
 *                            CANADA
 *
 ****************************************************************************
 * 
 * Created for Mobile Application vehicle on the map
 * Aug 17, 2015 Y Yin
 * PF-16698. Added attributes in the response. Reuse preDispatchETA for zoro.
 * 
 * Aug 13, 2015 Y Yin
 * PF-16702. Make sure driver ID is not null and job ID is null when getting available cars since
 * sometimes cars stuck in 'F' state.
 * 
 * PF-16496, 06/01/15, DChen, add S_NAV_SERV_TIME_ADJ as adjustment.
 * 
 * PF-16496, 05/29/15, DChen, add pre dispatch eta.
 * 
 * PF-16496, 05/27/15, DChen, add pre dispatch eta.
 * 
 */
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import com.dds.pathfinder.callbooker.server.dwnavigation.ArrayOfDistTimeResult;
import com.dds.pathfinder.callbooker.server.dwnavigation.DistTimeResponse;
import com.dds.pathfinder.callbooker.server.dwnavigation.DistTimeResult;
import com.dds.pathfinder.callbooker.server.dwnavigation.Location;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.callbooker.server.pfa.ejb.PfaLocal;
import com.dds.pathfinder.itaxiinterface.scheduletask.JobInfo;
import com.dds.pathfinder.itaxiinterface.util.Debug2;
import com.dds.pathfinder.itaxiinterface.webservice.AvailableCarsReq;
import com.dds.pathfinder.itaxiinterface.webservice.AvailableCarsRes;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.CarGPSListItem;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.PreDispatchETAReq;
import com.dds.pathfinder.itaxiinterface.webservice.PreDispatchETARes;

/** 
 * @author ezhang
 *
 */
public class AvailableCarsImplement extends OSPBaseImplement{
	
	private static Debug2 log = Debug2.getLogger(JobInfo.class);

    private DataSource pfDataSource;
    
    private PfaLocal pfaLocal;
    
    
    public static int MAP_SCALE = Integer.MAX_VALUE;
    public static double LONG_ORIG = Double.NaN;
    public static double LONG_SCALE = Double.NaN;
    public static double LAT_ORIG = Double.NaN;
    public static double LAT_SCALE = Double.NaN;
    
    public static int MIN_VEHICLE_TO_CALCULATE_ETA = 2;
     
    private static String ATTR_ID_LIST_DELIMITER=",";
    private static String ATTR_VEH_TYPE="V_";
    private static String ATTR_DRV_TYPE="D_";
    
    private static int MAX_ETA = 9999; 
    
    private enum AvailableCarsErrorCode {
		NO_ERROR(0),
		NOT_AUTHENTICATED(1),
		INVALID_SESSIONID(2),
		INVALID_COM_ID(3),
		INVALID_MAX_AGE(4),
		PRE_ETA_NOT_FOUND(5),
		DEFAULT_ERROR(99);
		
		private int code;
		
		private AvailableCarsErrorCode(int c) {
			   code = c;
		}

		public int getCode() {
			   return code;
		}
	}
    
    public AvailableCarsImplement(DataSource pfDataSource) {
		super();
		this.pfDataSource = pfDataSource;
	}
    
    public AvailableCarsRes generateResponse(BaseReq request) {
		return generateAvailableCarsResponse((AvailableCarsReq)request);
	}
    
    private String getAttrIDList(String vehAttr, String drvAttr, int companyID)
    {
    	HashMap<Integer, HashMap<String, SysAttrListItem>> attributes = null;
		StringBuffer attrIDList=new StringBuffer();
		// Get the attribute ID list
		if(vehAttr != null)
		{
			attributes = cachedParam.getVehAttributeMap();
			
	        if(attributes != null && attributes.size() > 0){
	        	HashMap<String, SysAttrListItem> vehAttrMap = attributes.get(companyID);
	        
	        
	        	for(int index = 0; index < vehAttr.length(); index++)
	        	{
	        		if(vehAttr.charAt(index) == '1' && vehAttrMap.get(String.valueOf(index + 1))!=null)
	        		{
	        			if(attrIDList.length()> 0 )
	        			{
	        				attrIDList.append(ATTR_ID_LIST_DELIMITER);
	        			}
	        		
	        			attrIDList.append(ATTR_VEH_TYPE + (index +1) );
	        		}
	        	}
	        }
		}
		
		if(drvAttr != null)
		{
			attributes = cachedParam.getDrvAttributeMap();
			
	        if(attributes != null && attributes.size() > 0){
	        	HashMap<String, SysAttrListItem> drvAttrMap = attributes.get(companyID);
	        
	        
	        	for(int index = 0; index < drvAttr.length(); index++)
	        	{
	        	
	        		if(drvAttr.charAt(index) == '1' && drvAttrMap.get(String.valueOf(index + 1))!=null)
	        		{
	        			if(attrIDList.length()> 0 )
	        			{
	        				attrIDList.append(ATTR_ID_LIST_DELIMITER);
	        			}
	        		
	        			attrIDList.append(ATTR_DRV_TYPE + (index +1) );
	        		}
	        	}
	        }
		}
		return attrIDList.toString();
    	
    }
    private AvailableCarsRes generateAvailableCarsResponse(AvailableCarsReq request){
    	AvailableCarsRes response = new AvailableCarsRes();
    	response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
    	response.setErrorCode(AvailableCarsErrorCode.DEFAULT_ERROR.getCode());
    	
    	//validate system id
    	if(validateSystemId(pfDataSource, request) == false){
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			   response.setErrorCode(AvailableCarsErrorCode.NOT_AUTHENTICATED.getCode());
			   return response;
		}
    	
    	//validate company id
    	if(request.getTaxiCoID() <= 0){
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(AvailableCarsErrorCode.INVALID_COM_ID.getCode());
			return response;		
    	}
    	
    	//validate max age
    	if(request.getMaxAge()<=0){
    		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_MAX_AGE);
    		response.setErrorCode(AvailableCarsErrorCode.INVALID_MAX_AGE.getCode());
    		return response;
    	}
    	
    	
    		
    	Connection con = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();
		
		try{
			
			if ((con = pfDataSource.getConnection()) == null) {
				log.error("Failed to get db connection.");
				return response;
			}
			
			
			//generate query
			sb.append ("select vs.vehicle_id, vs.latitude as carlat, vs.longitude as carlong,\n ");
			sb.append (" TO_CHAR(vs.position_update_dtm,'YYYY-MM-DD HH24:MI:SS') as gpstime,\n");
			sb.append (" server_utility.attributes_to_binary(veh.attributes) as vehattr,\n");
			sb.append (" server_utility.attributes_to_binary(dv.attributes) as drvattr \n");
			sb.append (" from vehicle_states vs, taxi_co_vehicles tcv, vehicles veh, drivers dv \n");
			sb.append (" WHERE vs.latest_x_position IS NOT NULL and vs.latest_y_position IS NOT NULL \n");
			sb.append (" AND vs.vehicle_id = tcv.vehicle_id AND tcv.relation_type = 'A' AND vs.vehicle_status = 'F' \n" ); //vehicle is assigned to this company and free
			sb.append (" AND vs.vehicle_id = veh.vehicle_id \n");
			sb.append (" AND vs.current_job_id IS NULL \n"); // make sure it has no job
			sb.append ( " AND dv.driver_id = vs.current_driver_id \n");// make sure it has be signed on
			sb.append (" AND tcv.taxi_co_id = " + request.getTaxiCoID() + "\n"); 
			sb.append( " AND vs.position_update_dtm > (SYSDATE - " + request.getMaxAge() + "/86400 )\n"); //GPS within max latency in seconds
			
		
			//return the vehicle GPS for the whole company
			
			//log.info("sb: " + sb);
			stmt = con.prepareStatement(sb.toString());
			rs = stmt.executeQuery();
			ArrayList<CarGPSListItem> carList = new ArrayList<CarGPSListItem>();
			while(rs.next()){
				double carLat = rs.getDouble("carlat");
				double carLong = rs.getDouble("carlong");
				int    carNum = rs.getInt("vehicle_id");
				String gpsTime = rs.getString("gpstime");
				String vehAttr = rs.getString("vehattr");
				String drvAttr = rs.getString("drvattr");
				carList.add(new CarGPSListItem(carNum, carLat, carLong, gpsTime, getAttrIDList(vehAttr, drvAttr, request.getTaxiCoID())));		
				
			}
			
			generateCarListResonse(response, carList);
			
			
	       }catch(SQLException se){
		       	log.error("available Car request failed with exception", se);
		       	
	       }finally{
	    	   if(rs != null) try{rs.close();}catch(SQLException ignore){};
	    	   if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
	    	   if(con != null) try{con.close();}catch(SQLException ignore){};
	       }
		
    	
    	return response;
    }
    
    
   
    
	
	private void generateCarListResonse(AvailableCarsRes response, ArrayList<CarGPSListItem> carList){
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
    	response.setErrorCode(AvailableCarsErrorCode.NO_ERROR.getCode());
    	int nbOfCars = carList.size();
    	response.setNofCars(nbOfCars);
    	if(nbOfCars > 0){
    		CarGPSListItem[] carArray = new CarGPSListItem[nbOfCars];
    		carList.toArray(carArray);
    		response.setCarList(carArray);
    	}
	}
	
	
	public PreDispatchETARes getPreDispatchETA(PreDispatchETAReq request){
		long currentTime = System.currentTimeMillis();
		PreDispatchETARes response = new PreDispatchETARes();
    	response.setRequestStatus(GenErrMsgRes.STATUS_FAILED);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_FAILED);
    	response.setErrorCode(AvailableCarsErrorCode.PRE_ETA_NOT_FOUND.getCode());
    	
    	if(!isValidPreDispatchETARequest(request, response)) return response;
    	if(!isConstValuesInitialized()){
    		log.error("getPreDispatchETA failed with invalid const values in system parameters, return. ");
    		return response;
    	}
    	
    	// PF-16698. For now, if useStatistics is Y, just return number 9999 for ETA.
    	if("Y".equalsIgnoreCase(request.getUseStatistics()))
    	{
    		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
        	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
        	response.setErrorCode(AvailableCarsErrorCode.NO_ERROR.getCode());
	    	response.setEtaInSecond(MAX_ETA);
    	}
    	
    	int pfX = (int) Math.round(((request.getLongitude() - LONG_ORIG) * LONG_SCALE ) / MAP_SCALE );
    	int pfY = (int) Math.round(((request.getLatitude() - LAT_ORIG) * LAT_SCALE ) / MAP_SCALE );
    	int radius = request.getRadius() / MAP_SCALE;
    	
    	log.debug("getPreDispatchETA: long, lat and xpos, ypos are: " + request.getLongitude() +", "  + request.getLatitude() +", " + pfX +", " + pfY);
    	
    	String query = "select vs.vehicle_id, vs.latitude as carlat, vs.longitude as carlong, TO_CHAR(vs.position_update_dtm,'YYYY-MM-DD HH24:MI:SS') as gpstime\n " + 
            			"  from vehicle_states vs, taxi_co_vehicles tcv\n " + 
            			"  where vs.latest_x_position IS NOT NULL and vs.latest_y_position IS NOT NULL\n " + 
            			"  AND vs.vehicle_id = tcv.vehicle_id AND tcv.relation_type = 'A' AND vs.vehicle_status = 'F'\n " +
            			"  AND vs.current_driver_id IS NOT NULL\n " + // make sure it has be signed on
            			"  AND vs.current_job_id IS NULL \n" +  // make sure it has no job
            			"  AND tcv.taxi_co_id = " + request.getTaxiCoID() +"\n " +
    					"  AND vs.latest_x_position <= " + (pfX + radius) + " AND vs.latest_x_position >= " + (pfX - radius) + "\n " +
    					"  AND vs.latest_y_position <= " + (pfY + radius) + " AND vs.latest_y_position >= " + (pfY - radius) + "\n ";
    	log.debug("getPreDispatchETA: getPreDispatchETA query: " + query);
    	Connection con = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<CarGPSListItem> carList = new ArrayList<CarGPSListItem>();
        try{
        	con = pfDataSource.getConnection();
        	stmt = con.prepareStatement(query);
        	rs = stmt.executeQuery();
			while(rs.next()){
				double carLat = rs.getDouble("carlat");
				double carLong = rs.getDouble("carlong");
				int    carNum = rs.getInt("vehicle_id");
				String gpsTime = rs.getString("gpstime");
				carList.add(new CarGPSListItem(carNum, carLat, carLong, gpsTime, null));		
			}
        }catch(SQLException se){
        	log.error("getPreDispatchETA failes", se);
        }finally{
        	 if(rs != null) try{rs.close();}catch(SQLException ignore){};
        	 if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
        	 if(con != null) try{con.close();}catch(SQLException ignore){};
        }
        log.debug("getPreDispatchETA: get results from database spent sec: " + (System.currentTimeMillis() - currentTime)/1000.0);
        if(carList != null && carList.size() >= MIN_VEHICLE_TO_CALCULATE_ETA) calculateETA(carList, request, response);
        return response;
	}
	
	private void calculateETA( ArrayList<CarGPSListItem> carList, PreDispatchETAReq request, PreDispatchETARes response){
		long currentTime = System.currentTimeMillis();
		if(pfaLocal != null){
			ArrayList<Location> srcList = new ArrayList<Location>();
			ArrayList<Location> destList = new ArrayList<Location>();
			for(CarGPSListItem car : carList){
				Location src = new Location();
				src.setLatitude(car.getCarLat());
				src.setLongitude(car.getCarLong());
				srcList.add(src);
				log.debug("getPreDispatchETA: available car list: veh id: " + car.getCarNum() +" gps: " + car.getCarLong() +", " + car.getCarLat());
			}
			Location dest = new Location();
			dest.setLatitude(request.getLatitude());
			dest.setLongitude(request.getLongitude());
			destList.add(dest);
			DistTimeResponse dwResponse = pfaLocal.getDWDistanceTimeResp("", "", srcList, destList);
			if(dwResponse != null){
				List<String> errors = dwResponse.getError();
				if(errors == null || errors.size() == 0){
					log.error("calculateETA failed, as dw navigation returns null errors");
				}else{
		    		if("NoError".equalsIgnoreCase(errors.get(0))){
		    			ArrayOfDistTimeResult arrayResult = dwResponse.getDistTimes().getValue();
		    			List<DistTimeResult> listDistTime = arrayResult.getDistTimeResult();
		    			
		    			if(listDistTime != null && listDistTime.size() > 0){
		    				ArrayList<Integer> etaList = new ArrayList<Integer>();
		    				for(DistTimeResult distTime : listDistTime){
			    				if(distTime != null){
			    		        	etaList.add((int)(Math.ceil(distTime.getTravelTime())) );
			    				}
		    				}
		    				if(etaList.size() >= MIN_VEHICLE_TO_CALCULATE_ETA) {
		    					Collections.sort(etaList);
//		    					for(int eta : etaList){
//			    					log.debug("getPreDispatchETA: .....get eta: " + eta);
//			    				}
		    					int averageMinm = 0;
		    					for(int i=0; i < MIN_VEHICLE_TO_CALCULATE_ETA; i++) averageMinm += etaList.get(i);
		    					averageMinm = (int)(Math.ceil((averageMinm/MIN_VEHICLE_TO_CALCULATE_ETA) * getSystemAdjustAmount()));
		    					
		    					response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
		    			    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
		    			    	response.setErrorCode(AvailableCarsErrorCode.NO_ERROR.getCode());
		    			    	response.setEtaInSecond(averageMinm);
		    				}else{
		    					log.error("eta list results less than minimum, this size of list:" + etaList.size());
		    				}
		    			}
		    		}else{
		    			log.error("calculateETA failed, as dw navigation returns errors: " + errors.get(0));
		    		}
				}
			}
			
		}
		log.debug("getPreDispatchETA: navigation service checking spent sec: " + (System.currentTimeMillis() - currentTime)/1000.0);
	}
	
	private boolean isValidPreDispatchETARequest(PreDispatchETAReq request, PreDispatchETARes response){
		if(validateSystemId(pfDataSource, request) == false){
			   response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			   response.setErrorCode(AvailableCarsErrorCode.NOT_AUTHENTICATED.getCode());
			   return false;
		}
 	
	 	if(request.getTaxiCoID() <= 0){
	 		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			response.setErrorCode(AvailableCarsErrorCode.INVALID_COM_ID.getCode());
			return false;		
		}
	 	
	 	if(!isValidDoubleValue(request.getLatitude()) || !isValidDoubleValue(request.getLongitude())){
	 		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_GPS);
	 		return false;
	 	}
	 	
	 	if(request.getRadius() <= 0 ){
	 		response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
	 		return false;
	 	}
	 	
	 	return true;
	}

	public PfaLocal getPfaLocal() {
		return pfaLocal;
	}

	public void setPfaLocal(PfaLocal pfaLocal) {
		this.pfaLocal = pfaLocal;
	}

	public int getMAP_SCALE() {
		if(MAP_SCALE == Integer.MAX_VALUE && cachedParam != null){
			MAP_SCALE = cachedParam.getSystemParameterIntValue("MAP_SCALE", MAP_SCALE);
		}
		return MAP_SCALE;
	}

	

	public double getLONG_ORIG() {
		if(Double.isNaN(LONG_ORIG) && cachedParam != null){
			 try{
				 LONG_ORIG = Double.parseDouble(cachedParam.getSystemParameterValue("LONG_ORIGIN"));
			 }catch(NullPointerException | NumberFormatException e){
				 log.error("getLONG_ORIG failed", e);
			 }
		}
		return LONG_ORIG;
	}

	

	public double getLONG_SCALE() {
		if(Double.isNaN(LONG_SCALE) && cachedParam != null){
			 try{
				 LONG_SCALE = Double.parseDouble(cachedParam.getSystemParameterValue("LONG_SCALE"));
			 }catch(NullPointerException | NumberFormatException e){
				 log.error("getLONG_SCALE failed", e);
			 }
		}
		return LONG_SCALE;
	}

	

	public double getLAT_ORIG() {
		if(Double.isNaN(LAT_ORIG) && cachedParam != null){
			 try{
				 LAT_ORIG = Double.parseDouble(cachedParam.getSystemParameterValue("LAT_ORIGIN"));
			 }catch(NullPointerException | NumberFormatException e){
				 log.error("getLAT_ORIG failed", e);
			 }
		}
		return LAT_ORIG;
	}

	

	public double getLAT_SCALE() {
		if(Double.isNaN(LAT_SCALE) && cachedParam != null){
			 try{
				 LAT_SCALE = Double.parseDouble(cachedParam.getSystemParameterValue("LAT_SCALE"));
			 }catch(NullPointerException | NumberFormatException e){
				 log.error("getLAT_SCALE failed", e);
			 }
		}
		return LAT_SCALE;
	}
	
//	public void initConstValues(){
//		getMAP_SCALE();
//		getLONG_ORIG();
//		getLONG_SCALE();
//		getLAT_ORIG();
//		getLAT_SCALE();
//		
//	}
	
	public boolean isConstValuesInitialized(){
		if(getMAP_SCALE() > 100 || Double.isNaN(getLONG_ORIG()) || Double.isNaN(getLONG_SCALE()) 
				|| Double.isNaN(getLAT_ORIG()) || Double.isNaN(getLAT_SCALE())) return false;
		else return true;
	}
	
	private double getSystemAdjustAmount() {
		double adjustAmt = 1.0;
		String sysParam =cachedParam.getSystemParameterValue("S_NAV_SERV_TIME_ADJ");
		
		if (sysParam == null || sysParam.length() == 0) return adjustAmt;

		sysParam = sysParam.replace(',', '.'); // for some reason this is
												// returning comma instead of
												// decimal point
		try {
			adjustAmt = Double.parseDouble(sysParam);
		} catch (NumberFormatException e) {
			log.warn("Warn: system paramter S_NAV_SERV_TIME_ADJ is not set.");
		}
		
		if(adjustAmt <= 0.0) adjustAmt = 1.0;
		return adjustAmt;
	}  
	
}
