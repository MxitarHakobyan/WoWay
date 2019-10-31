package com.mino.woway.users_model;

import static com.mino.woway.utils.Constants.NAME_URL_ENDPOINT;
import static com.mino.woway.utils.Constants.NAME_URL_START_POINT;

public abstract class User {

    private String mId;
    private UserName mName;
    private String mEmail;
    private String mAvatarUrl;
    private int mDrawableId;

    User(String id, UserName name, String email, int drawableId) {
        this.mId = id;
        this.mName = name;
        this.mEmail = email;
        this.mAvatarUrl = NAME_URL_START_POINT + id + NAME_URL_ENDPOINT;
        this.mDrawableId = drawableId;
    }

    public String getId() {
        return mId;
    }

    public UserName getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId='" + mId + '\'' +
                ", mName=" + mName.toString() +
                ", mEmail='" + mEmail + '\'' +
                ", mAvatarUrl='" + mAvatarUrl + '\'' +
                '}';
    }
}
