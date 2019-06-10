package github.chorman0773.gac14.datapack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import com.google.common.base.Function;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

public abstract class DatapackLoader<E> implements ISelectiveResourceReloadListener {
	
	private static final JsonParser jsonparser = new JsonParser();
	
	private final BiConsumer<? super ResourceLocation,? super E> registerFn;
	private final String resPrefix;
	private final Function<? super JsonObject,? extends E> parser;
	private final IResourceType type;
	
	public DatapackLoader(String resPrefix,Function<? super JsonObject,? extends E> parserFn,IResourceType type,BiConsumer<? super ResourceLocation,? super E> registerFn) {
		this.resPrefix = resPrefix;
		this.parser = parserFn;
		this.type = type;
		this.registerFn = registerFn;
	}
	

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		if(!resourcePredicate.test(type))
			return;
		try {
			for(ResourceLocation loc:resourceManager.getAllResourceLocations(resPrefix, r->r.endsWith(".json"))) {
				String path = loc.getPath();
				path = path.substring(resPrefix.length()+1,path.length()-5);
				ResourceLocation key = new ResourceLocation(loc.getNamespace(),path);
				IResource res = resourceManager.getResource(loc);
				InputStream in = res.getInputStream();
				JsonElement e = jsonparser.parse(new InputStreamReader(in));
				E object = parser.apply(e.getAsJsonObject());
				registerFn.accept(key, object);
			}
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

}
