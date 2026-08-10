/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
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

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.layout.HtmlPageBreak;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;
 
import java.io.IOException;
import java.util.List;
 
/**
 * Utility for converting HTML content into PDF documents using iText 7.
 * <p>
 * Loads fonts from a bundled {@code /font} resource directory, splits the
 * HTML into iText elements via {@link HtmlConverter} and renders them onto a
 * landscape A4 {@link Document}, honouring explicit page breaks.
 * </p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */

public class Html2PdfUtil {

    /**
     * Classpath directory that bundles the {@code .ttf} font files used during
     * conversion.
     */
    private static final String FONT_RESOURCE_DIR = "/font";

    /**
     * Converts the supplied HTML fragment into a PDF file written to
     * {@code dest}.
     * <p>
     * Standard PDF fonts are registered together with every font found under
     * {@link #FONT_RESOURCE_DIR}. The HTML is converted to a list of iText
     * elements and added to a landscape A4 document, with
     * {@link HtmlPageBreak} elements treated as explicit page breaks.
     * </p>
     *
     * @param htmlContent the HTML fragment to convert
     * @param dest        absolute output file path, e.g. {@code /xxx/xxx.pdf}
     * @throws IOException if the font directory or output file cannot be read
     *                     or written
     */
    public static void createPdf(String htmlContent, String dest) throws IOException {
        ConverterProperties props = new ConverterProperties();
        // props.setCharset("UFT-8"); encoding
        FontProvider fp = new FontProvider();
        fp.addStandardPdfFonts();
        // Directory containing the .ttf fonts
        String resources = Html2PdfUtil.class.getResource(FONT_RESOURCE_DIR).getPath();
        fp.addDirectory(resources);
        props.setFontProvider(fp);
        // Base URI for images and other resources referenced by the HTML
        // (images may also be referenced by URL or as base64 instead)
        // props.setBaseUri(resources);

        List<IElement> elements = HtmlConverter.convertToElements(htmlContent, props);
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf, PageSize.A4.rotate(), false);
        for (IElement element : elements) {
            // Page break
            if (element instanceof HtmlPageBreak) {
                document.add((HtmlPageBreak) element);

            // Regular block-level element
            } else {
                document.add((IBlockElement) element);
            }
        }
        document.close();
    }
}