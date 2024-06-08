package org.bitbucket._newage.commandhook.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Pre_1_17 implements VersionMapping {

    private Class<?> argumentParser, blockPosition, craftWorld, worldServer, entity, tileEntityCommand, commandBlockListenerAbstract, commandListenerWrapper, stringReader, entitySelector;
    private Method b_selector, b_parser, worldServer_getTileEntity, commandBlockListenerAbstract_getWrapper, tileEntityCommand_getCommandBlock;
    private Field entityUUID;
    private final Logger logger = LoggerFactory.getLogger(Pre_1_17.class);
    private final String version;

    public Pre_1_17(String version) {
        this.version = version;
        try {
            argumentParser = Class.forName("net.minecraft.server."+ version+".ArgumentParserSelector");
            stringReader = Class.forName("com.mojang.brigadier.StringReader");
            blockPosition = Class.forName("net.minecraft.server."+ version+".BlockPosition");
            craftWorld = Class.forName("org.bukkit.craftbukkit."+ version+".CraftWorld");
            entity = Class.forName("net.minecraft.server."+ version+".Entity");
            tileEntityCommand = Class.forName("net.minecraft.server."+ version+".TileEntityCommand");
            commandBlockListenerAbstract = Class.forName("net.minecraft.server."+ version+".CommandBlockListenerAbstract");
            commandListenerWrapper = Class.forName("net.minecraft.server."+ version+".CommandListenerWrapper");
            entitySelector = Class.forName("net.minecraft.server."+ version+".EntitySelector");
            worldServer = Class.forName("net.minecraft.server."+ version+".WorldServer");

            b_parser = argumentParser.getDeclaredMethod("parseSelector", boolean.class);
            b_selector = entitySelector.getDeclaredMethod("getEntities", commandListenerWrapper);

            entityUUID = entity.getDeclaredField("uniqueID");

            worldServer_getTileEntity = worldServer.getMethod("getTileEntity", blockPosition, boolean.class);
            commandBlockListenerAbstract_getWrapper = commandBlockListenerAbstract.getMethod("getWrapper");
            tileEntityCommand_getCommandBlock = tileEntityCommand.getMethod("getCommandBlock");
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException ex) {
            logger.error("Error preparing reflection mapping for version {}", version, ex);
        }
    }

    @Override
    public Class<?> getArgumentParser() {
        return argumentParser;
    }

    @Override
    public Class<?> getBlockPosition() {
        return blockPosition;
    }

    @Override
    public Class<?> getStringReader() {
        return stringReader;
    }

    @Override
    public Class<?> getCraftWorld() {
        return craftWorld;
    }

    @Override
    public Class<?> getEntity() {
        return entity;
    }

    @Override
    public Class<?> getTileEntityCommand() {
        return tileEntityCommand;
    }

    @Override
    public Class<?> getCommandBlockListenerAbstract() {
        return commandBlockListenerAbstract;
    }

    @Override
    public Class<?> getCommandListenerWrapper() {
        return commandListenerWrapper;
    }

    @Override
    public Class<?> getEntitySelector() {
        return entitySelector;
    }

    @Override
    public Class<?> getWorldServer() {
        return worldServer;
    }

    @Override
    public Method getParseSelector() {
        return b_parser;
    }

    @Override
    public Method getEntities() {
        return b_selector;
    }

    @Override
    public Field getEntityUUID() {
        return entityUUID;
    }

    @Override
    public Method worldServer__getTileEntity() {
        return worldServer_getTileEntity;
    }

    @Override
    public Method commandBlockListenerAbstract_getWrapper() {
        return commandBlockListenerAbstract_getWrapper;
    }

    @Override
    public Method tileEntityCommand_getCommandBlock() {
        return tileEntityCommand_getCommandBlock;
    }

    @Override
    public String getVersion() {
        return version;
    }
}

