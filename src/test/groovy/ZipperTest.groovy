import com.google.common.io.Files
import spock.lang.Specification

/**
 * Created by daniev on 16/04/17.
 */
class ZipperTest extends Specification {
    def "zipping files in a dir"() {
        setup:
            File dataDir = new File("data")
            File testDir = new File("tmpData");
            testDir.mkdirs();

            new AntBuilder().copy( todir:testDir ) {
                fileset( dir:dataDir )
            }

            def zipDestination = new File("zip")
            zipDestination.deleteDir()

        when:
            Zipper z = new Zipper()
            def result = z.zipDirInDir(testDir, zipDestination)

        then:
            result==true
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
}
