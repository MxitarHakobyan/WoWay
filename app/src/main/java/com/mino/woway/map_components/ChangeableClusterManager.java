package com.mino.woway.map_components;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.mino.woway.users_model.Owner;
import com.mino.woway.utils.OwnerClient;

public class ChangeableClusterManager extends ClusterManager<ClusterMarker> {

    private final ClusterRenderer mRenderer;
    private final GoogleMap mMap;
    private final Context mContext;
    private Owner mOwner;
    private ClusterMarker[] mClusterMarkers = new ClusterMarker[2];

    public ChangeableClusterManager(Context context, GoogleMap map) {
        super(context, map);
        mRenderer = new ClusterRenderer(context, map, ChangeableClusterManager.this);
        mMap = map;
        mContext = context;
        mOwner = ((OwnerClient) mContext.getApplicationContext()).getOwner();
        super.setRenderer(mRenderer);
    }

    public void drawStartPointMarker() {
        removeOldMarkerIfExist();
        mClusterMarkers[0] = getClusterMarker(true);
        renderMarker();
    }


    public void drawEndpointMarker() {
        removeOldMarkerIfExist();
        mClusterMarkers[1] = getClusterMarker(false);
        renderMarker();
    }

    private void removeOldMarkerIfExist() {
        for (ClusterMarker clusterMarker : mClusterMarkers) {
            if (clusterMarker != null) {
                super.removeItem(clusterMarker);
            }
        }
    }

    private ClusterMarker getClusterMarker(boolean isStartPoint) {
        if (isStartPoint) {
            return new ClusterMarker(mContext,
                    mOwner.getStartLatLng(),
                    mOwner.getAvatarUrl());
        } else {
            return new ClusterMarker(mContext,
                    mOwner.getEndLatLng(),
                    mOwner.getDrawableId());
        }
    }

    private void renderMarker() {
        if (mMap != null) {
            addNotNullMarkers();
            super.cluster();
        }
    }

    private void addNotNullMarkers() {
        for (ClusterMarker clusterMarker : mClusterMarkers) {
            if (clusterMarker != null) {
                super.addItem(clusterMarker);
            }
        }
    }
}
