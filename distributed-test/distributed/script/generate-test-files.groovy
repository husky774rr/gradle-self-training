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

import java.nio.file.*

def self = Paths.get('build.gradle').toAbsolutePath().parent

if (!self.endsWith('distributed')) {
    throw new IllegalStateException("Please execute this script at root directory. expected: [distributed], current: [${self.fileName}]")
}

def srcDir = self.resolve('src/test/java')
def rootPackage = 'sample.tests'

def names = [
        sheepdog: true,
        foxhound: true,
        bulldog: false,
        chihuahua: false,
        dalmatian: true
]

def asTypeName = {String name ->
    "${name.substring(0,1).toUpperCase()}${name.substring(1)}"
}

names.each {pkg ->
    def packageName = "$rootPackage.${pkg.key}"
    def dir = srcDir.resolve(packageName.replace('.','/'))
    names.each {name ->
        def type = "${asTypeName(name.key)}Test"
        def time = name.value ? '200l' : '3000l'
        def classContents = """
package ${packageName};

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import sample.TestTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ${type} {

    @Rule
    public final TestTime testTime = new TestTime();

    @After
    public void teardown() {
        System.out.println(testTime.getStamp());
    }

    @Test
    public void testName() throws InterruptedException {
        Thread.sleep(${time});
        assertThat(testTime.getTestName())
                .isEqualTo("testName");
    }

    @Test
    public void className() throws InterruptedException {
        Thread.sleep(${time});
        assertThat(testTime.getClassName())
                .isEqualTo("${packageName}.${type}");
    }

    @Test
    public void stamp() throws InterruptedException {
        Thread.sleep(${time});
        assertThat(testTime.getStamp())
                .contains("${packageName}.${type}#stamp");
    }
}
"""
        if (!Files.exists(dir)) {
            Files.createDirectories(dir)
        }
        dir.resolve("${type}.java").toFile().write(classContents, 'UTF-8')
    }
}
