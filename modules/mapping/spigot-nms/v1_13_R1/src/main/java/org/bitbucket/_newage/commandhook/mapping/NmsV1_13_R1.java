package org.bitbucket._newage.commandhook.mapping;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_13_R1.CommandListenerWrapper;
import net.minecraft.server.v1_13_R1.ArgumentParserSelector;
import net.minecraft.server.v1_13_R1.EntitySelector;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.CommandBlockListenerAbstract;
import net.minecraft.server.v1_13_R1.World;
import net.minecraft.server.v1_13_R1.TileEntityCommand;
import org.bitbucket._newage.commandhook.mapping.api.AMapping;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NmsV1_13_R1 extends AMapping {

    @Override
    public List<Entity> getEntitiesFromSelector(String selector, Block commandBlock) {
        List<Entity> entities = Collections.emptyList();

        CommandListenerWrapper wrapper = getCommandListenerWrapper(commandBlock);
        ArgumentParserSelector argumentParser = getArgumentParser(selector);
        try {
            List<? extends net.minecraft.server.v1_13_R1.Entity> nmsEntities = getNmsEntities(argumentParser, wrapper);
            entities = convertToBukkitEntity(nmsEntities);
        } catch (CommandSyntaxException ex) {
            logger.error("Error parsing selector in CommandBlock at [x={}, y={}, z={}, world={}]", commandBlock.getX(), commandBlock.getY(), commandBlock.getZ(), commandBlock.getWorld().getName(), ex);
        }

        return entities;
    }

    /**
     * Vanilla CommandListenerWrapper
     * @param block
     * @return
     */
    @Override
    public CommandListenerWrapper getCommandListenerWrapper(Block block) {
        World world = ((CraftWorld) block.getWorld()).getHandle();
        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());

        TileEntityCommand tileEntityCommand = (TileEntityCommand) world.getTileEntity(blockPosition);
        CommandBlockListenerAbstract commandBlockListenerAbstract = tileEntityCommand.getCommandBlock();
        return commandBlockListenerAbstract.getWrapper();
    }

    /**
     * Vanilla Argument Parser Selector
     * @param selector
     * @return
     */
    @Override
    public ArgumentParserSelector getArgumentParser(String selector) {
        StringReader stringReader = new StringReader(selector);
        return new ArgumentParserSelector(stringReader);
    }

    /**
     * Vanilla Entities
     * @param argumentParser
     * @param wrapper
     * @return
     * @throws CommandSyntaxException
     */
    private List<? extends net.minecraft.server.v1_13_R1.Entity> getNmsEntities(ArgumentParserSelector argumentParser, CommandListenerWrapper wrapper) throws CommandSyntaxException {
        EntitySelector selector = argumentParser.s();
        return selector.b(wrapper);
    }

    /**
     * Spigot related
     * @param entities
     * @return
     */
    private List<Entity> convertToBukkitEntity(List<? extends net.minecraft.server.v1_13_R1.Entity> entities) {
        return entities.stream()
                .map(net.minecraft.server.v1_13_R1.Entity::getBukkitEntity)
                .collect(Collectors.toList());
    }

}
