package com.spine.viewer.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class ProgressBar extends Widget implements HasName, HasText, HasValue<Float> {

    interface MyUiBinder extends UiBinder<DivElement, ProgressBar> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField Element progressBar;
    @UiField LabelElement progressLabel;

    @UiConstructor
    public ProgressBar(String text, int value, int max) {
        setElement(uiBinder.createAndBindUi(this));

        String uid = DOM.createUniqueId();
        progressBar.setId(uid);
        progressLabel.setHtmlFor(uid);
        progressBar.setPropertyInt("value", value);
        progressBar.setPropertyInt("max", max);
        progressLabel.setInnerText(text);
    }
    
    @Override
    public String getText() {
        return progressLabel.getInnerText();
    }

    @Override
    public void setText(String text) {
        progressLabel.setInnerText(text);
    }

    @Override
    public void setName(String name) {
        progressBar.setPropertyString("name", name);
    }

    @Override
    public String getName() {
        return progressBar.getPropertyString("name");
    }

    @Override
    public Float getValue() {
        return (float) progressBar.getPropertyDouble("value");
    }

    @Override
    public void setValue(Float value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Float value, boolean fireEvents) {
        progressBar.setPropertyDouble("value", value);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Float> handler) {
        return null;
    }
}