package com.wolfodev;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import java.lang.Thread;

import com.caoccao.javet.enums.JSRuntimeType;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.V8ScriptOrigin;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.caoccao.javet.interop.engine.IJavetEngine;
import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.caoccao.javet.interop.engine.JavetEnginePool;
import com.caoccao.javet.interop.loader.IJavetLibLoadingListener;
import com.caoccao.javet.interop.loader.JavetLibLoader;
import com.caoccao.javet.utils.JavetOSUtils;
import com.caoccao.javet.values.reference.V8ValueFunction;

import com.wolfodev.Eval;

/**
 * Echo 'HIT' on ProjectileHitEvent
 */
public class App extends JavaPlugin implements Listener {
  @Override
  public void onDisable() {
    getServer().broadcastMessage("Disabling Jakkit");
    V8Host v8Host = V8Host.getInstance(JSRuntimeType.Node);
    v8Host.unloadLibrary();
    // V8Host v8host = V8Host.getInstance(JSRuntimeType.Node);
    // v8host.unloadLibrary();
    // try {
    // v8host.close();
    // } catch (JavetException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
  }

  @Override
  public void onEnable() {
    V8Host.setLibraryReloadable(true);
    Thread thread = new Thread(() -> {
      if (!this.getDataFolder().exists()) {
        try {
          this.getDataFolder().mkdir();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      Path codeFile = Path.of(this.getDataFolder().getAbsolutePath()).resolve("dist/index.js");
       
      try (NodeRuntime nodeRuntime = V8Host.getNodeInstance().createV8Runtime()) {
        try {
          JavetProxyConverter javetProxyConverter = new JavetProxyConverter();
          nodeRuntime.setConverter(javetProxyConverter);
          nodeRuntime.getGlobalObject().set("Class", Class.class);
          System.out.println(Files.readString(codeFile).toString());
          nodeRuntime.getExecutor(Files.readString(codeFile).toString()).executeVoid();
        } catch (JavetException e) {
          e.printStackTrace();
        }
      } catch (JavetException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }

    });
    thread.start();

    getCommand("eval").setExecutor(new Eval(this));
    getServer().broadcastMessage("Enabled Jakkit");
    getServer().getPluginManager().registerEvents(this, this);
  }
}
