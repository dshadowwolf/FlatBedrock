package com.bigxplosion.flatbedrock.world.retrogen;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RetroActiveWorldGenerator {

	private static ArrayList<IRetroGenerator> generators = Lists.newArrayList();
	private static ArrayList<RetroGenEntry> genQueue = Lists.newArrayList();

	public static final String retroGenSaveDataName = "fb_retrogen_data";

	public static void registerRetroGenerator(IRetroGenerator generator)
	{
		generators.add(generator);
	}

	private RetroGenSaveData getRetroGenSaveData(World world)
	{
		RetroGenSaveData data = (RetroGenSaveData) world.getPerWorldStorage().loadData(RetroGenSaveData.class, retroGenSaveDataName);

		if (data == null)
		{
			data = new RetroGenSaveData(retroGenSaveDataName);
			world.getPerWorldStorage().setData(retroGenSaveDataName, data);
		}

		return data;
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load event)
	{
		RetroGenSaveData data = getRetroGenSaveData(event.world);
		ChunkCoord coord = new ChunkCoord(event.getChunk());
		World world = event.world;
		Chunk chunk = event.getChunk();

		for (IRetroGenerator gen : generators)
			if (gen.canGenerateIn(world, chunk) && data.isGenerationNeeded(coord, gen.getUniqueGenerationID()))
			{
				genQueue.add(new RetroGenEntry(world, coord, gen));
				data.markChunkRetroGenerated(coord, gen.getUniqueGenerationID());
			}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if (genQueue.isEmpty())
			return;

		if (event.phase == TickEvent.Phase.START)
			return;

		int count = 0;
		ArrayList<RetroGenEntry> removeQueue = Lists.newArrayList();
		ArrayList<RetroGenEntry> iterationQueue = (ArrayList<RetroGenEntry>) genQueue.clone();

		for (RetroGenEntry entry : iterationQueue)
		{
			entry.gen.generate(entry.world.rand, entry.world, entry.coord.chunkX, entry.coord.chunkZ);
			removeQueue.add(entry);
			count++;

			if (count >= 32)
				break;
		}

		genQueue.removeAll(removeQueue);
	}

	public class RetroGenEntry
	{
		World world;
		ChunkCoord coord;
		IRetroGenerator gen;

		public RetroGenEntry(World world, ChunkCoord coord, IRetroGenerator gen)
		{
			this.world = world;
			this.coord = coord;
			this.gen = gen;
		}
	}
}
