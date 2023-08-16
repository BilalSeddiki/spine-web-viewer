package com.projectqt.viewer.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class Fieldset extends ComplexPanel {

    interface MyUiBinder extends UiBinder<Element, Fieldset> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField Element legend;

    public Fieldset() {
        super();
        setElement(uiBinder.createAndBindUi(this));
    }

    @UiConstructor
    public Fieldset(String legend) {
        this();
        this.legend.setInnerText(legend);
    }

    @Override
    public void add(Widget widget) {
        add(widget, getElement());
    }
}