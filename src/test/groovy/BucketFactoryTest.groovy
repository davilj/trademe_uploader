import org.davilj.trademe.google.BucketFactory
import spock.lang.Specification

/**
 * Created by daniev on 16/04/17.
 */
class BucketFactoryTest extends Specification {
    def "upload files to google storage"() {
        when:
        BucketFactory.BucketWrapper bw = BucketFactory.get("trademetestdata")
        def result = bw.addFile(new File("zip/20170411.zip"))

        then:
        result==true
        //manual test: check file is uploaded to trademetestdata bucket
    }
}
