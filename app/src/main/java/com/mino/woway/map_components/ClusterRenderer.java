package com.mino.woway.map_components;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.mino.woway.R;

public class ClusterRenderer extends DefaultClusterRenderer<ClusterMarker> {

    private final Context mContext;
    private IconGenerator mIconGenerator;
    private static final int PADDING = 16;

    ClusterRenderer(Context aContext, GoogleMap aMap, ClusterManager<ClusterMarker> aClusterManager) {
        super(aContext, aMap, aClusterManager);
        this.mContext = aContext;
        initIconGenerator();
    }

    private void initIconGenerator() {
        mIconGenerator = new IconGenerator(mContext.getApplicationContext());
        mIconGenerator.setColor(R.color.colorAccent);
        mIconGenerator.setContentPadding(PADDING, PADDING, PADDING, PADDING);
        mIconGenerator.setBackground(mContext.getDrawable(R.drawable.drawable_round_marker));
    }

    @Override
    protected void onBeforeClusterItemRendered(ClusterMarker item, MarkerOptions markerOptions) {
        mIconGenerator.setContentView(item.getIvMarker());
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.getTitle());
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<ClusterMarker> cluster) {
        return false;
    }

}
