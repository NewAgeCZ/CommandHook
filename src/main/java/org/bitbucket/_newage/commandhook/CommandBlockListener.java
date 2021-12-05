package org.bitbucket._newage.commandhook;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandBlockListener implements Listener {

    private final RefUtil refUtil;

    public CommandBlockListener(RefUtil util) {
        refUtil = util;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandBlockDispatch(ServerCommandEvent e) {
        if (e.getSender() instanceof BlockCommandSender) {
            String cmd = e.getCommand();
            if (cmd.startsWith("/")) {
                cmd = cmd.replaceFirst("/", "");
            }

            if (cmd.startsWith("minecraft:")) {
                return;
            }

            String[] args = cmd.split(" ");
            for(String arg : args) {
                if(arg.startsWith("@")) {
                    switch(arg.substring(0, 2)) {
                        case "@a":
                        case "@r":
                        case "@s":
                        case "@e":
                        case "@p":
                            List<Entity> entities = refUtil.parse(arg, ((BlockCommandSender)e.getSender()).getBlock());

                            for(Iterator<Entity> i = entities.iterator(); i.hasNext();) {
                                Entity o = i.next();
                                if (o == null) continue;
                                Bukkit.dispatchCommand(e.getSender(), cmd.replace(arg, o.getName()));
                            }
                            e.setCancelled(true);
                            break;
                            
                        default:
                            return;
                    }
                }
            }
        }
    }
}
