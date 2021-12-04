package org.bitbucket._newage.commandhook.versions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.bitbucket._newage.commandhook.RefUtil.NMS_VERSION;

public class Pre_1_17 implements VersionMapping {

    private Class<?> argumentParser, blockPosition, craftWorld, worldServer, entity, tileEntityCommand, commandBlockListenerAbstract, commandListenerWrapper, stringReader, entitySelector;
    private Method b_selector, b_parser, worldServer_getTileEntity, commandBlockListenerAbstract_getWrapper, tileEntityCommand_getCommandBlock;
    private Field entityUUID;

    public Pre_1_17(String version) {
        NMS_VERSION = version;
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

            b_parser = argumentParser.getDeclaredMethod("parseSelector", boolean.class);
            b_selector = entitySelector.getDeclaredMethod("getEntities", commandListenerWrapper);

            entityUUID = entity.getDeclaredField("uniqueID");

            worldServer_getTileEntity = worldServer.getMethod("getTileEntity", blockPosition, boolean.class);
            commandBlockListenerAbstract_getWrapper = commandBlockListenerAbstract.getMethod("getWrapper");
            tileEntityCommand_getCommandBlock = tileEntityCommand.getMethod("getCommandBlock");
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
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
        return NMS_VERSION;
    }
}

