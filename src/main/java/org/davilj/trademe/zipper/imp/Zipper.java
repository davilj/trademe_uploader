package org.davilj.trademe.zipper.imp;

import groovy.util.AntBuilder;
import org.davilj.trademe.zipper.IZipper;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by daniev on 16/04/17.
 */
public class Zipper implements IZipper {
    public static final Logger LOGGER = Logger.getLogger(Zipper.class.getName());

    /**
     * take each dir find in the supplied and zip contents into a zip file
     */
    @Override
    public File zipFilesInDir(File baseDir, File outputFolder) {
        LOGGER.info(String.format("zipping files in %s", baseDir));
        AntBuilder antBuilder = new AntBuilder();
        File[] dirs = baseDir.listFiles();
        if (dirs==null) {
            throw new RuntimeException(String.format("Error when listing dir in [%s]", baseDir ));
        }
        String zipFile = outputFolder.getPath() + File.separator + baseDir.getName() + ".zip";
        antBuilder.invokeMethod("zip", new HashMap() {{
            put("basedir", baseDir.getPath());
            put("destfile", zipFile);
        }});
        LOGGER.info(String.format("zipped %s", zipFile));
        return new File(zipFile);
    }

    /**
     * take each dir find in the supplied and zip contents into a zip file
     */
    @Override
    public List<File> zipDirInDir(File baseDir, File outputFolder) {
        LOGGER.info(String.format("zipping dirs in " + baseDir));
        AntBuilder antBuilder = new AntBuilder();
        File[] dirs = baseDir.listFiles();
        if (dirs==null) {
            throw new RuntimeException(String.format("Error when listing dir in [%s]", baseDir ));
        }

        return Arrays.stream(dirs).map(file -> {
                String zipFile = outputFolder.getPath() + File.separator + file.getName() + ".zip";
                antBuilder.invokeMethod("zip", new HashMap() {{
                    put("basedir", file.getPath());
                    put("destfile", zipFile);
                }});
                LOGGER.info(String.format("zipped %s", zipFile));
                return new File(zipFile);
            }).collect(Collectors.toList());

    }

    public File mergeFilesInDirAndZip(File baseDir, File outputFolder) {
        String zipFile = outputFolder.getPath() + File.separator + baseDir.getName() + ".zip";
        ZipOutputStream out=null;
        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
            out.putNextEntry(new ZipEntry(String.format("%s.big.txt", baseDir.getName())));
            addFileContentToZip(baseDir, out);
            out.closeEntry();
            out.finish();
            out.close();
        } catch (Exception e) {
            if (out!=null) {
                try { out.close();} catch (IOException e1) { /*do nothing*/}
            }
        }
        return new File(zipFile);
    }

    private void addFileContentToZip(final File file, final ZipOutputStream zip) {
        if (file.isDirectory()) {
            LOGGER.info(String.format("--found a dir: %s", file));
            Arrays.stream(file.listFiles()).forEach(dir -> addFileContentToZip(dir, zip));
        } else {
            try {
                LOGGER.info(String.format("---+Adding to zip: %s", file));
                Files.readAllLines(file.toPath()).forEach(line -> {
                    try {
                        zip.write(String.format("%s%n", line).getBytes());
                    } catch (IOException ioe) {
                        LOGGER.severe(String.format("---+Error reading: [%s] %s", line, ioe.getMessage()));
                        throw new RuntimeException(ioe);
                    }
                });
            } catch (IOException ioe) {
                LOGGER.severe(String.format("---+Error reading: %s  %s", file, ioe.getMessage()));
                throw new RuntimeException(ioe);
            }
        }
    }
}

