package org.bitbucket._newage.commandhook;

import java.util.Iterator;
import java.util.List;

import org.bitbucket._newage.commandhook.mapping.api.IMapping;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandBlockListener implements Listener {

    private final IMapping mapping;

    public CommandBlockListener(IMapping mapping) {
        this.mapping = mapping;
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
                            List<Entity> entities = mapping.getEntitiesFromSelector(arg, ((BlockCommandSender) e.getSender()).getBlock());

                            for (Iterator<Entity> i = entities.iterator(); i.hasNext();) {
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
