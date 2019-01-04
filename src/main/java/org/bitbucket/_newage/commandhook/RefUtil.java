package org.bitbucket._newage.commandhook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

@SuppressWarnings("rawtypes")
public class RefUtil {

	public static String NMS_VERSION;
	private static Class argumentParser, blockPosition, craftWorld, worldServer, entity, tileEntityCommand, commandBlockListenerAbstract, commandListenerWrapper, stringReader, entitySelector;
	private static Constructor c_argumentParser, c_stringReader, c_blockPosition;
	private static Method a_parser, b_parser, b_selector, getTileEntityAt, getCommandBlock, getWrapper;
	private static Field entityUUID, world;
	
	//private Map<World, Map<Location, Object[]>> cache;
	
	@SuppressWarnings("unchecked")
	public RefUtil(String version) {
		NMS_VERSION = version;
		//cache = new HashMap<>();
		try {
			argumentParser = Class.forName("net.minecraft.server."+NMS_VERSION+".ArgumentParserSelector");
			stringReader = Class.forName("com.mojang.brigadier.StringReader");
			blockPosition = Class.forName("net.minecraft.server."+NMS_VERSION+".BlockPosition");
			craftWorld = Class.forName("org.bukkit.craftbukkit."+NMS_VERSION+".CraftWorld");
			entity = Class.forName("net.minecraft.server."+NMS_VERSION+".Entity");
			tileEntityCommand = Class.forName("net.minecraft.server."+NMS_VERSION+".TileEntityCommand");
			commandBlockListenerAbstract = Class.forName("net.minecraft.server."+NMS_VERSION+".CommandBlockListenerAbstract");
			commandListenerWrapper = Class.forName("net.minecraft.server."+NMS_VERSION+".CommandListenerWrapper");
			entitySelector = Class.forName("net.minecraft.server."+NMS_VERSION+".EntitySelector");
			worldServer = Class.forName("net.minecraft.server."+NMS_VERSION+".WorldServer");
			
			c_argumentParser = argumentParser.getConstructor(stringReader);
			c_stringReader = stringReader.getConstructor(String.class);
			c_blockPosition = blockPosition.getConstructor(int.class, int.class, int.class);
			
			a_parser = argumentParser.getMethod("a");
			b_parser = argumentParser.getDeclaredMethod("b");
			b_selector = entitySelector.getDeclaredMethod("b", commandListenerWrapper);
			getTileEntityAt = worldServer.getMethod("getTileEntity", blockPosition);//craftWorld.getMethod("getTileEntityAt", int.class, int.class, int.class);
			getWrapper = commandBlockListenerAbstract.getMethod("getWrapper");
			getCommandBlock = tileEntityCommand.getMethod("getCommandBlock");
			
			entityUUID = entity.getDeclaredField("uniqueID");
			world = craftWorld.getDeclaredField("world");
			
			entityUUID.setAccessible(true);
			world.setAccessible(true);
			b_parser.setAccessible(true);
			
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	public List<Entity> parse(String arg, Block block) {
		return reflectedParse(arg, block);
		/*
		if(cache.containsKey(block.getWorld())) {
			if(cache.get(block.getWorld()).containsKey(block.getLocation())) {
				System.out.println("Fully cached");
				Object[] ws = cache.get(block.getWorld()).get(block.getLocation());
				return cachedParse(ws[0], ws[1]);
			} else {
				System.out.println("Caching cmdblock");
				return reflectedParse(arg, block);
			}
		} else {
			System.out.println("Caching world");
			cache.put(block.getWorld(), new HashMap<Location, Object[]>());
			return reflectedParse(arg, block);
		}
		*/
		
	}
	@SuppressWarnings("unused")
	private List<Entity> cachedParse(Object selectorInstance, Object wrapper) {
		try {
			return ((List<?>)b_selector.invoke(selectorInstance, wrapper)).stream().map(e -> Bukkit.getEntity(getEntityUUID(e))).collect(Collectors.toList());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return new ArrayList<Entity>();
	}
	
	public List<Entity> reflectedParse(String arg, Block block) {
		List<Entity> entities = new java.util.ArrayList<>();
		try {
			Object wrapper = getWrapper(block);
			Object parser = getArgumentParser(getStringReader(arg));
			b_parser.invoke(parser);
			Object selectorInstance = a_parser.invoke(parser);
			
			entities = ((List<?>)b_selector.invoke(selectorInstance, wrapper)).stream().map(e -> Bukkit.getEntity(getEntityUUID(e))).collect(Collectors.toList());
			//cache.get(block.getWorld()).put(block.getLocation(), new Object[] { selectorInstance, wrapper });
			
			return entities;//(List<?>)b_selector.invoke(selectorInstance, wrapper);
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return entities;
	}
	
	public UUID getEntityUUID(Object entity) {
		try {
			return (UUID)entityUUID.get(entity);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getWrapper(Block cmdBlock) {
		try {
			final Object wld = world.get(craftWorld.cast(cmdBlock.getWorld()));
			final Object bPos = c_blockPosition.newInstance(cmdBlock.getX(), cmdBlock.getY(), cmdBlock.getZ());
			
			Object cmdInstance = getTileEntityAt.invoke(wld, /*ARGS*/bPos);
			//Object cmdInstance = getTileEntityAt.invoke(craftWorld.cast(cmdBlock.getWorld()), cmdBlock.getX(), cmdBlock.getY(), cmdBlock.getZ());
			Object cmdBlockListenerInstance = getCommandBlock.invoke(cmdInstance);
			return getWrapper.invoke(cmdBlockListenerInstance);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public Object getStringReader(String arg) {
		try {
			return c_stringReader.newInstance(arg.substring(1));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public Object getArgumentParser(Object stringReaderInstance) {
		try {
			return c_argumentParser.newInstance(stringReaderInstance);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
