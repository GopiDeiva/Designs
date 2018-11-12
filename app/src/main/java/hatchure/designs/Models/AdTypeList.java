package hatchure.designs.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdTypeList {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("Ads_type")
    @Expose
    private List<AdType> adsType = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<AdType> getAdTypes() {
        return adsType;
    }

    public void setAdTypes(List<AdType> adsType) {
        this.adsType = adsType;
    }
}
