package com.mino.woway.map_components;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.mino.woway.users_model.Another;

public class UnchangeableClusterManager extends ClusterManager<ClusterMarker> {

    private final ClusterRenderer mRenderer;
    private final GoogleMap mMap;
    private final Context mContext;
    private Another mAntoher;
    private ClusterMarker[] mClusterMarkers = new ClusterMarker[2];

    public UnchangeableClusterManager(Context context, GoogleMap map, Another another) {
        super(context, map);
        mRenderer = new ClusterRenderer(context, map, UnchangeableClusterManager.this);
        mMap = map;
        mContext = context;
        mAntoher = another;
        super.setRenderer(mRenderer);
    }

}
