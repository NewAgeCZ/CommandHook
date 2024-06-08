package org.bitbucket._newage.commandhook.mapping;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import org.bitbucket._newage.commandhook.mapping.api.AMapping;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MojangMapping extends AMapping {

    @Override
    public List<Entity> getEntitiesFromSelector(String selector, Block commandBlock) {
        List<Entity> entities = Collections.emptyList();

        CommandSourceStack wrapper = getCommandListenerWrapper(commandBlock);
        EntitySelectorParser argumentParser = getArgumentParser(selector);
        try {
            List<? extends net.minecraft.world.entity.Entity> nmsEntities = getNmsEntities(argumentParser, wrapper);
            entities = convertToBukkitEntity(nmsEntities);
        } catch (CommandSyntaxException ex) {
            handleCommandSyntaxException(commandBlock, ex);
        }

        return entities;
    }

    /**
     * Vanilla CommandSourceStack
     * @param block
     * @return
     */
    @Override
    public CommandSourceStack getCommandListenerWrapper(Block block) {
        Level world = ((CraftWorld) block.getWorld()).getHandle();
        BlockPos blockPosition = new BlockPos(block.getX(), block.getY(), block.getZ());

        CommandBlockEntity tileEntityCommand = (CommandBlockEntity) world.getBlockEntity(blockPosition, true);
        BaseCommandBlock commandBlockListenerAbstract = tileEntityCommand.getCommandBlock();
        return commandBlockListenerAbstract.createCommandSourceStack();
    }

    /**
     * Vanilla Argument Parser Selector
     * @param selector
     * @return
     */
    @Override
    public EntitySelectorParser getArgumentParser(String selector) {
        StringReader stringReader = new StringReader(selector);
        return new EntitySelectorParser(stringReader);
    }

    /**
     * Vanilla Entities
     * @param argumentParser
     * @param wrapper
     * @return
     * @throws CommandSyntaxException
     */
    private List<? extends net.minecraft.world.entity.Entity> getNmsEntities(EntitySelectorParser argumentParser, CommandSourceStack wrapper) throws CommandSyntaxException {
        EntitySelector selector = argumentParser.parse(false);
        return selector.findEntities(wrapper);
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
