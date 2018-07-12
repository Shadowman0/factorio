package de.factorio.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

public class SourceFileWritingService {

	private Configuration freemarkerCfg;

	public SourceFileWritingService() {
		freemarkerCfg = new Configuration(new Version(2, 3, 20));
		freemarkerCfg.setClassForTemplateLoading(this.getClass(), "/");
		freemarkerCfg.setDefaultEncoding("UTF-8");
		freemarkerCfg.setLocale(Locale.US);
		freemarkerCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}

	private void createFiles(Input input) throws TemplateNotFoundException, MalformedTemplateNameException,
			ParseException, IOException, TemplateException {
		Template template = freemarkerCfg.getTemplate(input.getTemplate());
		FileWriter fileWriter = new FileWriter("example//d3test//recipes.json");
		template.process(input, fileWriter);
		fileWriter.close();
	}

	public void createFilesSafely(Input input) {
		Objects.requireNonNull(input);
		try {
			createFiles(input);
		} catch (IOException | TemplateException e) {
			System.out.println(e.toString());
		}

	}

}
