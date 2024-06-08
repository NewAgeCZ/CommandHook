package org.bitbucket._newage.commandhook.legacy.mapping;

import org.bitbucket._newage.commandhook.legacy.VersionMapping;
import org.bitbucket._newage.commandhook.mapping.api.AMapping;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.List;

public class LegacyMapping extends AMapping {

    private final RefUtil reflectionUtil;

    public LegacyMapping(String nmsVersion) {
        VersionMapping reflectionMapping = ReflectionMappingSelector.fromNmsVersion(nmsVersion);
        reflectionUtil = new RefUtil(reflectionMapping);
    }

    @Override
    public List<Entity> getEntitiesFromSelector(String selector, Block commandBlock) {
        return reflectionUtil.parse(selector, commandBlock);
    }

    /**
     * Vanilla CommandListenerWrapper
     * @param block
     * @return
     */
    @Override
    public Object getCommandListenerWrapper(Block block) {
        throw new UnsupportedOperationException("Not implemented in reflection");
    }

    /**
     * Vanilla Argument Parser Selector
     * @param selector
     * @return
     */
    @Override
    public Object getArgumentParser(String selector) {
        throw new UnsupportedOperationException("Not implemented in reflection");
    }

}
