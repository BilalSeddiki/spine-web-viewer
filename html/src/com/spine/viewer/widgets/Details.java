package com.spine.viewer.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class Details extends ComplexPanel {

    interface MyUiBinder extends UiBinder<Element, Details> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField Element summary;

    public Details() {
        super();
        setElement(uiBinder.createAndBindUi(this));
    }

    @UiConstructor
    public Details(String summary) {
        this();
        this.summary.setInnerText(summary);
    }

    @Override
    public void add(Widget widget) {
        add(widget, getElement());
    }
}