package hatchure.designs.Interfaces;

import hatchure.designs.Models.AdTypeList;
import hatchure.designs.Models.OfferTitleList;
import hatchure.designs.Models.ShopCategoryList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("Retrive/getOffersTitle")
    Call<OfferTitleList> GetOfferTypes();

    @GET("Retrive/getShopsCategories")
    Call<ShopCategoryList> GetShopCategories();

    @GET("Retrive/getAdsType")
    Call<AdTypeList> GetAdTypes();
}