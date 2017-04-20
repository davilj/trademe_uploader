package org.davilj.trademe.zipper.imp;

import groovy.util.AntBuilder;
import org.davilj.trademe.zipper.IZipper;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by daniev on 16/04/17.
 */
public class Zipper implements IZipper {

    /**
     * take each dir find in the supplied and zip contents into a zip file
     */
    @Override
    public File zipFilesInDir(File baseDir, File outputFolder) {
        AntBuilder antBuilder = new AntBuilder();
        File[] dirs = baseDir.listFiles();
        if (dirs==null) {
            throw new RuntimeException(String.format("Error when listing dir in [%s]", baseDir ));
        }
        String zipFile = outputFolder.getPath() + File.separator + baseDir.getName() + ".zip";
        antBuilder.invokeMethod("zip", new HashMap() {{
            put("basedir", baseDir.getPath());
            put("destfile", outputFolder.getPath() + File.separator + baseDir.getName() + ".zip");
        }});
        return new File(zipFile);
    }

    /**
     * take each dir find in the supplied and zip contents into a zip file
     */
    @Override
    public List<File> zipDirInDir(File baseDir, File outputFolder) {
        AntBuilder antBuilder = new AntBuilder();
        File[] dirs = baseDir.listFiles();
        if (dirs==null) {
            throw new RuntimeException(String.format("Error when listing dir in [%s]", baseDir ));
        }

        return Arrays.stream(dirs).map(file -> {
                String zipFile = outputFolder.getPath() + File.separator + baseDir.getName() + ".zip";
                antBuilder.invokeMethod("zip", new HashMap() {{
                    put("basedir", file.getPath());
                    put("destfile", zipFile);
                }});
                return new File(zipFile);
            }).collect(Collectors.toList());

    }
}

