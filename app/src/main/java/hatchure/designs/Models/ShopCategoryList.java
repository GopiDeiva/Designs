package hatchure.designs.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopCategoryList {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("shopcategories_list")
    @Expose
    private List<ShopCategory> shopcategoriesList = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<ShopCategory> getShopCategoriesList() {
        return shopcategoriesList;
    }

    public void setShopCategoriesList(List<ShopCategory> shopcategoriesList) {
        this.shopcategoriesList = shopcategoriesList;
    }
}
