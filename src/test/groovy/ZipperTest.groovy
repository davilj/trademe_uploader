import com.google.common.io.Files
import org.davilj.trademe.zipper.IZipper
import org.davilj.trademe.zipper.imp.Zipper
import spock.lang.Specification

/**
 * Created by daniev on 16/04/17.
 */
class ZipperTest extends Specification {
    File dataDir = new File("src/test/resources/data")

    def "merging and zipping files in dir"() {
        setup:
        File testDir = setupTestData()

        def zipDestination = new File("zipAll")
        zipDestination.deleteDir()
        zipDestination.mkdirs()

        when:
        IZipper z = new Zipper()
        def result = z.mergeFilesInDirAndZip(testDir,zipDestination);

        then:
        result.size()==5428633
        result.getName()=="tmpData.zip"

    }

    def "zipping files in a dir"() {
        setup:
        File testDir = setupTestData()

        def zipDestination = new File("zip")
        zipDestination.deleteDir()

        when:
        IZipper z = new Zipper()
        def result = z.zipFilesInDir(testDir, zipDestination)

        then:
        result.size()==6483717
        result.getName()=="tmpData.zip"
    }


    def "zipping dirs in a dir"() {
        setup:
            File testDir = setupTestData()

            def zipDestination = new File("zip")
            zipDestination.deleteDir()

        when:
            IZipper z = new Zipper()
            def result = z.zipDirInDir(testDir, zipDestination)

        then:
            result.size()==2
            result.contains(new File("zip/20170411.zip"))
            result.contains(new File("zip//20170412.zip"))
            File[] results = zipDestination.listFiles();
            results.length==2
            def setOfFiles = new HashSet()
            def sizeSets = new HashSet();
            results.each {
                setOfFiles.add(it.getName());
                sizeSets.add(it.getBytes().length)
            }

            setOfFiles.contains("20170411.zip")==true
            setOfFiles.contains("20170412.zip")==true


            sizeSets.contains(3310876)==true
            sizeSets.contains(3150895)==true

    }

    def File setupTestData() {
        File testDir = new File("tmpData")
        testDir.deleteDir()
        testDir.mkdirs()

        new AntBuilder().copy( todir:testDir ) {
            fileset( dir:dataDir )
        }
        return testDir
    }
}
