package com.spine.viewer;

import com.badlogic.gdx.backends.gwt.preloader.DefaultAssetFilter;

public class AssetFilter extends DefaultAssetFilter {
    
    @Override
    public boolean preload(String file) {
        return file.equals("characters.json") || file.startsWith("api/") || file.endsWith(".csv");
    }
}
