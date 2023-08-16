package com.spine.viewer.widgets;

import com.badlogic.gdx.graphics.Color;
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
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;

public class ColorInput extends FocusWidget implements HasName, HasText, HasValue<Color> {

    interface MyUiBinder extends UiBinder<DivElement, ColorInput> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField InputElement colorInput;
    @UiField LabelElement colorLabel;

    @UiConstructor
    public ColorInput(String text) {
        setElement(uiBinder.createAndBindUi(this));

        setText(text);
        String uid = DOM.createUniqueId();
        colorInput.setId(uid);
        colorLabel.setHtmlFor(uid);
    }

    public ColorInput(String text, ValueChangeHandler<Color> handler) {
        this(text);
        addValueChangeHandler(handler);
    }

    @Override
    public void setName(String name) {
        colorInput.setName(name);
    }

    @Override
    public String getName() {
        return colorInput.getName();
    }

    @Override
    public String getText() {
        return colorLabel.getInnerText();
    }

    @Override
    public void setText(String text) {
        colorLabel.setInnerText(text);
    }

    @Override
    public Color getValue() {
        return Color.valueOf(colorInput.getValue());
    }

    @Override
    public void setValue(Color value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Color value, boolean fireEvents) {
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Color> handler) {
        DOM.sinkBitlessEvent(colorInput, "input");
        DOM.setEventListener(colorInput, event -> setValue(getValue(), true));
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
