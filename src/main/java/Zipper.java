import groovy.util.AntBuilder;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by daniev on 16/04/17.
 */
public class Zipper {

    /**
     * take each dir find in the supplied and zip contents into a dir
     */
    public boolean zipDirInDir(File baseDir, File outputFolder) {
        AntBuilder antBuilder = new AntBuilder();
        File[] dirs = baseDir.listFiles();
        if (dirs==null) {
            throw new RuntimeException(String.format("Error when listing dir in [%s]", baseDir ));
        }

        Arrays.stream(dirs).forEach(file -> {
            System.out.println("sdhfksdhfsd:" + file);
            antBuilder.invokeMethod("zip", new HashMap() {{
                put("basedir", file.getPath());
                put("destfile", outputFolder.getPath() + File.separator + file.getName() + ".zip");
            }});
        });
        return true;
    }
}
