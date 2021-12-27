package org.bitbucket._newage.commandhook.mapping.api;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.List;

/**
 * Mapping interface - plugin does not need to know more.
 */
public interface IMapping {

    /**
     * Bukkit entities matching selector for further processing.
     *
     * @param selector selector in command block, e.g. @e[distance=..3,type=player]
     * @param commandBlock block in world that is certainly Command Block
     * @return list of entities matching given selector
     */
    List<Entity> getEntitiesFromSelector(String selector, Block commandBlock);

}

