package org.davilj.trademe.zipper;

import java.io.File;
import java.util.List;

/**
 * Created by daniev on 17/04/17.
 */
public interface IZipper {
    List<File> zipDirInDir(File baseDir, File outputFolder);
    File zipFilesInDir(File baseDir, File outputFolder);
    File mergeFilesInDirAndZip(File baseDir, File outputFolder);
}
