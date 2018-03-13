package com.wqz.houseanalysis.activity;

import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.layer.CommonConstants;
import com.wqz.houseanalysis.layer.GoogleMapLayer;
import com.wqz.houseanalysis.utils.CoordinateUtils;
import com.wqz.houseanalysis.utils.DrawableUtils;
import com.wqz.houseanalysis.utils.FileUtils;
import com.wqz.houseanalysis.utils.ScreenUtils;

import butterknife.BindDrawable;
import butterknife.BindView;

public class MainActivity extends BaseActivity
{
    private GoogleMapLayer mGoogleMapLayer = null;
    private GoogleMapLayer mGoogleVectorMapLayer = null;
    private GraphicsLayer mLocLayer = null;
    private PictureMarkerSymbol mLocMeasureSymbol;
    public Double scaleIndex;
    //回馈系数，UI设计时以800dp为基准设计，DP以360dp为基准。比率为 0.45：1，回馈系数近似为2.
    private Double feedback = 1.3;

    @BindView(R.id.main_map)
    MapView mapView;

    @BindDrawable(R.mipmap.self_loc)
    Drawable selfLoc;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_main;
    }


    @Override
    protected void onInitLogic()
    {
        super.onInitLogic();
        onMapInit();
        onLocInit();
    }

    private void onMapInit()
    {
        scaleIndex = getScaleIndex();

        String cachePath = FileUtils.getSDPath() + "/com/wqz/house/analysis";
        mGoogleMapLayer = new GoogleMapLayer(
                CommonConstants.GoogleMapType.ImageMap, cachePath);
        mGoogleVectorMapLayer = new GoogleMapLayer(
                CommonConstants.GoogleMapType.VectorMap, cachePath);
        mLocLayer = new GraphicsLayer();
        mLocMeasureSymbol = new PictureMarkerSymbol(DrawableUtils.zoomDrawable(
                selfLoc,scaleIndex * feedback));

        mapView.enableWrapAround(true);
        mapView.removeAll();
        mapView.addLayer(mGoogleMapLayer);
        mapView.addLayer(mLocLayer);
    }

    private void onLocInit()
    {
        AMapLocationListener mAMapLocationListener = new AMapLocationListener()
        {
            @Override
            public void onLocationChanged(AMapLocation amapLocation)
            {
                if(amapLocation.getErrorCode() != 0)
                {
                    Toast.makeText(MainActivity.this,"定位失败",Toast.LENGTH_LONG).show();
                    return;
                }
                Point startPoint = CoordinateUtils.lonLatToMercatorPoint(amapLocation.getLongitude(), amapLocation.getLatitude());
                setLoc(startPoint);
                mapView.centerAt(startPoint ,true);
                mapView.setScale(100000);
            }
        };
        BaseApplication.getInstances().getLocClient().setLocationListener(mAMapLocationListener);
        BaseApplication.getInstances().getLocClient().startLocation();
    }

    /**
     * Loc point set
     */
    public void setLoc(Point locPoint)
    {
        mLocLayer.removeAll();
        mLocLayer.addGraphic(new Graphic(locPoint, mLocMeasureSymbol));
    }

    public Double getScaleIndex()
    {
        Integer h = ScreenUtils.getScreenHeight();
        Integer w = ScreenUtils.getScreenWidth();

        Double fCurrentLength = Math.sqrt(h * h + w * w);
        Double fModelLength = Math.sqrt(1920 * 1920 + 1200 * 1200);
        return fCurrentLength / fModelLength;
    }
}
