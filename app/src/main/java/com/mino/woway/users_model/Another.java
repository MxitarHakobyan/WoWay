package com.mino.woway.users_model;

import com.google.android.gms.maps.model.LatLng;

public class Another extends User {

    private LatLng mStartLatLng;
    private LatLng mEndLatLng;

    public Another(String aId, UserName aName, String aEmail, int drawableId) {
        super(aId, aName, aEmail, drawableId);
    }

    public Another(String aId, UserName aName, String aEmail, int drawableId,  LatLng startLatLng, LatLng endLatLng) {
        super(aId, aName, aEmail, drawableId);
        mStartLatLng = startLatLng;
        mEndLatLng = endLatLng;
    }

    public LatLng getStartLatLng() {
        return mStartLatLng;
    }

    public LatLng getEndLatLng() {
        return mEndLatLng;
    }
}
