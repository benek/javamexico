package org.javamexico.test.dao;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.javamexico.dao.ForoDao;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.foro.ComentForo;
import org.javamexico.entity.foro.Foro;
import org.javamexico.entity.foro.TagForo;
import org.javamexico.entity.foro.TemaForo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/org/javamexico/test/dao/spring.xml", "/org/javamexico/test/dao/postgres.xml"})
public class TestForos {

	@Resource(name="usuarioDao")
	private UserDao udao;
	@Resource(name="foroDao")
	private ForoDao fdao;

	private void printForos(List<Foro> foros) {
		for (Foro f : foros) {
			System.out.printf("%s (creado el %s por %s) bajo '%s' - %d votos%n", f.getTitulo(), f.getFecha(),
					f.getAutor().getNombre(), f.getTema().getTema(), f.getVotos());
		}
	}

	@Test
	public void testForos1() {
		Usuario u1 = udao.validaLogin("uname1", "password");
		List<Foro> list = null;
		System.out.println("Foros recientes:");
		list = fdao.getForosRecientes(1, 10);
		printForos(list);
		System.out.printf("Foros de %s%n", u1.getNombre());
		list = fdao.getForosByUser(u1, false);
		printForos(list);
		List<TemaForo> temas = fdao.getTemas();
		if (temas.size() > 0) {
			list = fdao.getForosConTema(temas.get(0), 1, 10);
			System.out.printf("Foros con tema '%s':%n", temas.get(0).getTema());
			printForos(list);
		}
	}

	@Test
	public void testForos2() {
		Usuario u1 = udao.validaLogin("uname1", "password");
		Usuario u2 = udao.validaLogin("uname2", "password");
		TemaForo tema = fdao.getTemas().get(0);
		Foro foro = new Foro();
		foro.setAutor(u1);
		foro.setTema(tema);
		foro.setTitulo("Foro de prueba");
		foro.setTexto("Este es un foro de prueba, creado desde la prueba unitaria de foros");
		fdao.insert(foro);
		fdao.addTag("threads", foro);
		ComentForo com = fdao.addComment("Comentario de prueba", foro, u2);
		fdao.addComment("Respuesta al comentario de prueba", com, u1);
	}

	@Test
	public void testForos3() {
		Usuario u1 = udao.validaLogin("uname1", "password");
		Usuario u2 = udao.validaLogin("uname2", "password");
		Usuario u3 = udao.validaLogin("domix", "test");
		Usuario u4 = udao.validaLogin("jb", "test");
		List<Foro> list = fdao.getForosRecientes(1, 10);
		Assert.assertFalse("Debe haber foros con status > 0", list.size() == 0);
		fdao.vota(u2, list.get(0), true);
		fdao.vota(u1, list.get(1), true);
		fdao.vota(u3, list.get(0), true);
		Foro foro = list.get(1);
		List<ComentForo> coms = fdao.getComentarios(foro, 1, 10, false);
		Assert.assertFalse(String.format("El foro con ID %d debe tener al menos 1 comentario con 1 respuesta",
				foro.getFid()), coms.size() == 0);
		fdao.vota(u4, coms.get(0), true);
		Set<ComentForo> resps = fdao.getRespuestas(coms.get(0));
		fdao.vota(u1, resps.iterator().next(), false);
	}

	//@Test
	public void testForos4() {
		List<TagForo> poptags = fdao.getTagsPopulares(10);
		System.out.printf("Los tags mas populares son %s%n", poptags);
		if (poptags.size() > 0) {
			List<Foro> list = fdao.getForosConTag(poptags.get(0));
			System.out.printf("Hay %d foros con %s%n", list.size(), poptags.get(0));
			printForos(list);
		}
	}

	/** Este metodo crea datos de prueba; debe ejecutarse una sola vez en una base de datos que no tenga foros,
	 * para probar el modulo. */
	//@Test
	public void testData() {
		
	}

}
