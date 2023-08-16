package com.spine.viewer.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasValue;

public class NumberInput extends FocusWidget implements HasValue<Float> {

    private static final int MAX_NUMBERS_AFTER_DOT = 2;

    interface MyUiBinder extends UiBinder<DivElement, NumberInput> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField LabelElement inputLabel;
    @UiField InputElement numberInput;

    @UiConstructor
    public NumberInput(String label, String value, String step) {
        setElement(uiBinder.createAndBindUi(this));
        inputLabel.setInnerText(label);
        numberInput.setPropertyString("value", value);
        numberInput.setPropertyString("step", step);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Float> handler) {
        DOM.sinkBitlessEvent(numberInput, "input");
        DOM.setEventListener(numberInput, event -> setValue(getValue(), true));
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public Float getValue() {
        return Float.valueOf(numberInput.getPropertyString("value"));
    }

    @Override
    public void setValue(Float value) {
        numberInput.setPropertyString("value", formatString(value));
    }

    @Override
    public void setValue(Float value, boolean fireEvents) {
        setValue(value);
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    public void setMin(String value) {
        numberInput.setPropertyString("min", value);
    }

    public void setMax(String value) {
        numberInput.setPropertyString("max", value);
    }

    private String formatString(float value) {
        String stringValue = String.valueOf(value);
        int dotIndex = stringValue.indexOf(".");
        if (dotIndex == -1) {
            return stringValue;
        }
        int index = dotIndex + MAX_NUMBERS_AFTER_DOT;
        index = index >= stringValue.length() ? stringValue.length() : index;
        return stringValue.substring(0, index);
    }
}
