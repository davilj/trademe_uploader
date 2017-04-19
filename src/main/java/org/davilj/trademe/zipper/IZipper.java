package org.davilj.trademe.zipper;

import java.io.File;

/**
 * Created by daniev on 17/04/17.
 */
public interface IZipper {
    boolean zipDirInDir(File baseDir, File outputFolder);
    boolean zipFilesInDir(File baseDir, File outputFolder);
}
