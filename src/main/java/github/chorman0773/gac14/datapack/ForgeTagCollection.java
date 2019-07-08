package github.chorman0773.gac14.datapack;


import java.util.Optional;

import net.minecraft.tags.TagCollection;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ForgeTagCollection<V extends IForgeRegistryEntry<V>> extends TagCollection<V>{

	public ForgeTagCollection(IForgeRegistry<V> registry, String resourceLocationPrefixIn,
			boolean preserveOrderIn, String itemTypeNameIn) {
		super(r->Optional.ofNullable(registry.getValue(r)), resourceLocationPrefixIn, preserveOrderIn, itemTypeNameIn);
		// TODO Auto-generated constructor stub
	}
	
}