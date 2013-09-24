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

package gaiden

import gaiden.command.CommandFactory
import gaiden.command.GaidenCommand

/**
 * A command line to execute Gaiden.
 *
 * @author Hideki IGARASHI
 * @author Kazuki YAMAMOTO
 */
class GaidenMain {

    static final String USAGE_MESSAGE = """\
Usage: gaiden <command>

   commands:
       build                          Build pages from source pages
       clean                          Clean the build directory
       create-project <project name>  Create the project directory
"""

    private static final String CONFIG_FILE_NAME = "GaidenConfig.groovy"

    private CommandFactory commandFactory = new CommandFactory()

    /**
     * A main command line interface.
     *
     * @param args all command line args
     */
    static void main(String... args) {
        new GaidenMain().run(args)
    }

    void run(String... args) {
        if (!args) {
            usage()
            System.exit(1)
        }

        def configFile = new File(CONFIG_FILE_NAME)
        Holders.config = new GaidenConfigLoader().load(configFile)

        executeCommand(args.first(), configFile, args.tail() as List)
    }

    void executeCommand(String commandName, File configFile, List args) {
        GaidenCommand command = createCommand(commandName)

        if (!command) {
            usage()
            System.exit(1)
        }

        if (command.onlyGaidenProject && !configFile.exists()) {
            System.err.println("ERROR: Not a Gaiden project (Cannot find ${configFile.name})")
            System.exit(1)
        }

        command.execute(args)
    }

    protected void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory
    }

    private GaidenCommand createCommand(String commandName) {
        commandFactory.createCommand(commandName)
    }

    private void usage() {
        System.err.println USAGE_MESSAGE
    }

}
