/* package com.esotericsoftware.spine;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class QtSkeletonBinary extends SkeletonBinary {

    final private String defaultSkinName;
    final private String defaultAnimationName;
    final private float defaultMix;

    public QtSkeletonBinary(TextureAtlas atlas, String defaultSkinName, String defaultAnimationName, float defaultMix) {
        super(atlas);
        this.defaultSkinName = defaultSkinName;
        this.defaultAnimationName = defaultAnimationName;
        this.defaultMix = defaultMix;
    }
    
    @Override
    public QtSkeletonData readSkeletonData(FileHandle file) {
        SkeletonData skeletonData = super.readSkeletonData(file);
        QtSkeletonData qtSkeletonData = new QtSkeletonData(skeletonData, this);
        qtSkeletonData.setDefaultAnimation(defaultAnimationName);
        qtSkeletonData.setDefaultAnimation(defaultAnimationName);
        qtSkeletonData.setDefaultAnimation(defaultAnimationName);
        return qtSkeletonData;
    }

    public String getDefaultSkinName() {
        return defaultSkinName;
    }

    public String getDefaultAnimationName() {
        return defaultAnimationName;
    }

    public float getDefaultMix() {
        return defaultMix;
    }
} */