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
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/webservice/PFiTaxiServiceInterface.java $
 * 
 * PF-16498, 06/01/15, DChen, pre after dispatch eta. 
 * 
 * PF-16398, 04/06/15, DChen, add isAddressServiceable service
 * 
 * 08/26/14, DChen, added TSS shared rider service.
 * 
 * 24    1/16/14 3:43p Dchen
 * PF-15847, UDI callout extensions.
 * 
 * 23    10/28/13 12:54p Sfoladian
 * PF-15595
 * PF-15587
 * 
 * 22    10/23/13 3:05p Ezhang
 * PF-15613 added account validation.
 * PF-15582 added token payment
 * 
 * 21    11/27/12 1:06p Dchen
 * PF-14930, Veolia osp changes.
 * 
 * 20    9/27/10 11:15a Ajiang
 * Added tripUpdate()
 * 
 * 19    9/20/10 12:02p Ezhang
 * Added pickupTimeValidation(), carValidation(), phoneNumberValidation(), updateOrderByConfirmationNum(), 
 * cancelOrderByConfirmationNum() and advancedAddressMatch().
 * 
 * 18    9/20/10 11:49a Ezhang
 * Added new APIs for OSP 2.0
 * 
 * 17    9/14/10 2:42p Ajiang
 * C34078 - Texas Taxi Enhancements
 * 
 * 16    2/09/10 11:20a Jwong
 * 
 * 15    1/18/10 1:33p Yyin
 * Added getPredefinedMessage().
 * 
 * 14    1/12/10 2:27p Yyin
 * Added methods driverMessageType() and predefinedMessageList().
 * 
 * 13    12/03/09 10:42a Jwong
 * 
 * 12    11/30/09 9:45a Yyin
 * Added logoff() method.
 * 
 * 11    11/26/09 11:59a Jwong
 * 
 * 10    11/25/09 2:57p Yyin
 * Added login() method.
 * 
 * 9     11/17/09 5:32p Yyin
 * Added getAdviseArrivals().
 * 
 * 8     11/16/09 11:13a Jwong
 * 
 * 7     10/28/09 2:22p Yyin
 * C32549. Rename GetJobs and GetJobDetail to RecallJob and
 * RecallJobDetail.
 * 
 * 6     10/21/09 2:30p Yyin
 * Added GetAttributeList method
 * 
 * 5     10/19/09 4:59p Yyin
 * Added GetAccountList method
 * 
 * 4     10/15/09 10:31a Jwong
 * 
 * 3     10/01/09 1:46p Yyin
 * 
 * 2     8/13/09 5:47p Dchen
 * pathfinder iTaxi interface.
 * 
 * 1     8/13/09 12:04p Dchen
 * pathfinder iTaxi interface.
 * 
 */

package com.dds.pathfinder.itaxiinterface.webservice;

import javax.jws.WebParam;

import com.dds.pathfinder.itaxiinterface.tss.TSSCancelVehReq;
import com.dds.pathfinder.itaxiinterface.tss.TSSCancelVehRes;
import com.dds.pathfinder.itaxiinterface.tss.TSSRequireVehReq;
import com.dds.pathfinder.itaxiinterface.tss.TSSRequireVehRes;




/**
 * An EJB3 remote interface
 *
 */
public interface PFiTaxiServiceInterface{

   public BookJobRes bookJob(BookJobReq request);
   public CancelJobRes cancelJob(CancelJobReq request);
   public AddressRes validateAddress(AddressReq request);
   public DispatchRegionRes getDispatchRegions(DispatchRegionReq request);
   public RecallJobDetailResponse recallJobDetail(RecallJobDetailRequest request);
   public RecallJobsResponse recallJobs(RecallJobsRequest request);
   public GetAccountDetailResponse getAccountDetail(GetAccountDetailRequest request);
   public GetAcctListResponse getAccountList(GetAcctListRequest request);
   public GetAttrListResponse getAttributeList(GetAttrListRequest request);
   public AdviseArrivalResponse getAdviseArrivals(AdviseArrivalRequest request);
   public LoginResponse login(LoginRequest request);
   public ClientProfileResponse getClientProfile(ClientProfileRequest request);
   public LogoffResponse logoff(LogoffRequest request);
   public TripInfoResponse getTripInfo(TripInfoRequest reqest);
   public DriverMessageTypeRsp driverMessageType(DriverMessageTypeReq request);
   public PredefinedMessageListRsp predefinedMessageList(PredefinedMessageListReq request);
   public GetPredefinedMsgRsp getPredefinedMessage(GetPredefinedMsgReq request);
   public SendDriverMessageResponse sendDriverMessage(SendDriverMessageRequest request);
   public NGSynchTripDetailsResponse nGSynchTripDetails(NGSynchTripDetailsRequest request);
   public NGReSynchReferenceResponse nGReSynchReference(NGReSynchReferenceRequest request);
   public AdvancedAddressRes advancedAddressMatch(AdvancedAddressReq request);
   public CancelOrderByConfNumRes cancelOrderByConfirmationNum(CancelOrderByConfNumReq request);
   public UpdateOrderByConfNumRes updateOrderByConfirmationNum(BookJobReq request);
   public PhoneNumberValidationRsp phoneNumberValidation(PhoneNumberValidationReq request);
   public CarValidationRsp carValidation(CarValidationReq request);
   public PickupTimeValidationRsp pickupTimeValidation(PickupTimeValidationReq request);
   public TripUpdateResponse tripUpdate(TripUpdateRequest request);
   public GetVehicleInventoryResponse getVehInventory(GetVehicleInventoryRequest request);
   public GetVehicleStatusResponse getVehicleStatus(GetVehicleStatusRequest request);
   public TokenPaymentRes tokenPayment(TokenPaymentReq request);
   public AccountValidationRsp accountValidation(AccountValidationReq request);
   public GetMBParamResponse getMbParameter(GetMBParamRequest request);
   public AccountPaymentResponse accountPayment(AccountPaymentRequest request);
   public GetUDICalloutResponse getUDICallout(GetUDICalloutRequest request);
   public UpdateUDICalloutRes updateUDICallout(UpdateUDICalloutReq request);
   public AvailableCarsRes availableCars(AvailableCarsReq request);
   //TSS services
   public TSSRequireVehRes tssRequireVehicle(TSSRequireVehReq request);
   public TSSCancelVehRes tssCancelVehicle(TSSCancelVehReq request);
   
   public GPSServiceableResponse isAddressServiceable(GPSServiceableRequest request);
   //PF-16547 eCabs services
   public RateTripRes rateTrip(RateTripReq request);
   
   public PreDispatchETARes preDispatchETA(PreDispatchETAReq request);
   
   
}