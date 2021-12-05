package org.bitbucket._newage.commandhook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bitbucket._newage.commandhook.versions.VersionMapping;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Entity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class RefUtil {

    private static final Pattern radius = Pattern.compile("r=[0-9]*");
    private static final Pattern level = Pattern.compile("l=[0-9]*");
    private static final Pattern levelMore = Pattern.compile("lm=[0-9]*");

    public static String NMS_VERSION;

    private Class<?> argumentParser, blockPosition, craftWorld, worldServer, entity, tileEntityCommand, commandBlockListenerAbstract, commandListenerWrapper, stringReader, entitySelector;
    private Constructor c_argumentParser, c_stringReader, c_blockPosition;
    private Method a_parser, b_parser, b_selector, i_method, getTileEntityAt, getCommandBlock, getWrapper;
    private Field entityUUID, world;

    private Logger logger = LoggerFactory.getLogger(RefUtil.class);

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

            getTileEntityAt = mapping.worldServer__getTileEntity();
            getWrapper = mapping.commandBlockListenerAbstract_getWrapper();
            getCommandBlock = mapping.tileEntityCommand_getCommandBlock();

            entityUUID = mapping.getEntityUUID();
            world = craftWorld.getDeclaredField("world");

            entityUUID.setAccessible(true);
            world.setAccessible(true);
            b_parser.setAccessible(true);
            i_method.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException | NoSuchFieldException ex) {
            logger.error("Error in reflection mapping", ex);
        }
    }
    
    public List<Entity> parse(String arg, Block block) {
        return reflectedParse(arg, block);
    }
    @SuppressWarnings("unused")
    private List<Entity> cachedParse(Object selectorInstance, Object wrapper) {
        try {
            return ((List<?>)b_selector.invoke(selectorInstance, wrapper)).stream().map(e -> Bukkit.getEntity(getEntityUUID(e))).collect(Collectors.toList());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error("Error parsing selector in CommandBlock", ex);
        }
        return new ArrayList<Entity>();
    }
    
    public List<Entity> reflectedParse(String arg, Block block) {
        List<Entity> entities = new java.util.ArrayList<>();
        Object wrapper = null, parser, selectorInstance;
        try {
            wrapper = getWrapper(block);
            parser = getArgumentParser(getStringReader(arg));
            
            b_parser.invoke(parser, false);
            i_method.invoke(parser);

            selectorInstance = a_parser.invoke(parser);
                
            entities = ((List<?>)b_selector.invoke(selectorInstance, wrapper)).stream()
                    .map(e ->
                            Bukkit.getEntity(getEntityUUID(e))
                    )
                    .collect(Collectors.toList());
            
            return entities;
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            logger.error("Error parsing selector in CommandBlock", ex);
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof CommandSyntaxException) {
                CommandSyntaxException commandSyntaxException = (CommandSyntaxException)ex.getCause();
                logger.warn("CommandBlock at x={} y={} z={} world={} has thrown SyntaxException. Please check the input. Input={} ({})", block.getX(), block.getY(), block.getZ(), block.getWorld().getName(), commandSyntaxException.getInput(), commandSyntaxException.getContext());
                
                if (commandSyntaxException.getMessage().contains("Unknown option 'r'")) {
                    CommandBlock commandBlock = (CommandBlock)block.getState();
                    Matcher radiusMatcher = radius.matcher(commandBlock.getCommand());
                    if (radiusMatcher.find()) {
                        logger.info("Attempting to fix 'r' Command Block... (old syntax)");
                        String radiusGroup = radiusMatcher.group();
                        commandBlock.setCommand(commandBlock.getCommand().replace(radiusGroup, radiusGroup.replace("r=", "distance=..")));
                        commandBlock.update();
                    }
                }
                if (commandSyntaxException.getMessage().contains("Unknown option 'l'")) {
                    CommandBlock commandBlock = (CommandBlock)block.getState();
                    Matcher levelMatcher = level.matcher(commandBlock.getCommand());
                    if (levelMatcher.find()) {
                        logger.info("Attempting to fix 'l' Command Block... (old syntax)");
                        String levelGroup = levelMatcher.group();
                        commandBlock.setCommand(commandBlock.getCommand().replace(levelGroup, levelGroup.replace("l=", "level=..")));
                        commandBlock.update();
                    }
                }
                if (commandSyntaxException.getMessage().contains("Unknown option 'lm'")) {
                    CommandBlock commandBlock = (CommandBlock)block.getState();
                    Matcher levelMoreMatcher = levelMore.matcher(commandBlock.getCommand());
                    if (levelMoreMatcher.find()) {
                        logger.info("Attempting to fix 'lm' Command Block... (old syntax)");
                        String levelMoreGroup = levelMoreMatcher.group();
                        commandBlock.setCommand(commandBlock.getCommand().replace(levelMoreGroup, levelMoreGroup.replace("lm=", "level=")+".."));
                        commandBlock.update();
                    }
                }
            } else {
                logger.error("Error parsing selector in CommandBlock", ex);
            }
        }
        
        return entities;
    }
    
    public UUID getEntityUUID(Object entity) {
        try {
            return (UUID)entityUUID.get(entity);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            logger.error("Error accessing Entity UUID field", ex);
        }
        return null;
    }
    
    public Object getWrapper(Block cmdBlock) {
        try {
            final Object wld = world.get(craftWorld.cast(cmdBlock.getWorld()));
            final Object bPos = c_blockPosition.newInstance(cmdBlock.getX(), cmdBlock.getY(), cmdBlock.getZ());
            
            Object cmdInstance = getTileEntityAt.invoke(wld, /*ARGS*/bPos, true);
            Object cmdBlockListenerInstance = getCommandBlock.invoke(cmdInstance);
            return getWrapper.invoke(cmdBlockListenerInstance);
            
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
            logger.error("Error obtaining CommandBlockWrapper", ex);
        }
        return null;
    }
    
    
    public Object getStringReader(String arg) {
        try {
            return c_stringReader.newInstance(arg.substring(1));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
            logger.error("Error instantiating StringReader", ex);
        }
        return null;
    }
    
    
    public Object getArgumentParser(Object stringReaderInstance) {
        try {
            return c_argumentParser.newInstance(stringReaderInstance);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
            logger.error("Error instantiating ArgumentParser", ex);
        }
        return null;
    }
}
