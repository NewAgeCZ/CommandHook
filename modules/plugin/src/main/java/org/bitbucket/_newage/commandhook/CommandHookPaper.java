package org.bitbucket._newage.commandhook;

import org.bitbucket._newage.commandhook.mapping.ServerBrand;

/**
 * Paper plugin entry point.
 */
public class CommandHookPaper extends CommandHook {

    @Override
    protected ServerBrand getServerBrand() {
        return ServerBrand.PAPER_BASED;
    }
}
