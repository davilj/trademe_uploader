package org.davilj.trademe.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by daniev on 18/04/17.
 * Contains all task that was already completed.  Also responsible to update data file
 */
public class CompletedTasks {
    private Set<String> completedTasks;
    private File dataFile;

    public CompletedTasks(File dataFile) throws IOException{
        this.dataFile = dataFile;
        this.completedTasks =  loadTaskFromFile(this.dataFile.toPath());
    }

    public boolean isCompleted(String taskName) {
        return this.completedTasks.contains(taskName);
    }

    public boolean addCompletedTask(String taskName) {
        try {
            String line = taskName + "\n";
            Files.write(dataFile.toPath(), line.getBytes(), StandardOpenOption.APPEND);
            this.completedTasks =  loadTaskFromFile(this.dataFile.toPath());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> loadTaskFromFile(Path path) throws IOException {
        return  new HashSet<>(Files.readAllLines(path));
    }
}
