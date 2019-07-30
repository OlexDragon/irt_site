
package irt.controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class RestFileWorkerTest {
	private final Logger logger = LogManager.getLogger();

	private String libPath = "C:\\ApacheTomcat\\webapps";

	@Test
	public void test() {
		String filename = "Converter-bias ASSY-C-Band-01.zip";
		final Path root = Paths.get(libPath, "irt", "lib", "AAA");
		final Path path = Paths.get(root.toString(), filename);
		File f = path.toFile();
		final String link = Paths.get(libPath).relativize(path).toUri().toString();
		logger.trace("\n filename\t= {}\n root\t= {}\n path\t= {}\n f\t= {}\n link\t= {}", filename, root, path, f, link);
	}

}
