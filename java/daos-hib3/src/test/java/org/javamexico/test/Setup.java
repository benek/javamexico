package org.javamexico.test;

import javax.annotation.*;
import org.slf4j.*;
import java.io.*;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class Setup {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private SimpleJdbcTemplate jdbc;
	private String scriptPath = "../../sql/tablas_h2_v1.sql";

	public void setScriptPath(String value) {
		scriptPath = value;
	}
	@PostConstruct
	public void createDatabase() throws IOException {
		File f = new File(scriptPath);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		StringBuilder sb = new StringBuilder();
		//Leemos lineas...
		String line = reader.readLine();
		while (line != null) {
			line = line.trim();
			int commpos = line.indexOf("--");
			if (commpos > 0) {
				line = line.substring(0, commpos).trim();
			}
			if (!line.startsWith("--")) {
				sb.append(line);
				if (line.endsWith(";")) {
					sb.deleteCharAt(sb.length()-1);
					jdbc.update(sb.toString());
					sb.setLength(0);
				} else {
					sb.append(' ');
				}
			}
			line = reader.readLine();
		}
		reader.close();
	}

}
