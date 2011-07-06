package org.javamexico.test.dao;

import java.util.List;

import javax.annotation.Resource;

import org.javamexico.dao.ForoDao;
import org.javamexico.dao.PreguntaDao;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.foro.ComentForo;
import org.javamexico.entity.foro.Foro;
import org.javamexico.entity.pregunta.Pregunta;
import org.javamexico.entity.pregunta.Respuesta;
import org.javamexico.util.PrivilegioInsuficienteException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** Pruebas unitarias para el sistema de votos de distintas entidades, que deben
 * modificar la reputacion de los usuarios.
 * 
 * @author Enrique Zamudio
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"/org/javamexico/test/dao/spring.xml"
})
public class TestVotos {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource(name="usuarioDao")
	private UserDao udao;
	@Resource(name="preguntaDao")
	private PreguntaDao qdao;
	@Resource(name="foroDao")
	private ForoDao fdao;

	@Test
	public void testVotosPreguntas() {
		List<Usuario> todos = udao.getAllUsers();
		//Recorremos todos los usuarios
		for (Usuario u : todos) {
			//Obtenemos todas las preguntas hechas por este usuario
			List<Pregunta> qus = qdao.getPreguntasUsuario(u);
			Pregunta q = null;
			//Tomamos una que tenga respuestas
			for (Pregunta p : qus) {
				if (q == null) {
					List<Respuesta> resps = qdao.getRespuestas(p, 1, 50, false);
					if (resps.size() > 0) {
						log.info("Vamos a probar con la pregunta {} de {}", p.getPid(), p.getAutor().getUid());
						q = p;
					}
				}
			}
			if (q != null) {
				int rep = u.getReputacion();
				//Ahora hacemos que todos los demas voten por la pregunta
				//Primero todos mal
				for (Usuario vot : todos) {
					if (vot.getUid() != u.getUid()) {
						try {
							qdao.vota(vot, q, false);
							rep--;
						} catch (PrivilegioInsuficienteException ex) {
							//no pasa nada, no afectamos reputacion
						}
					}
				}
				//La reputacion debe haber bajado
				Usuario u2 = udao.getUser(u.getUid());
				Assert.assertEquals("La reputacion no baja como debe", rep, u2.getReputacion());
				u = u2;
				rep = u.getReputacion();
				//Luego todos bien
				for (Usuario vot : todos) {
					if (vot.getUid() != u.getUid()) {
						try {
							qdao.vota(vot, q, true);
							rep+=2;
						} catch (PrivilegioInsuficienteException ex) {
						}
					}
				}
				//La reputacion debe haber subido
				u2 = udao.getUser(u.getUid());
				Assert.assertEquals("La reputacion no sube como debe", rep, u2.getReputacion());
			}
		}
	}

	@Test
	public void testVotosForos() {
		List<Usuario> todos = udao.getAllUsers();
		//Recorremos todos los usuarios
		for (Usuario u : todos) {
			//Obtenemos todas las preguntas hechas por este usuario
			List<Foro> foros = fdao.getForosByUser(u, false);
			Foro foro = null;
			//Tomamos una que tenga respuestas
			for (Foro f : foros) {
				if (foro == null) {
					List<ComentForo> coms = fdao.getComentarios(f, 1, 50, false);
					if (coms.size() > 0) {
						foro = f;
						log.info("Jugamos con foro {} de uid {}", foro.getFid(), foro.getAutor().getUid());
					}
				}
			}
			if (foro != null) {
				int rep = u.getReputacion();
				//Ahora hacemos que todos los demas voten por el foro
				//Primero todos mal
				for (Usuario vot : todos) {
					if (vot.getUid() != u.getUid()) {
						try {
							fdao.vota(vot, foro, false);
							rep--;
						} catch (PrivilegioInsuficienteException ex) {
							//no pasa nada
						}
					}
				}
				//La reputacion debe haber bajado
				Usuario u2 = udao.getUser(u.getUid());
				Assert.assertEquals("La reputacion no baja como debe", rep, u2.getReputacion());
				u = u2;
				rep = u.getReputacion();
				//Luego todos bien
				for (Usuario vot : todos) {
					if (vot.getUid() != u.getUid()) {
						try {
							fdao.vota(vot, foro, true);
							rep+=2;
						} catch (PrivilegioInsuficienteException ex) {
							//no pasa nada
						}
					}
				}
				//La reputacion debe haber subido
				u2 = udao.getUser(u.getUid());
				Assert.assertEquals("La reputacion no sube como debe", rep, u2.getReputacion());
			}
		}
	}

}
