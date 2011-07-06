package org.javamexico.test;

import javax.annotation.*;
import org.slf4j.*;
import java.io.*;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;

public class Setup {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private SimpleJdbcTemplate jdbc;
	@Resource(name="usuarioDao")
	private UserDao udao;

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
		poblarUsuarios();
	}

	protected void poblarUsuarios() {
		Usuario u = new Usuario();
		u.setNombre("Usuario de prueba 1");
		u.setUsername("uname1");
		u.setPassword("password");
		u.setReputacion(20);
		u.setEmail("testmail1@test.com");
		u.setStatus(1);
		udao.insert(u);
		u = new Usuario();
		u.setNombre("Segundo usuario de prueba");
		u.setUsername("uname2");
		u.setPassword("password");
		u.setEmail("test2@test.com");
		u.setReputacion(50);
		u.setStatus(1);
		u.setVerificado(true);
		udao.insert(u);
		u = new Usuario();
		u.setNombre("Domingo Suarez");
		u.setUsername("domix");
		u.setEmail("domix@javamexico.org");
		u.setPassword("test");
		u.setReputacion(40);
		u.setStatus(1);
		udao.insert(u);
		u = new Usuario();
		u.setNombre("Eric Camacho");
		u.setUsername("ecamacho");
		u.setPassword("test");
		u.setEmail("erick@javamexico.org");
		u.setReputacion(30);
		u.setStatus(1);
		udao.insert(u);
		u = new Usuario();
		u.setNombre("Javier Benek");
		u.setUsername("jb");
		u.setEmail("benek@javamexico.org");
		u.setPassword("test");
		u.setReputacion(80);
		u.setStatus(1);
		udao.insert(u);
		u = new Usuario();
		u.setNombre("Enrique Zamudio");
		u.setEmail("ezl@javamexico.org");
		u.setUsername("ezl");
		u.setPassword("test");
		u.setReputacion(50);
		u.setStatus(1);
		udao.insert(u);
	}

}
