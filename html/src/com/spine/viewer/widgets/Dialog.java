package com.spine.viewer.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class Dialog extends ComplexPanel {

    interface MyUiBinder extends UiBinder<Element, Dialog> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField Element dialogWindow;

    @UiConstructor
    public Dialog() {
        super();
        setElement(uiBinder.createAndBindUi(this));
        getElement().getString();
    }

    public void show() {
        dialogWindow.setPropertyBoolean("open", true);
    }

    public void hide() {
        dialogWindow.setPropertyBoolean("open", false);
    }

    @Override
    public void add(Widget widget) {
        add(widget, getElement());
    }
}