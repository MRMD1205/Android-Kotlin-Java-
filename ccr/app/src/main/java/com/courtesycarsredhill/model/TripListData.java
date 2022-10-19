package com.courtesycarsredhill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TripListData implements Serializable {
    @Expose
    @SerializedName("DailyRouteStudentMasterModelList")
    private String dailyroutestudentmastermodellist;
    @Expose
    @SerializedName("EscortImage")
    private String escortimage;
    @Expose
    @SerializedName("DriverImage")
    private String driverimage;
    @Expose
    @SerializedName("DestinationReachTime")
    private String destinationreachtime;
    @Expose
    @SerializedName("SourceReachTime")
    private String sourcereachtime;
    @Expose
    @SerializedName("TotalStundents")
    private int totalstundents;
    @Expose
    @SerializedName("IsClose")
    private boolean isclose;
    @Expose
    @SerializedName("TripDate")
    private String tripdate;
    @Expose
    @SerializedName("UpdatedBy")
    private int updatedby;
    @Expose
    @SerializedName("SchoolContactNo")
    private String schoolcontactno;
    @Expose
    @SerializedName("SchoolAddress")
    private String schooladdress;
    @Expose
    @SerializedName("SchoolName")
    private String schoolname;
    @Expose
    @SerializedName("TripStatus")
    private String tripstatus;
    @Expose
    @SerializedName("TripStatusId")
    private int tripstatusid;
    @Expose
    @SerializedName("Notes")
    private String notes;
    @Expose
    @SerializedName("LeaveTime")
    private String leavetime;
    @Expose
    @SerializedName("ArrivalTime")
    private String arrivaltime;
    @Expose
    @SerializedName("EscortContactNo")
    private String escortcontactno;
    @Expose
    @SerializedName("EscortCost")
    private double escortcost;
    @Expose
    @SerializedName("EscortName")
    private String escortname;
    @Expose
    @SerializedName("EscortID")
    private int escortid;
    @Expose
    @SerializedName("DriverContactNo")
    private String drivercontactno;
    @Expose
    @SerializedName("DriverCost")
    private double drivercost;
    @Expose
    @SerializedName("DriverName")
    private String drivername;
    @Expose
    @SerializedName("DriverID")
    private int driverid;
    @Expose
    @SerializedName("CouncilCost")
    private double councilcost;

    @Expose
    @SerializedName("DriverTotalEarning")
    private double DriverTotalEarning;

    @SerializedName("Driverimg")
    private String driverimg;

    @Expose
    @SerializedName("DriverMonthlyEarning")
    private double DriverMonthlyEarning;

    @Expose
    @SerializedName("DriverTodayEarning")
    private double DriverTodayEarning;

    @Expose
    @SerializedName("TripType")
    private String triptype;
    @Expose
    @SerializedName("TripTypeID")
    private int triptypeid;
    @Expose
    @SerializedName("TripName")
    private String tripname;
    @Expose
    @SerializedName("RouteName")
    private String routename;
    @Expose
    @SerializedName("RouteTripID")
    private int routetripid;
    @Expose
    @SerializedName("DailyRouteID")
    private int dailyrouteid;
    @Expose
    @SerializedName("DailyRouteTripID")
    private String dailyroutetripid;

    @SerializedName("NewPickupTime")
    private String newPickupTime;

    @Expose
    @SerializedName("SourceAddress")
    private String sourceAddress;
    @Expose
    @SerializedName("DestinationAddress")
    private String destinationAddress;

    public String getDailyroutestudentmastermodellist() {
        return dailyroutestudentmastermodellist;
    }

    public void setDailyroutestudentmastermodellist(String dailyroutestudentmastermodellist) {
        this.dailyroutestudentmastermodellist = dailyroutestudentmastermodellist;
    }

    public String getEscortimage() {
        return escortimage;
    }

    public void setEscortimage(String escortimage) {
        this.escortimage = escortimage;
    }

    public String getDriverimage() {
        return driverimage;
    }

    public void setDriverimage(String driverimage) {
        this.driverimage = driverimage;
    }

    public String getDestinationreachtime() {
        return destinationreachtime;
    }

    public void setDestinationreachtime(String destinationreachtime) {
        this.destinationreachtime = destinationreachtime;
    }

    public String getSourcereachtime() {
        return sourcereachtime;
    }

    public void setSourcereachtime(String sourcereachtime) {
        this.sourcereachtime = sourcereachtime;
    }

    public int getTotalstundents() {
        return totalstundents;
    }

    public void setTotalstundents(int totalstundents) {
        this.totalstundents = totalstundents;
    }

    public boolean getIsclose() {
        return isclose;
    }

    public void setIsclose(boolean isclose) {
        this.isclose = isclose;
    }

    public String getTripdate() {
        return tripdate;
    }

    public void setTripdate(String tripdate) {
        this.tripdate = tripdate;
    }

    public int getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(int updatedby) {
        this.updatedby = updatedby;
    }

    public String getSchoolcontactno() {
        return schoolcontactno;
    }

    public void setSchoolcontactno(String schoolcontactno) {
        this.schoolcontactno = schoolcontactno;
    }

    public String getSchooladdress() {
        return schooladdress;
    }

    public void setSchooladdress(String schooladdress) {
        this.schooladdress = schooladdress;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getTripstatus() {
        return tripstatus;
    }

    public void setTripstatus(String tripstatus) {
        this.tripstatus = tripstatus;
    }

    public int getTripstatusid() {
        return tripstatusid;
    }

    public void setTripstatusid(int tripstatusid) {
        this.tripstatusid = tripstatusid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLeavetime() {
        return leavetime;
    }

    public void setLeavetime(String leavetime) {
        this.leavetime = leavetime;
    }

    public String getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(String arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public String getEscortcontactno() {
        return escortcontactno;
    }

    public void setEscortcontactno(String escortcontactno) {
        this.escortcontactno = escortcontactno;
    }

    public String  getNewPickupTime() {
        return newPickupTime;
    }

    public void setNewPickupTime(String newPickupTime) {
        this.newPickupTime = newPickupTime;
    }

    public double getEscortcost() {
        return escortcost;
    }

    public void setEscortcost(double escortcost) {
        this.escortcost = escortcost;
    }

    public String getEscortname() {
        return escortname;
    }

    public void setEscortname(String escortname) {
        this.escortname = escortname;
    }

    public int getEscortid() {
        return escortid;
    }

    public void setEscortid(int escortid) {
        this.escortid = escortid;
    }

    public String getDrivercontactno() {
        return drivercontactno;
    }

    public void setDrivercontactno(String drivercontactno) {
        this.drivercontactno = drivercontactno;
    }

    public double getDrivercost() {
        return drivercost;
    }

    public void setDrivercost(double drivercost) {
        this.drivercost = drivercost;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public int getDriverid() {
        return driverid;
    }

    public void setDriverid(int driverid) {
        this.driverid = driverid;
    }

    public double getCouncilcost() {
        return councilcost;
    }

    public void setCouncilcost(double councilcost) {
        this.councilcost = councilcost;
    }

    public String getTriptype() {
        return triptype;
    }

    public void setTriptype(String triptype) {
        this.triptype = triptype;
    }

    public int getTriptypeid() {
        return triptypeid;
    }

    public void setTriptypeid(int triptypeid) {
        this.triptypeid = triptypeid;
    }

    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public int getRoutetripid() {
        return routetripid;
    }

    public void setRoutetripid(int routetripid) {
        this.routetripid = routetripid;
    }

    public int getDailyrouteid() {
        return dailyrouteid;
    }

    public void setDailyrouteid(int dailyrouteid) {
        this.dailyrouteid = dailyrouteid;
    }

    public String getDailyroutetripid() {
        return dailyroutetripid;
    }

    public void setDailyroutetripid(String dailyroutetripid) {
        this.dailyroutetripid = dailyroutetripid;
    }

    public double getDriverTotalEarning() {
        return DriverTotalEarning;
    }

    public void setDriverTotalEarning(double driverTotalEarning) {
        DriverTotalEarning = driverTotalEarning;
    }

    public double getDriverMonthlyEarning() {
        return DriverMonthlyEarning;
    }

    public void setDriverMonthlyEarning(double driverMonthlyEarning) {
        DriverMonthlyEarning = driverMonthlyEarning;
    }

    public double getDriverTodayEarning() {
        return DriverTodayEarning;
    }

    public void setDriverTodayEarning(double driverTodayEarning) {
        DriverTodayEarning = driverTodayEarning;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDriverimg() {
        return driverimg;
    }

    public void setDriverimg(String driverimg) {
        this.driverimg = driverimg;
    }
}
