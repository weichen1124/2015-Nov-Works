
/****************************************************************************
 * 
 *					Copyright (c), $Date: 4/30/14 11:09a $
 *					All rights reserved
 *
 *					DIGITAL DISPATCH SYSTEMS, INC
 *					RICHMOND, BRITISH COLUMBIA
 *					CANADA
 *
 * **************************************************************************
 * $Log: /TaxiProduct/Code/ControlRoom/iTaxiInterface/src/com/dds/pathfinder/itaxiinterface/scheduletask/JobInfo.java $
 * Jun 12, 2015, Yutian Yin
 * PF-16549. Getting redispatch job ID for redispatch event.
 * 
 * * Y Yin May 29, 2015
 * PF-16093. Changed GetLatLon to static method.
 *
 * PF-16074 Added sysorigin for TaxiLimo(TM)
 * 
 * Added meter off, pob, reject and trip offer to mobile booker.
 * 
 * 6     4/30/14 11:09a Ezhang
 * PF_15967 Added TaxiLimo Mobile Bookers.
 * 
 * 5     1/29/14 3:58p Ezhang
 * PF-15809 use cached parameter value for API key
 * 
 * 4     1/17/14 5:08p Ezhang
 * PF-15841 Added Mobile booker API key support
 * 
 * 
 * 3     1/14/14 9:38a Dchen
 * port PF-15634 to latest version.
 * 
 * 2     11/20/13 11:25a Sfoladian
 * PF-15597- Late Trip Notification Support
 * 
 * 1     3/08/13 3:31p Dchen
 * Separate rider charge service from callbooker package.
 * 
 * 2     12/04/12 12:22p Ezhang
 * PF-14959 
 * 
 * 1     11/02/12 9:53a Ezhang
 * 
 * PF-14845  OSP status update to support GoFastCab and Mobile booking
 * 
 */
package com.dds.pathfinder.itaxiinterface.scheduletask;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



import javax.sql.DataSource;

import com.dds.pathfinder.callbooker.server.paramcache.LoadDispatchParametersLocal;
import com.dds.pathfinder.itaxiinterface.common.impl.CompanyDefaultValues;
import com.dds.pathfinder.itaxiinterface.util.Debug2;


/**
 * @author ezhang
 * Helper class for OSP status update, to retrieve info from database
 * Update the TripAlertRequest with job Info
 */
public class JobInfo {
	
			// company id
			String apiKey;

			int jobID;
			int eventID;
			String sysOrign; //From GoFastCab or MobileBooking etc.

			// lat/long of acp (accept trip)
			double acp_lat;
			double acp_long;
			
			// lat/long of arrived(cab arrived) PF-14825
			double arrived_lat;
			double arrived_long;

			// lat/long of pob (Load passenger)
			double pob_lat;
			double pob_long;

			// lat/long of meter off event
			double meteroff_lat;
			double meteroff_long;

			// lat/long of complete by no-show event
			double noshow_lat;
			double noshow_long;

			// lat/long of gps update event
			double gpsupdate_lat;
			double gpsupdate_long;
			Date gpsupdate_time;
			String gpsupdate_vehicle_status;

			String vehicle_number;
			String driver_name;
			String time_zone;
			
			private DataSource pfDataSource;

			private static Debug2 log = Debug2.getLogger(JobInfo.class);
			
			private LoadDispatchParametersLocal cachedParam;
			
			/**
			 * Update the TripAlertRequest with job Info
			 * 
			 * @param req
			 */
			public void updateJobInfo(TripAlertRequest req) {
				// Save the appropriate lat/long into the TripUpdateRequest
				switch (req.getEvent().intValue()) {
					case TripAlertRequest.ACCEPT_EVENT:
						req.setLatitude(acp_lat);
						req.setLongitude(acp_long);
						break;
					case TripAlertRequest.ARRIVED_EVENT:
						req.setLatitude(arrived_lat);
						req.setLongitude(arrived_long);
						break;
					case TripAlertRequest.METER_ON_EVENT:
						req.setLatitude(pob_lat);
						req.setLongitude(pob_long);
						break;
					case TripAlertRequest.METER_OFF_EVENT:
						req.setLatitude(meteroff_lat);
						req.setLongitude(meteroff_long);

						break;
					case TripAlertRequest.NO_SHOW_EVENT:
						req.setLatitude(noshow_lat);
						req.setLongitude(noshow_long);
						
						break;
					case TripAlertRequest.FORCED_COMPLETE_EVENT:
						
						break;
		
					case TripAlertRequest.CANCELLED_EVENT:
						
						break;
		
					case TripAlertRequest.GPS_UPDATE_EVENT:
						// we send GPS update when the trip between accept and meter
						// off)
						
						if ("A".equals(gpsupdate_vehicle_status)
								|| "P".equals(gpsupdate_vehicle_status)
								|| "Y".equals(gpsupdate_vehicle_status)) {
							req.setLatitude(gpsupdate_lat);
							req.setLongitude(gpsupdate_long);
							req.setTime(gpsupdate_time);
						} else {
							//don't send status update
							req.setEvent(-1);
						}
						
						break;
						case TripAlertRequest.LATE_TRIP_EVENT:
						case TripAlertRequest.REJECT_FOR_STREET_HIRE:
						case TripAlertRequest.REJECT_OFFER:
						case TripAlertRequest.TRIP_OFFER:
						
						break;
					case TripAlertRequest.TRIP_REDISPATCH:
						Connection dbConnection = null;
						CallableStatement cs = null;
						try {
							if ((dbConnection = pfDataSource.getConnection()) != null) {
								cs = dbConnection.prepareCall("{call booking.get_job_redespatched_to(?,?)}");
								cs.setInt(1, jobID);
								cs.registerOutParameter(2, java.sql.Types.INTEGER);
								cs.execute();
								Integer redispatchJobId = cs.getInt(2);
								req.setRedispatchTaxiRideId(redispatchJobId);
							}
						} catch (SQLException e) {
							log.error("Exception in get redispatch job ID for (jobID " + jobID + ")", e);
						}
						finally {
							try {
								if (cs != null)
									cs.close();
								} catch (SQLException ignore) {
								}
								;
							
								try {
								if (dbConnection != null)
									dbConnection.close();
								} catch (SQLException ignore) {
								}
								;
						}
						break;
					default:
						// all other events, we do not have lat/long for them
						break;
				}
			
				req.setTaxi_api_key(apiKey);
				
				req.setVehicle_number(vehicle_number);
				req.setDriver_name(driver_name);
				req.setTime_zone(time_zone);

			}

			/**
			 * Create the job info according to the jobID
			 * 
			 * @param id
			 */
			public JobInfo(int id, int eId, String sysOrig , DataSource dataSource, LoadDispatchParametersLocal cachedParam) {
				jobID = id;
				eventID = eId;
				sysOrign = sysOrig;
				pfDataSource = dataSource;
				this.cachedParam = cachedParam;
				getJobInfo();
			}

			/**
			 * Private helper function to print out the job info
			 */
			private void printJobInfo() {
				log.info("############################# ");
				log.info("api_key " + apiKey);
				log.info("JobInfo for JobID " + jobID);
				log.info("pob_lat " + pob_lat);
				log.info("pob_long " + pob_long);
				log.info("meteroff_lat " + meteroff_lat);
				log.info("meteroff_long " + meteroff_long);
				log.info("noshow_lat " + noshow_lat);
				log.info("noshow_long " + noshow_long);
				log.info("vehicle_number " + vehicle_number);
				log.info("driver_name " + driver_name);
				log.info("time_zone " + time_zone);
				 log.info("event " + eventID);
				 log.info("gpsupdate_lat " + gpsupdate_lat);
				 log.info("gpsupdate_long " + gpsupdate_long);
				 log.info("arrived_lat " + arrived_lat);
				 log.info("arrived_long " + arrived_long);
				 log.info("orign " + sysOrign);
				log.info("#############################");
			}

			/**
			 * Get all the Job Info needed to create the Trip Update Request.
			 * 
			 * @return
			 */
			public void getJobInfo() {
				
				
				StringBuffer query = new StringBuffer();
				//PF-14478 added correct driver_name to query
				//PF_14959 get gps update from vehicle_states instead of status_updates table for performance reason.
				if (eventID == TripAlertRequest.GPS_UPDATE_EVENT) {
					query.append("select jb.TAXI_CO_ID, vs.latitude, vs.longitude, vs.position_update_dtm, vs.vehicle_status, vehicles.CALLSIGN, drivers.DRIVER_NAME ");
					query.append(" from vehicle_states vs, vehicles, drivers, jobs jb where vs.current_job_id = ? ");
					query.append(" and vs.current_job_id = jb.job_id and jb.current_job_stage != 'C'");
					query.append(" and vs.vehicle_id = vehicles.vehicle_id ");
					query.append(" and vs.current_driver_id = drivers.driver_id(+) ");
				} else if (TripAlertRequest.GOFASTCAB_ORIGN.equalsIgnoreCase(sysOrign)){ /*GOFastCab requires POB, MON, MOF, NO SHOW*/

					query.append("select jobs.TAXI_CO_ID, POB_GPS_X, POB_GPS_Y, ARRIVED_GPS_LAT, ARRIVED_GPS_LON, ");   //PF-15634 add arrived gps
					query.append(" METER_OFF_GPS_X, METER_OFF_GPS_Y, NO_SHOW_GPS_LAT, NO_SHOW_GPS_LON, ");
					query.append(" CALLSIGN, DRIVER_NAME ");
					query.append(" from jobs, job_references, vehicles, drivers where jobs.JOB_ID = ? ");
					query.append(" and jobs.job_id = job_references.job_id and job_references.reference_type = '" + TripAlertRequest.GOFASTCAB_ORIGN + "'");
					query.append(" and jobs.vehicle_id = vehicles.vehicle_id (+)");
					query.append(" and jobs.driver_id = drivers.driver_id (+)");
				}
				else if (TripAlertRequest.MOBILEBOOKING_ORIGN.equalsIgnoreCase(sysOrign)){ /*MobileBooking require arrived and no show state*/
					
					query.append("select jobs.TAXI_CO_ID, ARRIVED_GPS_LAT, ARRIVED_GPS_LON, ");
					query.append(" NO_SHOW_GPS_LAT, NO_SHOW_GPS_LON, ");
					query.append(" CALLSIGN, DRIVER_NAME ");
					query.append(" from jobs, job_references, vehicles, drivers where jobs.JOB_ID = ? ");
					query.append(" and jobs.job_id = job_references.job_id and job_references.reference_type = '" + TripAlertRequest.MOBILEBOOKING_ORIGN + "'");
					query.append(" and jobs.vehicle_id = vehicles.vehicle_id (+)");
					query.append(" and jobs.driver_id = drivers.driver_id (+)");
				}//PF-16074
				else if (TripAlertRequest.TAXILIMO_MOBILE_ORIGN.equalsIgnoreCase(sysOrign)){ /*TaxiLimo require POB,MON, MOF,no show state*/
					
					query.append("select jobs.TAXI_CO_ID, POB_GPS_X, POB_GPS_Y, ARRIVED_GPS_LAT, ARRIVED_GPS_LON, ");
					query.append(" METER_OFF_GPS_X, METER_OFF_GPS_Y, NO_SHOW_GPS_LAT, NO_SHOW_GPS_LON, ");
					query.append(" CALLSIGN, DRIVER_NAME ");
					query.append(" from jobs, job_references, vehicles, drivers where jobs.JOB_ID = ? ");
					query.append(" and jobs.job_id = job_references.job_id and job_references.reference_type = '" + TripAlertRequest.TAXILIMO_MOBILE_ORIGN + "'");
					query.append(" and jobs.vehicle_id = vehicles.vehicle_id (+)");
					query.append(" and jobs.driver_id = drivers.driver_id (+)");
				}
			
				PreparedStatement stmt = null;
				ResultSet rset = null;

				Connection dbConnection = null;
				CallableStatement cs = null;

				try {
					if ((dbConnection = pfDataSource.getConnection()) == null) {
						log.error("Failed to get db connection.");
						return;
					}

					// get job accept latitude and longitude from logs table
					/**
					 * PROCEDURE get_lat_long(p_job_id IN jobs.job_id%TYPE
					 * ,p_log_type IN logs.log_type%TYPE ,p_out_latitude OUT
					 * buildings.latitude%TYPE ,p_out_longitude OUT
					 * buildings.longitude%TYPE);
					 */
					if (eventID == TripAlertRequest.ACCEPT_EVENT) {
						cs = dbConnection
								.prepareCall("{call logging.get_lat_long(?,?,?,?)}");
						cs.setInt(1, jobID);
						cs.setString(2, "DV_ACCEPT");
						cs.registerOutParameter(3, java.sql.Types.DOUBLE);
						cs.registerOutParameter(4, java.sql.Types.DOUBLE);
						cs.execute();
						acp_lat = cs.getDouble(3);
						acp_long = cs.getDouble(4);
					}
					//log.info(query.toString());
					stmt = dbConnection.prepareStatement(query.toString());
					stmt.setInt(1, jobID);
					rset = stmt.executeQuery();

					// get Gps update latitude and longitude from status_updates
					// table
					if (eventID == TripAlertRequest.GPS_UPDATE_EVENT) {
						if (rset.next()) {
							//PF-14959 change
							gpsupdate_lat = rset.getDouble("latitude");
							gpsupdate_long = rset.getDouble("longitude");
							apiKey = Integer.toString(cachedParam.getCompanyParameterIntValue(rset.getInt("TAXI_CO_ID"), CompanyDefaultValues.COMP_PARAMETER_C_GFC_API_KEY));
							
							vehicle_number = rset.getString("CALLSIGN");
							driver_name = rset.getString("DRIVER_NAME");
							time_zone = getSystemTimeZone();

							gpsupdate_vehicle_status = rset.getString("vehicle_status");
							gpsupdate_time = rset.getTimestamp("position_update_dtm");


							//printJobInfo();
						} else {
							log.debug("Failed to get GPS Update info for job "
									+ jobID);
						}
					} 
					else if (TripAlertRequest.GOFASTCAB_ORIGN.equalsIgnoreCase(sysOrign)){

						if (rset.next()) {
							noshow_lat = rset.getDouble("NO_SHOW_GPS_LAT");
							noshow_long = rset.getDouble("NO_SHOW_GPS_LON");
							
							arrived_lat = rset.getDouble("ARRIVED_GPS_LAT");
							arrived_long = rset.getDouble("ARRIVED_GPS_LON");
							
							double pob_gps_x = rset.getDouble("POB_GPS_X");
							double pob_gps_y = rset.getDouble("POB_GPS_Y");

							double meter_off_x = rset.getDouble("METER_OFF_GPS_X");
							double meter_off_y = rset.getDouble("METER_OFF_GPS_Y");

							
							apiKey = Integer.toString(cachedParam.getCompanyParameterIntValue(rset.getInt("TAXI_CO_ID"), CompanyDefaultValues.COMP_PARAMETER_C_GFC_API_KEY));
							
							vehicle_number = rset.getString("CALLSIGN");
							driver_name = rset.getString("DRIVER_NAME");
							time_zone = getSystemTimeZone();

							// Convert x_coord and y_coord to their corresponding
							// latitude and longitude
							double[] latitude = new double[1];
							double[] longitude = new double[1];
							getLatLong(dbConnection, pob_gps_x, pob_gps_y,
									latitude, longitude);
							pob_lat = latitude[0];
							pob_long = longitude[0];

							getLatLong(dbConnection, meter_off_x, meter_off_y,
									latitude, longitude);
							meteroff_lat = latitude[0];
							meteroff_long = longitude[0];

							//printJobInfo();
						} else {
							log.debug("Failed to get trip status info for GoFastCab job "
									+ jobID);
						}
					}else if (TripAlertRequest.MOBILEBOOKING_ORIGN.equalsIgnoreCase(sysOrign)){			
		
						if (rset.next()) {
							noshow_lat = rset.getDouble("NO_SHOW_GPS_LAT");
							noshow_long = rset.getDouble("NO_SHOW_GPS_LON");
							
							arrived_lat = rset.getDouble("ARRIVED_GPS_LAT");
							arrived_long = rset.getDouble("ARRIVED_GPS_LON");
							/*
							double pob_gps_x = rset.getDouble("POB_GPS_X");
							double pob_gps_y = rset.getDouble("POB_GPS_Y");

							double meter_off_x = rset.getDouble("METER_OFF_GPS_X");
							double meter_off_y = rset.getDouble("METER_OFF_GPS_Y");
							*/
							vehicle_number = rset.getString("CALLSIGN");
							driver_name = rset.getString("DRIVER_NAME");
							time_zone = getSystemTimeZone();
							
							// Convert x_coord and y_coord to their corresponding
							// latitude and longitude
							/*
							double[] latitude = new double[1];
							double[] longitude = new double[1];
							getLatLong(dbConnection, pob_gps_x, pob_gps_y,
									latitude, longitude);
							pob_lat = latitude[0];
							pob_long = longitude[0];

							getLatLong(dbConnection, meter_off_x, meter_off_y,
									latitude, longitude);
							meteroff_lat = latitude[0];
							meteroff_long = longitude[0];
							*/
							apiKey = cachedParam.getCompanyParameterValue(rset.getInt("TAXI_CO_ID"), CompanyDefaultValues.COMP_PARAMETER_C_MB_API_KEY);
							
							
							printJobInfo();
						}
					}
					else if ( TripAlertRequest.TAXILIMO_MOBILE_ORIGN.equalsIgnoreCase(sysOrign)){

						if (rset.next()) {
							noshow_lat = rset.getDouble("NO_SHOW_GPS_LAT");
							noshow_long = rset.getDouble("NO_SHOW_GPS_LON");
							
							arrived_lat = rset.getDouble("ARRIVED_GPS_LAT");
							arrived_long = rset.getDouble("ARRIVED_GPS_LON");
							
							double pob_gps_x = rset.getDouble("POB_GPS_X");
							double pob_gps_y = rset.getDouble("POB_GPS_Y");

							double meter_off_x = rset.getDouble("METER_OFF_GPS_X");
							double meter_off_y = rset.getDouble("METER_OFF_GPS_Y");
							
							vehicle_number = rset.getString("CALLSIGN");
							driver_name = rset.getString("DRIVER_NAME");
							time_zone = getSystemTimeZone();
							
							// Convert x_coord and y_coord to their corresponding
							// latitude and longitude
							double[] latitude = new double[1];
							double[] longitude = new double[1];
							getLatLong(dbConnection, pob_gps_x, pob_gps_y,
									latitude, longitude);
							pob_lat = latitude[0];
							pob_long = longitude[0];

							getLatLong(dbConnection, meter_off_x, meter_off_y,
									latitude, longitude);
							meteroff_lat = latitude[0];
							meteroff_long = longitude[0];
							
							apiKey = cachedParam.getCompanyParameterValue(rset.getInt("TAXI_CO_ID"), CompanyDefaultValues.COMP_PARAMETER_C_MB_API_KEY);
							
							
							printJobInfo();
						} else {
							log.debug("Failed to get trip status info for mobilebooking job "
									+ jobID);
						}
					}

				} catch (SQLException e) {
					log.error("Exception in getJobInfo(jobID " + jobID + ")", e);
				} finally {
					try {
						if (cs != null)
							cs.close();
					} catch (SQLException ignore) {
					}
					;
					try {
						if (rset != null)
							rset.close();
					} catch (SQLException ignore) {
					}
					;
					try {
						if (stmt != null)
							stmt.close();
					} catch (SQLException ignore) {
					}
					;
					try {
						if (dbConnection != null)
							dbConnection.close();
					} catch (SQLException ignore) {
					}
					;
				}
			}

			/**
			 * Procedure to convert from the scaled x and y coordinates and bring
			 * back a close approximation to latitude and longitude
			 * 
			 * @param dbConnection
			 * @param x_coord
			 * @param y_coord
			 * @param latitude
			 * @param longitude
			 * @throws SQLException
			 */
			static public void getLatLong(Connection dbConnection, double x_coord,
					double y_coord, double[] latitude, double[] longitude)
					throws SQLException {
				CallableStatement cs = null;
				latitude[0] = 0;
				longitude[0] = 0;
				try {
					/**
					 * PROCEDURE convert_xy_to_latlong ( p_x_coordinate IN
					 * buildings.x_coordinate%TYPE, p_y_coordinate IN
					 * buildings.y_coordinate%TYPE, p_latitude OUT
					 * buildings.latitude%TYPE, p_longitude OUT
					 * buildings.longitude%TYPE ) ;
					 */
					cs = dbConnection
							.prepareCall("{ call server_utility.convert_xy_to_latlong(?,?,?,?) }");
					cs.setDouble(1, x_coord);
					cs.setDouble(2, y_coord);
					cs.registerOutParameter(3, java.sql.Types.DOUBLE);
					cs.registerOutParameter(4, java.sql.Types.DOUBLE);
					cs.executeUpdate();
					latitude[0] = cs.getDouble(3);
					longitude[0] = cs.getDouble(4);
				} finally {
					if (cs != null)
						cs.close();
				}
			}

			private String getSystemTimeZone() {
				TimeZone timeZone = Calendar.getInstance().getTimeZone();
				String strTimeZone = timeZone.getDisplayName(); // returns the full
																// name of a time
																// zone; e.g.
																// Pacific Standard
																// Time

				// get time zone abbreviation; e.g. PST
				StringBuilder sbTimeZone = new StringBuilder();
				String[] tzParts = strTimeZone.split("\\s");
				for (String part : tzParts) {
					sbTimeZone.append(part.charAt(0));
				}

				return sbTimeZone.toString();
			}
			
			
		
}
