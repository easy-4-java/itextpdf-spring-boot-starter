package com.itextpdf.spring.boot;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;

/**
 * Spring Boot auto-configuration for iText 7's HTML-to-PDF conversion.
 * <p>
 * Activates when {@link HtmlConverter} is on the classpath and
 * {@code spring.itext.html2pdf.enabled=true}, exposing a default
 * {@link FontProvider} and {@link ConverterProperties} that other components
 * (such as {@code Html2PdfUtil}) can inject to render PDFs from HTML.
 * </p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(HtmlConverter.class)
@ConditionalOnProperty(prefix = Html2pdfProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties(Html2pdfProperties.class)
public class Html2pdfAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * Creates the default {@link FontProvider} used by the HTML-to-PDF
	 * converter, unless the application has defined its own bean.
	 *
	 * @return a {@link DefaultFontProvider} instance
	 */
	@Bean
	@ConditionalOnMissingBean
	public FontProvider fontProvider() {
		FontProvider fontProvider = new DefaultFontProvider();
		return fontProvider;
	}

	/**
	 * Creates the {@link ConverterProperties} bean consumed by
	 * {@link HtmlConverter}.
	 *
	 * @return a new {@link ConverterProperties} instance
	 */
	@Bean
	public ConverterProperties converterProperties() {
		ConverterProperties props = new ConverterProperties();
		// props.setCharset("UFT-8"); encoding
		return props;
	}

	/**
	 * Stores the {@link ApplicationContext} injected by Spring for later lookup.
	 *
	 * @param applicationContext the running application context
	 * @throws BeansException never thrown by this implementation
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Returns the application context previously injected by Spring.
	 *
	 * @return the stored application context
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
