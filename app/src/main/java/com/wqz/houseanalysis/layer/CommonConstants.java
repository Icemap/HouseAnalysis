package com.wqz.houseanalysis.layer;

public class CommonConstants
{
    public static final double MARKER_ICON_SIZE = 40.0D;

    public enum TiltStep {
        Step1, Step2, Step3, Step4, Step5
    }

    public enum GoogleMapType {
        ImageMap, VectorMap, RoadMap,AMapImageMap,AMapVectorMap
    }

    public enum MarkerFlag {
        LeftTop, RightTop, RightBottom, LeftBottom, CenterTop, CenterRight, CenterBottom, CenterLeft, MoveCenter, RotationLeft, RotationRight
    }
}