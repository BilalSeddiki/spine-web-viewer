package com.spine.viewer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.esotericsoftware.spine.AnimationState.TrackEntry;

public class SkeletonViewer extends ApplicationAdapter {

    private Stage stage;
	private PolygonSpriteBatch batch;
	private SkeletonRenderer renderer;
	private SkeletonRendererDebug debugRenderer;
    private OrthographicCamera camera;
    private CanvasSettings canvasSettings;
    private Array<SkeletonSettings> loadedSkeletons;
    private int selectedSkeletonIndex = -1;

	@Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport(camera));
		batch = new PolygonSpriteBatch();
		renderer = new SkeletonRenderer();
		debugRenderer = new SkeletonRendererDebug();
        loadedSkeletons = new Array<>();
        setBackgroundColor(new Color(34f / 255f, 34f / 255f, 34f / 255f, 1f));
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new GestureDetector(new SkeletonGestureAdapter()), new SkeletonInputAdapter()));
        setCameraPositionX(0f);
        setCameraPositionY(0f);
    }

    @Override
    public void dispose() {
	}

    @Override
    public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float delta = Gdx.graphics.getDeltaTime();
    
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        debugRenderer.getShapeRenderer().setProjectionMatrix(camera.combined);
        
        for (SkeletonSettings skeletonSetting : loadedSkeletons) {
            SkeletonObject skeletonObject = skeletonSetting.getSkeletonObject();

            Skeleton skeleton = skeletonObject.getSkeleton();
            AnimationState state = skeletonObject.getState();
            renderer.setPremultipliedAlpha(skeletonObject.getPremultiplied());

            delta = Math.min(delta, 0.032f) * skeletonObject.getSpeed();
            skeleton.update(delta);
            if (skeletonObject.isPlaying()) {
                state.update(delta);
                state.apply(skeleton);
            }
            skeleton.updateWorldTransform();

            batch.begin();
            renderer.draw(batch, skeleton);
            batch.end();

            debugRenderer.setBones(skeletonObject.getBones());
            debugRenderer.setRegionAttachments(skeletonObject.getRegions());
            debugRenderer.setBoundingBoxes(skeletonObject.getBounds());
            debugRenderer.setMeshHull(skeletonObject.getMeshHull());
            debugRenderer.setMeshTriangles(skeletonObject.getMeshTriangles());
            debugRenderer.draw(skeleton);
            
            stage.act();
            stage.draw();

            // Draw indicator for timeline position.
            if (skeletonObject.isPlaying() && state != null) {
                TrackEntry entry = state.getCurrent(0);
                if (entry != null) {
                    float progress = (entry.getTime() % entry.getEndTime()) / entry.getEndTime();
                    if (Float.isFinite(progress)) {
                        skeletonSetting.setProgress(progress);
                    }
                }
            }
        }
    }

    public void loadAsset(Asset asset, SkeletonSettings settings) {
        SkeletonObject skeletonObject = new SkeletonObject(asset);
        settings.setSkeletonObject(skeletonObject);
        loadedSkeletons.add(settings);
        settings.loadSkins(skeletonObject.getSkeleton().getData().getSkins());
        settings.loadAnimations(skeletonObject.getAnimations());
        settings.loadSlots(skeletonObject.getSkeleton().getSlots());
    }

    public void toggleViewportChange() {
        stage.setViewport(new FitViewport(loadedSkeletons.get(0).getSkeletonObject().getSkeleton().getData().getWidth(), loadedSkeletons.get(0).getSkeletonObject().getSkeleton().getData().getHeight()));
    }

    public void setBackgroundColor(Color backgroundColor) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
    }

    public void setCameraPositionX(float value) {
        camera.position.x = value;
        canvasSettings.setCameraPosition(value, camera.position.y);
    }

    public void setCameraPositionY(float value) {
        camera.position.y = value;
        canvasSettings.setCameraPosition(camera.position.x, value);
    }

    public void setCameraRotation(float angle) {
        camera.up.set(0, 1, 0);
        camera.rotate(angle);
    }

    public void setCameraZoom(float zoomValue) {
        camera.zoom = zoomValue >= 0.1f ? zoomValue : 0.1f;
        canvasSettings.setCameraZoom(camera.zoom);
    }

    public void setCanvasSettings(CanvasSettings canvasSettings) {
        this.canvasSettings = canvasSettings;
    }

    public void toggleFullscreenMode() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }

    public void translateCameraZoom(float zoomValue) {
        setCameraZoom(camera.zoom + zoomValue);
    }

    public void setSkeletonPosition(SkeletonSettings skeletonSettings, int index) {
        if (index < 0 || index >= loadedSkeletons.size) return;
        loadedSkeletons.removeValue(skeletonSettings, true);
        loadedSkeletons.insert(index, skeletonSettings);
    }

    public void translateSkeletonPosition(SkeletonSettings skeletonSettings, int delta) {
        int index = loadedSkeletons.indexOf(skeletonSettings, true) + delta;
        setSkeletonPosition(skeletonSettings, index);
    }

    public void removeSkeleton(SkeletonSettings skeletonSettings) {
        skeletonSettings.getSkeletonObject().dispose();
        loadedSkeletons.removeValue(skeletonSettings, true);
    }

    private class SkeletonGestureAdapter extends GestureAdapter {

        @Override
        public boolean tap(float x, float y, int count, int button) {
            Gdx.app.log("tap", "x: " + x + " y: " + y + " count: " + count + " button: " + button);
            return true;
        }

        @Override
        public boolean longPress(float x, float y) {
            Vector3 coordinates = new Vector3(x, y, 0);
            camera.unproject(coordinates);
            for (int i = loadedSkeletons.size - 1; i >= 0; i--) {
                SkeletonSettings skeletonSetting = loadedSkeletons.get(i);
                SkeletonObject skeletonObject = skeletonSetting.getSkeletonObject();
                if (!skeletonObject.isClicked(coordinates)) continue;
                selectedSkeletonIndex = i;
                return true;
            }
            return false;
        }
    
	    @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            camera.translate(-deltaX * camera.zoom, deltaY * camera.zoom);
            canvasSettings.setCameraPosition(camera.position.x, camera.position.y);
            return true;
        }
    
        @Override
        public boolean zoom(float initialDistance, float distance) {
            float factor = distance - initialDistance;
            translateCameraZoom(factor > 0 ? -0.01f : 0.01f);
            return true;
        }
    }

    private class SkeletonInputAdapter extends InputAdapter {

        private int previousX = 0;
        private int previousY = 0;
        
        @Override
        public boolean scrolled(float amountX, float amountY) {
            if (!Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT))
                return false;
            translateCameraZoom(amountY / 10f);
            return true;
        }

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (selectedSkeletonIndex < 0) {
                return false;
            }
            int deltaX = (int) ((screenX - previousX) * camera.zoom);
            int deltaY = -(screenY - previousY);
            loadedSkeletons.get(selectedSkeletonIndex).getSkeletonObject().translatePosition(deltaX, deltaY);
            
            previousX = screenX;
            previousY = screenY;
            return true;
		}

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            previousX = screenX;
            previousY = screenY;
            return true;
        }
    }
}
