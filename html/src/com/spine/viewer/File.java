package com.spine.viewer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.backends.gwt.GwtFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.typedarrays.shared.Int8Array;

public class File extends GwtFileHandle {

    private final String name;
    private final byte[] content;
    private final Array<File> child = new Array<>();
    private File parent;

    public File(String filename, byte[] content) {
        super(filename);
        this.name = filename;
        this.content = content;
    }

    public File(String filename) {
        this(filename, new byte[0]);
    }

    public void addChild(File file) {
        child.add(file);
    }

    public void setParent(File file) {
        parent = file;
    }

    public String base64() {
        Int8Array array = JavaScriptObject.createArray().cast();
        array.set(content);
        return base64Encode(array);
    }

    private static native String base64Encode(Int8Array content) /*-{
        var binary = '';
        for (var i = 0; i < content.length; i++) {
            binary += String.fromCharCode(content[i]);
        }
        return $wnd.btoa(binary);
    }-*/;

    @Override
    public String path() {
        return name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String extension() {
		int dotIndex = name.lastIndexOf('.');
		if (dotIndex == -1) return "";
		return name.substring(dotIndex + 1);
    }

    @Override
    public InputStream read() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public String readString() {
        return new String(content, StandardCharsets.UTF_8);
    }

    @Override
    public FileHandle child(String name) {
        for (File file : child) {
            if (file.name().equals(name)) {
                return file;
            }
        }
        return null;
    }

    @Override
    public FileHandle parent() {
        return parent;
    }
}
