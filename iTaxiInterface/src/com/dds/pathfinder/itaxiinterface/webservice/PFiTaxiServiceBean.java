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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/PFiTaxiServiceBean.java $
 * 
 * PF-16496, 05/29/15, DChen, add after dispatch eta.
 * 
 * PF-16496, 05/27/15, DChen, add pre dispatch eta.
 * 
 * PF-16398, 04/06/15, DChen, add isAddressServiceable service
 * 
 * 08/26/14, DChen, added TSS shared rider service.
 * 
 * PF-16067, 6/24/14 DChen, added carRapidFlag in OSP RecallJobDetails
 * 
 * 51    3/12/14 3:32p Dchen
 * Added osp_users table instead of hard coding.
 * 
 * 50    1/29/14 3:47p Ezhang
 * PF-15809 used cachedParam for sendDriverMessage, TokenPayment and account validation
 * 
 * 49    1/16/14 3:43p Dchen
 * PF-15847, UDI callout extensions.
 * 
 * 48    10/31/13 12:20p Sfoladian
 * Changed to get the parameter from the cachedParam instead of DB
 * 
 * 47    10/30/13 4:21p Sfoladian
 * PF-15621: changed sendDriverMessage to return SendDriverMessageResponse
 * instead of null
 * 
 * 46    10/28/13 12:54p Sfoladian
 * PF-15595
 * PF-15587
 * 
 * 45    10/23/13 3:05p Ezhang
 * PF-15582 added payment gateway service interface
 * PF-15613 added account validation
 * 
 * 44    3/12/13 4:34p Dchen
 * Added pf parameters load bean.
 * 
 * 43    11/27/12 1:07p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 42    7/31/12 4:45p Ezhang
 * PF-14630 modify recallJobDetail().
 * 
 * 41    5/22/12 12:03p Ezhang
 * added cxf soap msg tracing
 * 
 * 40    8/19/11 3:38p Dchen
 * port C34861, 34862, 35460 here.
 * 
 * 39    8/18/11 3:32p Dchen
 * upgrade to 3.75.
 * 
 * 38    4/19/11 6:28p Dchen
 * C35277, speed webbooker order tracking.
 * 
 * 37    12/10/10 4:58p Mkan
 * - added UserAccountHome for C34595
 * 
 * 36    9/27/10 11:15a Ajiang
 * Added tripUpdate()
 * 
 * 35    9/24/10 1:33p Ezhang
 * added advancedadress match
 * 
 * 34    9/20/10 12:03p Ezhang
 * Added new APIs for OSP 2.0
 * 
 * 33    9/14/10 2:41p Ajiang
 * C34078 - Texas Taxi Enhancements
 * 
 * 32    4/15/10 11:07a Mkan
 * recallJobs() and getAdviseArrivals() - updated to also use pfDataSource
 * 
 * 31    2/25/10 3:10p Dchen
 * OSP interface.
 * 
 * 30    2/23/10 2:41p Dchen
 * OSP interface.
 * 
 * 29    2/09/10 11:20a Jwong
 * 
 * 28    1/29/10 10:22a Dchen
 * OSP interface.
 * 
 * 27    1/26/10 5:44p Dchen
 * OSP interface.
 * 
 * 26    1/22/10 4:53p Dchen
 * OSP interface.
 * 
 * 25    1/18/10 1:33p Yyin
 * Added getPredefinedMessage().
 * 
 * 24    1/18/10 1:23p Dchen
 * OSP interface.
 * 
 * 23    1/13/10 6:19p Dchen
 * OSP interface.
 * 
 * 22    1/12/10 2:27p Yyin
 * Added methods driverMessageType() and predefinedMessageList().
 * 
 * 21    1/11/10 1:22p Dchen
 * some code changes.
 * 
 * 20    12/03/09 10:42a Jwong
 * 
 * 19    11/30/09 9:45a Yyin
 * Added logoff() method.
 * 
 * 18    11/26/09 11:59a Jwong
 * 
 * 17    11/25/09 2:57p Yyin
 * Added login() method.
 * 
 * 16    11/17/09 5:32p Yyin
 * Added getAdviseArrivals().
 * 
 * 15    11/16/09 11:13a Jwong
 * 
 * 14    10/28/09 2:28p Dchen
 * set default passenger number 1.
 * 
 * 13    10/28/09 2:22p Yyin
 * C32549. Rename GetJobs and GetJobDetail to RecallJob and
 * RecallJobDetail.
 * 
 * 12    10/21/09 4:38p Dchen
 * Telus issue, booking job with lat and longitude only.
 * 
 * 11    10/21/09 2:30p Yyin
 * Added GetAttributeList method
 * 
 * 10    10/19/09 4:59p Yyin
 * Added GetAccountList method
 * 
 * 9     10/15/09 10:32a Dchen
 * added a new method for jimmy.
 * 
 * 8     10/13/09 4:09p Dchen
 * modified namespace for OSP project.
 * 
 * 7     10/02/09 5:25p Dchen
 * pathfinder iTaxi interface.
 * 
 * 6     10/01/09 1:46p Yyin
 * 
 * 5     9/25/09 5:44p Dchen
 * pathfinder iTaxi interface.
 * 
 * 4     9/17/09 3:40p Dchen
 * pathfinder iTaxi interface.
 * 
 * 3     8/14/09 4:21p Dchen
 * pathfinder iTaxi interface.
 * 
 * 2     8/13/09 5:47p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 12:04p Dchen
 * pathfinder iTaxi interface.
 * 
 */

package com.dds.pathfinder.itaxiinterface.webservice;



import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.sql.DataSource;

import org.apache.cxf.feature.Features;
//import org.jboss.ejb3.annotation.RemoteBinding;
import org.jboss.ejb3.annotation.TransactionTimeout;
//import org.jboss.wsf.spi.annotation.WebContext;
import org.jboss.ws.api.annotation.WebContext;

import com.dds.pathfinder.callbooker.server.account.user.ejb.UserAccountLocal;
import com.dds.pathfinder.callbooker.server.facade.ejb.CallbookerFacadeLocal;
import com.dds.pathfinder.callbooker.server.jobsearch.ejb.JobSearchLocal;
import com.dds.pathfinder.callbooker.server.order.ejb.OrderLocal;
import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.callbooker.server.pfa.ejb.PfaLocal;
import com.dds.pathfinder.callbooker.server.telephone.ejb.UserTelephoneLocal;
import com.dds.pathfinder.callbooker.server.vehicle.ejb.VehicleLocal;
import com.dds.pathfinder.itaxiinterface.osp.impl.AccountListImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.AccountPaymentImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.AccountValidationImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.AcctDetailImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.AdvancedAddressImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.AdviseArrivalTypesImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.AttribListImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.AvailableCarsImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.BookJobImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.CancelJobImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.CancelOrderByConfNumImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.CarValidationImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.ClientProfileImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.DispRegionListImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.FindJobImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.JobDetailsImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.LoginImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.LogoffImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.MbParameterImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.PhoneNumberValidationImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.PickupTimeValidationImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.RateTripImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.SendDriverMessageImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.TokenPaymentImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.TripInfoImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.UpdateOrderByConfNumImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.ValidateAddressImplement;
import com.dds.pathfinder.itaxiinterface.osp.impl.VehInventoryImplement;
import com.dds.pathfinder.itaxiinterface.tss.TSSCancelVehReq;
import com.dds.pathfinder.itaxiinterface.tss.TSSCancelVehRes;
import com.dds.pathfinder.itaxiinterface.tss.TSSRequireVehReq;
import com.dds.pathfinder.itaxiinterface.tss.TSSRequireVehRes;
import com.dds.pathfinder.itaxiinterface.tss.impl.TSSCancelVehImplement;
import com.dds.pathfinder.itaxiinterface.tss.impl.TSSRequireVehImplement;
import com.dds.pathfinder.itaxiinterface.util.Debug2;
import com.dds.pathfinder.itaxiinterface.wslookup.CallbookerAddressLookup;
import com.dds.pathfinder.itaxiinterface.wslookup.IAddressLookup;



/**
 * A stateless session bean to do service deploy as web service on jbossws.
 * Created for Smart Cab Fleet Manager API project. 
 *
 */
// standard JSR181 annotations
@WebService(name = "OSPTaxiServiceInterface", targetNamespace = "http://com.dds.osp.itaxi.interface/", serviceName = "OSPTaxiService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
// standard EJB3 annotations
@Remote(PFiTaxiServiceInterface.class)
@Stateless
// jboss propriatary annotations
// @RemoteBinding(jndiBinding = "/taxiproduct/PFiTaxiServiceBean")
@WebContext(contextRoot = "/com.dds.osp", urlPattern="/OSPTaxiService")
@Features(features = "org.apache.cxf.feature.LoggingFeature" )       //add cxf soap msg tracing
public class PFiTaxiServiceBean implements PFiTaxiServiceInterface
{
	private static Debug2 logger = Debug2.getLogger(PFiTaxiServiceBean.class);
	
	IAddressLookup addressLookUp = null;
	//PaymentGatewayServiceInterface pgService = null;
//	static{
//		getAddressLookUp();
//	}
	
    @Resource(mappedName =com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_PF_DATA_SOURCE)
    private DataSource pfDataSource;
    
    @EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + UserAccountLocal.JNDI_BINDING)
	private UserAccountLocal userAccountLocal;
    
    @EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + JobSearchLocal.JNDI_BINDING)
    private JobSearchLocal jobSearchLocal;
    
    @EJB(mappedName= com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + OrderLocal.JNDI_BINDING)
    private OrderLocal orderLocal;
    
    @EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + CallbookerFacadeLocal.JNDI_BINDING)
    private CallbookerFacadeLocal facadeLocal;
    
    @EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + UserTelephoneLocal.JNDI_BINDING)
    private UserTelephoneLocal telephoneLocal;
    
    @EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + VehicleLocal.JNDI_BINDING)
    private VehicleLocal vehicleLocal;
    
    @EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + LoadDispatchParametersLocal.JNDI_BINDING)
    private LoadDispatchParametersLocal cachedParam;
    
    @EJB(mappedName = com.dds.pathfinder.itaxiinterface.util.Utilities.JNDI_GLOBAL_NAMESPACE + PfaLocal.JNDI_BINDING)
    private PfaLocal pfaLocal;
	
    public static String PERCENTAGE_CHAR="%";
    public static final String RESPONSE_SUCCESS = "SUCCESS";
    public final static int MAX_FLOAT_VALUE = 181;
    
    @PostConstruct
    public void initAddressLookup(){
    	addressLookUp = getAddressLookUp();
    	if(pfaLocal != null) {
    		if(pfaLocal.initNavServiceStubPool() ) logger.info("DW navigation service is initialized....");		//init DW navigation service.
    	}
    }
    
  
    
   /**
    * Provide job Transaction Request.
    * @param CoreBookJobReq wrapped income XML document
    * @return JobTransRes wrapped return XML document
    * 
    */
   @WebMethod(operationName = "BookJob")
   @WebResult(name = "BookJobRes")
   @TransactionTimeout(600)
   public BookJobRes bookJob(@WebParam(name = "BookJobReq")BookJobReq request){
	   BookJobImplement bookJobImp = new BookJobImplement(getAddressLookUp(), pfDataSource, orderLocal, userAccountLocal, facadeLocal, cachedParam);
	   bookJobImp.setUserTelephone(telephoneLocal);
	   return bookJobImp.generateResponse(request);
	   //return (generateBookJobRes(request));
   }
   
   @WebMethod(operationName = "ValidateAddress")
   @WebResult(name = "AddressRes")
   @TransactionTimeout(600)
   public AddressRes validateAddress(@WebParam(name = "AddressReq")AddressReq request){
	   ValidateAddressImplement addressImp = new ValidateAddressImplement(getAddressLookUp(), pfDataSource);
	   addressImp.setCachedParam(cachedParam);
	   return (addressImp.generateResponse(request));
   }
   
   
   @WebMethod(operationName = "IsAddressServiceable")
   @WebResult(name = "GPSServiceableResponse")
   @TransactionTimeout(600)
   public GPSServiceableResponse isAddressServiceable(@WebParam(name = "GPSServiceableRequest")GPSServiceableRequest request){
	   ValidateAddressImplement addressImp = new ValidateAddressImplement(getAddressLookUp(), pfDataSource);
	   addressImp.setCachedParam(cachedParam);
	   return (addressImp.getGPSServiceableResponse(request));
   }
   
   @WebMethod(operationName = "GetDispatchRegions")
   @WebResult(name = "DispatchRegionRes")
   @TransactionTimeout(600)
   public DispatchRegionRes getDispatchRegions(@WebParam(name = "DispatchRegionReq")DispatchRegionReq request){
	   DispRegionListImplement regionList = new DispRegionListImplement(pfDataSource);
	   regionList.setCachedParam(cachedParam);
	   return regionList.generateResponse(request);
   }
   
   @WebMethod(operationName = "RecallJobDetail")
   @WebResult(name = "RecallJobDetailResponse")
   @TransactionTimeout(600)
   public RecallJobDetailResponse recallJobDetail(@WebParam(name = "RecallJobDetailRequest")RecallJobDetailRequest request){
	   JobDetailsImplement jobDetail = new JobDetailsImplement(jobSearchLocal, pfDataSource, getAddressLookUp(), orderLocal);
	   jobDetail.setCachedParam(cachedParam);
	   return jobDetail.generateResponse(request);
   }
   
   @WebMethod(operationName = "RecallJobs")
   @WebResult(name = "RecallJobsResponse")
   @TransactionTimeout(600)
   public RecallJobsResponse recallJobs(@WebParam(name = "RecallJobsRequest")RecallJobsRequest request){
	   FindJobImplement findJob = new FindJobImplement(pfDataSource, jobSearchLocal, facadeLocal, getAddressLookUp(), cachedParam);
	   return findJob.generateResponse(request);
   }

   @WebMethod(operationName = "GetAccountDetail")
   @WebResult(name = "GetAccountDetailResponse")
   @TransactionTimeout(600)
   public GetAccountDetailResponse getAccountDetail(@WebParam(name = "GetAccountDetailRequest")GetAccountDetailRequest request){
	   AcctDetailImplement acctDetail = new AcctDetailImplement(userAccountLocal, pfDataSource);
	   acctDetail.setCachedParam(cachedParam);
	   return acctDetail.generateResponse(request);
   }
   
   @WebMethod(operationName = "GetAccountList")
   @WebResult(name = "GetAcctListResponse")
   @TransactionTimeout(600)
   public GetAcctListResponse getAccountList(@WebParam(name = "GetAcctListRequest")GetAcctListRequest request){
	   AccountListImplement listAcct = new AccountListImplement(pfDataSource);
	   listAcct.setCachedParam(cachedParam);
	   return listAcct.generateResponse(request);
   }
   
   @WebMethod(operationName = "GetAttributeList")
   @WebResult(name = "GetAttrListResponse")
   @TransactionTimeout(600)
   public GetAttrListResponse getAttributeList(@WebParam(name = "GetAttrListRequest")GetAttrListRequest request){
	   AttribListImplement listAttr = new AttribListImplement(pfDataSource);
	   listAttr.setCachedParam(cachedParam);
	   return listAttr.generateResponse(request);
   }
   
   @WebMethod(operationName = "GetVehInventory")
   @WebResult(name = "GetVehicleInventoryResponse")
   @TransactionTimeout(600)
   public GetVehicleInventoryResponse getVehInventory(@WebParam(name = "GetVehicleInventoryRequest")GetVehicleInventoryRequest request){
	   VehInventoryImplement vehList = new VehInventoryImplement(vehicleLocal, pfDataSource);
	   vehList.setCachedParam(cachedParam);
	   return vehList.generateResponse(request);
	   
   }
   
   @WebMethod(operationName = "GetVehicleStatus")
   @WebResult(name = "GetVehicleStatusResponse")
   @TransactionTimeout(600)
   public GetVehicleStatusResponse getVehicleStatus(@WebParam(name = "GetVehicleStatusRequest")GetVehicleStatusRequest request){   
	   VehInventoryImplement vehList = new VehInventoryImplement(vehicleLocal, pfDataSource);
	   vehList.setCachedParam(cachedParam);
	   return vehList.generateStatusResponse(request);
	   
   }
   
   @WebMethod(operationName = "GetAdviseArrivals")
   @WebResult(name = "AdviseArrivalResponse")
   @TransactionTimeout(600)
   public AdviseArrivalResponse getAdviseArrivals(@WebParam(name = "AdviseArrivalRequest")AdviseArrivalRequest request){
	   AdviseArrivalTypesImplement adviseTypes = new AdviseArrivalTypesImplement(pfDataSource);
	   adviseTypes.setCachedParam(cachedParam);
	   return adviseTypes.generateResponse(request);
   }
   
   
   @WebMethod(operationName = "GetUDICallout")
   @WebResult(name = "GetUDICalloutResponse")
   @TransactionTimeout(600)
   public GetUDICalloutResponse getUDICallout(@WebParam(name = "GetUDICalloutRequest")GetUDICalloutRequest request){
	   AdviseArrivalTypesImplement aaImplement = new AdviseArrivalTypesImplement(pfDataSource, orderLocal, true);
	   aaImplement.setCachedParam(cachedParam);
	   return aaImplement.generateGetUDICalloutResponse(request);
   }
   
   @WebMethod(operationName = "UpdateUDICallout")
   @WebResult(name = "UpdateUDICalloutRes")
   @TransactionTimeout(600)
   public UpdateUDICalloutRes updateUDICallout(@WebParam(name = "UpdateUDICalloutReq")UpdateUDICalloutReq request){
	   AdviseArrivalTypesImplement aaImplement = new AdviseArrivalTypesImplement(pfDataSource, orderLocal, true);
	   aaImplement.setCachedParam(cachedParam);
	   return aaImplement.generateUpdateUDICalloutResponse(request);
   }
   
   @WebMethod(operationName = "Login")
   @WebResult(name = "LoginResponse")
   @TransactionTimeout(600)
   public LoginResponse login(@WebParam(name = "LoginRequest")LoginRequest request){
	   LoginImplement loginImp = new LoginImplement(pfDataSource);
	   loginImp.setCachedParam(cachedParam);
	   return loginImp.generateResponse(request);
   }
   
   @WebMethod(operationName = "Logoff")
   @WebResult(name = "LogoffResponse")
   @TransactionTimeout(600)
   public LogoffResponse logoff(@WebParam(name = "LogoffRequest")LogoffRequest request){
	   LogoffImplement logoffImp = new LogoffImplement();
	   return logoffImp.generateResponse(request);
   }
   
   @WebMethod(operationName = "GetClientProfile")
   @WebResult(name = "ClientProfileResponse")
   @TransactionTimeout(600)
   public ClientProfileResponse getClientProfile(@WebParam(name = "ClientProfileRequest")ClientProfileRequest request){
	   ClientProfileImplement profileImp = new ClientProfileImplement(telephoneLocal, pfDataSource);
	   profileImp.setCachedParam(cachedParam);
	   return profileImp.generateResponse(request);
   }
   
   @WebMethod(operationName = "GetTripInfo")
   @WebResult(name = "TripInfoResponse")
   @TransactionTimeout(600)
   public TripInfoResponse getTripInfo(@WebParam(name = "TripInfoRequest")TripInfoRequest request){
	   TripInfoImplement tripInfo = new TripInfoImplement(pfDataSource, getAddressLookUp(), facadeLocal);
	   tripInfo.setCachedParam(cachedParam);
	   return tripInfo.generateResponse(request);
   }
   
   @WebMethod(operationName = "CancelJob")
   @WebResult(name = "CancelJobRes")
   @TransactionTimeout(600)
   public CancelJobRes cancelJob(@WebParam(name = "CancelJobReq")CancelJobReq request){
	   CancelJobImplement cancelJobImp = new CancelJobImplement(pfDataSource);
	   cancelJobImp.setCachedParam(cachedParam);
	   return cancelJobImp.generateResponse(request);
   }

   @WebMethod(operationName = "DriverMessageType")
   @WebResult(name = "DriverMessageTypeRsp")
   @TransactionTimeout(600)
   public DriverMessageTypeRsp driverMessageType(@WebParam(name = "DriverMessageTypeReq")DriverMessageTypeReq request){
	   return null;
   }
   
   @WebMethod(operationName = "PredefinedMessageList")
   @WebResult(name = "PredefinedMessageListRsp")
   @TransactionTimeout(600)
   public PredefinedMessageListRsp predefinedMessageList(@WebParam(name = "PredefinedMessageListReq")PredefinedMessageListReq request){
	   return null;
   }
   
   @WebMethod(operationName = "GetPredefinedMessage")
   @WebResult(name = "GetPredefinedMsgRsp")
   @TransactionTimeout(600)
   public GetPredefinedMsgRsp getPredefinedMessage(@WebParam(name = "GetPredefinedMsgReq")GetPredefinedMsgReq request){
	   return null;
   }
   
   @WebMethod(operationName = "SendDriverMessage")
   @WebResult(name = "SendDriverMessageResponse")
   @TransactionTimeout(600)
   public SendDriverMessageResponse sendDriverMessage(@WebParam(name = "SendDriverMessageRequest")SendDriverMessageRequest request){
	   SendDriverMessageImplement sendDriverMsgImp = new SendDriverMessageImplement(
				pfDataSource, cachedParam);
		return sendDriverMsgImp.generateResponse(request);
   }
   
   @WebMethod(operationName = "NGSynchTripDetails")
   @WebResult(name = "NGSynchTripDetailsResponse")
   @TransactionTimeout(600)
   public NGSynchTripDetailsResponse nGSynchTripDetails(@WebParam(name = "NGSynchTripDetailsRequest")NGSynchTripDetailsRequest request){
	   return null;
   }
   
   @WebMethod(operationName = "NGReSynchReference")
   @WebResult(name = "NGReSynchReferenceResponse")
   @TransactionTimeout(600)
   public NGReSynchReferenceResponse nGReSynchReference(@WebParam(name = "NGReSynchReferenceRequest")NGReSynchReferenceRequest request){
	   return null;
   }
   
	private IAddressLookup getAddressLookUp(){
		if(addressLookUp == null){
			long time = System.currentTimeMillis();
			CallbookerAddressLookup cbAddressLookup = new CallbookerAddressLookup();
			addressLookUp = cbAddressLookup.getBasicHttpBindingIAddressLookup();
			if(addressLookUp != null){
				logger.info("get address look up interface ....." + (System.currentTimeMillis() - time)/1000.00+"s");
			}else{
				logger.error("get address look up interface failed .....");
			}
		}
		return addressLookUp;
	}
	
	@WebMethod(operationName = "AdvancedAddressMatch")
	@WebResult(name = "AdvancedAddressRes")
	@TransactionTimeout(600)
	public AdvancedAddressRes advancedAddressMatch(
			@WebParam(name = "AdvancedAddressReq") AdvancedAddressReq request) {
		AdvancedAddressImplement advancedAddress = new AdvancedAddressImplement(
				pfDataSource, getAddressLookUp());
		advancedAddress.setCachedParam(cachedParam);
		return advancedAddress.generateResponse(request);
	}

	@WebMethod(operationName = "CancelOrderByConfirmationNum")
	@WebResult(name = "CancelOrderByConfNumRes")
	@TransactionTimeout(600)
	public CancelOrderByConfNumRes cancelOrderByConfirmationNum(
			@WebParam(name = "CancelOrderByConfNumReq") CancelOrderByConfNumReq request) {
		CancelOrderByConfNumImplement cancelOrderByConfNumImp = new CancelOrderByConfNumImplement(
				pfDataSource);
		cancelOrderByConfNumImp.setCachedParam(cachedParam);
		return cancelOrderByConfNumImp.generateResponse(request);
	}

	@WebMethod(operationName = "UpdateOrderByConfirmationNum")
	@WebResult(name = "UpdateOrderByConfNumRes")
	@TransactionTimeout(600)
	public UpdateOrderByConfNumRes updateOrderByConfirmationNum(
			@WebParam(name = "BookJobReq") BookJobReq request) {
		UpdateOrderByConfNumImplement updateOrderByConfNumImp = new UpdateOrderByConfNumImplement(
				getAddressLookUp(), pfDataSource, orderLocal, userAccountLocal, facadeLocal, cachedParam);
		updateOrderByConfNumImp.setUserTelephone(telephoneLocal);
		return updateOrderByConfNumImp.generateResponse(request);
	}

	@WebMethod(operationName = "PhoneNumberValidation")
	@WebResult(name = "PhoneNumberValidationRsp")
	@TransactionTimeout(600)
	public PhoneNumberValidationRsp phoneNumberValidation(
			@WebParam(name = "PhoneNumberValidationReq") PhoneNumberValidationReq request) {
		PhoneNumberValidationImplement phoneNumberdValidationImp = new PhoneNumberValidationImplement(
				pfDataSource);
		phoneNumberdValidationImp.setCachedParam(cachedParam);
		return phoneNumberdValidationImp.generateResponse(request);
	}

	@WebMethod(operationName = "CarValidation")
	@WebResult(name = "CarValidationRsp")
	@TransactionTimeout(600)
	public CarValidationRsp carValidation(
			@WebParam(name = "CarValidationReq") CarValidationReq request) {
		CarValidationImplement cardValidationImp = new CarValidationImplement(
				pfDataSource);
		cardValidationImp.setCachedParam(cachedParam);
		return cardValidationImp.generateResponse(request);

	}

	@WebMethod(operationName = "PickupTimeValidation")
	@WebResult(name = "PickupTimeValidationRsp")
	@TransactionTimeout(600)
	public PickupTimeValidationRsp pickupTimeValidation(
			@WebParam(name = "PickupTimeValidationReq") PickupTimeValidationReq request) {
		PickupTimeValidationImplement pikupTimeValidationImp = new PickupTimeValidationImplement(
				pfDataSource);
		pikupTimeValidationImp.setCachedParam(cachedParam);
		return pikupTimeValidationImp.generateResponse(request);
	}
	
	@WebMethod(operationName = "TripUpdate")
	@WebResult(name = "TripUpdateRsp")
	@TransactionTimeout(600)
	public TripUpdateResponse tripUpdate(
			@WebParam(name = "TripUpdateRequest") TripUpdateRequest request) {
		return null;
	}
	
	//PF-15582
	@WebMethod(operationName = "TokenPayment")
	@WebResult(name = "TokenPaymentRes")
	@TransactionTimeout(600)
	public TokenPaymentRes tokenPayment(
			@WebParam(name = "TokenPaymentReq") TokenPaymentReq request) {
		TokenPaymentImplement tokenPaymentImp = new TokenPaymentImplement(pfDataSource, cachedParam);
		return tokenPaymentImp.generateResponse(request);

	}
	//PF-15613
	@WebMethod(operationName = "AccountValidation")
	@WebResult(name = "AccountValidationRsp")
	@TransactionTimeout(600)
	public AccountValidationRsp accountValidation(
			@WebParam(name = "AccountValidationReq") AccountValidationReq request) {
		AccountValidationImplement accountdValidationImp = new AccountValidationImplement(
				pfDataSource, cachedParam);
		return accountdValidationImp.generateResponse(request);

	}
	//PF-15595
	@WebMethod(operationName = "GetMbParameter")
	   @WebResult(name = "GetMBParamResponse")
	   @TransactionTimeout(600)
	public GetMBParamResponse getMbParameter(@WebParam(name = "GetMBParamRequest")GetMBParamRequest request){
		 MbParameterImplement paramList = new MbParameterImplement(pfDataSource, cachedParam);
		 return paramList.generateResponse(request);
	}
	//PF-15587
	@WebMethod(operationName = "AccountPayment")
	@WebResult(name = "AccountPaymentResponse")
	@TransactionTimeout(600)
	public AccountPaymentResponse accountPayment(
			@WebParam(name = "AccountPaymentRequest") AccountPaymentRequest request) {
		AccountPaymentImplement accPaymentImp = new AccountPaymentImplement(
				pfDataSource);
		accPaymentImp.setCachedParam(cachedParam);
		return accPaymentImp.generateResponse(request);

	}
	
	
	//TSS services
	@WebMethod(operationName = "TSSRequireVehicle")
	@WebResult(name = "TSSRequireVehRes")
	@TransactionTimeout(600)
	public TSSRequireVehRes tssRequireVehicle(@WebParam(name = "TSSRequireVehReq") TSSRequireVehReq request) {
		TSSRequireVehImplement requireVehImp = new TSSRequireVehImplement(getAddressLookUp(), pfDataSource, orderLocal);
		requireVehImp.setCachedParam(cachedParam);
		return requireVehImp.generateTSSResponse(request);
	}
	
	@WebMethod(operationName = "TSSCancelVehicle")
	@WebResult(name = "TSSCancelVehRes")
	@TransactionTimeout(600)
	public TSSCancelVehRes tssCancelVehicle(@WebParam(name = "TSSCancelVehReq") TSSCancelVehReq request) {
		TSSCancelVehImplement requireVehImp = new TSSCancelVehImplement(pfDataSource, orderLocal);
		requireVehImp.setCachedParam(cachedParam);
		return requireVehImp.generateTSSResponse(request);
	}
	
	@WebMethod(operationName = "AvailableCars")
	@WebResult(name = "AvailableCarsRes")
	@TransactionTimeout(600)
	public AvailableCarsRes availableCars(
			@WebParam(name = "AvailableCarsReq") AvailableCarsReq request) {
		AvailableCarsImplement availableCarsImp = new AvailableCarsImplement(
				pfDataSource);
		availableCarsImp.setCachedParam(cachedParam);
		return availableCarsImp.generateResponse(request);

	}
	
	@WebMethod(operationName = "PreDispatchETA")
	@WebResult(name = "PreDispatchETARes")
	@TransactionTimeout(60)
	public PreDispatchETARes preDispatchETA(@WebParam(name = "PreDispatchETAReq") PreDispatchETAReq request) {
		AvailableCarsImplement availableCarsImp = new AvailableCarsImplement(pfDataSource);
		availableCarsImp.setCachedParam(cachedParam);
		availableCarsImp.setPfaLocal(pfaLocal);
		return availableCarsImp.getPreDispatchETA(request);

	}
	
	@WebMethod(operationName = "RateTrip")
	@WebResult(name = "RateTripRes")
	@TransactionTimeout(600)
	public RateTripRes rateTrip(
			@WebParam(name = "RateTripReq") RateTripReq request) {
		RateTripImplement rateTripImp = new RateTripImplement(
				pfDataSource);
		rateTripImp.setCachedParam(cachedParam);
		return rateTripImp.generateResponse(request);

	}
	
}
