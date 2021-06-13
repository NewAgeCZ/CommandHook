package org.bitbucket._newage.commandhook.versions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    Field getEntityUUID();
}
