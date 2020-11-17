package com.app.miniproject.Models;

public class ExpertiseModel {

    private String expertiseImage,expertiseTitle,expertiseDescription,expertisePrice;
    private boolean expertiseFree;

    public String getExpertiseImage() {
        return expertiseImage;
    }

    public void setExpertiseImage(String expertiseImage) {
        this.expertiseImage = expertiseImage;
    }

    public String getExpertiseTitle() {
        return expertiseTitle;
    }

    public void setExpertiseTitle(String expertiseTitle) {
        this.expertiseTitle = expertiseTitle;
    }

    public String getExpertiseDescription() {
        return expertiseDescription;
    }

    public void setExpertiseDescription(String expertiseDescription) {
        this.expertiseDescription = expertiseDescription;
    }

    public String getExpertisePrice() {
        return expertisePrice;
    }

    public void setExpertisePrice(String expertisePrice) {
        this.expertisePrice = expertisePrice;
    }

    public boolean isExpertiseFree() {
        return expertiseFree;
    }

    public void setExpertiseFree(boolean expertiseFree) {
        this.expertiseFree = expertiseFree;
    }
}
