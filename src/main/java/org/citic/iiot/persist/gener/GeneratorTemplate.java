package org.citic.iiot.persist.gener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.citic.iiot.persist.gener.*;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class GeneratorTemplate {

	private VelocityEngine velocityEngine;

	public GeneratorTemplate() {
		Properties properties = new Properties();
		properties.setProperty("resource.loader", "class");
		properties.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init(properties);
		this.velocityEngine = velocityEngine;
	}

	private Template getTemplate(String name) {
		return this.velocityEngine.getTemplate(name);
	}

	private String formatMapper(String content) {
		try {
			SAXBuilder builder = new SAXBuilder();

			Document doc = (Document) builder.build(new StringReader(content));
			Format format = Format.getCompactFormat();
			format.setIndent("    ");

			XMLOutputter serializer = new XMLOutputter();
			serializer.setFormat(format);

			return serializer.outputString(doc).replaceAll("<!-- .*? -->", "");
		} catch (Exception e) {
			return content;
		}
	}

	public void generateMapper(String mapperTemplate, File mapperFile) {
		try {
			StringWriter stringWriter = new StringWriter();
			getTemplate(mapperTemplate).merge(org.citic.iiot.persist.gener.GeneratorContext.getContext(), stringWriter);
			String content = formatMapper(stringWriter.toString());

			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(mapperFile));
			writer.write(content);

			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("Generate mapper error.", e);
		}
	}

	public void generateDomain(String className, String domainTemplate, File domainFile) {
		try {
			String serialVersionUID = "serialVersionUID";

			StringWriter stringWriter = new StringWriter();
			org.citic.iiot.persist.gener.GeneratorContext.setParameter(serialVersionUID, null);
			getTemplate(domainTemplate).merge(org.citic.iiot.persist.gener.GeneratorContext.getContext(), stringWriter);

			org.citic.iiot.persist.gener.GeneratorContext.setParameter(serialVersionUID,
					serialVersionUID(className, stringWriter.toString()));
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(domainFile));
			getTemplate(domainTemplate).merge(org.citic.iiot.persist.gener.GeneratorContext.getContext(), writer);

			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("Generate domain error.", e);
		}
	}

	public void generateDao(String daoTemplate, File daoFile) {
		try {
			StringWriter stringWriter = new StringWriter();
			getTemplate(daoTemplate).merge(org.citic.iiot.persist.gener.GeneratorContext.getContext(), stringWriter);
			String content = formatMapper(stringWriter.toString());

			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(daoFile));
			writer.write(content);

			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("Generate dao error.", e);
		}
	}

	private long serialVersionUID(String className, String sourceCode) {
		try {
			String packageName = org.citic.iiot.persist.gener.GeneratorContext.getDomainPackage();
			String fullName = packageName + "." + className;
			JavaFileObject javaFileObjects[] = new JavaFileObject[] { new StringJavaFileObject(
					fullName, sourceCode) };

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null,
					Locale.getDefault(), null);
			Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(javaFileObjects);

			String buildPath = org.citic.iiot.persist.gener.GeneratorContext.getBuildPath();

			List<String> compileOptions = new ArrayList<String>();
			compileOptions.addAll(Arrays.asList("-d", buildPath));

			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			CompilationTask compilerTask = compiler.getTask(null, stdFileManager, diagnostics,
					compileOptions, null, compilationUnits);

			if (!compilerTask.call()) {
				for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
					System.out.format("Error on line %d in %s", diagnostic.getLineNumber(),
							diagnostic);
				}
			}

			stdFileManager.close();

			URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File(buildPath)
					.toURI().toURL() });
			Class<?> fileClass = Class.forName(fullName, true, classLoader);

			return ObjectStreamClass.lookup(fileClass).getSerialVersionUID();
		} catch (Exception e) {
			return -1L;
		}
	}

	private class StringJavaFileObject extends SimpleJavaFileObject {

		private final String code;

		public StringJavaFileObject(String name, String code) {
			super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
					Kind.SOURCE);
			this.code = code;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return code;
		}

	}

}
