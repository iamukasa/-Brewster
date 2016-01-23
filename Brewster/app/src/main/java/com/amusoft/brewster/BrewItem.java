package com.amusoft.brewster;

/**
 * Created by irving on 8/6/15.
 */
public class BrewItem {
String BTitle;
    String BDescription;
    String BPhoto;
    String BVideo;
    String BSender;
    public BrewItem(String btitle,String bdescription,String bhoto,String bvideo,String bsender){
        this.BTitle=btitle;
        this.BDescription=bdescription;
        this.BPhoto=bhoto;
        this.BVideo=bvideo;
        this.BSender=bsender;
    }

    public String getBDescription() {
        return BDescription;
    }

    public String getBPhoto() {
        return BPhoto;
    }

    public String getBSender() {
        return BSender;
    }

    public String getBTitle() {
        return BTitle;
    }

    public String getBVideo() {
        return BVideo;
    }

    public void setBDescription(String BDescription) {
        this.BDescription = BDescription;
    }

    public void setBSender(String BSender) {
        this.BSender = BSender;
    }

    public void setBPhoto(String BPhoto) {
        this.BPhoto = BPhoto;
    }

    public void setBVideo(String BVideo) {
        this.BVideo = BVideo;
    }

    public void setBTitle(String BTitle) {
        this.BTitle = BTitle;
    }

}
