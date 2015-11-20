/****************************************************************************
 *
 *		   		    Copyright (c), 2009
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/util/Utilities.java $
 * 
 *  PF-16499, 04/07/15, DChen, attributes failed in pfrest.
 * 
 *  PF-16428, 03/13/15, DChen, based on json-api format
 * 
 *  PF-16183, 08/29/14, DChen,added TSS require service.
 * 
 * 08/26/14, DChen, Modified for TSS Shared rider.
 * 
 * 11    2/13/14 3:41p Dchen
 * PF15872, OSP should link area attributes even not exposed.
 * 
 * 10    2/19/13 3:41p Dchen
 * PF-15221, add some attributes linked information in OSP.
 * 
 * 9     1/02/11 7:06p Mkan
 * added upcaseString() (C34954)
 * 
 * 8     10/06/10 4:04p Ezhang
 * OSP 2.0 fixed the OSP time formatting.
 * 
 * 7     4/15/10 11:04a Mkan
 * fixed attributes and date/time parsing/formatting
 * 
 * 6     3/03/10 4:40p Dchen
 * OSP interface.
 * 
 * 5     2/23/10 2:40p Dchen
 * OSP interface.
 * 
 * 4     2/02/10 2:35p Dchen
 * OSP interface.
 * 
 * 3     1/26/10 5:44p Dchen
 * OSP interface.
 * 
 * 2     1/22/10 4:52p Dchen
 * OSP interface.
 * 
 * 1     1/13/10 6:21p Dchen
 * OSP interface.
 * 
 * 
 * ******/

package com.dds.pathfinder.itaxiinterface.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.dds.pathfinder.callbooker.server.paramcache.SysAttrListItem;
import com.dds.pathfinder.itaxiinterface.webservice.Attribute;


public class Utilities {
	public static String INVALID_MUNGE_PASSWORD = "0Failed";
	
	private final static int PW_LENGTH = 16;
	private final static int PW_MULT = 0x5555;
	private final static int  PW_INC = 0x2003;
	private final static char PW_APPEND_CHAR = 'x';
	
	public static String PERCENTAGE_CHAR="%";
	
	public final static String JNDI_PF_DATA_SOURCE = "java:jboss/datasources/PathfinderDS";
	public static final String JNDI_GLOBAL_NAMESPACE = "java:global/";
    public static final String JNDI_JBOSS_NAMESPACE = "java:jboss/";
    public static final String JNDI_APP_NAMESPACE = "java:app/"; 
    public static final String PFREST_VERSION_NUMBER = "v1";
    public static final String PFREST_CONTEXT_ROOT = "http://" + (System.getProperty("jboss.bind.address") == null? "localhost:8080" : System.getProperty("jboss.bind.address") + ":8080") + "/pfrestapi/v1";
	
	
	public static String MungePassword(String password){
		if(password == null || password.length() == 0) return INVALID_MUNGE_PASSWORD;
		
		StringBuffer sBuff = new StringBuffer(password.toUpperCase());
		for(int i=password.length(); i<PW_LENGTH; i++){
			sBuff.append(PW_APPEND_CHAR);
		}
		String pwd = sBuff.toString();

	    int lRn   = PW_MULT ;
	    int lRval = 0;

	    for(int i=0; i < PW_LENGTH; i++) {
	    	char ch = pwd.charAt(i);
	        lRval += lRn * ch ;
	        lRn = (lRn + PW_INC) * PW_MULT ;
	        lRn &= 0x0000FFFFL ;
	    }

	    return ""+lRval;		
	}
	
	public static String ToBinaryAttributesString(String hexAttributesString){
		if(hexAttributesString == null || hexAttributesString.length() == 0) return hexAttributesString;
		StringBuffer sb = new StringBuffer();
		DecimalFormat numFormat = new DecimalFormat("0000");
		for(int i=0; i<hexAttributesString.length(); i++){
			String bit = "" +hexAttributesString.charAt(i);
			try{
				sb.append(numFormat.format(Integer.valueOf(Integer.toBinaryString(Integer.parseInt(bit, 16)))));
			}catch(NumberFormatException ne){
				ne.printStackTrace();
				sb.append("0000");
			}
		}
		return sb.toString();
	}
	
	public static String calcJobPriority(String priority){
		int jobPriority = 0;
		try{
			jobPriority = Integer.parseInt(priority);
		}catch(NumberFormatException ne){
			ne.printStackTrace();
		}
		if(jobPriority >0 && jobPriority <= 20){  		//should define later.
			return "H";
		}else if(jobPriority > 40){
			return "L";
		}else{
			return "N";
		}
	}
	
	
	public static String addNotes(String originalNote, String newNote, String separator)    {
        if (newNote == null || newNote.trim().length() == 0)  {
            return originalNote;
        }

        if (originalNote != null && originalNote.trim().length() > 0)    {
        	if(!originalNote.contains(newNote))
        		originalNote += ( getIndentSeparator(separator) + newNote );
        } else  {
            originalNote = "" + newNote;
        }

        return originalNote;
    }
	
	private static String getIndentSeparator(String separator) {
        return (separator==null ? "" : separator) + " ";
    }
	
	public static ArrayList<SysAttrListItem> getAttributesItem(HashMap<String, SysAttrListItem> attrMap, String attrString){
		ArrayList<SysAttrListItem> attributes = new ArrayList<SysAttrListItem>();
		if(attrMap == null || attrString == null || attrMap.size() == 0 || attrString.length() ==0) return attributes;
		for(int i=0; i<attrString.length(); i++){
			if("1".equals(Character.toString(attrString.charAt(i)) ) ){
				String bitPosition = "" + (i+1);
				if(attrMap.containsKey(bitPosition)){
					attributes.add(attrMap.get(bitPosition));
				}
			}
		}
		return attributes;
	}
	
	public static ArrayList<String> getAttrShortNameList(ArrayList<SysAttrListItem> attributes){
		if(attributes == null || attributes.size() == 0) return null;
		ArrayList<String> shortList = new ArrayList<String>();
		for(SysAttrListItem attribute : attributes){
			shortList.add(attribute.getAttrShortName());
		}
		return shortList;
	}
	
	public static String[] appendRequiredAttribute(String[] inputAttrs, ArrayList<String> attributes){
		if(attributes == null || attributes.size() == 0)  return inputAttrs;
		
		if(inputAttrs == null || inputAttrs.length == 0){
				return attributes.toArray(new String[attributes.size()]);
		}else{
			ArrayList<String> attrList = new ArrayList<String>(Arrays.asList(inputAttrs));
			for(String attr : attributes){
				if(!attrList.contains(attr)) attrList.add(attr);
			}
			return attrList.toArray(new String[attrList.size()]);
		}
	}
	
	public static ArrayList<String> appendArrayListString(ArrayList<String> s1, ArrayList<String> s2){
		if(s2 == null || s2.size() == 0) return s1;
		if(s1 == null || s1.size() == 0) return s2;
		
		ArrayList<String> s3 = new ArrayList<String>(s1);
		for(String attr : s2){
			if(!s3.contains(attr)) s3.add(attr);
		}
		return s3;
	}
	
	
	public static final int ATTRIBUTES_BITS_NUMBER = 128;
	/**
	 * Find PF attribute binary representation of specified attribute.
	 * Note: this function expects attrMap that specifies position from 1 to 128 (ATTRIBUTES_BITS_NUMBER) inclusively.
	 * 
	 * @param attrMap		attribute map built with PF parameter
	 * @param attributes	the attribute to find out
	 * @return	The binary string
	 * 
	 */
	public static String getPFAttributesBinary(HashMap<String, SysAttrListItem> attrMap, Attribute[] attributes){
		StringBuffer attrBinary = new StringBuffer();
        for(int i=0; i<ATTRIBUTES_BITS_NUMBER; i++ ) {
        	attrBinary.append("0");
        }
        
		if(attrMap == null || attrMap.size() == 0 || attributes == null || attributes.length == 0) return attrBinary.toString();
		for(int i=0; i<attributes.length; i++){
			Attribute attribute = attributes[i];
			int bitPosition = findBitPosition(attrMap, attribute);
			if(bitPosition >= 1 && bitPosition <=ATTRIBUTES_BITS_NUMBER) {
				attrBinary.setCharAt(bitPosition-1, //need "-1" here because database code (that made up the attrMap) considers the first bit position to be "1".
													//you can check the bit positions with the following query
													//select ar.BIT_POSITION, ar.ATTRIB_TYPE, ar.DESCRIPTION from attributes ar where ar.KILLED_FLAG ='N'
									 '1'); 
				
			}
		}
		return attrBinary.toString();
	}
	
	
	public static String getPFAttributesBinary(HashMap<String, SysAttrListItem> attrMap, String[] shortAttrs){
		StringBuffer attrBinary = new StringBuffer();
        for(int i=0; i<ATTRIBUTES_BITS_NUMBER; i++ ) {
        	attrBinary.append("0");
        }
        
		if(attrMap == null || attrMap.size() == 0 || shortAttrs == null || shortAttrs.length == 0) return attrBinary.toString();
		for(String attrName : shortAttrs){
			
			int bitPosition = findBitPosition(attrMap, attrName);
			if(bitPosition >= 1 && bitPosition <=ATTRIBUTES_BITS_NUMBER) {
				attrBinary.setCharAt(bitPosition-1, //need "-1" here because database code (that made up the attrMap) considers the first bit position to be "1".
													//you can check the bit positions with the following query
													//select ar.BIT_POSITION, ar.ATTRIB_TYPE, ar.DESCRIPTION from attributes ar where ar.KILLED_FLAG ='N'
									 '1'); 
				
			}
		}
		return attrBinary.toString();
	}
	
	public static String getPFAttributesBinaryByDesc(HashMap<String, SysAttrListItem> attrMap, ArrayList<String>attributes){
		StringBuffer attrBinary = new StringBuffer();
        for(int i=0; i<ATTRIBUTES_BITS_NUMBER; i++ ) {
        	attrBinary.append("0");
        }
        
		if(attrMap == null || attrMap.size() == 0 || attributes == null || attributes.size() == 0) return attrBinary.toString();
		for(String desc : attributes){
			int bitPosition = findBitPositionByDesc(attrMap, desc);
			
			if(bitPosition >= 1 && bitPosition <=ATTRIBUTES_BITS_NUMBER) {
				attrBinary.setCharAt(bitPosition-1, //need "-1" here because database code (that made up the attrMap) considers the first bit position to be "1".
													//you can check the bit positions with the following query
													//select ar.BIT_POSITION, ar.ATTRIB_TYPE, ar.DESCRIPTION from attributes ar where ar.KILLED_FLAG ='N'
									 '1'); 
				
			}
		}
		return attrBinary.toString();
	}
	
	
	public static int findBitPosition(HashMap<String, SysAttrListItem> attrMap, String shortName){
		int position = -1;
		if(shortName == null  || shortName.trim().length() == 0) return position;
		Set<String> keys = attrMap.keySet();
		for(String key : keys){
			SysAttrListItem att = attrMap.get(key);
			if(att != null && shortName.equals(att.getAttrShortName())){
				try{
					position = Integer.parseInt(key);
					return position ; 
				}catch(NumberFormatException ne){
					ne.printStackTrace();
				}
			}
		}
		return position;
	}
	
	/**
	 * Return bit position of attribute according to provided attribute map.
	 * @param attrMap	the attribute map
	 * @param attribute	the attribute
	 * @return
	 */
	public static int findBitPosition(HashMap<String, SysAttrListItem> attrMap, Attribute attribute){
		int position = -1;
		if(attribute == null || attribute.getAttrShortName() == null || attribute.getAttrShortName().trim().length() == 0) return position;
		Set<String> keys = attrMap.keySet();
		for(String key : keys){
			SysAttrListItem att = attrMap.get(key);
			if(att != null && attribute.getAttrShortName().equals(att.getAttrShortName())){
				try{
					position = Integer.parseInt(key);
					return position ; 
				}catch(NumberFormatException ne){
					ne.printStackTrace();
				}
			}
		}
		return position;
	}
	
	
	public static int findBitPositionByDesc(HashMap<String, SysAttrListItem> attrMap, String desc){
		int position = -1;
		if(desc == null || desc.isEmpty()) return position;
		
		Set<String> keys = attrMap.keySet();
		for(String key : keys){
			SysAttrListItem att = attrMap.get(key);
			if(desc.equalsIgnoreCase(att.getAttrLongName())){
				try{
					position = Integer.parseInt(key);
					return position ; 
				}catch(NumberFormatException ne){
					ne.printStackTrace();
				}
			}
		}
		return position;
	}
	
	public static String getPFAttributesBinary(HashMap<String, SysAttrListItem> attrMap, ArrayList<SysAttrListItem> attributes){
		StringBuffer attrBinary = new StringBuffer();
        for(int i=0; i<ATTRIBUTES_BITS_NUMBER; i++ ) {
        	attrBinary.append("0");
        }
        
		if(attrMap == null || attrMap.size() == 0 || attributes == null || attributes.size() == 0) return attrBinary.toString();
		for(SysAttrListItem attribute : attributes){
			int bitPosition = findBitPosition(attrMap, attribute);
			if(bitPosition >= 1 && bitPosition <=ATTRIBUTES_BITS_NUMBER) {
				attrBinary.setCharAt(bitPosition-1, //need "-1" here because database code (that made up the attrMap) considers the first bit position to be "1".
													//you can check the bit positions with the following query
													//select ar.BIT_POSITION, ar.ATTRIB_TYPE, ar.DESCRIPTION from attributes ar where ar.KILLED_FLAG ='N'
									 '1'); 
				
			}
		}
		return attrBinary.toString();
	}
	
	public static int findBitPosition(HashMap<String, SysAttrListItem> attrMap, SysAttrListItem attribute){
		int position = -1;
		if(attrMap == null || attrMap.size() == 0) return position;
		if(attribute == null || attribute.getAttrShortName() == null || attribute.getAttrShortName().trim().length() == 0) return position;
		
		Set<String> keys = attrMap.keySet();
		for(String key : keys){
			SysAttrListItem att = attrMap.get(key);
			if(att != null && attribute.getAttrShortName().equals(att.getAttrShortName())){
				try{
					position = Integer.parseInt(key);
					return position ; 
				}catch(NumberFormatException ne){
					ne.printStackTrace();
				}
			}
		}
		return position;
	}
	
	public static String binStringBitOR(String binString1, String binString2){
		if(binString1 == null || binString1.trim().length() == 0) return binString2;
		if(binString2 == null || binString2.trim().length() == 0) return binString1;
		
		int maxLength = Math.max(binString1.length(), binString2.length());
		StringBuffer bitOrString = new StringBuffer();
		char[] string1 = binString1.toCharArray();
		char[] string2 = binString2.toCharArray();
		for(int i=0; i< maxLength; i++){
			if( i >= string1.length) bitOrString.append(string2[i]);
			else{
				if(i >= string2.length) bitOrString.append(string1[i]);
				else bitOrString.append((char)(string1[i] | string2[i]));
			}
		}
		return bitOrString.toString();
		
	}
	
	   /**
	   * Utility method to convert a string to Timestamp by using input format.
	   * 
	   * @param String	input string, if the input is null, method returns null
	   * @param String	input format, if the input format is illegal, returns null
	   * @return Timestamp	the parsed object from input string
	   * 
	   */
	    public static java.util.Date convertUtilDate(String sDate, String sFormat){
	        SimpleDateFormat dateFormat = null;
	        try{
	              dateFormat=new SimpleDateFormat(sFormat);
	        }catch(Exception e) {return null;}
	        
	        ParsePosition parsePosition = new ParsePosition(0);
	        java.util.Date date=null;
	
	        if(sDate ==null) return null;
	        else
	          date = dateFormat.parse(sDate, parsePosition);
	
	        return date;   
	    }	
	    
		/**
		 * Method to convert an input util Date to output string using input format.
		 * 
		 * @param	Date		input util Date, if input is null, returns null
		 * @param 	String		input format, if format is illegal, returns null
		 * @return	String 		the result formatted string
		 * 
		 */
		public static String formatUtilDate(java.util.Date aDate, String sFormat){
			
			SimpleDateFormat dateFormat = null;
			try{
		    	dateFormat=new SimpleDateFormat(sFormat);
			}catch(Exception e){return null;}	
			
		    FieldPosition fieldPosition = new FieldPosition(0);
		    StringBuffer buff=new StringBuffer();
		    

		    if(aDate ==null) return null;
		    else{
		        try{
		        	buff=dateFormat.format(aDate,buff,fieldPosition);
		            return new String(buff);
		        }catch(NullPointerException e){
		            return null;
		        }
		    }

		}	        
		/** OSP default date only format, yyyy-MM-dd */
		public static final String OSP_DEFAULT_DATEONLY_FORMAT = "yyyy-MM-dd";
		/** OSP default date/time format, yyyy-MM-dd HH:mm:ss */
		public static final String OSP_DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; 
		public static final String OSP_2ND_DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
		public static final String CALLBOOKER_DEFAULT_DATE_FORMAT = "yyyy-MMM-dd HH:mm";
		
		public static String formatOSPDefaultDate(java.util.Date aDate){
			return formatUtilDate(aDate, OSP_DEFAULT_DATE_FORMAT);
		}
		
		public static java.util.Date convertOSPDefaultDate(String sDate){
			Date date = convertUtilDate(sDate, OSP_DEFAULT_DATE_FORMAT);
			if(date == null) {
				return convertUtilDate(sDate, OSP_2ND_DEFAULT_DATE_FORMAT);  //try the 2nd format
			}
			return date;
		}
		
		/**
		 * Given date string and time string, return the osp date/time string {@link #OSP_DEFAULT_DATE_FORMAT}
		 * @param sDate	the date string in OSP date format, {@link #OSP_DEFAULT_DATEONLY_FORMAT}
		 * @param sTime the time string in OSP time format, time portion of {@link #OSP_DEFAULT_DATE_FORMAT}
		 * @return
		 */
		public static String composeOSPDefaultDate(String sDate, String sTime) {
			if (sDate == null || sDate.length() == 0 
					|| sTime == null || sTime.length() == 0 || sTime.equals("00:00:00")) {
				return null;
			}
			return sDate + " " + sTime;
		}
		
		public static final String DB_DATE_FORMAT = "dd,MM,yyyy";
		
		/**
		 * This converts database date format into OSP date format, {@link #OSP_DEFAULT_DATEONLY_FORMAT} 
		 * @param dbDate	the string contains the Date
		 * @return the reformatted date string in OSP format 
		 */
		public static String convertDB2OSPDateFormat(String dbDate){
			if(dbDate == null || dbDate.length() == 0) {
				return dbDate;
			}
			Date date = Utilities.convertUtilDate(dbDate, DB_DATE_FORMAT);
			if(date == null) {
				return dbDate; //failed to get Date from dbDate
			}
			return Utilities.formatUtilDate(date, OSP_DEFAULT_DATEONLY_FORMAT);	//parse into date only format
		}
		
		/**
		 * This converts database time format into OSP time format
		 * @param dbTime	the string contains the time
		 * @return	the reformatted date string in OSP format 
		 */
		public static String convertDB2OSPTimeFormat(String dbTime){
			if(dbTime == null || dbTime.length() == 0) {
				return "00:00:00";
			}
			else if(dbTime != null && dbTime.length()>5){
				//for case time is like 14:54:43
				return dbTime;
			}
			else{
				//for case time is like 10:17
				return dbTime+":00";
			}
		}

		/**
		 * A common utility to uppercase a string.
		 * Created specifically to fix issue found in German database
		 * (C34954 Character ß not converting properly)
		 * @param  str	the string to convert
		 * @return the upper-cased string. note: the 'ß' character will remain the same.
		 */
		public static String upcaseString(String str) {
			if(str == null || str.trim().length() == 0) {
				return str;
			}
			String funnyChar = "^".toUpperCase();
			boolean replaceDone = false;
			if (str.contains("ß")){
				str = str.replace("ß", funnyChar);
				replaceDone = true;
			}
			String convertedStr =  str.toUpperCase();
			if (replaceDone) {
				return convertedStr.replace(funnyChar, "ß");
			}
			return convertedStr;
		}
		
		/**
		 * Trim and append percentage character to string as specified
		 * 
		 * @param inputString	the string to trim and append
		 * @param appendPer		true to append percentage
		 * 					    false otherwise
		 * @return the updated string
		 */
		public static String appendPercentage(String inputString, boolean appendPer) {
			if (inputString == null || inputString.trim().length() == 0) {
				if (appendPer) {
					return PERCENTAGE_CHAR;
				}
				return "";
			}
			inputString = inputString.trim();
			if (inputString.endsWith(PERCENTAGE_CHAR)) {
				return inputString; //already end with percentage, return the trimmed string
			}
			if (appendPer) {
				return inputString + PERCENTAGE_CHAR;
			}
			return inputString; //append percentage not requested, return the trimmed string

		}
		
}

