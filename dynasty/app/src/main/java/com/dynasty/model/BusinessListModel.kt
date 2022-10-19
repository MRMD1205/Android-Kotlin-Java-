package com.dynasty.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class BusinessListModel(
    @SerializedName("Data") @Expose var data: List<Data>? = null,
    @SerializedName("Success") @Expose var success: Boolean? = null,
    @SerializedName("Message") @Expose var message: String? = null
) : Serializable {
    class Data(@SerializedName("BusinessId") @Expose var businessId: String? = null,
               @SerializedName("CategoryId") @Expose var categoryId: Int? = null,
               @SerializedName("BusinessName") @Expose var businessName: String? = null,
               @SerializedName("BusinessLogo") @Expose var businessLogo: String? = null,
               @SerializedName("BackgroundImage") @Expose var backgroundImage: String? = null,
               @SerializedName("Email") @Expose var email: Any? = null,
               @SerializedName("About") @Expose var about: String? = null,
               @SerializedName("TotalRecords") @Expose var totalRecords: String? = null,
               @SerializedName("City") @Expose var city: String? = null,
               @SerializedName("Service") @Expose var service: List<Service>? = null,
               @SerializedName("Latitude") @Expose var latitude: String? = null,
               @SerializedName("Longitude") @Expose var longitude: String? = null,
               @SerializedName("Tags") @Expose var tags: List<Tag>? = null,
               @SerializedName("Address") @Expose var address: String? = null,
               @SerializedName("PostCode") @Expose var postCode: Any? = null,
               @SerializedName("LinkedInLink") @Expose var linkedInLink: Any? = null,
               @SerializedName("TwitterLink") @Expose var twitterLink: Any? = null,
               @SerializedName("FacebookLink") @Expose var facebookLink: Any? = null,
               @SerializedName("InstagramLink") @Expose var instagramLink: Any? = null,
               @SerializedName("CategoryName") @Expose var categoryName: String? = null,
               @SerializedName("CategoryLogo") @Expose var categoryLogo: String? = null,
               @SerializedName("IsActive") @Expose var isActive: Boolean? = null,
               @SerializedName("CreatedBy") @Expose var createdBy: Int? = null,
               @SerializedName("ModifiedBy") @Expose var modifiedBy: Any? = null,
               @SerializedName("CreatedDate") @Expose var createdDate: Any? = null,
               @SerializedName("ModifiedDate") @Expose var modifiedDate: Any? = null,
               @SerializedName("ContactNumber") @Expose var contactNumber: List<ContactNumber>? = null,
               @SerializedName("Image") @Expose var image: List<Image>? = null
    ) : Serializable {
        data class Service(
            @SerializedName("BusinessServiceId") @Expose var businessServiceId: Int? = null,
            @SerializedName("BusinessId") @Expose var businessId: Int? = null,
            @SerializedName("Description") @Expose var description: String? = null,
            @SerializedName("Service") @Expose var service: String? = null
        ) : Serializable {}

        data class Tag(
            @SerializedName("BusinessTagId") @Expose var businessTagId: Int? = null,
            @SerializedName("BusinessId") @Expose var businessId: Int? = null,
            @SerializedName("TagName") @Expose var tagName: String? = null
        ) : Serializable {}

        data class Image(
            @SerializedName("BusinessImageId") @Expose var businessImageId: Int? = null,
            @SerializedName("ImageURL") @Expose var imageURL: String? = null
        ) : Serializable {}

        data class ContactNumber(
            @SerializedName("BusinessContactNoId") @Expose var businessContactNoId: Int? = null,
            @SerializedName("BusinessId") @Expose var businessId: Int? = null,
            @SerializedName("ContactNo") @Expose var contactNo: String? = null
        ) : Serializable {}
    }

}
