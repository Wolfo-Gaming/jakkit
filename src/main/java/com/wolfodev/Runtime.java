package com.wolfodev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import com.caoccao.javet.values.reference.V8ValueFunction;

public class Runtime {
    public NodeRuntime nodeRuntime;
    public Thread thread;

    public NodeRuntime createRuntime() {
        try {
            nodeRuntime = V8Host.getNodeInstance().createV8Runtime();
        } catch (JavetException e) {

            e.printStackTrace();
        }
        return nodeRuntime;
    }

    public NodeRuntime getRuntime() {
        return nodeRuntime;
    }

    public Thread createThreadAndRun(Path codeFile) {
        thread = new Thread(() -> {

            try {
                NodeRuntime nodeRuntime = this.createRuntime();

                JavetProxyConverter javetProxyConverter = new JavetProxyConverter();
                nodeRuntime.setConverter(javetProxyConverter);
                nodeRuntime.getGlobalObject().set("Class", Class.class);

                System.out.println(Files.readString(codeFile).toString());

                nodeRuntime.getExecutor(Files.readString(codeFile).toString()).executeVoid();
                nodeRuntime.await();
            } catch (JavetException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        thread.start();
        return thread;
    }

    public void closeRuntime() {
        Thread closethread = new Thread(() -> {
    
                try {
                    nodeRuntime.getExecutor("process.emit('close')").executeVoid();
                    nodeRuntime.lowMemoryNotification();
                    nodeRuntime.setPurgeEventLoopBeforeClose(true);
                    nodeRuntime.lowMemoryNotification();
                    nodeRuntime.terminateExecution();
                    nodeRuntime.lowMemoryNotification();
                    nodeRuntime.close(true);
                } catch (JavetException e) {
                    e.printStackTrace();
                }
            

        });
        closethread.start();

        return;
    }
}
