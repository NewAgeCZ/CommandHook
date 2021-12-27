package org.bitbucket._newage.commandhook.mapping.api;

import org.bitbucket._newage.commandhook.util.SelectorOptionConverter;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent class for NMS mappings
 */
public abstract class AMapping implements IMapping {

    protected final Logger logger = LoggerFactory.getLogger(IMapping.class);
    
    protected abstract Object getArgumentParser(String selector);
    protected abstract Object getCommandListenerWrapper(Block block);

    /**
     * Convenience method for problematic command blocks.
     * Prints location of the command block and its issue.
     *
     * It also tries to automatically rewrite old 1.13 syntax for radius, level, levelMore.
     * Check {@link SelectorOptionConverter} for more details.
     *
     * @param block Command Block
     * @param ex Thrown CommandSyntaxException
     */
    protected final void handleCommandSyntaxException(Block block, Exception ex) {
        logger.error("Error parsing selector in CommandBlock at [x={}, y={}, z={}, world={}]", block.getX(), block.getY(), block.getZ(), block.getWorld().getName(), ex);
        SelectorOptionConverter.isUnknownOptionConverted((CommandBlock) block.getState(), ex.getMessage());
    }
}
