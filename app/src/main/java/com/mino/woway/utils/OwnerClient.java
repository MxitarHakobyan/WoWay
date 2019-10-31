package com.mino.woway.utils;

import android.app.Application;

import com.mino.woway.users_model.Owner;

public class OwnerClient extends Application {
    Owner mOwner = null;

    public Owner getOwner() {
        return mOwner;
    }

    public void setOwner(Owner aOwner) {
        this.mOwner = aOwner;
    }
}
