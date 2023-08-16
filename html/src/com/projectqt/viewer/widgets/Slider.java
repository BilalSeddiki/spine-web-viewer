package com.projectqt.viewer.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;

public class Slider extends FocusWidget implements HasName, HasValue<Float> {

    interface MyUiBinder extends UiBinder<DivElement, Slider> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getFormat("0.00");

    @UiField LabelElement sliderLabel;
    @UiField ButtonElement sliderButton;
    @UiField InputElement sliderInput;

    private final boolean onchange; 

    @UiConstructor
    public Slider(String label, String min, String max, String value, String step, boolean onchange) {
        setElement(uiBinder.createAndBindUi(this));
        
        this.onchange = onchange;

        String uid = DOM.createUniqueId();
        sliderLabel.setHtmlFor(uid);
        sliderInput.setId(uid);

        sliderLabel.setInnerText(label);
        sliderInput.setName(label);
        sliderInput.setPropertyString("min", min);
        sliderInput.setPropertyString("max", max);
        sliderInput.setPropertyString("value", value);
        sliderInput.setPropertyString("step", step);

        DOM.sinkEvents(sliderButton, Event.ONCLICK);
        DOM.setEventListener(sliderButton, event -> setValue(Float.valueOf(value), true));

        Element sliderOptions = DOM.createDiv();
        Element sliderSlide = DOM.createDiv();
        sliderOptions.appendChild(sliderLabel);
        sliderOptions.appendChild(sliderButton);
        sliderSlide.appendChild((sliderInput));
        getElement().appendChild(sliderOptions);
        getElement().appendChild(sliderSlide);
    }

    public Slider(String label, String min, String max, String value, String step, boolean onchange, ValueChangeHandler<Float> handler) {
        this(label, min, max, value, step, onchange);
        addValueChangeHandler(handler);
    }

    public Slider(String label, float min, float max, float value, float step, ValueChangeHandler<Float> handler) {
        this(label, NUMBER_FORMAT.format(min), NUMBER_FORMAT.format(max), NUMBER_FORMAT.format(value), NUMBER_FORMAT.format(step), false, handler);
    }

    public float getMax() {
        return (float) sliderInput.getPropertyDouble("max");
    }

    @Override
    public void setName(String name) {
        sliderInput.setName(name);
    }

    @Override
    public String getName() {
        return sliderInput.getName();
    }

    @Override
    public Float getValue() {
        return Float.valueOf(sliderInput.getValue());
    }

    @Override
    public void setValue(Float value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Float value, boolean fireEvents) {
        sliderInput.setValue(NUMBER_FORMAT.format(value));
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Float> handler) {
        if (onchange) {
            DOM.sinkEvents(sliderInput, Event.ONCHANGE);
        }
        else {
            DOM.sinkBitlessEvent(sliderInput, "input");
        }
        DOM.setEventListener(sliderInput, event -> setValue(getValue(), true));
        return addHandler(handler, ValueChangeEvent.getType());
    }
}