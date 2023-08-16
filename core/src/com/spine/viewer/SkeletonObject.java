package com.spine.viewer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Animation.EventTimeline;
import com.esotericsoftware.spine.Animation.Timeline;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.attachments.Attachment;

public class SkeletonObject {

    private Skeleton skeleton;
    private AnimationState state;
    private final SkeletonBinary skeletonBinary;
    private final ObjectMap<String, Attachment> slotsAttachments = new ObjectMap<>();
    private final ObjectMap<Sound, Long> playingSounds = new ObjectMap<>();
    private final ObjectMap<Event, Sound> eventSounds = new ObjectMap<>();

    private float speed = 1f;
    private float volume = 1f;
    private boolean playing = true;
    private boolean loop = true;
    private boolean premultiplied = true;
    private boolean bones;
    private boolean regions;
    private boolean bounds;
    private boolean meshHull;
    private boolean meshTriangles;
    private boolean game;

    public SkeletonObject(Asset asset) {
        TextureAtlas atlas = new TextureAtlas(asset.getAtlasFile());
        skeletonBinary = new SkeletonBinary(atlas);
        SkeletonData skeletonData = skeletonBinary.readSkeletonData(asset.getSkelFile());
		skeleton = new Skeleton(skeletonData);
		skeleton.setToSetupPose();
		skeleton.updateWorldTransform();
        for (Animation animation : skeletonData.getAnimations()) {
            Array<Timeline> timelines = animation.getTimelines();
            for (Timeline timeline : timelines) {
                if (timeline instanceof EventTimeline) {
                    EventTimeline eventTimeline = (EventTimeline) timeline;
                    for (Event event : eventTimeline.getEvents()) {
                        if (event != null) {
                            String eventFilename = eventToFilename(event.getString());
                            for (FileHandle soundFile : asset.getOtherFiles()) {
                                if (soundFile.path().equals(eventFilename)) {
                                    eventSounds.put(event, Gdx.audio.newSound(soundFile));
                                }
                            }
                        }
                    }
                }
            }
        }
        // Event
        AnimationStateData stateData = new AnimationStateData(skeletonData);
		state = new AnimationState(stateData);
        state.addListener(new QtAnimationStateAdapter());
    }

    private String eventToFilename(String eventName) {
        String filename = eventName.replace("\n", "");
        StringBuilder sfx = new StringBuilder("sfx/");
        sfx.append(filename);
        if (!filename.endsWith(".mp3")) sfx.append(".mp3");
        return sfx.toString();
    }

    private void stopSounds() {
        for (Entry<Sound, Long> entry : playingSounds.entries()) {
            Sound sound = entry.key;
            long soundId = entry.value;
            sound.stop(soundId);
            sound.dispose();
            Gdx.app.log("stopSounds()", "Sound " + soundId + " has been stopped.");
        }
        playingSounds.clear();
    }

    public void setSkin(String skinName) {
        skeleton.setSkin(skinName);
        skeleton.setSlotsToSetupPose();
    }

    /**
    Only play background animations if they're different than the one already set, or if the same main animation is being replayed. */
    private void setAnimation(String animationName, boolean loop) {
        state.setAnimation(0, animationName, loop);
    }

    public void setAnimation(String animationName) {
        setAnimation(animationName, loop);
    }

    public void setSlotVisibility(String slotName, boolean value) {
        Slot slot = skeleton.findSlot(slotName);
        if (slot == null) {
            return;
        }
        if (value) { // add slot
            slot.setAttachment(slotsAttachments.get(slotName));
            slotsAttachments.remove(slotName);
        }
        else { // remove slot
            if (!slotsAttachments.containsKey(slotName)) {
                slotsAttachments.put(slotName, slot.getAttachment()); // 1st remove
            }
            slot.setAttachment(null);
        }
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public AnimationState getState() {
        return state;
    }

    public Array<Animation> getAnimations() {
        return skeleton.getData().getAnimations();
    }

    public void setPosition(float x, float y) {
        skeleton.setPosition(x, y);
    }

    public void translatePosition(float deltaX, float deltaY) {
        setPosition(skeleton.getX() + deltaX, skeleton.getY() + deltaY);
    }

    public void setPositionX(float x) {
        skeleton.x = x;
    }

    public void setPositionY(float y) {
        skeleton.y = y;
    }

    public void setScale(float scale) {
        throw new UnsupportedOperationException("setScale() is not supported yet.");
    }

    public void setRotation(float rotation) {
        skeleton.getRootBone().setRotation(rotation);
        skeleton.updateWorldTransform();
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void translateSpeed(float speed) {
        this.speed += speed;
    }

    public void setMix(float mix) {
        state.getData().setDefaultMix(mix);
    }

    public void setVolume(float volume) {
        this.volume = volume;
        for (Entry<Sound, Long> entry : playingSounds.entries()) {
            Sound sound = entry.key;
            long id = entry.value;
            sound.setVolume(id, volume);
            Gdx.app.log("setVolume()", "Setting sound " + id + " volume to " + volume);
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    private void setPlaying(boolean playing) {
        this.playing = playing;
        for (Entry<Sound, Long> entry : playingSounds.entries()) {
            Sound sound = entry.key;
            Long id = entry.value;
            if (!playing)
                sound.pause(id);
            else
                sound.resume(id);
        }
    }

    public void togglePlaying() {
        setPlaying(!playing);
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        Array<TrackEntry> tracks = state.getTracks();
        if (tracks == null || tracks.size == 0) return;

        for (TrackEntry track : tracks) {
            if (track == null) continue;
            track.setLoop(loop);
        }
    }

    public void setFlipX(boolean flipX) {
        skeleton.setFlipX(flipX);
        skeleton.updateWorldTransform();
    }

    public void setFlipY(boolean flipY) {
        skeleton.setFlipY(flipY);
        skeleton.updateWorldTransform();
    }
    
    public void setBones() {
        skeleton.setBonesToSetupPose();
    }

    public void setSlots() {
        skeleton.setSlotsToSetupPose();
    }

    public boolean getPremultiplied() {
        return premultiplied;
    }

    public void setPremultiplied(boolean premultiplied) {
        this.premultiplied = premultiplied;
    }

    public boolean getBones() {
        return bones;
    }

    public void setBones(boolean bones) {
        this.bones = bones;
    }

    public boolean getRegions() {
        return regions;
    }

    public void setRegions(boolean regions) {
        this.regions = regions;
    }

    public boolean getBounds() {
        return bounds;
    }

    public void setBounds(boolean bounds) {
        this.bounds = bounds;
    }

    public boolean getMeshHull() {
        return meshHull;
    }

    public void setMeshHull(boolean meshHull) {
        this.meshHull = meshHull;
    }

    public boolean getMeshTriangles() {
        return meshTriangles;
    }

    public void setMeshTriangles(boolean meshTriangles) {
        this.meshTriangles = meshTriangles;
    }

    public boolean getGame() {
        return game;
    }

    private void setGame(boolean game) {
        this.game = game;
    }

    public void toggleGame() {
        setGame(!game);
    }

    public boolean isClicked(Vector3 coordinates) {
        float width = skeleton.getData().getWidth() / 2;
        float height = skeleton.getData().getHeight() / 2;
        return coordinates.x > skeleton.getX() - width && coordinates.x < skeleton.getX() + width && coordinates.y > skeleton.getY() - height && coordinates.y < skeleton.getY() + height;
    }

    public void dispose() {
        stopSounds();
    }

    private class QtAnimationStateAdapter extends AnimationStateAdapter {

        @Override
        public void event(int trackIndex, Event event) {
            if (event.getString().isEmpty()) return;
            Sound eventSound = eventSounds.get(event);
            if (eventSound != null) {
                playingSounds.put(eventSound, eventSound.play(volume));
            }
        }
    }
}
