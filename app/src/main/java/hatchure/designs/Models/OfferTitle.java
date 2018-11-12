package hatchure.designs.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferTitle {
    @SerializedName("offertitle_id")
    @Expose
    private String offerTitleId;
    @SerializedName("offertitle_name")
    @Expose
    private String offerTitleName;

    public String getOfferTitleId() {
        return offerTitleId;
    }

    public void setOfferTitleId(String offertitleId) {
        this.offerTitleId = offertitleId;
    }

    public String getOfferTitleName() {
        return offerTitleName;
    }

    public void setOfferTitleName(String offertitleName) {
        this.offerTitleName = offertitleName;
    }
}
