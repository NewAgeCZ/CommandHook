package org.bitbucket._newage.commandhook;

import org.bitbucket._newage.commandhook.mapping.ServerBrand;

/**
 * Spigot plugin entry point.
 */
public class CommandHookSpigot extends CommandHook {

    @Override
    protected ServerBrand getServerBrand() {
        return ServerBrand.SPIGOT;
    }
}
