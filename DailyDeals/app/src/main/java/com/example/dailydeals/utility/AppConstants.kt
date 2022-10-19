package com.example.dailydeals.utility

// others
const val SPLASH_TIME_OUT: Long = 2500
const val REQUEST_CODE: Int = 1111
const val WIFI_ENABLE_REQUEST = 0x1006
const val RESULT_LOAD_IMAGE = 2
const val GOOGLE_SIGN_IN_CODE: Int = 0
const val GALLERY: Int = 0x51
const val CAMERA: Int = 0x50
const val FILE_CHOOSER_RESULT_CODE: Int = 1011

//Drawable Sides
const val DRAWABLE_LEFT = 0x29
const val DRAWABLE_RIGHT = 0x2A
const val DRAWABLE_TOP = 0x2B
const val DRAWABLE_BOTTOM = 0x2C

// Shared preferences
const val PREF_IMEI = "pref_imei"
const val PREF_FCM_TOKEN = "pref_fcm_token"
const val PREF_DEVICE_TOKEN = "pref_device_token"
const val PREF_IS_USER_LOGGED_IN = "PREF_IS_USER_LOGGED_IN"
const val PREF_EMAIL = "pref_email"
const val PREF_USER_DATA = "pref_user_data"
const val PREF_ZILLOW_HOME_DETAILS = "pref_zillow_home_details"
const val PREF_ADDRESS = "pref_address"
const val PREF_PRICE = "pref_price"
const val PREF_RENT = "pref_rent"
const val PREF_IMAGE = "pref_image"
const val PREF_OWNER = "pref_owner"
const val BEDROOM = "bedroom"
const val BATHROOM = "bathroom"
const val FLOOR = "floor"
const val TYPE = "type"
const val LOT = "lot"
const val SQUARE_FEET = "square_feet"
const val YEAR_BUILT = "year_built"
const val PARKING = "parking"
const val DESCRIPTION = "description"
const val PREF_ANALYZE_DEAL_ARV = "pref_analyze_deal_arv"
const val PREF_ANALYZE_DEAL_RENT = "pref_analyze_deal_rent"
const val PREF_ANALYZE_DEAL_TAX = "pref_analyze_deal_tax"
const val PREF_ANALYZE_DEAL_CASH_TO_SELLER = "pref_analyze_deal_cash_to_seller"
const val PREF_ANALYZE_DEAL_PURCHASE_PRICE = "pref_analyze_deal_purchase_price"
const val PREF_ADDRESS_LINE_1 = "pref_address_line_1"
const val PREF_ADDRESS_LINE_2 = "pref_address_line_2"

// Extras
const val WEB_CONTENT_URL = "web_content_url"

// URL Path for video
const val ANALYZE_MY_DEAL_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
const val MORTGAGE_CALCULATOR_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
const val RENTAL_ANALYZER_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
const val WHOLESALE_ANALYZER_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
const val OWNER_FINANCE_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
const val SUBJECT_TO_ANALYZER_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
const val LEASE_OPTION_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"

//Web content type
const val TERMS_AND_CONDITION = "terms_and_condition"
const val PRIVACY_POLICY = "privacy_policy"
const val CONTACT_US = "contact_us"
const val ABOUT_US = "about_us"

// Arguments

const val ANALYZE_MY_DEAL = 0
const val MORTGAGE_CALCULATOR = 1
const val RENTAL_ANALYZER = 2
const val WHOLESALE_ANALYZER = 3
const val OWNER_FINANCE = 4
const val SUBJECT_TO_ANALYZER = 5
const val LEASE_OPTION = 6

//Home screen selector

//Api Params
const val BASE_URL: String = "https://team11.devhostserver.com/anydeal/" // local

const val STATUS = "Status"
const val MESSAGE = "Message"
const val MESSAGE_SMALL = "message"
const val RESPONSE_CODE_SUCCESS_200 = 200
const val RESPONSE_CODE_SUCCESS_201 = 201
const val RESPONSE_CODE_400 = 400
const val RESPONSE_CODE_404 = 404
const val RESPONSE_CODE_401 = 401

const val DEVICE_TYPE = "A"
const val DEVICE_TOKEN = "Device_token"

// API Names
const val API_LOGIN = "user_login"
const val API_LOGOUT = "user_logout"
const val API_WEB_PAGE_CONTENT = "web_page_content"

//analyzer type for api subject to
const val ANALYZER_TYPE_SUBJECT_TO_HOLD = "subjectToHold"
const val ANALYZER_TYPE_SUBJECT_TO_Assignment = "subjectToAssignment"
const val ANALYZER_TYPE_SUBJECT_TO_Lease = "subjectToLease"
const val ANALYZER_TYPE_SUBJECT_TO_Wrap = "subjectToWrap"

//analyzer type for api owner Finance
const val ANALYZER_TYPE_OWNER_FINANCE_HOLD = "ownerToHold"
const val ANALYZER_TYPE_OWNER_FINANCE_ASSIGNMENT = "ownerAssignment"
const val ANALYZER_TYPE_OWNER_FINANCE_LEASE = "ownerLease"
const val ANALYZER_TYPE_OWNER_FINANCE_WRAP = "ownerWrap"

const val ANALYZER_TYPE_WHOLESALE = "wholesale"
const val ANALYZER_TYPE_LEASE_OPTION = "leaseOption"


// Fragments
const val LOGIN_FRAGMENT = "Login Fragment"
const val REGISTER_FRAGMENT = "Register Fragment"
const val ANALYZE_DEAL_FRAGMENT = "Analyze Deal Fragment"
const val FORGOT_PASSWORD_FRAGMENT = "Forgot Password Fragment"
const val CHANGE_PASSWORD_FRAGMENT = "Change Password Fragment"
const val HELP_TUTORIAL_FRAGMENT = "Help Tutorial Fragment"
const val ADDRESS_LIST_FRAGMENT = "Address List Fragment"
const val MORTGAGE_FRAGMENT = "Mortgage Step 1 Fragment"
const val MORTGAGE_DETAILS_FRAGMENT = "Mortgage Step 2 Fragment"
const val AMORTGAGE_DETAILS_FRAGMENT = "AMortgage Fragment"
const val RENTAL_FRAGMENT = "Rental Step 1 Fragment"
const val RENTAL_DETAILS_FRAGMENT = "Rental Step 2 Fragment"
const val WHOLESALE_FRAGMENT = "WholeSale Step 1 Fragment"
const val WHOLESALE_DETAILS_FRAGMENT = "WholeSale Step 2 Fragment"
const val CHOOSE_SUBJECT_TO_ANALYZER_FRAGMENT = "Choose Subject To Analyzer Fragment"
const val SUBJECT_TO_ANALYZER_FRAGMENT = "Subject To Analyzer Fragment"
const val CHOOSE_OWNER_FRAGMENT = "Choose Owner Finance Fragment"
const val OWNER_FRAGMENT = "Owner Analyzer Fragment"
const val ANALYZE_DEAL_FRAGMENT_STEP_1 = "Analyze Deal Fragment Step 1"
const val ANALYZE_DEAL_FRAGMENT_STEP_2 = "Analyze Deal Fragment Step 2"
const val ANALYZE_DEAL_FREE_FRAGMENT_STEP_1 = "Analyze Deal Free Fragment Step 1"
const val ANALYZE_DEAL_FREE_FRAGMENT_STEP_2 = "Analyze Deal Free Fragment Step 2"
const val ANALYZE_DEAL_FREE_FRAGMENT_OPTION = "Analyze Deal Free Fragment Option "
const val LEASE_OPTION_FRAGMENT = "LEASE OPTION FRAGMENT"

object Constants {
    val IS_FIRST_TIME = "is_first_time"
    val APPLICATION_PREFS = "app_preference"
    val RENTAL = "Rental"
    val WHOLESALE = "Wholesale"
    val SUBJECT_TO = "SubjectTo"
    val OWNER = "Owner"
    val LEASE_OPTION = "LeaseOption"
    val ANALYZE_MY_DEAL = "AnalyzeMyDeal"
    val MORTGAGE = "Mortgage"
    val HOLD_OWNER = "Hold Owner"
    val WRAP_OWNER = "Wrap Owner"
    val LEASE_OWNER = "Lease Owner"
    val ASSIGN_OWNER = "Assign Owner"
    val SUBJECT_TO_OPTION = "subject_to_option"
    val HOLD_SUBJECT_TO = "Hold Subject To"
    val WRAP_SUBJECT_TO = "Wrap Subject To"
    val LEASE_SUBJECT_TO = "Lease Subject To"
    val ASSIGN_SUBJECT_TO = "Assign Subject To"
    val HOLD_FC_OWNER = "Hold FC Owner"
    val WRAP_FC_OWNER = "Wrap FC Owner"
    val LEASE_FC_OWNER = "Lease FC Owner"
    val ASSIGN_FC_OWNER = "Assign FC Owner"
    val HOLD_M_SUBJECT_TO = "Hold M Subject To"
    val WRAP_M_SUBJECT_TO = "Wrap M Subject To"
    val LEASE_M_SUBJECT_TO = "Lease M Subject To"
    val ASSIGN_M_SUBJECT_TO = "Assign M Subject To"
    val ARV_SUBJECT_TO = "ARV Subject To"
    val VIDEO_PATH = "video_path"
    val RENTAL_RATE_SUBJECT_TO = "Rental rate Subject To"
    val OWNER_NAME_PROPERTY_DETAIL = "owner_name_property_detail"
    val SUBJECT_TO_HOLD_OBJECT = "subject_to_hold_object"
    val SUBJECT_TO_LEASE_OBJECT = "subject_to_lease_object"
    val SUBJECT_TO_WRAP_OBJECT = "subject_to_wrap_object"
    val SUBJECT_TO_ASSIGN_OBJECT = "subject_to_assign_object"

    val OWNER_FINANCE_HOLD_OBJECT = "owner_finance_hold_object"
    val OWNER_FINANCE_LEASE_OBJECT = "owner_finance_lease_object"
    val OWNER_FINANCE_WRAP_OBJECT = "owner_finance_wrap_object"
    val OWNER_FINANCE_ASSIGN_OBJECT = "owner_finance_assign_object"
}

class AppConstants {

}