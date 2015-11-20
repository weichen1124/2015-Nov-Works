/****************************************************************************
 *
 *		   		    Copyright (c), 2015
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/common/impl/CompanyDefaultValues.java $
 * 
 * PF-16385, 03/03/15, DChen, share with pfrest.
 * 
 * 
 * *************************************************/

package com.dds.pathfinder.itaxiinterface.common.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccountDAO;
import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;

public class CompanyDefaultValues {
	private static final Logger logger = Logger.getLogger(CompanyDefaultValues.class);
	
	public final static String COMP_PARAMETER_C_DISPATCH_STATE_NAME = "C_DISPTCH_STATE_NME";
	public final static String COMP_PARAMETER_C_DISPATCH_COUNTRY_NAME = "C_DISPTCH_CNTRY_NME";
	
	public final static String COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NR= "GENERAL_CASH_ACCOUNT";
	public final static String COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NAME ="GECASH_ACCOUNT_NAME";
	public final static String COMP_PARAMETER_C_CITYNAV_SERVER ="C_CITYNAV_SERVER";
	public final static String COMP_PARAMETER_C_CITYNAV_PORT ="C_CITYNAV_PORT";
	
	public final static String COMP_PARAMETER_C_NOTES_SEPARATOR ="C_NOTES_SEPARATOR";
	public final static String COMP_PARAMETER_C_PRE_BOOK_IN_DAYS = "C_PRE_BOOK_IN_DAYS";
	
	public final static String COMP_PARAMETER_C_WB_HELD_JOB = "C_WB_HELD_JOB";
	public final static String COMP_PARAMETER_C_MAX_JOBS_TO_RETURN = "C_MAX_JOBS_TO_RETURN";
	
	public final static String COMP_PARAMETER_C_MG_ACCOUNT = "C_MB_ACCOUNT"; //PF-15230
	
	//GoFastCab Parameters
	public final static String COMP_PARAMETER_C_GFC_API_KEY = "C_GFC_API_KEY";
	public final static String SYSTEM_PARAMETER_GFC_PWD = "S_GFC_SYS_PWD";
	
	//PF-15595 - Mobile Booker Parameter
    public final static String COMP_PARAMETER_C_MB_USE_ACCOUNT_PWD = "C_MB_USE_ACCPW";//used in PF-15613(OSP Account validation)
    public final static String COMP_PARAMETER_C_MB_BASE_RATE = "C_MB_BASE_RATE";
    public final static String COMP_PARAMETER_C_MB_CC_PAMNT_ENABLE = "C_MB_CC_PAMNT_ENABLE";
    public final static String COMP_PARAMETER_C_MB_DRP_OFF_MAND= "C_MB_DRP_OFF_MAND";
    public final static String COMP_PARAMETER_C_MB_MLT_BOOK_ALWD = "C_MB_MLT_BOOK_ALWD";
    public final static String COMP_PARAMETER_C_MB_RATE_PER_DIST = "C_MB_RATE_PER_DIST";
    public final static String COMP_PARAMETER_C_MB_SAME_LOC_BK_ALW = "C_MB_SAME_LOC_BK_ALW";
    public final static String COMP_PARAMETER_C_MB_SND_MSG_DRV = "C_MB_SND_MSG_DRV";
    public final static String COMP_PARAMETER_C_MB_TIP_BUTTON1 = "C_MB_TIP_BUTTON1";
    public final static String COMP_PARAMETER_PAYMNT_TMOUT = "C_MB_PAYMNT_TMOUT";
    public final static String COMP_PARAMETER_C_MB_API_KEY = "C_MB_API_KEY";
    public final static String SYSTEM_PARAMETER_MB_PWD = "S_MB_SYS_PWD";

	//PF-15882
	public final static String COMP_PARAMETER_C_MB_PG_COMP_NUM = "C_MB_PG_COMP_NUM";
	public final static String COMP_PARAMETER_C_MB_PG_COMP_PWD = "C_MB_PG_COMP_PASSWD";
	
	//PF-16074
	public final static String COMP_PARAMETER_C_TL_PG_COMP_NUM = "C_TL_PG_COMP_NUM";
	public final static String COMP_PARAMETER_C_TL_PG_COMP_PWD = "C_TL_PG_COMP_PASSWD";
	
	public final static String DEFAULT_MG_ACCOUNT_CODE = "CREDIT"; //default to CREDIT for MG job, PF-15230
	
	public final static String DEFAULT_CASH_ACCOUNT_NAME_NR ="CASH";
	public final static int    DEFAULT_PICKUP_ACCOUNT_SET_ID = 4;
	public final static String SYSTEM_PARAMETER_MAP_SCALE = "MAP_SCALE";
	public final static String SYSTEM_PARAMETER_S_NAV_RESP_TIMEOUT = "S_NAV_RESP_TIMEOUT";
	public final static String SYSTEM_PARAMETER_MAX_TAXIS = "MAX_TAXIS";
	
	private final static String S_PF_OPTIONS = "S_PF_OPTIONS"; //parameter that contains all bits associated with pathfinder features
	/** the bit to check callout option, a.k.a. advise arrival */
	public final static int RTL_TYPE_CALLOUT_OPTION = 1;	//(copied from ...callbooker.client.util.GUIControlNames)				

// C35846 - Added parameters to apply area, address and region attributes	
	public static final String COMP_PARAMETER_C_USE_AREA_ATTR_FLAG = "C_USE_AREA_ATTR_FLAG";
    public static final String COMP_PARAMETER_C_USE_ADDR_ATTR_FLAG = "C_USE_ADDR_ATTR_FLAG";
    public static final String COMP_PARAMETER_C_USE_REGN_ATTR_FLAG = "C_USE_REGN_ATTR_FLAG";	
	
	public static HashMap<Integer, HashMap<String, SysAttrListItem>> DriverAttributesMap;
    public static HashMap<Integer, HashMap<String, SysAttrListItem>> VehicleAttributesMap;
    
    public static HashMap<String, String> attributePriority;
    public static HashMap<String, String> attributeDrvNotes;
    public static HashMap<String, String> attributeOptNotes;
    public static HashMap<String, String> attributeLeadTime;
    public static HashMap<String, String> attributeReservableFlag;
    // public static HashMap<String, String> attributelockFlag;
    
    //public static HashMap<String, String> CompanyParameters;
    
    public static HashMap<Integer, String> TaxiCompanies;
    
    public static HashMap<Integer, HashMap<String, String>> DispatchParameters;
    
    public static HashMap<String, String> SystemParameters;
    
	public static void getDefaultAttributesMaps(DataSource dataSource){
		if(DriverAttributesMap != null && VehicleAttributesMap != null) return;
		if(dataSource != null){
			Connection con = null;
			PreparedStatement stmt = null;
	        ResultSet rs = null;
	        getTaxiCompanies(dataSource);
	        
	        try{
	        	con = dataSource.getConnection();
	        	//PF-14461 added osp_flag to retrieve attribute expose to OSP only.
	        	stmt = con.prepareStatement( "select ar.BIT_POSITION, ar.ATTRIB_TYPE, ar.DESCRIPTION, ar.TAXI_CO_ID, " +
	        			" ar.PRIORITY, nvl(ar.LEAD_TIME_ADJ,0) LEAD_TIME_ADJ, " +
	        			" nt.DRIVER_NOTES, nt.CALLTAKER_NOTES, ar.RESERVABLE_FLAG, ar.LOCK_FLAG " +
	        			" from attributes ar, notes nt  " +
	        			" where ar.KILLED_FLAG ='N' and ar.osp_flag = 'Y' " +
	        			" and ar.attribute_id = nt.attribute_id(+) "  + 
	        			" ORDER BY ar.ATTRIB_TYPE, ar.DESCRIPTION ");
	        	
	        	rs = stmt.executeQuery();
	        	
	        	DriverAttributesMap = new HashMap<Integer, HashMap<String, SysAttrListItem>>();
	        	VehicleAttributesMap = new HashMap<Integer, HashMap<String, SysAttrListItem>>();
	        	
	        	attributePriority = new HashMap<String, String>();
	        	attributeDrvNotes = new HashMap<String, String>();
	            attributeOptNotes = new HashMap<String, String>();
	            attributeLeadTime = new HashMap<String, String>();
	            attributeReservableFlag = new HashMap<String, String>();
	            // attributelockFlag = new HashMap<String, String>();
	        	    	
	        	for(int taxiID : TaxiCompanies.keySet()){
	        		DriverAttributesMap.put(taxiID, new HashMap<String, SysAttrListItem>());
	        		VehicleAttributesMap.put(taxiID, new HashMap<String, SysAttrListItem>());
	        	}
	        	
	        	while(rs.next()){
	        		String bitPosition = rs.getString("BIT_POSITION");
	                String attribType = rs.getString("ATTRIB_TYPE");
	                String description = rs.getString("DESCRIPTION");
	                String priority = rs.getString("PRIORITY");
	                String driverNotes = rs.getString("DRIVER_NOTES");
	                String calltakerNotes = rs.getString("CALLTAKER_NOTES");
	                String leadTime = rs.getString("LEAD_TIME_ADJ");
	                String reserveFlag = rs.getString("RESERVABLE_FLAG");
	                String lockFlag = rs.getString("LOCK_FLAG");
	                
	                String shortName = attribType+"_"+bitPosition;
	                int taxiID = rs.getInt("TAXI_CO_ID");
	                if(taxiID == 0 || !TaxiCompanies.containsKey(taxiID)) continue;  
	                if("D".equalsIgnoreCase(attribType)){
	                	HashMap<String, SysAttrListItem> driverMap = DriverAttributesMap.get(taxiID);
	                	driverMap.put(bitPosition, new SysAttrListItem(shortName, description,"Y"));
	                	DriverAttributesMap.put(taxiID, driverMap);
	                }else{
	                	HashMap<String, SysAttrListItem> vehicleMap = VehicleAttributesMap.get(taxiID);
	                	vehicleMap.put(bitPosition, new SysAttrListItem(shortName, description,"Y"));
	                	VehicleAttributesMap.put(taxiID, vehicleMap);
	                }
	                
	                attributePriority.put(shortName, priority);
		        	attributeDrvNotes.put(shortName, driverNotes);
		            attributeOptNotes.put(shortName, calltakerNotes);
		            attributeLeadTime.put(shortName, leadTime);
		            attributeReservableFlag.put(shortName, reserveFlag);
		            // attributelockFlag.put(shortName, lockFlag);
	        	}
	
	        }catch(SQLException se){
	        	se.printStackTrace();
	        }finally{
	        	if(rs != null) try{rs.close();}catch(SQLException ignore){};
	        	if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
	        	if(con != null) try{con.close();}catch(SQLException ignore){};
	        }
		}
	}
	
	public static void getTaxiCompanies(DataSource dataSource){
		if(TaxiCompanies != null ) return;
		if(dataSource != null){
			Connection con = null;
			Statement stmt = null;
	        ResultSet rs = null;
	        try{
	        	con = dataSource.getConnection();
		        stmt = con.createStatement();
		        rs = stmt.executeQuery("select taxi_co_id, company_name from taxi_companies");
		        TaxiCompanies = new HashMap<Integer, String>();
		        while(rs.next()){
		        	int taxiID = rs.getInt("taxi_co_id");
		        	String companyName = rs.getString("company_name");
		        	if(taxiID > 0){
		        		TaxiCompanies.put(taxiID, companyName);
		        	}
		        }
	        }catch(SQLException se){
	        	se.printStackTrace();
	        }finally{
	        	if(rs != null) try{rs.close();}catch(SQLException ignore){};
	        	if(stmt != null) try{stmt.close();}catch(SQLException ignore){};
	        	if(con != null) try{con.close();}catch(SQLException ignore){};
	        }
		}
	}
	
	public static String getTaxiCompanyName(DataSource dataSource, int taxiID){
		getTaxiCompanies(dataSource);
		String companyName = ""; 
		if(taxiID <= 0) return companyName;
		if(TaxiCompanies != null && TaxiCompanies.containsKey(taxiID)){
			companyName = TaxiCompanies.get(taxiID);
		}
		return companyName;
	}
	
	/**
	 * Get the default account code for MobileGateway job. "CREDIT" by default.
	 * Please only use this for MG source.
	 * 
	 * @param dataSource	PF datasource
	 * @param taxiID		taxi company ID
	 * @return	the default account code
	 */
	public static String getCompanyDefaultMGAccountCode(DataSource dataSource, int taxiID) {
		getDispatchParameters(dataSource, taxiID);
		if (DispatchParameters == null ||!DispatchParameters.containsKey(taxiID)) { 
			logger.error("Failed to get dispatch parameters for company - " + taxiID);
			return DEFAULT_MG_ACCOUNT_CODE; //failed to get company parameter, default to use "CREDIT" account
		}
		HashMap<String, String> parameters = DispatchParameters.get(taxiID);
		if (parameters == null || !parameters.containsKey(COMP_PARAMETER_C_MG_ACCOUNT)) {
			logger.error("Failed to get " + COMP_PARAMETER_C_MG_ACCOUNT + " for company " + taxiID);
			return DEFAULT_MG_ACCOUNT_CODE; //failed to get the C_MG_ACCOUNT parameter, default to use "CREDIT" account
		}
		String defaultMGAcctCode = parameters.get(COMP_PARAMETER_C_MG_ACCOUNT);
		if (defaultMGAcctCode == null) {
			return DEFAULT_MG_ACCOUNT_CODE; //no such parameter for the company, default to use "CREDIT" account
		}
		String mgAccountCode = defaultMGAcctCode.trim(); 
		if (mgAccountCode.length() == 0) {
			return DEFAULT_MG_ACCOUNT_CODE; //user configured whitespace, default to use "CREDIT" account
		}
		return mgAccountCode; //use the configured account code
	}
	
	
	
	public static String getCompanyDefaultCashAccountNb(DataSource dataSource, int taxiID){
		getDispatchParameters(dataSource, taxiID);
		if(DispatchParameters != null && DispatchParameters.containsKey(taxiID)){
			HashMap<String, String> parameters = DispatchParameters.get(taxiID);
			if(parameters != null && parameters.containsKey(COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NR)){
				return parameters.get(COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NR);
			}
		}
		return DEFAULT_CASH_ACCOUNT_NAME_NR;
	}
	
	public static String getCompanyDefaultCashAccountName(DataSource dataSource, int taxiID){
		getDispatchParameters(dataSource, taxiID);
		if(DispatchParameters != null && DispatchParameters.containsKey(taxiID)){
			HashMap<String, String> parameters = DispatchParameters.get(taxiID);
			if(parameters != null && parameters.containsKey(COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NAME)){
				return parameters.get(COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NAME);
			}
		}
		return DEFAULT_CASH_ACCOUNT_NAME_NR;
	}
	
	public static void getDispatchParameters(DataSource dataSource, int taxiID){
		if(dataSource == null || taxiID <= 0) return;
		if(DispatchParameters != null && DispatchParameters.containsKey(taxiID)) return;
		
		if(DispatchParameters == null) DispatchParameters = new HashMap<Integer, HashMap<String, String>>();
		
		HashMap<String, String> parameters = getDefaultCompanyParameters(dataSource, taxiID);
		if(parameters != null) DispatchParameters.put(taxiID, parameters);
	}
	
	/**
	 * Check if specified feature is enabled in pathfinder.
	 * @param dataSource	the data source
	 * @param optionType	the feature bit, eg. {@link #RTL_TYPE_CALLBACK_OPTION}
	 * @return	true if enabled, false otherwise.
	 */
	public static boolean isOptionEnabled(DataSource dataSource, int optionType) {
		String sOptions = CompanyDefaultValues.getSystemParameterValue(dataSource, S_PF_OPTIONS);
		if (sOptions == null || sOptions.length() == 0) {
			return false; //somehow failed to get system param
		}
		
		long lOptions = 0;
		try {
			lOptions = Long.parseLong(sOptions);
		} catch (NumberFormatException e){
			logger.error("Failed to get S_PF_OPTIONS ", e);
			return false;
		}
		
		return (optionType == (optionType & lOptions));
	}
	
	public static void getSystemParameters(DataSource dataSource){
		if(SystemParameters != null && SystemParameters.size() > 0) return;
		SystemParameters = new HashMap<String, String>();
		
		if(dataSource != null){
			Connection con = null;
			PreparedStatement stmt = null;
	        ResultSet rs = null;
	        try {
	        	con = dataSource.getConnection();
		        stmt = con.prepareStatement("select * from SYSTEM_PARAMETER_VALUES");
	            rs = stmt.executeQuery();
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int numcols = rsmd.getColumnCount();
	            while (rs.next()) {
	                for (int i = 1; i < numcols + 1; i++) {
	                	SystemParameters.put(rsmd.getColumnLabel(i),
	                        rs.getString(rsmd.getColumnLabel(i)));
	                }
	            }
		        
	        }catch(SQLException se){
	        	se.printStackTrace();
	        }finally {
		        if (rs != null) try{rs.close();}catch(SQLException ignore){};
	    	    if (stmt != null) try{stmt.close();}catch(SQLException ignore){};
	    	    if(con != null) try{con.close();}catch(SQLException ignore){};
	        }   
		}
	}

	public static String getSystemParameterValue(DataSource dataSource, String parameter){
		if(parameter == null) return null;
		getSystemParameters(dataSource); 
		if(SystemParameters == null || !SystemParameters.containsKey(parameter)) return null;
		else{
			return SystemParameters.get(parameter);
		}
	}
	
	public static int getSystemParameterIntValue(DataSource dataSource, String parameter){
		String value = getSystemParameterValue(dataSource, parameter);
		int iValue = 0;
		try{
			if(value != null) iValue = Integer.parseInt(value);
		}catch(NumberFormatException ne){
		}
		return iValue;
	}
	
	public static int getSystemParameterIntValue(DataSource dataSource, String parameter, int defaultValue){
		int value = getSystemParameterIntValue(dataSource, parameter);
		return (value == 0)? defaultValue : value;
	}
	
	public static String getCompanyParameterValue(DataSource dataSource, int taxiID, String parameter){
		if(taxiID <= 0 || parameter == null) return null;
		getDispatchParameters(dataSource, taxiID);
		if(!DispatchParameters.containsKey(taxiID) || !DispatchParameters.get(taxiID).containsKey(parameter)) return null;
		return DispatchParameters.get(taxiID).get(parameter);
	}
	
	public static int getCompanyParameterIntValue(DataSource dataSource, int taxiID, String parameter){
		String value = getCompanyParameterValue(dataSource, taxiID, parameter);
		int iValue = 0;
		try{
			if(value != null) iValue = Integer.parseInt(value);
		}catch(NumberFormatException ne){
		}
		return iValue;
		
	}
	
	private static HashMap<String, String> getDefaultCompanyParameters(DataSource dataSource, int taxiID){
		//if(CompanyParameters != null) return;
		HashMap<String, String> companyParameters =null;
		if(dataSource != null){
			Connection con = null;
			Statement stmt = null;
	        ResultSet rs = null;
	        try {
	        	con = dataSource.getConnection();
		        stmt = con.createStatement();
				rs = stmt.executeQuery("select c.* from company_parameter_values c, taxi_companies t \n" 
										+" where t.taxi_co_id = " + taxiID + " \n"
										+" and t.taxi_co_id = c.taxi_co_id \n"
										+" and t.active_flag = 'Y'");
		        ResultSetMetaData rsmd = rs.getMetaData();
	    	    int numcols = rsmd.getColumnCount();
	    	    companyParameters = new HashMap<String, String>();
		        if (rs.next()) {
	        	    for (int i = 1; i < numcols + 1; i++) {
	        	    	companyParameters.put(rsmd.getColumnLabel(i),
	                	    rs.getString(rsmd.getColumnLabel(i)));
		            }
		        }
		        String cashAcctName = UserAccountDAO.getDefaultCashAccountName(taxiID, con);
		        companyParameters.put(COMP_PARAMETER_GENERAL_CASH_ACCOUNT_NAME, cashAcctName);
		        
	        }catch(SQLException se){
	        	se.printStackTrace();
	        }
	        finally {
		        if (rs != null) try{rs.close();}catch(SQLException ignore){};
	    	    if (stmt != null) try{stmt.close();}catch(SQLException ignore){};
	    	    if(con != null) try{con.close();}catch(SQLException ignore){};
	        }
	        
		}
		return companyParameters;
	}
	
	 
		
}
