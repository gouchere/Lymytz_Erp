package yvs.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

public class WatcherService extends Thread {
    
    private String pathDirectory;    
    private boolean running = true;

    public WatcherService() {
    }

    public WatcherService(String pathDirectory) {
        this.pathDirectory = pathDirectory;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public String getPathDirectory() {
        return pathDirectory;
    }

    public void setPathDirectory(String pathDirectory) {
        this.pathDirectory = pathDirectory;
    }

    @Override
    public void run() {
        WatchService watcher;
        try {
            watcher = FileSystems.getDefault().newWatchService();
            final Path dir = Paths.get(pathDirectory);

            WatchKey key = dir.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            while (running) {
                try {
                    // key = watcher.take();
                    key = watcher.poll(1000, TimeUnit.MILLISECONDS);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }

                if (key != null) {
                    for (final WatchEvent<?> event : key.pollEvents()) {
                        final Path name = (Path) event.context();
                        System.out.format(event.kind() + " " + "%s\n", name);
                    }
                    boolean reset = key.reset();
                    if (!reset) {
                        running = false;
                    }
                }
            }
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
