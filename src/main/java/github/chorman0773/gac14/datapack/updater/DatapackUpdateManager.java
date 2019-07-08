package github.chorman0773.gac14.datapack.updater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

import org.eclipse.jgit.api.errors.GitAPIException;

import github.chorman0773.gac14.Gac14Core;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.SelectiveReloadStateHandler;

public class DatapackUpdateManager{
	
	private MinecraftServer server;
	
	private Map<String,DatapackUpdater> updaters  = new TreeMap<>();
	
	public static final Set<IResourceType> resourceTypesToReload = new HashSet<>();
	
	private volatile boolean serverRunning = true;
	
	public DatapackUpdateManager(MinecraftServer server) throws IOException, GitAPIException {
		this.server = server;
		for(ResourcePackInfo info:server.getResourcePacks().getAvailablePacks())
			handleResourcePack(info.getResourcePack());
		Thread t = new Thread(this::doUpdate);
		t.setDaemon(true);
		t.start();
	}
	
	public void stopUpdateManager() {
		serverRunning = false;
	}
	
	private void handleResourcePack(IResourcePack pack) throws IOException, GitAPIException {
		if(pack instanceof ResourcePack) {
			File f = ((ResourcePack)pack).file;
			if(f.isDirectory()) {
				Path p = f.toPath();
				if(Files.exists(p.resolve(".git")))
					updaters.put(pack.getName(), new DatapackUpdater(p));
			}
		}
	}
	
	private Instant nextUpdateTime = Instant.now();
	
	private static final Duration updateDelta = Duration.ofMinutes(1);
	
	private void doUpdate() {
		while(serverRunning) {
			Instant now = Instant.now();
			if(now.isAfter(nextUpdateTime)) {
				server.deferTask(this::update);
				nextUpdateTime = now.plus(updateDelta);
			}
		}
	}
	
	public void update() {
		for(DatapackUpdater updater:updaters.values())
			updater.run();
		SelectiveReloadStateHandler.INSTANCE.beginReload(resourceTypesToReload::contains);
		server.reload();
		SelectiveReloadStateHandler.INSTANCE.endReload();
	}
	
	
}
