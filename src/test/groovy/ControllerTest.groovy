import org.apache.tools.ant.util.FileUtils
import spock.lang.Specification

import java.nio.file.Files
import java.util.stream.Collectors

/**
 * Created by daniev on 17/04/17.
 */
class ControllerTest extends Specification {
    def "controller test"() {
        setup:
        def root = "src/test/resources"
        //create testFile
        File testFile = rmFile(root, "controllerTest.ct");
        testFile.createNewFile()

        //build testDir
        File templateDir = new File("data")
        File testDir = rmDir(root, "controller_test");
        testDir.mkdirs()
        new AntBuilder().copy( todir:testDir ) {
            fileset( dir:templateDir )
        }

        //build testZip
        File zipDir = rmDir(root, "controller_zip");
        zipDir.mkdirs()

        when:
        Controller.main(testDir.getAbsolutePath(), zipDir.getAbsolutePath(), testFile.getAbsolutePath(), "trademetestbucket")

        List<String> completed = testFile.readLines()

        List<String> zipFiles = Arrays.stream(zipDir.listFiles())
                .map { file -> file.getName()}
                .collect(Collectors.toList())
        println zipFiles


        then:
        completed.size()==2
        completed.contains("20170411")
        completed.contains("20170412")
        zipFiles.size()==2
        zipFiles.contains("20170411.zip")
        zipFiles.contains("20170412.zip")

    }

    def File rmDir(root, name) {
        File tmp = new File(root + File.separator + name)
        def ant = new AntBuilder()
        ant.delete(dir:tmp,failonerror:false)
        tmp.mkdirs()
        return tmp;
    }

    def File rmFile(root, name) {
        File tmp = new File(root + File.separator + name)
        Files.deleteIfExists(tmp.toPath())
        tmp.createNewFile()
        return tmp;
    }
}
