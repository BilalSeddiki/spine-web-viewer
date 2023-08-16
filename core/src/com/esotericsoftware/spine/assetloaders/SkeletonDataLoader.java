package com.esotericsoftware.spine.assetloaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;

/** {@link AssetLoader} for {@link SkeletonData} instances. 
 * Loads an exported Spine's skeleton data.
 * The atlas with the images will be loaded as a dependency. This has to be declared as a {@link SkeletonDataLoaderParameter}
 * in the  {@link AssetManager#load(String, Class, AssetLoaderParameters)} call.
 * Supports both binary and JSON skeleton format files. If the animation file name has a 'skel' extension,
 * it will be loaded as binary. Any other extension will be assumed as JSON.
 *
 * Example: suppose you have 'data/spine/character.atlas', 'data/spine/character.png' and 'data/spine/character.skel'.
 * To load it with an asset manager, just do the following:
 * * <pre>
* {@code
* assetManager.setLoader(SkeletonData.class, new SkeletonDataLoader(new InternalFileHandleResolver()));
* SkeletonDataLoaderParameter parameter = new SkeletonDataLoaderParameter("data/spine/character.atlas");
* assetManager.load("data/spine/character.skel", SkeletonData.class, parameter);
* }
* </pre>
 * @author Alvaro Barbeira */
public class SkeletonDataLoader extends AsynchronousAssetLoader<SkeletonData, SkeletonDataLoader.SkeletonDataLoaderParameter> {

	SkeletonData skeletonData;
    FileHandle textureAtlasFile;

	public SkeletonDataLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkeletonDataLoaderParameter parameter) {
		skeletonData = null;
		TextureAtlas atlas = manager.get(textureAtlasFile.path(), TextureAtlas.class);
        
		SkeletonBinary skeletonBinary = new SkeletonBinary(atlas);
		skeletonData = skeletonBinary.readSkeletonData(file);
	}

	@Override
	public SkeletonData loadSync(AssetManager manager, String fileName, FileHandle file, SkeletonDataLoaderParameter parameter) {
		return skeletonData;
	}

	@Override @SuppressWarnings("rawtypes")
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkeletonDataLoaderParameter parameter) {
        textureAtlasFile = file.parent().list(".atlas").length > 1 ? file.parent().child(file.nameWithoutExtension() + ".atlas") : file.parent().list(".atlas")[0];
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
        deps.add(new AssetDescriptor<>(textureAtlasFile, TextureAtlas.class));
		return deps;
	}

	/** 
	 * Mandatory parameter to be passed to {@link AssetManager#load(String, Class, AssetLoaderParameters)}.
	 * This will insure the skeleton data is loaded correctly
     * @author Alvaro Barbeira */
    static public class SkeletonDataLoaderParameter extends AssetLoaderParameters<SkeletonData> {
        
        float scale = 1f;

        public SkeletonDataLoaderParameter() {
        }
    }
}