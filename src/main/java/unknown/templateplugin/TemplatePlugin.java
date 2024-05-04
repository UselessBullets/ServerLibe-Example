package unknown.templateplugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.serverlibe.ServerLibe;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.ServerLibeEntrypoint;
import org.useless.serverlibe.api.annotations.EventListener;
import org.useless.serverlibe.api.event.player.PlayerChatEvent;


public class TemplatePlugin implements ServerLibeEntrypoint, Listener {
    public static final String MOD_ID = "templateplugin";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void serverlibeInit() {
		LOGGER.info("TemplatePlugin initialized.");
		ServerLibe.registerListener(this);
	}
	@EventListener
	public void onChat(PlayerChatEvent chatEvent){
		chatEvent.player.addChatMessage(String.format("[%s] onChat called!", MOD_ID));
	}
}
