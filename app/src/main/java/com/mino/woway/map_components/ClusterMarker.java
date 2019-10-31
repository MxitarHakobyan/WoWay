package com.mino.woway.map_components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.mino.woway.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClusterMarker implements ClusterItem {

    private LatLng mPosition;
    private final Context mContext;
    private CircleImageView mIvMarker;

    ClusterMarker(Context aContext, LatLng aPosition, String avatarUrl) {
        this.mContext = aContext;
        this.mPosition = aPosition;
        mIvMarker = new CircleImageView(mContext.getApplicationContext());
        setMarkerViewForStartPoint();
        loadAvatarIntoMarker(avatarUrl);
    }

    ClusterMarker(Context aContext, LatLng position, int drawableId) {
        this.mContext = aContext;
        this.mPosition = position;
        mIvMarker = new CircleImageView(mContext.getApplicationContext());
        setMarkerViewForStartPoint();
        //TODO is it same method ?
//        setMarkerViewForEndpoint();
        loadDrawableIntoMarker(drawableId);
    }

    private void setMarkerViewForStartPoint() {
        int padding = (int) mContext.getResources().getDimension(R.dimen.custom_marker_padding);
        int mMarkerHeight = (int) mContext.getResources().getDimension(R.dimen.custom_marker_image);
        int mMarkerWidth = (int) mContext.getResources().getDimension(R.dimen.custom_marker_image);
        mIvMarker.setLayoutParams(new ViewGroup.LayoutParams(mMarkerWidth, mMarkerHeight));
        mIvMarker.setBackground(mContext.getDrawable(R.drawable.drawable_round_avatar));
        mIvMarker.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvMarker.setPadding(padding, padding, padding, padding);
    }

    private void loadAvatarIntoMarker(String aUrl) {
        Glide.with(mContext).load(aUrl).into(mIvMarker);
    }

    private void loadDrawableIntoMarker(int drawableId) {
        Glide.with(mContext).load(mContext.getDrawable(drawableId)).into(mIvMarker);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public String getSnippet() {
        return "";
    }

    ImageView getIvMarker() {
        return mIvMarker;
    }
}
