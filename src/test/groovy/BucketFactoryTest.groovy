import org.davilj.trademe.google.BucketFactory
import org.junit.Ignore
import spock.lang.Specification

/**
 * Created by daniev on 16/04/17.
 * !!!!
 *  This is a munual test
 * !!!!
 */
@Ignore
class BucketFactoryTest extends Specification {
    def "upload files to google storage"() {
        when:
        BucketFactory.BucketWrapper bw = BucketFactory.get("trademetestdata")
        def result = bw.addFile(new File("zip/20170411.zip"))

        then:
        result.le==true
        //manual test: check file is uploaded to trademetestdata bucket
    }
}
