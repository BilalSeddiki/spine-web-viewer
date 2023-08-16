package com.projectqt.viewer;

public interface CanvasSettings {

    public void loadAsset(Asset asset);

    public void setCameraPosition(float x, float y);

    public void setCameraZoom(float zoom);
}