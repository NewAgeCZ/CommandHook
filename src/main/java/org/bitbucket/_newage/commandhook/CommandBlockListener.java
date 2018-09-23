package org.bitbucket._newage.commandhook;


import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

/*
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftCommandBlock;

import net.minecraft.server.v1_13_R2.ArgumentParserSelector;
import net.minecraft.server.v1_13_R2.CommandBlockListenerAbstract;
import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntitySelector;
import net.minecraft.server.v1_13_R2.TileEntityCommand;
*/

public class CommandBlockListener implements Listener {
	
	private final RefUtil refUtil;
	
	public CommandBlockListener(RefUtil util) {
		super();
		refUtil = util;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommandBlockDispatch(ServerCommandEvent e) {
		if(e.getSender() instanceof BlockCommandSender) {
			//CommandBlock cmdBlock = ((CommandBlock)((BlockCommandSender)e.getSender()).getBlock().getState());
			String cmd = e.getCommand();
			if(cmd.startsWith("/"))
				cmd = cmd.replaceFirst("/", "");
			
			if(cmd.startsWith("minecraft:")) 
				return;
			
			
			
			
			String[] args = cmd.split(" ");
			for(String arg : args) {
				if(arg.startsWith("@")) {
					//System.out.println(arg);
					//System.out.println(arg.substring(0,2));
					switch(arg.substring(0, 2)) {
					case "@r":
					case "@s":
					case "@e":
					case "@p":
						List<org.bukkit.entity.Entity> entities = refUtil.parse(arg, ((BlockCommandSender)e.getSender()).getBlock());
						/*
						if(entities.isEmpty()) {
							System.out.println("Empty result");
							return;
						}
						*/
						for(Iterator<org.bukkit.entity.Entity> i = entities.iterator(); i.hasNext();) {
							org.bukkit.entity.Entity o = i.next();
							Bukkit.dispatchCommand(e.getSender(), cmd.replace(arg,o.getName()));
							System.out.println(o.getName());
						}
						e.setCancelled(true);
						//callArgumentParser(arg, ((BlockCommandSender)e.getSender()).getBlock());
						//PlayerSelector s = new PlayerSelector(cmdBlock, arg);
						//s.getArgs();
						break;
					
						//RandomSelector r = new RandomSelector(cmdBlock, arg);
						//e.setCommand(cmd.replace(arg, r.randomPlayer().toString()));
						//Bukkit.dispatchCommand(e.getSender(), cmd.replace(arg, r.randomPlayer().toString()));
					default:
						return;
					}
				}
			}
		}
	}
	
	/*
	private void callArgumentParser(String arg, Block cmdBlock) {
		try {
			//Class SRClass = Class.forName("com.mojang.brigadier.StringReader");
			Class TileCommandClass = Class.forName("net.minecraft.server.v1_13_R2.TileEntityCommand");
			Class APSelClass = Class.forName("net.minecraft.server.v1_13_R2.ArgumentParserSelector");
			Constructor APConst = APSelClass.getConstructor(StringReader.class);
			
			CraftCommandBlock ccb = new CraftCommandBlock(cmdBlock);
			TileEntityCommand tec = (TileEntityCommand)((CraftWorld)cmdBlock.getWorld()).getTileEntityAt(cmdBlock.getX(), cmdBlock.getY(), cmdBlock.getZ());//(TileEntityCommand)TileCommandClass.cast(ccb);
			//Field h = tec.getClass().getDeclaredField("h");
			//h.setAccessible(true);
			//Object hInstance = h.get(tec.getComm);
			
			CommandBlockListenerAbstract cbla = tec.getCommandBlock();//(CommandBlockListenerAbstract)(Class.forName("net.minecraft.server.v1_13_R2.CommandBlockListenerAbstract").cast(hInstance));
			CommandListenerWrapper clw = cbla.getWrapper();
			
			//Object stringReaderInstance =  SRConst.newInstance(arg);
			//StringReader stringReader = (StringReader)SRClass.cast(stringReaderInstance);
			StringReader stringReader = new StringReader(arg.substring(1));
			ArgumentParserSelector APSelector = (ArgumentParserSelector)APSelClass.cast(APConst.newInstance(stringReader));
			
			//APSelector.b();
			Method b = APSelector.getClass().getDeclaredMethod("b");
			b.setAccessible(true);
			b.invoke(APSelector);
			
			EntitySelector es = APSelector.a();//APSelector.s();
			List<? extends Entity> l = es.b(clw);
			if(!l.isEmpty())
				for(Iterator<? extends Entity> i = l.iterator(); i.hasNext();) {
					Entity ent = i.next();
					System.out.print(ent.toString());
				}
			else System.out.println("No target found");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CommandSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	*/
}
