package com.projectqt.viewer.widgets;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;
import com.google.gwt.dom.client.Element;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RadioButton;
import com.projectqt.viewer.SkeletonObject;
import com.projectqt.viewer.SkeletonSettings;

public class WebSkeletonSettings extends Composite implements SkeletonSettings, HasText {

    interface MyUiBinder extends UiBinder<HTMLPanel, WebSkeletonSettings> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    
    private SkeletonObject skeletonObject;

    @UiField Element title;

    @UiField Fieldset skinFieldset;
    @UiField Fieldset animationFieldset;
    @UiField Fieldset slotFieldset;
        @UiField Details slotsDetails;

    @UiField Button pauseButton;
    @UiField CheckBox loopCheckbox;

    @UiField NumberInput positionXSlider;
    @UiField NumberInput positionYSlider;
    @UiField NumberInput scaleSlider;
    @UiField NumberInput rotationSlider;
    @UiField NumberInput speedSlider;
    @UiField NumberInput mixSlider;
    @UiField NumberInput volumeSlider;
    
    @UiField CheckBox flipXCheckbox;
    @UiField CheckBox flipYCheckbox;

    @UiField Button bonesButton;
    @UiField Button slotsButton;
    @UiField Button bothButton;
    
    @UiField CheckBox premultipliedCheckbox;
    @UiField CheckBox bonesCheckbox;
    @UiField CheckBox regionsCheckbox;
    @UiField CheckBox boundsCheckbox;
    @UiField CheckBox meshHullCheckbox;
    @UiField CheckBox meshTrianglesCheckbox;
    @UiField ProgressBar durationBar;

    public WebSkeletonSettings() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("positionXSlider")
    void handlePositionXChange(ValueChangeEvent<Float> event) {
        skeletonObject.setPositionX(event.getValue());
    }

    @UiHandler("positionYSlider")
    void handlePositionYChange(ValueChangeEvent<Float> event) {
        skeletonObject.setPositionY(event.getValue());
    }

    @UiHandler("scaleSlider")
    void handleScaleChange(ValueChangeEvent<Float> event) {
        skeletonObject.setScale(event.getValue());
    }

    @UiHandler("rotationSlider")
    void handleRotationChange(ValueChangeEvent<Float> event) {
        skeletonObject.setRotation(event.getValue());
    }

    @UiHandler("speedSlider")
    void handleSpeedChange(ValueChangeEvent<Float> event) {
        skeletonObject.setSpeed(event.getValue());
    }

    @UiHandler("mixSlider")
    void handleMixChange(ValueChangeEvent<Float> event) {
        skeletonObject.setMix(event.getValue());
    }

    @UiHandler("volumeSlider")
    void handleVolumeChange(ValueChangeEvent<Float> event) {
        skeletonObject.setVolume(event.getValue());
    }

    @UiHandler("pauseButton")
    void handlePauseClick(ClickEvent event) {
        skeletonObject.togglePlaying();
    }

    @UiHandler("loopCheckbox")
    void handleLoopChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setLoop(event.getValue());
    }

    @UiHandler("flipXCheckbox")
    void handleFlipXChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setFlipX(event.getValue());
    }

    @UiHandler("flipYCheckbox")
    void handleFlipYChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setFlipY(event.getValue());
    }

    @UiHandler("bonesButton")
    void handleBonesClick(ClickEvent event) {
        skeletonObject.setBones();
    }

    @UiHandler("slotsButton")
    void handleSlotsClick(ClickEvent event) {
        skeletonObject.setSlots();
    }

    @UiHandler("bothButton")
    void handleBothClick(ClickEvent event) {
        handleBonesClick(event);
        handleSlotsClick(event);
    }

    @UiHandler("premultipliedCheckbox")
    void handlePremultipliedChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setPremultiplied(event.getValue());      
    }

    @UiHandler("bonesCheckbox")
    void handleBonesChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setBones(event.getValue());
    }

    @UiHandler("regionsCheckbox")
    void handleRegionsChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setRegions(event.getValue());
    }

    @UiHandler("boundsCheckbox")
    void handleBoundsChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setBounds(event.getValue());
    }

    @UiHandler("meshHullCheckbox")
    void handleMeshHullChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setMeshHull(event.getValue());
    }

    @UiHandler("meshTrianglesCheckbox")
    void handleMeshTrianglesChange(ValueChangeEvent<Boolean> event) {
        skeletonObject.setMeshTriangles(event.getValue());
    }

    @Override
    public void loadSkins(Array<Skin> skins) {
        skinFieldset.clear();
        for (int i = 0; i < skins.size; i++) {
            Skin skin = skins.get(i);
            String skinName = skin.getName();
            RadioButton skinRadio = new RadioButton("skin", skinName);
            skinRadio.addClickHandler(event -> skeletonObject.setSkin(skinName));
            if (i == 0 || skin.getName().equals("default")) {
                skinRadio.setValue(true, true);
            }
            skinFieldset.add(skinRadio);
        }
    }

    @Override
    public void loadAnimations(Array<Animation> animations) {
        animationFieldset.clear();
        for (int i = 0; i < animations.size; i++) {
            Animation animation = animations.get(i);
            String animationName = animation.getName();
            RadioButton animationRadio = new RadioButton("animation", animationName);
            animationRadio.addClickHandler(event -> skeletonObject.setAnimation(animationName));
            if (i == 0 || animationName.equals("idle")) {
                animationRadio.setValue(true, true);
            }
            animationFieldset.add(animationRadio);
        }
    }

    @Override
    public void loadSlots(Array<Slot> slots) {
        slotsDetails.clear();

        for (Slot slot : slots) {
            String slotName = slot.getData().getName();
            CheckBox slotCheck = new CheckBox(slotName);
            slotCheck.setValue(true);
            slotCheck.addValueChangeHandler(event -> skeletonObject.setSlotVisibility(slotName, event.getValue()));
            slotsDetails.add(slotCheck);
        }
    }

    @Override
    public void setProgress(float value) {
        durationBar.setValue(value);
    }

    @Override
    public SkeletonObject getSkeletonObject() {
        return skeletonObject;
    }

    @Override
    public void setSkeletonObject(SkeletonObject skeletonObject) {
        this.skeletonObject = skeletonObject;
    }

    @Override
    public String getText() {
        return title.getInnerText();
    }

    @Override
    public void setText(String text) {
        title.setInnerText(text);
    }
}