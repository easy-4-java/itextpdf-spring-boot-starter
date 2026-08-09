/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jeefw.cloud.htmltox;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Html2PdfUtil}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
@DisplayName("Html2PdfUtil Tests")
class Html2PdfUtilTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Instance can be created via constructor")
    void testInstantiation() {
        Html2PdfUtil instance = new Html2PdfUtil();
        assertThat(instance).isNotNull();
    }

    @Test
    @DisplayName("createPdf generates a PDF file from HTML content")
    void testCreatePdf() throws Exception {
        String html = "<html><body><h1>Hello World</h1><p>Test content</p></body></html>";
        String dest = tempDir.resolve("test.pdf").toString();

        Html2PdfUtil.createPdf(html, dest);

        Path pdfPath = Path.of(dest);
        assertThat(Files.exists(pdfPath)).isTrue();
        assertThat(Files.size(pdfPath)).isGreaterThan(0);
    }

    @Test
    @DisplayName("createPdf handles page breaks in HTML")
    void testCreatePdfWithPageBreak() throws Exception {
        String html = "<html><body><p>Page 1</p><div style='page-break-before: always;'></div><p>Page 2</p></body></html>";
        String dest = tempDir.resolve("pagebreak.pdf").toString();

        Html2PdfUtil.createPdf(html, dest);

        Path pdfPath = Path.of(dest);
        assertThat(Files.exists(pdfPath)).isTrue();
        assertThat(Files.size(pdfPath)).isGreaterThan(0);
    }
}
