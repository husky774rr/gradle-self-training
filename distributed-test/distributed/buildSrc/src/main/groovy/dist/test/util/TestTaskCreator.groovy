/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dist.test.util

import dist.test.Names
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestTaskCreator {

    static final String GROUP = 'distributed test'

    private final Project project

    private final Names names;

    TestTaskCreator(Project project, Names names) {
        this.project = project
        this.names = names
    }

    private File file(Object name) {
        project.file(name)
    }

    String getTaskName() {
        "${names.breeds}Test"
    }

    void doWork() {
        def reportBaseDir = "${project.buildDir}/dist-test/${names.breeds}"
        project.tasks.create(this.taskName, Test).configure {
            group = GROUP
            description = "Runs tests under ${names.breeds} package."
            dependsOn project.tasks.findByName('testClasses')
            include "**/${names.breeds}/**/*"
            maxParallelForks = 2
            reports.html.destination = "${reportBaseDir}/html"
            reports.junitXml.destination = "${reportBaseDir}/xml"
            binResultsDir = file("${reportBaseDir}/bin")
        }
        project.tasks.findByName('test').configure {
            exclude "**/${names.breeds}/**/*"
        }
    }
}
