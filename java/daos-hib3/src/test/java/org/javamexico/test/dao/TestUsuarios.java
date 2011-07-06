package org.javamexico.test.dao;

import javax.annotation.Resource;

import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Pruebas unitarias del DAO de usuarios.
 * 
 * @author Enrique Zamudio
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/org/javamexico/test/dao/spring.xml"})
public class TestUsuarios {

	@Resource(name="usuarioDao")
	private UserDao udao;

	@Test
	public void test1() {
		System.out.println("Creando usuarios de prueba");
		Usuario u1 = udao.validaLogin("uname1", "password");
		if (u1 == null) {
			u1 = new Usuario();
			u1.setNombre("Usuario de prueba 1");
			u1.setUsername("uname1");
			u1.setPassword("password");
			u1.setReputacion(20);
			u1.setEmail("testmail1@test.com");
			u1.setStatus(1);
			udao.insert(u1);
		} else {
			System.out.printf("Tags de %s: %s%n", u1.getUsername(), u1.getTags());
		}
		Usuario u2 = udao.validaLogin("uname2", "password");
		if (u2 == null) {
			u2 = new Usuario();
			u2.setNombre("Segundo usuario de prueba");
			u2.setUsername("uname2");
			u2.setPassword("password");
			u2.setEmail("test2@test.com");
			u2.setReputacion(50);
			u2.setStatus(1);
			u2.setVerificado(true);
			udao.insert(u2);
		} else {
			System.out.printf("Tags de %s: %s%n", u2.getUsername(), u2.getTags());
		}
		try {
			udao.addTag("spring", u1);
			udao.addTag("hibernate", u2);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try { Thread.sleep(1000); } catch (InterruptedException ex) {}
	}

	@Test //esta prueba genera datos que se quedan en la base de datos
	public void testData() {
		Usuario u = udao.validaLogin("ezl", "test");
		if (u == null) {
			u = new Usuario();
			u.setNombre("Enrique Zamudio");
			u.setEmail("ezl@javamexico.org");
			u.setUsername("ezl");
			u.setPassword("test");
			u.setReputacion(50);
			u.setStatus(1);
			udao.insert(u);
		}
		udao.addTag("tapestry", u);
		udao.addTag("spring", u);
		u = udao.validaLogin("jb", "test");
		if (u == null) {
			u = new Usuario();
			u.setNombre("Javier Benek");
			u.setUsername("jb");
			u.setEmail("benek@javamexico.org");
			u.setPassword("test");
			u.setReputacion(80);
			u.setStatus(1);
			udao.insert(u);
		}
		udao.addTag("jsf", u);
		udao.addTag("icefaces", u);
		udao.addTag("scala", u);
		u = udao.validaLogin("ecamacho", "test");
		if (u == null) {
			u = new Usuario();
			u.setNombre("Eric Camacho");
			u.setUsername("ecamacho");
			u.setPassword("test");
			u.setEmail("erick@javamexico.org");
			u.setReputacion(30);
			u.setStatus(1);
			udao.insert(u);
		}
		udao.addTag("spring", u);
		udao.addTag("groovy", u);
		udao.addTag("flex", u);
		u = udao.validaLogin("domix", "test");
		if (u == null) {
			u = new Usuario();
			u.setNombre("Domingo Suarez");
			u.setUsername("domix");
			u.setEmail("domix@javamexico.org");
			u.setPassword("test");
			u.setReputacion(40);
			u.setStatus(1);
			udao.insert(u);
		}
		udao.addTag("spring", u);
		udao.addTag("grails", u);
		udao.addTag("ria", u);
	}

}
