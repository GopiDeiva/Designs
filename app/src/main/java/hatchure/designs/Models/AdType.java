package hatchure.designs.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdType {
    @SerializedName("ads_type_id")
    @Expose
    private String adsTypeId;
    @SerializedName("ads_type_name")
    @Expose
    private String adsTypeName;

    public String getAdTypeId() {
        return adsTypeId;
    }

    public void setAdTypeId(String adsTypeId) {
        this.adsTypeId = adsTypeId;
    }

    public String getAdTypeName() {
        return adsTypeName;
    }

    public void setAdTypeName(String adsTypeName) {
        this.adsTypeName = adsTypeName;
    }
}
