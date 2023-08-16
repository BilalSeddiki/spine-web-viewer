package com.spine.viewer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class Asset {

    private FileHandle skelFile;
    private FileHandle atlasFile;
    private FileHandle textureFile;
    private final Array<FileHandle> otherFiles = new Array<>();

    public Asset(FileHandle... files) {
        for (FileHandle file : files) {
            switch (file.extension()) {
                case "skel":
                    skelFile = file;
                    break;
                case "atlas":
                    atlasFile = file;
                    break;
                case "png":
                    textureFile = file;
                    break;
                default:
                    otherFiles.add(file);
            }
        }
        if (skelFile == null) {
            throw new IllegalArgumentException("No skeleton file found");
        }
        else if (atlasFile == null) {
            throw new IllegalArgumentException("No atlas file found");
        }
        else if (textureFile == null) {
            throw new IllegalArgumentException("No texture file found");
        }
    }

    public FileHandle getSkelFile() {
        return skelFile;
    }

    public FileHandle getAtlasFile() {
        return atlasFile;
    }

    public FileHandle getTextureFile() {
        return textureFile;
    }

    public Array<FileHandle> getOtherFiles() {
        return otherFiles;
    }
}
