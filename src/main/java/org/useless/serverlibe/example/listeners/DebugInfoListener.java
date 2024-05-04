package org.useless.serverlibe.example.listeners;

import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.enums.Priority;
import org.useless.serverlibe.api.event.player.PlayerEntityInteractEvent;
import org.useless.serverlibe.api.event.player.PlayerItemUseEvent;
import org.useless.serverlibe.api.event.player.inventory.InventoryClickEvent;
import org.useless.serverlibe.api.event.player.inventory.InventoryCloseEvent;
import org.useless.serverlibe.api.event.player.inventory.InventoryServerOpenEvent;
import org.useless.serverlibe.example.ExamplePlugin;

import java.util.Arrays;

public class DebugInfoListener implements Listener {
	@EventListener(priority = Priority.HIGH)
	public void onItemUsed(PlayerItemUseEvent useEvent){
		useEvent.player.addChatMessage(String.format("[%s] item right click", ExamplePlugin.MOD_ID));
	}
	@EventListener(priority = Priority.HIGH)
	public void onGuiClose(InventoryCloseEvent closeEvent){
		closeEvent.player.addChatMessage(String.format("[%s] GUI closed", ExamplePlugin.MOD_ID));
	}
	@EventListener(priority = Priority.HIGH)
	public void onGuiOpen(InventoryServerOpenEvent openEvent){
		openEvent.player.addChatMessage(String.format("[%s] GUI " + openEvent.windowTitle + " opened", ExamplePlugin.MOD_ID));
	}
	@EventListener(priority = Priority.HIGH)
	public void onGuiClick(InventoryClickEvent clickEvent){
		clickEvent.player.addChatMessage(String.format(String.format("[%s] action: %s, args: %s, actionID: %s, Itemstack: %s", ExamplePlugin.MOD_ID, clickEvent.action, Arrays.toString(clickEvent.args), clickEvent.actionId, clickEvent.itemStack));
	}
	@EventListener(priority = Priority.HIGH)
	public void onAttack(PlayerEntityInteractEvent interactEvent){
		interactEvent.player.addChatMessage(String.format("[%s] target: %s, mouseButton: %d", ExamplePlugin.MOD_ID, interactEvent.targetEntity, interactEvent.mouseButton));
	}
}
