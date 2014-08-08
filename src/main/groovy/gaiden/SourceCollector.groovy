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

import gaiden.util.PathUtils
import groovy.transform.CompileStatic
import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.nio.file.LinkOption
import java.nio.file.Path

/**
 * A source collector collects {@link PageSource} and builds {@link DocumentSource}.
 *
 * @author Kazuki YAMAMOTO
 * @author Hideki IGARASHI
 */
@Component
@CompileStatic
class SourceCollector {

    static final List<String> PAGE_SOURCE_EXTENSIONS = ['md', 'markdown']

    @Autowired
    GaidenConfig gaidenConfig

    /**
     * Collect {@link PageSource} and then returns {@link DocumentSource}.
     *
     * @return {@link DocumentSource}'s instance
     */
    DocumentSource collect() {
        new DocumentSource(pageSources: collectPageSources())
    }

    private List<PageSource> collectPageSources() {
        def pageSources = []
        PathUtils.eachFileRecurse(gaidenConfig.pagesDirectory, [gaidenConfig.outputDirectory, gaidenConfig.templateFile.parent]) { Path path ->
            if (isPageSourceFile(path)) {
                pageSources << createPageSource(path)
            }
        }
        pageSources
    }

    private static boolean isPageSourceFile(Path path) {
        FilenameUtils.getExtension(path.toString()) in PAGE_SOURCE_EXTENSIONS
    }

    private PageSource createPageSource(Path path) {
        def relativePath = gaidenConfig.pagesDirectory.relativize(path)
        def outputPath = gaidenConfig.outputDirectory.resolve(PathUtils.replaceExtension(relativePath, "html"))
        new PageSource(
            path: path,
            outputPath: outputPath,
            intputEncoding: gaidenConfig.inputEncoding,
        )
    }
}
