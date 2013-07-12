/*
 * Copyright 2013 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gaiden.command

import spock.lang.Specification

class GaidenCleanSpec extends Specification {

    def "'execute' should clean the build directory"() {
        setup:
        def command = new GaidenClean()

        and:
        def buildDirectory = File.createTempDir()
        new File(buildDirectory, "dummy").write("dummy")
        assert buildDirectory.exists()

        when:
        command.execute(buildDirectory)

        then:
        !buildDirectory.exists()
    }

}
