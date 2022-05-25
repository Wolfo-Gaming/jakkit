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
<<<<<<< HEAD
=======
import com.wolfodev.Generator;
>>>>>>> 43e0fa5 (Initial Working Commit)

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
import com.wolfodev.Runtime;

/**
 * Echo 'HIT' on ProjectileHitEvent
 */
public class App extends JavaPlugin implements Listener {
  Runtime runtime = new Runtime();
  
  @Override
  public void onDisable() {
    getServer().broadcastMessage("Disabling Jakkit");
    runtime.closeRuntime();
    V8Host v8Host = V8Host.getInstance(JSRuntimeType.Node);
    //try {
      //v8Host.close();
    //} catch (JavetException e) {
      // TODO Auto-generated catch block
     //e.printStackTrace();
    //}
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
    App self = this;
    JavetLibLoader.setLibLoadingListener(new IJavetLibLoadingListener() {
      @Override
      public File getLibPath(JSRuntimeType jsRuntimeType) {
        Generator generator = new Generator();
        String randomFolder = generator.generateRandom(10);

        String totalPath = self.getDataFolder().getAbsolutePath() + "/" + randomFolder;
        File file = new File(totalPath);
        if (!file.exists()) {
          try {
            file.mkdir();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        return file;
      }
    });
    V8Host.setLibraryReloadable(true);
    if (!this.getDataFolder().exists()) {
      try {
        this.getDataFolder().mkdir();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    Path codeFile = Path.of(this.getDataFolder().getAbsolutePath()).resolve("dist/index.js");
    runtime.createThreadAndRun(codeFile);
    getCommand("eval").setExecutor(new Eval(this));

    getServer().broadcastMessage("Enabled Jakkit");
    getServer().getPluginManager().registerEvents(this, this);
  }
}
