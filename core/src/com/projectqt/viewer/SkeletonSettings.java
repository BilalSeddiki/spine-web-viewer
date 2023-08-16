package com.projectqt.viewer;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.Slot;

public interface SkeletonSettings {

    public SkeletonObject getSkeletonObject();

    public void setSkeletonObject(SkeletonObject skeletonObject);

    public void loadSkins(Array<Skin> skins);

    public void loadAnimations(Array<Animation> animations);

    public void loadSlots(Array<Slot> slots);

    public void setProgress(float progress);
}