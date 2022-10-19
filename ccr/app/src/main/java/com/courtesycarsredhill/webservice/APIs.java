package com.courtesycarsredhill.webservice;

import static com.courtesycarsredhill.util.AppConstants.BASE_DOMAIN;

public class APIs {

    public static final String GOOGLE_DIRECTION_API = "https://maps.googleapis.com/maps/api/directions/";
    static final String BASE_URL = BASE_DOMAIN + "/api/";
    public static final String BASE_IMAGE_PATH = BASE_DOMAIN;


    public static final String API_LOGIN = "AuthenticateMobileUser";
    public static final String API_FORGOT_PASSWORD = "MobileForgotPassword";
    public static final String API_GET_TRIP_LIST = "GetDailyRouteTripList_Mobile";
    public static final String API_GET_VEHICFLE_CHECKLIST = "GetVehicleChecklist";
    public static final String API_SAVE_VEHICFLE_CHECKLIST = "SaveVehicleChecklist";
    public static final String API_SAVE_FEEDBACK= "TripFeedback";
    public static final String API_TRIP_DETAIL = "GetDailyRouteTripById_Mobile";
    public static final String API_CHANGE_TRIP_STATUS = "ChangeTripStatus_Mobile";
    public static final String API_CHANGE_STUDENT_STATUS = "UpdateStudentStatus";
    public static final String API_GET_CURRENT_APP_VERSION = "GetCurrentAppVersion";
    public static final String API_SOS_REMINDER = "SaveSOSReminder";
    public static final String API_GET_ACTIVE_CONTACT_LIST = "GetActiveContactList";
    public static final String API_GET_DOCUMENT_TYPE_LIST = "GetDocumentTypeList";
    public static final String API_GET_ALL_DOCUMENT_LIST = "GetAllDocumentList";
    public static final String API_SAVE_DOCUMENT = "SaveDocument";

}