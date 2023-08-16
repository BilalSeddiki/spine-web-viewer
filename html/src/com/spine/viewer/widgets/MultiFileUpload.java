package com.spine.viewer.widgets;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.utils.Array;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.Int8Array;
import com.google.gwt.typedarrays.shared.TypedArrays;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HasValue;
import com.spine.viewer.File;

public class MultiFileUpload extends FileUpload implements HasValue<Array<File>> {

    private Array<File> loadedFiles;

    public MultiFileUpload() {
        getElement().setAttribute("multiple", "multiple");
    }

    public void setAccept(String... extensions) {
        getElement().setAttribute("accept", String.join(",", extensions));
    }

    public void loadFiles() {
        readFiles(getElement().getPropertyJSO("files"));
    }

    private native void readFiles(JavaScriptObject files) /*-{
        var self = this;
        var index = 0;
        var buffer = [];
        var f = function() {
            var reader = new FileReader();
            current_file = files.item(index);
            reader.onload = function(e) {
                buffer.push(current_file.name, reader.result);
                if (index >= files.length - 1) {
                    self.@com.projectqt.viewer.widgets.MultiFileUpload::setJsValue(*)(buffer);
                }
                else {
                    index++;
                    f();
                }
            }
            if (current_file.name.endsWith(".png")) {
                reader.readAsDataURL(current_file);
            }
            else {
                reader.readAsArrayBuffer(current_file);
            }
        }
        f();
    }-*/;

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Array<File>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public Array<File> getValue() {
        return loadedFiles;
    }

    @Override
    public void setValue(Array<File> value) {
        this.loadedFiles = value;
    }

    @Override
    public void setValue(Array<File> value, boolean fireEvents) {
        setValue(value);
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    private void setJsValue(FileObject value) {
        Array<File> files = new Array<>();
        for (int i = 0; i < value.length(); i += 2) {
            files.add(new File(value.getName(i), value.getContentAsByteArray(i)));
        }
        setValue(files, true);
    }

    private static class FileObject extends JavaScriptObject {

        protected FileObject() {}

        public final native String getName(int index) /*-{
            return this[index];
        }-*/;

        public final native JavaScriptObject getContent(int index) /*-{
            return this[index + 1];
        }-*/;

        public final native String getContentString(int index) /*-{
            return this[index + 1];
        }-*/;

        public final native int length() /*-{
            return this.length;
        }-*/;

        public final byte[] getContentAsByteArray(int index) {
            if (getName(index).endsWith(".png")) {
                String dataUrl = getContentString(index);
                return dataUrl.getBytes(StandardCharsets.UTF_8);
            }
            ArrayBuffer arrayBuffer = getContent(index).cast();
            Int8Array intArray = TypedArrays.createInt8Array(arrayBuffer);
            byte[] bytes = new byte[arrayBuffer.byteLength()];
            for (int i = 0; i < arrayBuffer.byteLength(); i++) {
                bytes[i] = intArray.get(i);
            }
            return bytes;
        }
    }
}
