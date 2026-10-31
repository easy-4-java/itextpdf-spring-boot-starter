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
* by 明明如月 github :https://github.com/chujianyun
*/
 
public class Html2PdfUtil {
 
    /**
     * 字体所在目录
     */
    private static final String FONT_RESOURCE_DIR = "/font";
 
    /**
     * @param htmlContent html文本
     * @param dest        目的文件路径，如 /xxx/xxx.pdf
     * @throws IOException IO异常
     */
    public static void createPdf(String htmlContent, String dest) throws IOException {
        ConverterProperties props = new ConverterProperties();
        // props.setCharset("UFT-8"); 编码
        FontProvider fp = new FontProvider();
        fp.addStandardPdfFonts();
        // .ttf 字体所在目录
        String resources = Html2PdfUtil.class.getResource(FONT_RESOURCE_DIR).getPath();
        fp.addDirectory(resources);
        props.setFontProvider(fp);
        // html中使用的图片等资源目录（图片也可以直接用url或者base64格式而不放到资源里）
        // props.setBaseUri(resources); 
 
        List<IElement> elements = HtmlConverter.convertToElements(htmlContent, props);
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf, PageSize.A4.rotate(), false);
        for (IElement element : elements) {
            // 分页符
            if (element instanceof HtmlPageBreak) {
                document.add((HtmlPageBreak) element);
 
            //普通块级元素
            } else {
                document.add((IBlockElement) element);
            }
        }
        document.close();
    }
}
// ————————————————
// 版权声明：本文为CSDN博主「明明如月小角落」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
// 原文链接：https://blog.csdn.net/w605283073/article/details/83352856