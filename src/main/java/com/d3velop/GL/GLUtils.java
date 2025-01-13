package com.d3velop.GL;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GLUtils {
    private static final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    // Method to queue tasks from any thread
    public static void invokeLater(Runnable task) {
        taskQueue.offer(task);
    }

    // Call this method on the main thread to process tasks
    public static void processTasks() {
        Runnable task;
        while ((task = taskQueue.poll()) != null) {
            task.run();
        }
    }
}
