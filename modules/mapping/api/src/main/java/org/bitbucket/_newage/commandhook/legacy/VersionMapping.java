package org.bitbucket._newage.commandhook.legacy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Interface describing necessary classes, methods and fields for reflection (legacy mapping) to work.
 */
public interface VersionMapping {

    String getVersion();

    Class<?> getArgumentParser();
    Class<?> getBlockPosition();
    Class<?> getStringReader();
    Class<?> getCraftWorld();
    Class<?> getEntity();
    Class<?> getTileEntityCommand();
    Class<?> getCommandBlockListenerAbstract();
    Class<?> getCommandListenerWrapper();
    Class<?> getEntitySelector();
    Class<?> getWorldServer();

    Method getParseSelector();
    Method getEntities();
    Method worldServer__getTileEntity();
    Method commandBlockListenerAbstract_getWrapper();
    Method tileEntityCommand_getCommandBlock();

    Field getEntityUUID();
}
