package org.davilj.trademe.tasks

import spock.lang.Specification

import java.nio.file.Files

/**
 * Created by daniev on 18/04/17.
 */
class CompletedTasksTest extends Specification {
    def "test completed tasks"() {
        when:
        CompletedTasks ct = new CompletedTasks(new File("src/test/resources/CompletedTaskTest.ct"))
        def taskCompleted = ct.isCompleted("20170204");
        def taskNotCompleted = ct.isCompleted("20170205")

        then:
        taskCompleted==true
        taskNotCompleted==false
    }

    def "add a new completed task, update file"() {
        setup:
        File file = new File("src/test/resources/Empty.ct");
        if (file.exists())  {
            file.delete();
        }
        file.createNewFile();

        CompletedTasks ct = new CompletedTasks(file)

        when:
        def taskName = "12340101"
        def taskBefore = ct.isCompleted(taskName)
        ct.addCompletedTask(taskName)
        def taskAfter = ct.isCompleted(taskName)

        then:
        taskBefore==false
        taskAfter==true


    }


}
