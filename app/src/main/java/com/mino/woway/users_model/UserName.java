package com.mino.woway.users_model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserName implements Parcelable {

    private String mFirstName;
    private String mLastName;

    public UserName(String aFirstName, String aLastName) {
        this.mFirstName = aFirstName;
        this.mLastName = aLastName;
    }

    private UserName(Parcel in) {
        this.mFirstName = in.readString();
        this.mLastName = in.readString();
    }

    public static final Creator<UserName> CREATOR = new Creator<UserName>() {
        @Override
        public UserName createFromParcel(Parcel in) {
            return new UserName(in);
        }

        @Override
        public UserName[] newArray(int size) {
            return new UserName[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFirstName);
        parcel.writeString(mLastName);
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getFullName() {
        return mFirstName + " " + mLastName;
    }

    @Override
    public String toString() {
        return "UserName{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mLastName='" + mLastName + '\'' +
                '}';
    }
}
