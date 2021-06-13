package org.bitbucket._newage.commandhook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bitbucket._newage.commandhook.versions.VersionMapping;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Entity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@SuppressWarnings("rawtypes")
public class RefUtil {

	public static String NMS_VERSION;
	private static Class<?> argumentParser, blockPosition, craftWorld, worldServer, entity, tileEntityCommand, commandBlockListenerAbstract, commandListenerWrapper, stringReader, entitySelector;
	private static Constructor c_argumentParser, c_stringReader, c_blockPosition;
	private static Method a_parser, b_parser, b_selector, i_method, getTileEntityAt, getCommandBlock, getWrapper;
	private static Field entityUUID, world;
	
	private static final java.util.regex.Pattern radius = java.util.regex.Pattern.compile("r=[0-9]*");
	private static final java.util.regex.Pattern level = java.util.regex.Pattern.compile("l=[0-9]*");
	private static final java.util.regex.Pattern levelMore = java.util.regex.Pattern.compile("lm=[0-9]*");

	public RefUtil(VersionMapping mapping) {
		try {
			argumentParser = mapping.getArgumentParser();
			stringReader = mapping.getStringReader();
			blockPosition = mapping.getBlockPosition();
			craftWorld = mapping.getCraftWorld();
			entity = mapping.getEntity();
			tileEntityCommand = mapping.getTileEntityCommand();
			commandBlockListenerAbstract = mapping.getCommandBlockListenerAbstract();
			commandListenerWrapper = mapping.getCommandListenerWrapper();
			entitySelector = mapping.getEntitySelector();
			worldServer = mapping.getWorldServer();

			c_argumentParser = argumentParser.getConstructor(stringReader);
			c_stringReader = stringReader.getConstructor(String.class);
			c_blockPosition = blockPosition.getConstructor(int.class, int.class, int.class);

			a_parser = argumentParser.getMethod("a");
			b_parser = mapping.getParseSelector();


			i_method = argumentParser.getDeclaredMethod("I");

			b_selector = mapping.getEntities();

			getTileEntityAt = worldServer.getMethod("getTileEntity", blockPosition);
			getWrapper = commandBlockListenerAbstract.getMethod("getWrapper");
			getCommandBlock = tileEntityCommand.getMethod("getCommandBlock");

			entityUUID = mapping.getEntityUUID();
			world = craftWorld.getDeclaredField("world");

			entityUUID.setAccessible(true);
			world.setAccessible(true);
			b_parser.setAccessible(true);
			i_method.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException | NoSuchFieldException e) {
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
	
	/*
	public List<Entity> nmsParse(String arg, Block block) {
		List<Entity> entities = new ArrayList<>();
		
		CommandListenerWrapper clw = (CommandListenerWrapper)getWrapper(block);
		ArgumentParserSelector aps = (ArgumentParserSelector)getArgumentParser(getStringReader(arg));
		
		try {
			aps.parseSelector(false);
			aps.I();
			EntitySelector es = aps.a();
			//EntitySelector es = aps.parse(false);
			List<?> ent = es.getEntities(clw);
			System.out.println(ent.toString());
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		
		return entities;
	}
	*/
	
	public List<Entity> reflectedParse(String arg, Block block) {
		List<Entity> entities = new java.util.ArrayList<>();
		Object wrapper = null, parser, selectorInstance;
		try {
			wrapper = getWrapper(block);
			parser = getArgumentParser(getStringReader(arg));
			
			b_parser.invoke(parser, false);
			i_method.invoke(parser);

			selectorInstance = a_parser.invoke(parser);
			/*  DEBUG
			List<?> ent = ((List<?>)b_selector.invoke(selectorInstance, wrapper));
			if(ent.isEmpty())
				System.out.println("Empty");
			else
				System.out.println(ent.toString());
			*/
				
			entities = ((List<?>)b_selector.invoke(selectorInstance, wrapper)).stream().map(e -> Bukkit.getEntity(getEntityUUID(e))).collect(Collectors.toList());
			
			
			//cache.get(block.getWorld()).put(block.getLocation(), new Object[] { selectorInstance, wrapper });
			
			return entities;//(List<?>)b_selector.invoke(selectorInstance, wrapper);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if(e.getCause() instanceof CommandSyntaxException) {
				CommandSyntaxException ex = (CommandSyntaxException)e.getCause();
				System.out.println(String.format("CommandBlock at %s %s %s has thrown SyntaxException. Please check the input. (%s)\n", block.getX(), block.getY(), block.getZ(), ex.getInput()+" ("+ex.getContext()+")"));

				if(ex.getMessage().contains("Unknown option 'r'")) {
					CommandBlock cb = (CommandBlock)block.getState();
					java.util.regex.Matcher m = radius.matcher(cb.getCommand());
					if(m.find()) {
						System.out.println("Attempting to fix 'r' Command Block... (old syntax)");
						String g = m.group();
						cb.setCommand(cb.getCommand().replace(g, g.replace("r=", "distance=..")));
						cb.update();
						/*if(cb.update()) {
							//calling method again results in same error
							wrapper = getWrapper(cb.getBlock());
							parser = getArgumentParser(getStringReader(arg.replace(g, "distance=..")));
							try {
								b_parser.invoke(parser);
								selectorInstance = a_parser.invoke(parser);
								
								entities = ((List<?>)b_selector.invoke(selectorInstance, wrapper)).stream().map(en -> Bukkit.getEntity(getEntityUUID(e))).collect(Collectors.toList());
								return entities;
							} catch (Exception exc) {
								//Silence!
							}
							
						}*/
					}
				}
				if(ex.getMessage().contains("Unknown option 'l'")) {
					CommandBlock cb = (CommandBlock)block.getState();
					java.util.regex.Matcher m = level.matcher(cb.getCommand());
					if(m.find()) {
						System.out.println("Attempting to fix 'l' Command Block... (old syntax)");
						String g = m.group();
						cb.setCommand(cb.getCommand().replace(g, g.replace("l=", "level=..")));
						cb.update();
					}
				}
				if(ex.getMessage().contains("Unknown option 'lm'")) {
					CommandBlock cb = (CommandBlock)block.getState();
					java.util.regex.Matcher m = levelMore.matcher(cb.getCommand());
					if(m.find()) {
						System.out.println("Attempting to fix 'lm' Command Block... (old syntax)");
						String g = m.group();
						cb.setCommand(cb.getCommand().replace(g, g.replace("lm=", "level=")+".."));
						cb.update();
					}
				}
				
				
			} else {
				e.printStackTrace();
			}
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
