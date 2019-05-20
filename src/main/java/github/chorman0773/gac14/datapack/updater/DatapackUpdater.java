package github.chorman0773.gac14.datapack.updater;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;

import com.google.gson.JsonObject;

import github.chorman0773.gac14.Gac14Core;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.SelectiveReloadStateHandler;

public class DatapackUpdater implements Runnable {
	private Git git;
	
	public DatapackUpdater(Path gitPath) throws IOException, GitAPIException {
		git = Git.open(gitPath.toFile());
	}
	@Override
	public void run() {
		try {
			git.pull().call();
		} catch (GitAPIException e) {
			
		}
	}

}
