/****************************************************************************
 *
 *		   		    Copyright (c), 2012
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 ****************************************************************************
 *
 *
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/osp/impl/VehInventoryImplement.java $
 * 
 * 1     11/27/12 1:16p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 
 * ******/
package com.dds.pathfinder.itaxiinterface.osp.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.dds.pathfinder.callbooker.server.entities.Vehicle;
import com.dds.pathfinder.callbooker.server.entities.VehicleState;
import com.dds.pathfinder.callbooker.server.vehicle.ejb.InterfaceVehicle;
import com.dds.pathfinder.itaxiinterface.webservice.BaseReq;
import com.dds.pathfinder.itaxiinterface.webservice.GenErrMsgRes;
import com.dds.pathfinder.itaxiinterface.webservice.GetVehicleInventoryRequest;
import com.dds.pathfinder.itaxiinterface.webservice.GetVehicleInventoryResponse;
import com.dds.pathfinder.itaxiinterface.webservice.GetVehicleStatusRequest;
import com.dds.pathfinder.itaxiinterface.webservice.GetVehicleStatusResponse;
import com.dds.pathfinder.itaxiinterface.webservice.VehicleInfoListItem;

public class VehInventoryImplement extends OSPBaseImplement {
	
	// private Logger logger = LogManager.getLogger(this.getClass());

	private InterfaceVehicle vehicleAccess;
	
	private DataSource pfDataSource;
	
	public enum VehSearchType{
		
		SEARCH_BY_COMPANYID("0"),
		SEARCH_BY_PICKUPZONE("1"),
		SEARCH_BY_DROPOFFZONE("2"),
		SEARCH_BY_REGIONGPS("3"),
		NOTDEFINED("-1");
		
		private String searchType;
		
		VehSearchType(String searchType){
			this.searchType = searchType;
		}
		
		public String getSearchType() {return searchType;}
		
	};
	
	public enum VehStatus{
		VEH_STATUS_ARRIVED("A"),
		VEH_STATUS_TEMPOFF("B"),
		VEH_STATUS_CHECKING("C"),
		VEH_STATUS_EMERGENCY("E"),
		VEH_STATUS_FREE("F"),
		VEH_STATUS_ONHOLD("H"),
		VEH_STATUS_ONBREAK("K"),
		VEH_STATUS_OFFERED("O"),
		VEH_STATUS_LOAD("P"),
		VEH_STATUS_UNEXPLAINED("X"),
		VEH_STATUS_ACCEPTED("Y"),
		VEH_STATUS_OFF("Z"),
		VEH_STATUS_NOTDEFINED("-1");
		
		private String status;
		
		VehStatus(String status){
			this.status = status;
		}
		
		public String getStatus() {return status;}
	}
	
	
	public VehInventoryImplement(InterfaceVehicle vehicleAccess, DataSource pfDataSource) {
		super();
		this.vehicleAccess = vehicleAccess;
		this.pfDataSource = pfDataSource;
	}

	@Override
	public GetVehicleInventoryResponse generateResponse(BaseReq request) {
		return getVehInventory((GetVehicleInventoryRequest)request);
	}
	
	public GetVehicleStatusResponse generateStatusResponse(GetVehicleStatusRequest request){
		GetVehicleStatusResponse response = new GetVehicleStatusResponse();
		if(!isValidGetStatusRequest(request, response)) return response;
		Vehicle vehicle = vehicleAccess.findAVehicleByCallSignNb(request.getTaxiCoID(), request.getVehicleNumber());
		if(vehicle != null){
			response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
	    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
			response.setVehicleInfo(convertToVehItem(vehicle));
		}else{
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_FIND_ANYTHING);
		}
		
		return response;
	}
	
	private boolean isValidGetStatusRequest(GetVehicleStatusRequest request, GetVehicleStatusResponse response){
		if(request == null) return false;
		
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			return false;
		}
		if(request.getTaxiCoID() == null || request.getTaxiCoID() <= 0){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			return false;
		}
		if(request.getVehicleNumber() == null || request.getVehicleNumber().trim().length() == 0){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			return false;
		}
		return true;
	}
	
	private GetVehicleInventoryResponse getVehInventory(GetVehicleInventoryRequest request){
		GetVehicleInventoryResponse response = new GetVehicleInventoryResponse();
		if(!validateSystemId(pfDataSource, request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_AUTHENTICATED);
			return response;
		}
		
		if(!isValidRequest(request, response)) return response;
		
		VehSearchType searchType = getSearchType(request);
		
		switch(searchType){
			case SEARCH_BY_COMPANYID:
				getVehListByCompanyID(request,response);
				break;		
			case SEARCH_BY_PICKUPZONE:
				getVehListByPickupZone(request, response);
				break;
			case SEARCH_BY_DROPOFFZONE:
				getVehListByDropoffZone(request, response);
				break;
			case SEARCH_BY_REGIONGPS:
				getVehListByGPSRegion(request, response);
				break;
			default:
				break;
		}
		return response;
	}
	
	private void getVehListByPickupZone(GetVehicleInventoryRequest request, GetVehicleInventoryResponse response){
		List<Vehicle> vehicles = vehicleAccess.findAllVehicleByPickupZone(request.getPickupZoneNumber(), request.getTaxiCoID());
		convertListToResponse(vehicles, response);
	}
	
	private void getVehListByDropoffZone(GetVehicleInventoryRequest request, GetVehicleInventoryResponse response){
		List<Vehicle> vehicles = vehicleAccess.findAllVehicleByDropoffZone(request.getDropoffZoneNumber(), request.getTaxiCoID());
		convertListToResponse(vehicles, response);
	}
	
	private void getVehListByCompanyID(GetVehicleInventoryRequest request, GetVehicleInventoryResponse response){
		List<Vehicle> vehicles = vehicleAccess.findAllVehicleByCompanyID(request.getTaxiCoID());
		convertListToResponse(vehicles, response);
	}
	
	private void getVehListByGPSRegion(GetVehicleInventoryRequest request, GetVehicleInventoryResponse response){
		List<Vehicle> vehicles = vehicleAccess.findAllVehicleInsideRegion(request.getTaxiCoID(), request.getRegionLTopLatitude(), 
				request.getRegionLTopLongitude(), request.getRegionRBotLatitude(), request.getRegionRBotLongitude());
		convertListToResponse(vehicles, response);
	}
	
	private void convertListToResponse(List<Vehicle> vehicles, GetVehicleInventoryResponse response){
		if(vehicles != null && vehicles.size() > 0){
			generateVehList(vehicles, response); 
		}else{
			response.setNbOfVehicles(0);
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_NOT_FIND_ANYTHING);
		}
	}
	
	private boolean isValidRequest(GetVehicleInventoryRequest request, GetVehicleInventoryResponse response){
		if(request == null) return false;
		if(request.getTaxiCoID() == null || request.getTaxiCoID() <= 0){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_COMPANY);
			return false;
		}
		if(!isValidSearchType(request)){
			response.setErrorMessage(GenErrMsgRes.ERROR_CODE_INVALID_REQUEST);
			return false;
		}
		return true;
		
	}
	
	private boolean isValidSearchType(GetVehicleInventoryRequest request){
		VehSearchType searchType = getSearchType(request);
		switch(searchType){
			case SEARCH_BY_COMPANYID:
				return true;		
			case SEARCH_BY_PICKUPZONE:
				return isPickupZoneProvided(request);
			case SEARCH_BY_DROPOFFZONE:
				return isDropZoneProvided(request);
			case SEARCH_BY_REGIONGPS:
				return isRegionGPSProvided(request);
			default:
				return false;
		}
	}
	
	private VehSearchType getSearchType(GetVehicleInventoryRequest request){
		VehSearchType inputType = VehSearchType.NOTDEFINED;
		String searchType = request.getSearchType();
		if(searchType == null || searchType.trim().length() == 0 ) return inputType;
		
		for(VehSearchType type : VehSearchType.values()){
			if(searchType.equalsIgnoreCase(type.getSearchType())){
				inputType = type;
				break;
			}
		}
		
		return inputType;
	}
	
	private String convertVehStatus(String pfVehStatus){
		VehStatus status = getPFVehStatus(pfVehStatus);
		switch(status){
			case VEH_STATUS_OFFERED:
				return "2";
			case VEH_STATUS_ACCEPTED:
				return "3";
			case VEH_STATUS_ARRIVED:
				return "4";
			case VEH_STATUS_LOAD:
				return "5";
			case VEH_STATUS_OFF:
				return "6";
			case VEH_STATUS_NOTDEFINED:
				return "";
			default:
				return "1";				//signed on
		}
	}
	
	private VehStatus getPFVehStatus(String pfVehStatus){
		VehStatus status = VehStatus.VEH_STATUS_NOTDEFINED;
		if(pfVehStatus == null || pfVehStatus.trim().length() == 0) return status;
		for(VehStatus s : VehStatus.values()){
			if(pfVehStatus.equalsIgnoreCase(s.getStatus())){
				status = s;
				break;
			}
		}
		return status;
	}
	
	private boolean isPickupZoneProvided(GetVehicleInventoryRequest request){
		String pickupZone = request.getPickupZoneNumber();
		return (pickupZone != null && pickupZone.trim().length() > 0);
	}
	
	private boolean isDropZoneProvided(GetVehicleInventoryRequest request){
		String dropoffZone = request.getDropoffZoneNumber();
		return (dropoffZone != null && dropoffZone.trim().length() > 0);
	}
	
	private boolean isRegionGPSProvided(GetVehicleInventoryRequest request){
		if(request.getRegionLTopLatitude() == null || !isValidDoubleValue(request.getRegionLTopLatitude())) return false;
		if(request.getRegionLTopLongitude() == null || !isValidDoubleValue(request.getRegionLTopLongitude())) return false;
		if(request.getRegionRBotLatitude() == null || !isValidDoubleValue(request.getRegionRBotLatitude())) return false;
		if(request.getRegionRBotLongitude() == null || !isValidDoubleValue(request.getRegionRBotLongitude())) return false;
		
		return true;
	} 
	
	
	
	
	private void generateVehList(List<Vehicle> vehicles, GetVehicleInventoryResponse response){
		response.setRequestStatus(GenErrMsgRes.STATUS_SUCCESS);
    	response.setErrorMessage(GenErrMsgRes.ERROR_CODE_SUCCESS);
    	ArrayList<VehicleInfoListItem> vehList = new ArrayList<VehicleInfoListItem>();
    	for(Vehicle veh : vehicles){
    		vehList.add(convertToVehItem(veh));
    	}
    	response.setNbOfVehicles(vehList.size());
    	response.setVehicleInfoList((VehicleInfoListItem[]) vehList.toArray(new VehicleInfoListItem[vehList.size()]));
	}
	
	private VehicleInfoListItem convertToVehItem(Vehicle vehicle){
		if(vehicle == null) return null;
		VehicleInfoListItem vehItem = new VehicleInfoListItem();
		vehItem.setVehicleNumber(vehicle.getCallsign());
		VehicleState vehState = vehicle.getVehicleState();
		if(vehState != null){
			vehItem.setVehicleStatus(convertVehStatus(vehState.getVehicleStatus()));
			if(vehState.getLatitude() != null) vehItem.setVehLatitude(vehState.getLatitude().doubleValue());
			if(vehState.getLongitude() != null) vehItem.setVehLongitude(vehState.getLongitude().doubleValue());
			if(vehState.getCurrentJobId() != null && vehState.getCurrentJobId() > 0) vehItem.setTaxiRideID(vehState.getCurrentJobId());
			else vehItem.setTaxiRideID((long) -1);
		}
		return vehItem;
	}

}
