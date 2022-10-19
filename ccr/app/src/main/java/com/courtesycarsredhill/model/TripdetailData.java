package com.courtesycarsredhill.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TripdetailData implements Serializable {

    @SerializedName("DailyRouteTripID")
    private String dailyRouteTripID;
    @SerializedName("DailyRouteID")
    private Integer dailyRouteID;
    @SerializedName("RouteTripID")
    private Integer routeTripID;
    @SerializedName("RouteName")
    private String routeName;
    @SerializedName("TripName")
    private String tripName;
    @SerializedName("TripTypeID")
    private Integer tripTypeID;
    @SerializedName("TripType")
    private String tripType;
    @SerializedName("CouncilCost")
    private double councilCost;
    @SerializedName("DriverID")
    private Integer driverID;
    @SerializedName("DriverName")
    private String driverName;
    @SerializedName("DriverCost")
    private double driverCost;
    @SerializedName("DriverTotalEarning")
    private Object driverTotalEarning;
    @SerializedName("DriverMonthlyEarning")

    private Object driverMonthlyEarning;
    @SerializedName("DriverLongitude")

    private Double driverLongitude;
    @SerializedName("DriverLatitude")

    private Double driverLatitude;
    @SerializedName("DriverContactNo")

    private String driverContactNo;
    @SerializedName("VehicleRegisterNo")

    private String vehicleRegisterNo;
    @SerializedName("VehicleLicenceNo")

    private String vehicleLicenceNo;
    @SerializedName("DriverLicenceNo")

    private String driverLicenceNo;
    @SerializedName("VehicleColor")

    private String vehicleColor;
    @SerializedName("MakeModel")

    private String makeModel;
    @SerializedName("LastestUpdatedTime")

    private Object lastestUpdatedTime;
    @SerializedName("LatestLatitude")

    private Object latestLatitude;
    @SerializedName("LatestLongitude")

    private Object latestLongitude;
    @SerializedName("EscortID")

    private Integer escortID;
    @SerializedName("EscortName")

    private String escortName;
    @SerializedName("EscortCost")

    private double escortCost;
    @SerializedName("EscortTotalEarning")

    private Object escortTotalEarning;
    @SerializedName("EscortMonthlyEarning")

    private Object escortMonthlyEarning;
    @SerializedName("EscortContactNo")

    private String escortContactNo;
    @SerializedName("ArrivalTime")

    private String arrivalTime;
    @SerializedName("LeaveTime")

    private String leaveTime;
    @SerializedName("Notes")

    private Object notes;
    @SerializedName("TripStatusId")
    private Integer tripStatusId;
    @SerializedName("TripStatus")
    private String tripStatus;
    @SerializedName("Latitude")

    private Double latitude;
    @SerializedName("Longitude")

    private Double longitude;
    @SerializedName("StartTriplat")

    private Double startTriplat;
    @SerializedName("StartTriplang")

    private Double startTriplang;
    @SerializedName("StartTripTime")

    private String startTripTime;
    @SerializedName("EndTriplat")

    private Double endTriplat;
    @SerializedName("EndTriplang")

    private Double endTriplang;
    @SerializedName("EndTripTime")

    private String endTripTime;
    @SerializedName("SchoolName")

    private String schoolName;
    @SerializedName("SchoolAddress")

    private String schoolAddress;
    @SerializedName("SchoolContactNo")

    private String schoolContactNo;
    @SerializedName("UpdatedBy")

    private Integer updatedBy;
    @SerializedName("TripDate")

    private String tripDate;
    @SerializedName("IsClose")

    private Boolean isClose;
    @SerializedName("TotalStundents")

    private Integer totalStundents;
    @SerializedName("SourceReachTime")

    private Object sourceReachTime;
    @SerializedName("DestinationReachTime")

    private Object destinationReachTime;
    @SerializedName("SourceAddress")

    private String sourceAddress;
    @SerializedName("SourceLat")

    private double sourceLat;
    @SerializedName("SourceLang")

    private double sourceLang;
    @SerializedName("DestinationAddress")

    private String destinationAddress;
    @SerializedName("DestinationLat")

    private double destinationLat;
    @SerializedName("DestinationLang")

    private double destinationLang;
    @SerializedName("DriverImage")
    private String driverImage;
    @SerializedName("Driverimg")
    private String driverimg;
    @SerializedName("EscortImage")

    private String escortImage;
    @SerializedName("Escortimg")

    private String escortimg;
    @SerializedName("UserType")

    private Object userType;
    @SerializedName("UserId")

    private Integer userId;
    @SerializedName("NewPickupTime")
    private String newPickupTime;
    @SerializedName("DailyRouteStudentMasterModelList")
    private ArrayList<Dailyroutestudentmastermodellist> dailyRouteStudentMasterModelList;

    public String getDailyRouteTripID() {
        return dailyRouteTripID;
    }

    public void setDailyRouteTripID(String dailyRouteTripID) {
        this.dailyRouteTripID = dailyRouteTripID;
    }

    public Integer getDailyRouteID() {
        return dailyRouteID;
    }

    public void setDailyRouteID(Integer dailyRouteID) {
        this.dailyRouteID = dailyRouteID;
    }

    public Integer getRouteTripID() {
        return routeTripID;
    }

    public void setRouteTripID(Integer routeTripID) {
        this.routeTripID = routeTripID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Integer getTripTypeID() {
        return tripTypeID;
    }

    public void setTripTypeID(Integer tripTypeID) {
        this.tripTypeID = tripTypeID;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public double getCouncilCost() {
        return councilCost;
    }

    public void setCouncilCost(double councilCost) {
        this.councilCost = councilCost;
    }

    public Integer getDriverID() {
        return driverID;
    }

    public void setDriverID(Integer driverID) {
        this.driverID = driverID;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public double getDriverCost() {
        return driverCost;
    }

    public void setDriverCost(double driverCost) {
        this.driverCost = driverCost;
    }

    public Object getDriverTotalEarning() {
        return driverTotalEarning;
    }

    public void setDriverTotalEarning(Object driverTotalEarning) {
        this.driverTotalEarning = driverTotalEarning;
    }

    public Object getDriverMonthlyEarning() {
        return driverMonthlyEarning;
    }

    public void setDriverMonthlyEarning(Object driverMonthlyEarning) {
        this.driverMonthlyEarning = driverMonthlyEarning;
    }

    public Double getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(Double driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    public Double getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(Double driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public String getDriverContactNo() {
        return driverContactNo;
    }

    public void setDriverContactNo(String driverContactNo) {
        this.driverContactNo = driverContactNo;
    }

    public String getVehicleRegisterNo() {
        return vehicleRegisterNo;
    }

    public void setVehicleRegisterNo(String vehicleRegisterNo) {
        this.vehicleRegisterNo = vehicleRegisterNo;
    }

    public String getVehicleLicenceNo() {
        return vehicleLicenceNo;
    }

    public void setVehicleLicenceNo(String vehicleLicenceNo) {
        this.vehicleLicenceNo = vehicleLicenceNo;
    }

    public String getDriverLicenceNo() {
        return driverLicenceNo;
    }

    public void setDriverLicenceNo(String driverLicenceNo) {
        this.driverLicenceNo = driverLicenceNo;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getMakeModel() {
        return makeModel;
    }

    public void setMakeModel(String makeModel) {
        this.makeModel = makeModel;
    }

    public Object getLastestUpdatedTime() {
        return lastestUpdatedTime;
    }

    public void setLastestUpdatedTime(Object lastestUpdatedTime) {
        this.lastestUpdatedTime = lastestUpdatedTime;
    }

    public Object getLatestLatitude() {
        return latestLatitude;
    }

    public void setLatestLatitude(Object latestLatitude) {
        this.latestLatitude = latestLatitude;
    }

    public Object getLatestLongitude() {
        return latestLongitude;
    }

    public void setLatestLongitude(Object latestLongitude) {
        this.latestLongitude = latestLongitude;
    }

    public Integer getEscortID() {
        return escortID;
    }

    public void setEscortID(Integer escortID) {
        this.escortID = escortID;
    }

    public String getEscortName() {
        return escortName;
    }

    public void setEscortName(String escortName) {
        this.escortName = escortName;
    }

    public double getEscortCost() {
        return escortCost;
    }

    public void setEscortCost(double escortCost) {
        this.escortCost = escortCost;
    }

    public Object getEscortTotalEarning() {
        return escortTotalEarning;
    }

    public void setEscortTotalEarning(Object escortTotalEarning) {
        this.escortTotalEarning = escortTotalEarning;
    }

    public Object getEscortMonthlyEarning() {
        return escortMonthlyEarning;
    }

    public void setEscortMonthlyEarning(Object escortMonthlyEarning) {
        this.escortMonthlyEarning = escortMonthlyEarning;
    }

    public String getEscortContactNo() {
        return escortContactNo;
    }

    public void setEscortContactNo(String escortContactNo) {
        this.escortContactNo = escortContactNo;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Object getNotes() {
        return notes;
    }

    public void setNotes(Object notes) {
        this.notes = notes;
    }

    public Integer getTripStatusId() {
        return tripStatusId;
    }

    public void setTripStatusId(Integer tripStatusId) {
        this.tripStatusId = tripStatusId;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getStartTriplat() {
        return startTriplat;
    }

    public void setStartTriplat(Double startTriplat) {
        this.startTriplat = startTriplat;
    }

    public Double getStartTriplang() {
        return startTriplang;
    }

    public void setStartTriplang(Double startTriplang) {
        this.startTriplang = startTriplang;
    }

    public String getStartTripTime() {
        return startTripTime;
    }

    public void setStartTripTime(String startTripTime) {
        this.startTripTime = startTripTime;
    }

    public Double getEndTriplat() {
        return endTriplat;
    }

    public void setEndTriplat(Double endTriplat) {
        this.endTriplat = endTriplat;
    }

    public Double getEndTriplang() {
        return endTriplang;
    }

    public void setEndTriplang(Double endTriplang) {
        this.endTriplang = endTriplang;
    }

    public String getEndTripTime() {
        return endTripTime;
    }

    public void setEndTripTime(String endTripTime) {
        this.endTripTime = endTripTime;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getSchoolContactNo() {
        return schoolContactNo;
    }

    public void setSchoolContactNo(String schoolContactNo) {
        this.schoolContactNo = schoolContactNo;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public Boolean getIsClose() {
        return isClose;
    }

    public void setIsClose(Boolean isClose) {
        this.isClose = isClose;
    }

    public Integer getTotalStundents() {
        return totalStundents;
    }

    public void setTotalStundents(Integer totalStundents) {
        this.totalStundents = totalStundents;
    }

    public Object getSourceReachTime() {
        return sourceReachTime;
    }

    public void setSourceReachTime(Object sourceReachTime) {
        this.sourceReachTime = sourceReachTime;
    }

    public Object getDestinationReachTime() {
        return destinationReachTime;
    }

    public void setDestinationReachTime(Object destinationReachTime) {
        this.destinationReachTime = destinationReachTime;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public double getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public double getSourceLang() {
        return sourceLang;
    }

    public void setSourceLang(double sourceLang) {
        this.sourceLang = sourceLang;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLang() {
        return destinationLang;
    }

    public void setDestinationLang(double destinationLang) {
        this.destinationLang = destinationLang;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverimg() {
        return driverimg;
    }

    public void setDriverimg(String driverimg) {
        this.driverimg = driverimg;
    }

    public String getEscortImage() {
        return escortImage;
    }

    public void setEscortImage(String escortImage) {
        this.escortImage = escortImage;
    }

    public String getEscortimg() {
        return escortimg;
    }

    public void setEscortimg(String escortimg) {
        this.escortimg = escortimg;
    }

    public Object getUserType() {
        return userType;
    }

    public void setUserType(Object userType) {
        this.userType = userType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNewPickupTime() {
        return newPickupTime;
    }

    public void setNewPickupTime(String newPickupTime) {
        this.newPickupTime = newPickupTime;
    }

    public ArrayList<Dailyroutestudentmastermodellist> getDailyRouteStudentMasterModelList() {
        return dailyRouteStudentMasterModelList;
    }

    public void setDailyRouteStudentMasterModelList(ArrayList<Dailyroutestudentmastermodellist> dailyRouteStudentMasterModelList) {
        this.dailyRouteStudentMasterModelList = dailyRouteStudentMasterModelList;
    }

    public class Dailyroutestudentmastermodellist implements Serializable {
        @SerializedName("DailyRouteStudentID")
        @Expose
        private Integer dailyRouteStudentID;
        @SerializedName("DailyRouteTripID")
        @Expose
        private Integer dailyRouteTripID;
        @SerializedName("StudentID")
        @Expose
        private Integer studentID;
        @SerializedName("StudentName")
        @Expose
        private String studentName;
        @SerializedName("StudentFullName")
        @Expose
        private String studentFullName;
        @SerializedName("StudentEquipment")
        @Expose
        private String studentEquipment;
        @SerializedName("ArrivalTime")
        @Expose
        private Object arrivalTime;
        @SerializedName("strArrivalTime")
        @Expose
        private Object strArrivalTime;
        @SerializedName("ParentsAddress1")
        @Expose
        private String parentsAddress1;
        @SerializedName("Latitude")
        @Expose
        private Double latitude;
        @SerializedName("Longitude")
        @Expose
        private Double longitude;
        @SerializedName("StudentLongitude")
        @Expose
        private Double studentLongitude;
        @SerializedName("StudentLatitude")
        @Expose
        private Double studentLatitude;
        @SerializedName("SchoolLongitude")
        @Expose
        private Double schoolLongitude;
        @SerializedName("SchoolLatitude")
        @Expose
        private Double schoolLatitude;
        @SerializedName("ProfilePhoto")
        @Expose
        private String profilePhoto;
        @SerializedName("Photo")
        @Expose
        private String photo;
        @SerializedName("PickupOrderNo")
        @Expose
        private String pickupOrderNo;
        @SerializedName("PickupAddress")
        @Expose
        private String pickupAddress;
        @SerializedName("PickUpLang")
        @Expose
        private Object pickUpLang;
        @SerializedName("PickUpLat")
        @Expose
        private Object pickUpLat;
        @SerializedName("PickUpUserType")
        @Expose
        private Object pickUpUserType;
        @SerializedName("PickUpBy")
        @Expose
        private Object pickUpBy;
        @SerializedName("PickUpTime")
        @Expose
        private Object pickUpTime;
        @SerializedName("strPickUpTime")
        @Expose
        private Object strPickUpTime;
        @SerializedName("IsPickup")
        @Expose
        private Boolean isPickup;
        @SerializedName("IsParentDropPickup")
        @Expose
        private Boolean isParentDropPickup;
        @SerializedName("DropLang")
        @Expose
        private Object dropLang;
        @SerializedName("DropLat")
        @Expose
        private Object dropLat;
        @SerializedName("DropBy")
        @Expose
        private Object dropBy;
        @SerializedName("DropUserType")
        @Expose
        private Object dropUserType;
        @SerializedName("DropTime")
        @Expose
        private Object dropTime;
        @SerializedName("strDropTime")
        @Expose
        private Object strDropTime;
        @SerializedName("IsDrop")
        @Expose
        private Boolean isDrop;
        @SerializedName("IsAbsent")
        @Expose
        private Boolean isAbsent;
        @SerializedName("AbsentReason")
        @Expose
        private String absentReason;
        @SerializedName("ParentName1")
        @Expose
        private String parentName1;
        @SerializedName("ContactParent1")
        @Expose
        private String contactParent1;
        @SerializedName("RelationTypeParent1")
        @Expose
        private String relationTypeParent1;
        @SerializedName("Notes")
        @Expose
        private String notes;
        @SerializedName("SchoolName")
        @Expose
        private String schoolName;
        @SerializedName("SchoolAddress")
        @Expose
        private String schoolAddress;
        @SerializedName("SchoolContactNo")
        @Expose
        private String schoolContactNo;
        @SerializedName("SchoolStartTime")
        @Expose
        private String schoolStartTime;
        @SerializedName("SchoolEndTime")
        @Expose
        private String schoolEndTime;
        @SerializedName("EmergencyContact")
        @Expose
        private String emergencyContact;
        @SerializedName("MobileParent1")
        @Expose
        private String mobileParent1;
        @SerializedName("ContactParent2")
        @Expose
        private String contactParent2;
        @SerializedName("MobileParent2")
        @Expose
        private Object mobileParent2;
        @SerializedName("UpdatedBy")
        @Expose
        private Integer updatedBy;
        @SerializedName("NewPickupTime")
        @Expose
        private String newPickupTime;
        @SerializedName("NewDropTime")
        @Expose
        private String newDropTime;
        @SerializedName("IsNext")
        @Expose
        private Boolean isNext;
        private String studentname;

        @SerializedName("TripType")
        private String triptype;

        @SerializedName("TripStatus")
        private String tripStatus;

        @SerializedName("TripTypeID")
        private int triptypeid;




        public Integer getDailyroutestudentid() {
            return dailyRouteStudentID;
        }

        public void setDailyRouteStudentID(Integer dailyRouteStudentID) {
            this.dailyRouteStudentID = dailyRouteStudentID;
        }

        public Integer getDailyRouteTripID() {
            return dailyRouteTripID;
        }

        public void setDailyRouteTripID(Integer dailyRouteTripID) {
            this.dailyRouteTripID = dailyRouteTripID;
        }

        public Integer getStudentid() {
            return studentID;
        }

        public void setStudentID(Integer studentID) {
            this.studentID = studentID;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getTripstatus() {
            return tripStatus;
        }

        public void setTripstatus(String tripStatus) {
            this.tripStatus = tripStatus;
        }

        public String getStudentFullName() {
            return studentFullName;
        }

        public void setStudentFullName(String studentFullName) {
            this.studentFullName = studentFullName;
        }

        public String getStudentEquipment() {
            return studentEquipment;
        }

        public void setStudentEquipment(String studentEquipment) {
            this.studentEquipment = studentEquipment;
        }

        public Object getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(Object arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public Object getStrArrivalTime() {
            return strArrivalTime;
        }

        public void setStrArrivalTime(Object strArrivalTime) {
            this.strArrivalTime = strArrivalTime;
        }

        public String getParentsaddress1() {
            return parentsAddress1;
        }

        public void setParentsAddress1(String parentsAddress1) {
            this.parentsAddress1 = parentsAddress1;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getStudentLongitude() {
            return studentLongitude;
        }

        public void setStudentLongitude(Double studentLongitude) {
            this.studentLongitude = studentLongitude;
        }

        public Double getStudentLatitude() {
            return studentLatitude;
        }

        public void setStudentLatitude(Double studentLatitude) {
            this.studentLatitude = studentLatitude;
        }

        public Double getSchoolLongitude() {
            return schoolLongitude;
        }

        public void setSchoolLongitude(Double schoolLongitude) {
            this.schoolLongitude = schoolLongitude;
        }

        public Double getSchoolLatitude() {
            return schoolLatitude;
        }

        public void setSchoolLatitude(Double schoolLatitude) {
            this.schoolLatitude = schoolLatitude;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getPickupOrderNo() {
            return pickupOrderNo;
        }

        public void setPickupOrderNo(String pickupOrderNo) {
            this.pickupOrderNo = pickupOrderNo;
        }

        public String getPickupAddress() {
            return pickupAddress;
        }

        public void setPickupAddress(String pickupAddress) {
            this.pickupAddress = pickupAddress;
        }

        public Object getPickUpLang() {
            return pickUpLang;
        }

        public void setPickUpLang(Object pickUpLang) {
            this.pickUpLang = pickUpLang;
        }

        public Object getPickUpLat() {
            return pickUpLat;
        }

        public void setPickUpLat(Object pickUpLat) {
            this.pickUpLat = pickUpLat;
        }

        public Object getPickUpUserType() {
            return pickUpUserType;
        }

        public void setPickUpUserType(Object pickUpUserType) {
            this.pickUpUserType = pickUpUserType;
        }

        public Object getPickUpBy() {
            return pickUpBy;
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

        public void setPickUpBy(Object pickUpBy) {
            this.pickUpBy = pickUpBy;
        }

        public Object getPickUpTime() {
            return pickUpTime;
        }

        public void setPickUpTime(Object pickUpTime) {
            this.pickUpTime = pickUpTime;
        }

        public Object getStrPickUpTime() {
            return strPickUpTime;
        }

        public void setStrPickUpTime(Object strPickUpTime) {
            this.strPickUpTime = strPickUpTime;
        }

        public Boolean getIsParentDropPickup() {
            return isParentDropPickup;
        }

        public void setIsParentDropPickup(Boolean isParentDropPickup) {
            this.isParentDropPickup = isParentDropPickup;
        }

        public Boolean getIspickup() {
            return isPickup;
        }

        public void setIsPickup(Boolean isPickup) {
            this.isPickup = isPickup;
        }

        public Object getDropLang() {
            return dropLang;
        }

        public void setDropLang(Object dropLang) {
            this.dropLang = dropLang;
        }

        public Object getDropLat() {
            return dropLat;
        }

        public void setDropLat(Object dropLat) {
            this.dropLat = dropLat;
        }

        public Object getDropBy() {
            return dropBy;
        }

        public void setDropBy(Object dropBy) {
            this.dropBy = dropBy;
        }

        public Object getDropUserType() {
            return dropUserType;
        }

        public void setDropUserType(Object dropUserType) {
            this.dropUserType = dropUserType;
        }

        public Object getDropTime() {
            return dropTime;
        }

        public void setDropTime(Object dropTime) {
            this.dropTime = dropTime;
        }

        public Object getStrDropTime() {
            return strDropTime;
        }

        public void setStrDropTime(Object strDropTime) {
            this.strDropTime = strDropTime;
        }

        public Boolean getIsdrop() {
            return isDrop;
        }

        public void setIsDrop(Boolean isDrop) {
            this.isDrop = isDrop;
        }

        public Boolean getIsabsent() {
            return isAbsent;
        }

        public void setIsAbsent(Boolean isAbsent) {
            this.isAbsent = isAbsent;
        }

        public String getAbsentReason() {
            return absentReason;
        }

        public void setAbsentReason(String absentReason) {
            this.absentReason = absentReason;
        }

        public String getParentName1() {
            return parentName1;
        }

        public void setParentName1(String parentName1) {
            this.parentName1 = parentName1;
        }

        public String getContactparent1() {
            return contactParent1;
        }

        public void setContactParent1(String contactParent1) {
            this.contactParent1 = contactParent1;
        }

        public String getRelationTypeParent1() {
            return relationTypeParent1;
        }

        public void setRelationTypeParent1(String relationTypeParent1) {
            this.relationTypeParent1 = relationTypeParent1;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getSchoolname() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getSchoolAddress() {
            return schoolAddress;
        }

        public void setSchoolAddress(String schoolAddress) {
            this.schoolAddress = schoolAddress;
        }

        public String getSchoolcontactno() {
            return schoolContactNo;
        }

        public void setSchoolContactNo(String schoolContactNo) {
            this.schoolContactNo = schoolContactNo;
        }

        public String getSchoolStartTime() {
            return schoolStartTime;
        }

        public void setSchoolStartTime(String schoolStartTime) {
            this.schoolStartTime = schoolStartTime;
        }

        public String getSchoolEndTime() {
            return schoolEndTime;
        }

        public void setSchoolEndTime(String schoolEndTime) {
            this.schoolEndTime = schoolEndTime;
        }

        public String getEmergencycontact() {
            return emergencyContact;
        }

        public void setEmergencyContact(String emergencyContact) {
            this.emergencyContact = emergencyContact;
        }

        public String getMobileParent1() {
            return mobileParent1;
        }

        public void setMobileParent1(String mobileParent1) {
            this.mobileParent1 = mobileParent1;
        }

        public String getContactparent2() {
            return contactParent2;
        }

        public void setContactParent2(String contactParent2) {
            this.contactParent2 = contactParent2;
        }

        public Object getMobileParent2() {
            return mobileParent2;
        }

        public void setMobileParent2(Object mobileParent2) {
            this.mobileParent2 = mobileParent2;
        }

        public Integer getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(Integer updatedBy) {
            this.updatedBy = updatedBy;
        }

        public String getNewPickupTime() {
            return newPickupTime;
        }

        public void setNewPickupTime(String newPickupTime) {
            this.newPickupTime = newPickupTime;
        }

        public String getNewDropTime() {
            return newDropTime;
        }

        public void setNewDropTime(String newDropTime) {
            this.newDropTime = newDropTime;
        }

        public Boolean getIsNext() {
            return isNext;
        }

        public void setIsNext(Boolean isNext) {
            this.isNext = isNext;
        }

    }
}