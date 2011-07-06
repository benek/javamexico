package org.javamexico.test.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.javamexico.dao.PreguntaDao;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.pregunta.ComentPregunta;
import org.javamexico.entity.pregunta.ComentRespuesta;
import org.javamexico.entity.pregunta.Pregunta;
import org.javamexico.entity.pregunta.Respuesta;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Conjunto de pruebas unitarias para el modulo de preguntas (especificamente DAO).
 * 
 * @author Enrique Zamudio
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/org/javamexico/test/dao/spring.xml"})
public class TestPreguntas {

	@Resource(name="usuarioDao")
	private UserDao udao;
	@Resource(name="preguntaDao")
	private PreguntaDao qdao;

	@Test
	public void testPreguntas2() {
		//Preguntas por usuario
		long oldest = System.currentTimeMillis();
		for (Usuario u : udao.getAllUsers()) {
			List<Pregunta> pregs = qdao.getPreguntasUsuario(u);
			System.out.printf("Usuario %s tiene %d preguntas%n", u.getUsername(), pregs.size());
			//Aqui vamos obteniendo la pregunta mas vieja
			for (Pregunta q : pregs) {
				if (q.getFechaPregunta().getTime() < oldest) {
					oldest = q.getFechaPregunta().getTime();
				}
				printPregunta(qdao.getPregunta(q.getPid()));
			}
		}
		//Vamos a obtener todas excepto la mas vieja
		List<Pregunta> pregs = qdao.getPreguntasRecientes(1, 10);
		System.out.printf("Probando pregunta mas vieja (%s) solamente hubo %d%n", new Date(oldest), pregs.size());
		if( pregs.size() > 0 ) {
		    for (Pregunta q : pregs) {
			Assert.assertTrue(q.getFechaPregunta().getTime() > oldest);
		    }
                }
	}

	private void printPregunta(Pregunta p) {
		System.out.printf("Pregunta: %s%n%s%n", p.getTitulo(), p.getPregunta());
		System.out.printf("Por %1$s el %2$TF %2$TT%n", p.getAutor().getUsername(), p.getFechaPregunta());
		System.out.printf("Tags: %s%n", p.getTags());
		if (p.getComentarios() != null && p.getComentarios().size() > 0) {
			System.out.println("Comentarios:");
			for (ComentPregunta c : p.getComentarios()) {
				System.out.printf("%s: %s (%s)%n", c.getAutor().getUsername(), c.getComentario(), c.getFecha());
			}
		}
		System.out.println("Respuestas:");
		for (Respuesta r : p.getRespuestas()) {
			if (p.getRespuestaElegida() != null && p.getRespuestaElegida().intValue() == r.getRid()) {
				System.out.println("***** ELEGIDA: ");
			}
			System.out.printf("%s: %s (%s)%n", r.getAutor().getUsername(), r.getRespuesta(), r.getFecha());
			if (r.getComentarios() != null && r.getComentarios().size() > 0) {
				System.out.println("Comentarios a esta respuesta:");
				for (ComentRespuesta c : r.getComentarios()) {
					System.out.printf("%s: %s (%s)%n", c.getAutor().getUsername(), c.getComentario(), c.getFecha());
				}
			}
			System.out.println("-------------------------------------------------------------------");
		}
	}

	//@Test
	public void testPreguntas1() {
		List<Usuario> users = udao.getAllUsers();
		Usuario u1 = users.get(0);
		Usuario u2 = users.get(users.size() - 1);
		System.out.println("Creando una pregunta");
		Pregunta p = new Pregunta();
		p.setAutor(u1);
		p.setPregunta("Una pregunta de prueba, creada de manera automatica");
		p.setStatus(1);
		p.setTitulo("Titulo de pregunta de prueba");
		qdao.insert(p);
		qdao.addTag("jee", p);
		qdao.addTag("spring", p);
		qdao.addTag("hibernate", p);
		qdao.addTag("servlet", p);
		qdao.addTag("jsp", p);
		qdao.addTag("ant", p);
		Respuesta r = qdao.addRespuesta("Esta es una respuesta", p, u2);
		qdao.addComentario("Un comentario a la pregunta", p, u2);
		qdao.addComentario("Un comentario a la respuesta", r, u1);
		List<Pregunta> pregs = qdao.getPreguntasUsuario(u1);
		System.out.printf("Usuario %s tiene %d preguntas:%n", u1.getUsername(), pregs.size());
		for (Pregunta _p : pregs) {
			p = qdao.getPregunta(_p.getPid());
			printPregunta(p);
		}
		System.out.println("Borrando todo");
		qdao.delete(p);
	}

	//@Test
	public void testData() {
		Usuario u1 = udao.validaLogin("ezl", "test");
		Usuario u2 = udao.validaLogin("jb", "test");
		Usuario u3 = udao.validaLogin("domix", "test");
		assert u1 != null && u2 != null && u3 != null;
		Pregunta p = new Pregunta();
		p.setTitulo("Columnas agregadas en JPA/EJB3");
		p.setPregunta("Alguien sabe si se pueden definir columnas agregadas con anotaciones de EJB3/JPA?");
		p.setAutor(u1);
		p.setFechaPregunta(new Date(System.currentTimeMillis() - 86405430l));
		qdao.insert(p);
		qdao.addTag("jpa", p);
		qdao.addTag("ejb3", p);
		qdao.addTag("hibernate", p);
		qdao.addComentario("No entendi bien lo que quieres", p, u2);
		qdao.addComentario("Por ejemplo una columna que realmente sea un count(*) y obviamente es read-only", p, u1);
		qdao.addRespuesta("Con JPA solito no se puede eso, pero creo que Hibernate tiene unas anotaciones para poder hacerlo porque desde antes ya se podia", p, u2);
		Respuesta r = qdao.addRespuesta("Segun yo si se puede, pero no recuerdo con que anotaciones es", p, u3);
		qdao.addComentario("Chale pues cuando te acuerdes me dices, no?", r, u1);

		p = new Pregunta();
		p.setTitulo("Patron OSIV en Tapestry 5");
		p.setPregunta("Alguien sabe como se puede implementar el patron 'open session in view' en Tapestry 5?");
		p.setAutor(u2);
		p.setFechaPregunta(new Date(System.currentTimeMillis() - 89930125l));
		qdao.insert(p);
		qdao.addTag("tapestry5", p);
		qdao.addTag("tapestry", p);
		qdao.addTag("open-session-in-view", p);
		r = qdao.addRespuesta("Si, hay una libreria tapestry-hibernate que da soporte precisamente para eso", p, u1);
		qdao.addComentario("Pero como funciona o que?", r, u3);
		qdao.addComentario("Te inyecta un SessionFactory en la pagina y lo cierra al final del ciclo request-response", p, u1);
		qdao.vota(u2, r, true);
		p.setRespuestaElegida(r.getRid());
		qdao.update(p);
	}

}
