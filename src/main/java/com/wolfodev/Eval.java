package com.wolfodev;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.Class;
import org.bukkit.command.Command;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.caoccao.javet.interop.engine.IJavetEngine;
import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.caoccao.javet.interop.engine.JavetEnginePool;
import com.caoccao.javet.utils.JavetOSUtils;
import com.caoccao.javet.values.reference.V8ValueFunction;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Eval implements CommandExecutor {
	App plugin;

	public Eval(App plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String cmdName = cmd.getName().toLowerCase();

		if (!cmdName.equals("eval")) {
			return false;
		}
		// Make sure node_modules and test folders stay together.

		try (NodeRuntime nodeRuntime = V8Host.getNodeInstance().createV8Runtime()) {
			// Create a worker thread.
				try {
					JavetProxyConverter javetProxyConverter = new JavetProxyConverter();
					nodeRuntime.setConverter(javetProxyConverter);
					nodeRuntime.getGlobalObject().set("Class", Class.class);
					System.out.println(nodeRuntime.getExecutor(String.join(" ", args)).executeString());
					nodeRuntime.await();
				} catch (JavetException e) {
					e.printStackTrace();
				}
		} catch (JavetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		sender.sendMessage("Successfully used example command!");

		return true;
	}
}
