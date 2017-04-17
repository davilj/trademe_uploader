import org.davilj.trademe.zipper.IZipper;
import org.davilj.trademe.zipper.imp.Zipper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
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

        Controller controller = new Controller(new Zipper());
        controller.start(startDir, zipDir, dataFile);
    }

    public Controller(IZipper zipper) {
        this.zipper = zipper;
    }

    protected void start(File startDir, File zipDir, File dataFile) {
        try {
            Set<String> completed = loadCompletedFile(dataFile);

            long numberOfZip = Arrays.stream(startDir.listFiles())
                    .filter(file -> completed.contains(file.getName()))
                    .map(file -> {
                        this.zipper.zipDirInDir(file, zipDir);
                        return file;
                    })
                    .map(file -> add2DataFile(dataFile, file))
                    .count();

            System.out.println(numberOfZip)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<String> loadCompletedFile(File dataFile) throws IOException {
        return new HashSet<String>(Files.readAllLines(dataFile.toPath());
    }

    private File add2DataFile(File dataFile, File dir) {
        try {
            Files.write(dataFile.toPath(), dir.getName().getBytes(), StandardOpenOption.APPEND);
            return dir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
