package org.bitbucket._newage.commandhook.mapping;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.selector.ArgumentParserSelector;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.CommandBlockListenerAbstract;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.entity.TileEntityCommand;
import org.bitbucket._newage.commandhook.mapping.api.AMapping;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NmsV1_20_R4 extends AMapping {

    @Override
    public List<Entity> getEntitiesFromSelector(String selector, Block commandBlock) {
        List<Entity> entities = Collections.emptyList();

        CommandListenerWrapper wrapper = getCommandListenerWrapper(commandBlock);
        ArgumentParserSelector argumentParser = getArgumentParser(selector);
        try {
            List<? extends net.minecraft.world.entity.Entity> nmsEntities = getNmsEntities(argumentParser, wrapper);
            entities = convertToBukkitEntity(nmsEntities);
        } catch (CommandSyntaxException ex) {
            handleCommandSyntaxException(commandBlock, ex);
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

        TileEntityCommand tileEntityCommand = (TileEntityCommand) world.getBlockEntity(blockPosition, true);
        CommandBlockListenerAbstract commandBlockListenerAbstract = tileEntityCommand.b();
        return commandBlockListenerAbstract.i();
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
    private List<? extends net.minecraft.world.entity.Entity> getNmsEntities(ArgumentParserSelector argumentParser, CommandListenerWrapper wrapper) throws CommandSyntaxException {
        EntitySelector selector = argumentParser.parse(false);
        return selector.b(wrapper);
    }

    /**
     * Spigot related
     * @param entities
     * @return
     */
    private List<Entity> convertToBukkitEntity(List<? extends net.minecraft.world.entity.Entity> entities) {
        return entities.stream()
                .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                .collect(Collectors.toList());
    }

}
