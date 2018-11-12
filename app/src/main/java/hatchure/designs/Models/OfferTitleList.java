package hatchure.designs.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfferTitleList {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("offertitles_list")
    @Expose
    private List<OfferTitle> offerTitlesList = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<OfferTitle> getOfferTitlesList() {
        return offerTitlesList;
    }

    public void setOfferTitlesList(List<OfferTitle> offertitlesList) {
        this.offerTitlesList = offertitlesList;
    }
}
