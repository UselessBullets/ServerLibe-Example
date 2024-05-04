package org.useless.serverlibe.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.serverlibe.ServerLibe;
import org.useless.serverlibe.api.Listener;
import org.useless.serverlibe.api.ServerLibeEntrypoint;
import org.useless.serverlibe.example.listeners.GuiTestListener;
import org.useless.serverlibe.example.listeners.TestFeatureListener;


public class ExamplePlugin implements ServerLibeEntrypoint, Listener {
	public static final String MOD_ID = "templateplugin";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void serverlibeInit() {
		// In order for methods inside your listeners to be recognized by ServerLibe you must
		// register them into ServerLibe like such
		ServerLibe.registerListener(new TestFeatureListener());
		ServerLibe.registerListener(new GuiTestListener());
//		ServerLibe.registerListener(new DebugInfoListener()); // Prints out debug info to chat on a number of events, disable by default because its annoying
		LOGGER.info("TemplatePlugin initialized.");
	}
}
