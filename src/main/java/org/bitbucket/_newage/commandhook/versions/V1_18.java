package org.bitbucket._newage.commandhook.versions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.bitbucket._newage.commandhook.RefUtil.NMS_VERSION;

public class V1_18 implements VersionMapping {

    private Class<?> argumentParser, blockPosition, craftWorld, worldServer, entity, tileEntityCommand, commandBlockListenerAbstract, commandListenerWrapper, stringReader, entitySelector;
    private Method b_selector, b_parser, worldServer_getTileEntity, commandBlockListenerAbstract_createCommandSourceStack, tileEntityCommand_getCommandBlock;
    private Field entityUUID;

    public V1_18(String version) {
        NMS_VERSION = version;
        try {
            argumentParser = Class.forName("net.minecraft.commands.arguments.selector.ArgumentParserSelector");
            stringReader = Class.forName("com.mojang.brigadier.StringReader");
            blockPosition = Class.forName("net.minecraft.core.BlockPosition");
            craftWorld = Class.forName("org.bukkit.craftbukkit."+NMS_VERSION+".CraftWorld");
            entity = Class.forName("net.minecraft.world.entity.Entity");
            tileEntityCommand = Class.forName("net.minecraft.world.level.block.entity.TileEntityCommand");
            commandBlockListenerAbstract = Class.forName("net.minecraft.world.level.CommandBlockListenerAbstract");
            commandListenerWrapper = Class.forName("net.minecraft.commands.CommandListenerWrapper");
            entitySelector = Class.forName("net.minecraft.commands.arguments.selector.EntitySelector");
            worldServer = Class.forName("net.minecraft.server.level.WorldServer");

            b_parser = argumentParser.getDeclaredMethod("parseSelector", boolean.class);
            b_selector = entitySelector.getDeclaredMethod("b", commandListenerWrapper);

            entityUUID = entity.getDeclaredField("ak");

            worldServer_getTileEntity = worldServer.getMethod("getBlockEntity", blockPosition, boolean.class);
            commandBlockListenerAbstract_createCommandSourceStack = commandBlockListenerAbstract.getMethod("i");
            tileEntityCommand_getCommandBlock = tileEntityCommand.getMethod("c");
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
        return commandBlockListenerAbstract_createCommandSourceStack;
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
