package com.bigxplosion.flatbedrock;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.bigxplosion.flatbedrock.compat.IMCHandler;
import com.bigxplosion.flatbedrock.custom.CustomDimensionManager;
import com.bigxplosion.flatbedrock.lib.Reference;
import com.bigxplosion.flatbedrock.world.WorldGenFlatBedrock;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class FlatBedrock {

	@Mod.Instance(Reference.MOD_ID)
	public static FlatBedrock instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		CustomDimensionManager.execute();

		GameRegistry.registerWorldGenerator(new WorldGenFlatBedrock(), 10);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@Mod.EventHandler
	public void handleIMCMessages(FMLInterModComms.IMCEvent event)
	{
		IMCHandler.processMessages(event.getMessages());
	}
}
