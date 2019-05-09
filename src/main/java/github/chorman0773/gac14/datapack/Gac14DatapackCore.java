package github.chorman0773.gac14.datapack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("gac14-datapack-core")
public class Gac14DatapackCore
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Gac14DatapackCore() {
        
    	assert instance==null;
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }
    
    
    private static Gac14DatapackCore instance;
    
    public static Gac14DatapackCore getInstance() {
    	assert instance!=null;
    	return instance;
    }
     
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        
    }
}
