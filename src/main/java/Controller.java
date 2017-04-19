import org.davilj.trademe.tasks.CompletedTasks;
import org.davilj.trademe.zipper.IZipper;
import org.davilj.trademe.zipper.imp.Zipper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by daniev on 17/04/17.
 */
public class Controller {
    private IZipper zipper;

    public static void main(String[] args) {
        File startDir = new File(args[0]);
        File zipDir = new File(args[1]);
        File dataFile = new File(args[2]);

        try {
            Controller controller = new Controller(new Zipper());
            CompletedTasks completedTasks = new CompletedTasks(dataFile);
            controller.start(startDir, zipDir, completedTasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Controller(IZipper zipper) {
        this.zipper = zipper;
    }

    protected void start(File startDir, File zipDir, CompletedTasks completedTasks) {

            long numberOfZip = Arrays.stream(startDir.listFiles())
                    .filter(file -> !completedTasks.isCompleted(file.getName()))
                    .map(file -> {
                        this.zipper.zipFilesInDir(file, zipDir);
                        return file;
                    })
                    .map(file -> completedTasks.addCompletedTask(file.getName()))
                    .count();

            System.out.println(numberOfZip);

    }

}
