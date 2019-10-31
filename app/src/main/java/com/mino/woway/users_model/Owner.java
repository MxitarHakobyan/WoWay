package com.mino.woway.users_model;

import com.google.android.gms.maps.model.LatLng;

public class Owner extends User {

    private LatLng mStartLatLng;
    private LatLng mEndLatLng;

    public Owner(String aId, UserName aName, String aEmail, int drawableId) {
        super(aId, aName, aEmail, drawableId);
    }

    public void setStartLatLng(LatLng startLatLng) {
        mStartLatLng = startLatLng;
    }

    public void setEndLatLng(LatLng endLatLng) {
        mEndLatLng = endLatLng;
    }

    public LatLng getEndLatLng() {
        return mEndLatLng;
    }

    public LatLng getStartLatLng() {
        return mStartLatLng;
    }
}
