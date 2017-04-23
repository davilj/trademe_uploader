import org.davilj.trademe.bucket.BucketFactory
import org.davilj.trademe.bucket.IBucket
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
        IBucket bw = BucketFactory.getGoogleBucket("trademetestdata")
        def result = bw.addFile(new File("zip/20170411.zip"))

        then:
        result.le==true
        //manual test: check file is uploaded to trademetestdata bucket
    }
}
