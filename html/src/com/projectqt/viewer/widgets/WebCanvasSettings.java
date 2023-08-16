package com.projectqt.viewer.widgets;

import com.badlogic.gdx.graphics.Color;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.projectqt.viewer.Asset;
import com.projectqt.viewer.CanvasSettings;
import com.projectqt.viewer.File;
import com.projectqt.viewer.SkeletonViewer;

public class WebCanvasSettings extends Composite implements CanvasSettings {

    interface MyUiBinder extends UiBinder<FlowPanel, WebCanvasSettings> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    private final SkeletonViewer skeletonViewer;

    private final FlowPanel mainPanel;

    @UiField Button fullscreenButton;
    @UiField NumberInput xPositionInput;
    @UiField NumberInput yPositionInput;
    @UiField NumberInput zoomInput;
    @UiField NumberInput rotationInput;
    @UiField ColorInput colorInput;
    @UiField MultiFileUpload fileUploadWidget;
    @UiField HTMLPanel skeletonSettings;
    
    public WebCanvasSettings(SkeletonViewer skeletonViewer) {
        mainPanel = uiBinder.createAndBindUi(this);
        initWidget(mainPanel);
        this.skeletonViewer = skeletonViewer;
        fileUploadWidget.setAccept(".skel", ".atlas", ".png");
        fileUploadWidget.addValueChangeHandler(event -> {
            Asset asset = new Asset(event.getValue().first(), event.getValue().get(1), event.getValue().peek());
            File imageParent = new File("");
            imageParent.addChild((File) asset.getTextureFile());
            ((File) asset.getAtlasFile()).setParent(imageParent);

            File textureFile = ((File) asset.getTextureFile());
            Image img = new Image();
            img.addLoadHandler(loadEvent -> {
                textureFile.preloader.images.put(textureFile.path(), img.getElement().cast());
                loadAsset(asset);
                mainPanel.remove(img);
            });
            img.setUrl(textureFile.readString());
            img.getElement().setPropertyString("hidden", "hidden");
            mainPanel.add(img);
        });
    }

    @UiHandler("fullscreenButton")
    void handleFullscreenClick(ClickEvent event) {
        skeletonViewer.toggleFullscreenMode();
    }

    @UiHandler("xPositionInput")
    void handlePositionXChange(ValueChangeEvent<Float> event) {
        skeletonViewer.setCameraPositionX(event.getValue());
    }

    @UiHandler("yPositionInput")
    void handlePositionYChange(ValueChangeEvent<Float> event) {
        skeletonViewer.setCameraPositionY(event.getValue());
    }

    @UiHandler("zoomInput")
    void handleZoomChange(ValueChangeEvent<Float> event) {
        skeletonViewer.setCameraZoom(event.getValue());
    }

    @UiHandler("rotationInput")
    void handleRotationChange(ValueChangeEvent<Float> event) {
        skeletonViewer.setCameraRotation(event.getValue());
    }

    @UiHandler("colorInput")
    void handleColorChange(ValueChangeEvent<Color> event) {
        skeletonViewer.setBackgroundColor(event.getValue());
    }

    @UiHandler("fileUploadWidget")
    void handleFileUpload(ChangeEvent event) {
        fileUploadWidget.loadFiles();
    }

    @Override
    public void loadAsset(Asset asset) {
        HTMLPanel settingPanel = new HTMLPanel("");
        WebSkeletonSettings webSkeletonSetting = new WebSkeletonSettings();
        skeletonViewer.loadAsset(asset, webSkeletonSetting);
        webSkeletonSetting.setText(asset.getSkelFile().name());
        settingPanel.add(new Button("Up", (ClickHandler) event -> skeletonViewer.translateSkeletonPosition(webSkeletonSetting, 1)));
        settingPanel.add(new Button("Down",  (ClickHandler) event -> skeletonViewer.translateSkeletonPosition(webSkeletonSetting, -1)));
        settingPanel.add(new Button("Delete", (ClickHandler) event -> {
            skeletonViewer.removeSkeleton(webSkeletonSetting);
            skeletonSettings.remove(settingPanel);
        }));
        settingPanel.add(webSkeletonSetting);
        skeletonSettings.add(settingPanel);
    }

    @Override
    public void setCameraPosition(float x, float y) {
        xPositionInput.setValue(x);
        yPositionInput.setValue(y);
    }

    @Override
    public void setCameraZoom(float zoom) {
        zoomInput.setValue(zoom);
    }
}