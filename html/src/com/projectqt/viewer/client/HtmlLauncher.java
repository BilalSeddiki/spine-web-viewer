package com.projectqt.viewer.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.projectqt.viewer.AssetsUrl;
import com.projectqt.viewer.SkeletonViewer;
import com.projectqt.viewer.widgets.WebCanvasSettings;

public class HtmlLauncher extends GwtApplication {

    private HorizontalPanel mainPanel; 

    public HtmlLauncher() {
        mainPanel = new HorizontalPanel();
        RootPanel.get().insert(mainPanel, 0);
    }

    @Override
    public GwtApplicationConfiguration getConfig () {
        return new GwtApplicationConfiguration(1080, 1920);
    }

    @Override
    public ApplicationListener createApplicationListener () {
        SkeletonViewer skeletonViewer = new SkeletonViewer();
        WebCanvasSettings webCanvasSettings = new WebCanvasSettings(skeletonViewer);
        skeletonViewer.setCanvasSettings(webCanvasSettings);
        mainPanel.add(webCanvasSettings);
        return skeletonViewer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onModuleLoad() {
        super.onModuleLoad();
        Window.enableScrolling(true);
        try {
            Field f = ClassReflection.getDeclaredField(Preloader.class, "stillToFetchAssets");
            f.setAccessible(true);
            AssetsUrl.setAssetUrl((ObjectMap<String, Preloader.Asset>) f.get(getPreloader()));
        } catch (ReflectionException e) {
            Gdx.app.error("HtmlLauncher()", "ReflectionException", e);
        }
    }
}