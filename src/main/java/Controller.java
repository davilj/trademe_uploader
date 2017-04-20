import org.davilj.trademe.google.BucketFactory;
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
    private BucketFactory bucketFactory;

    public static void main(String[] args) {
        File startDir = new File(args[0]);
        File zipDir = new File(args[1]);
        File dataFile = new File(args[2]);
        String bucketName = args[3];

        try {
            Controller controller = new Controller(new Zipper());
            CompletedTasks completedTasks = new CompletedTasks(dataFile);
            controller.start(startDir, zipDir, completedTasks, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Controller(IZipper zipper) {
        this.zipper = zipper;
    }

    protected void start(File startDir, File zipDir, CompletedTasks completedTasks, String bucketName) throws Exception {
        BucketFactory.BucketWrapper bucketWrapper = BucketFactory.get(bucketName);

        long numberOfUploads  = Arrays.stream(startDir.listFiles())
                    .filter(file -> !completedTasks.isCompleted(file.getName()))
                    .map(file -> this.zipper.zipFilesInDir(file, zipDir))
                    .map(file -> uploadFile(bucketWrapper, file))
                    .map(file -> completedTasks.addCompletedTask(deriveTasknameFromFile(file)))
                    .count();
    }

    private File uploadFile(BucketFactory.BucketWrapper bucketWrapper, File file) {
        try {
            bucketWrapper.addFile(file);
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String deriveTasknameFromFile(File file) {
        String fileName = file.getName();
        return fileName.substring(0, fileName.length()-4);
    }

}
