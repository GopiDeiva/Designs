package hatchure.designs.Interfaces;

import java.util.HashMap;

import hatchure.designs.Models.AdTypeList;
import hatchure.designs.Models.OfferTitleList;
import hatchure.designs.Models.ShopCategoryList;
import hatchure.designs.Models.Status;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("Retrive/getOffersTitle")
    Call<OfferTitleList> GetOfferTypes();

    @GET("Retrive/getShopsCategories")
    Call<ShopCategoryList> GetShopCategories();

    @GET("Retrive/getAdsType")
    Call<AdTypeList> GetAdTypes();



    @Multipart
    @POST("Insert/postAds")
    Call<Object> PostInstoreAds(
            @Part("ads_type") RequestBody adsType,
            @Part("user_id") RequestBody userId,
            @Part("shop_id") RequestBody shopId,
            @Part("ads_name") RequestBody adTitle,
            @Part("offer_id") RequestBody offerTypeId,
            @Part("category_id") RequestBody categoryId,
            @Part("show_date") RequestBody fromDate,
            @Part("exp_date") RequestBody toDate,
            @Part("ads_description") RequestBody description,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("Insert/postAds")
    Call<Object> PostInstoreAds(
            @Part("ads_type") RequestBody adsType,
            @Part("user_id") RequestBody userId,
            @Part("shop_id") RequestBody shopId,
            @Part("ads_name") RequestBody adTitle,
            @Part("offer_id") RequestBody offerTypeId,
            @Part("category_id") RequestBody categoryId,
            @Part("show_date") RequestBody fromDate,
            @Part("exp_date") RequestBody toDate,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("Insert/postAds")
    Call<Object> PostProductAds(
            @Part("ads_type") RequestBody adsType,
            @Part("user_id") RequestBody userId,
            @Part("shop_id") RequestBody shopId,
            @Part("product_name") RequestBody prouductName,
            @Part("ads_name") RequestBody adTitle,
            @Part("offer_id") RequestBody offerTypeId,//not for product offers
            @Part("category_id") RequestBody categoryId,
            @Part("ads_discount_type") RequestBody adsDiscountType,
            @Part("ads_original_price") RequestBody adsOriginalPrice,
            @Part("ads_discount_price") RequestBody adsDiscountPrice,
            @Part("show_date") RequestBody fromDate,
            @Part("exp_date") RequestBody toDate,
            @Part("ads_description") RequestBody description,
            @Part MultipartBody.Part[] images
    );

    @Multipart
    @POST("Insert/postAds")
    Call<Object> PostProductAds(
            @Part("ads_type") RequestBody adsType,
            @Part("user_id") RequestBody userId,
            @Part("shop_id") RequestBody shopId,
            @Part("product_name") RequestBody prouductName,
            @Part("ads_name") RequestBody adTitle,
            @Part("offer_id") RequestBody offerTypeId,//not for product offers
            @Part("category_id") RequestBody categoryId,
            @Part("ads_discount_type") RequestBody adsDiscountType,
            @Part("ads_original_price") RequestBody adsOriginalPrice,
            @Part("ads_discount_price") RequestBody adsDiscountPrice,
            @Part("show_date") RequestBody fromDate,
            @Part("exp_date") RequestBody toDate,
            @Part MultipartBody.Part[] images
    );
}