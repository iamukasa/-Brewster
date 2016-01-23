package com.amusoft.brewster;

/**
 * Created by irving on 8/6/15.
 */
public class BrewOnlineItem {
    BrewItem bBrewItem;
    String BrewItemKey;

    public BrewOnlineItem(BrewItem bBrewItem,String brewitem) {
        this.bBrewItem=bBrewItem;
        this.BrewItemKey=brewitem;

    }

    public BrewItem getbBrewItem() {
        return bBrewItem;
    }

    public String getBrewItemKey() {
        return BrewItemKey;
    }

    public void setbBrewItem(BrewItem bBrewItem) {
        this.bBrewItem = bBrewItem;
    }

    public void setBrewItemKey(String brewItemKey) {
        BrewItemKey = brewItemKey;
    }

}
