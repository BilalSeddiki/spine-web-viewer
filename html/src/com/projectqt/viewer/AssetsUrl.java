package com.projectqt.viewer;

import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.Asset;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetsUrl {

    private static ObjectMap<String, Asset> assetsUrlMap;
    private static Preloader preloader;

    private AssetsUrl() {}

    public static String getAssetUrl(String file) {
        Asset asset = assetsUrlMap.get(file);
        if (asset == null) {
            return null;
        }
        return "assets/" + asset.url;
    }

    public static String getAssetUrl(FileHandle file) {
        return getAssetUrl(file.path());
    }

    public static Preloader getPreloader() {
        return preloader;
    }

    public static void setAssetUrl(ObjectMap<String, Asset> assetsUrl) {
        AssetsUrl.assetsUrlMap = assetsUrl;
    }

    public static void setPreloader(Preloader preloader) {
        AssetsUrl.preloader = preloader;
    }
}