import org.davilj.trademe.bucket.BucketFactory;
import org.davilj.trademe.bucket.IBucket;
import org.davilj.trademe.tasks.CompletedTasks;
import org.davilj.trademe.zipper.IZipper;
import org.davilj.trademe.zipper.imp.Zipper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by daniev on 17/04/17.
 */
public class Controller {
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());
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
            IBucket bucket = BucketFactory.getGoogleBucket(bucketName);
            controller.start(startDir, zipDir, completedTasks, bucket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Controller(IZipper zipper) {
        this.zipper = zipper;
    }

    protected void start(File startDir, File zipDir, CompletedTasks completedTasks, IBucket bucket) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(new Date());

        long numberOfUploads  = Arrays.stream(startDir.listFiles())
                    .filter(file -> !today.equals(file.getName()))
                    .filter(file -> !completedTasks.isCompleted(file.getName()))
                    .map(file -> this.zipper.zipFilesInDir(file, zipDir))
                    .map(file -> uploadFile(bucket, file))
                    .map(file -> completedTasks.addCompletedTask(deriveTasknameFromFile(file)))
                    .count();
    }

    private File uploadFile(IBucket bucket, File file) {
        try {
            bucket.addFile(file);
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
