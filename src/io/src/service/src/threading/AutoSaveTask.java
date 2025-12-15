package threading;

import io.FileManager;
import repo.StudentRepository;

public class AutoSaveTask implements Runnable {
    private final StudentRepository repo;
    private final FileManager fm;
    private final String path;
    private final int intervalSeconds;
    private volatile boolean running = true;

    public AutoSaveTask(StudentRepository repo, FileManager fm, String path, int intervalSeconds) {
        this.repo = repo;
        this.fm = fm;
        this.path = path;
        this.intervalSeconds = intervalSeconds;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                fm.saveCsv(path, repo.listAll());
                Thread.sleep(intervalSeconds * 1000L);
            } catch (Exception e) {
                System.out.println("[AutoSave] Error: " + e.getMessage());
            }
        }
        System.out.println("[AutoSave] Stopped.");
    }
}
