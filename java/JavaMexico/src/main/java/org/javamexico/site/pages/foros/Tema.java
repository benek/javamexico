package org.javamexico.site.pages.foros;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.javamexico.dao.ForoDao;
import org.javamexico.entity.foro.Foro;
import org.javamexico.entity.foro.TemaForo;
import org.slf4j.Logger;

/** Muestra los foros de un mismo tema.
 * 
 * @author Enrique Zamudio
 */
public class Tema {

	@Inject private Logger log;
	@Inject @Service("foroDao")
	private ForoDao fdao;
	@Inject private Request request;
	@Property private TemaForo tema;
    @SuppressWarnings("unused")
	@Property private Foro foro;
	@Property private int pagina = 1;
    @SuppressWarnings("unused")
	@Property private boolean lastPage;

	void onActivate(int tid) {
		tema = fdao.getTema(tid);
	}

	int onPassivate() {
		return tema == null ? 0 : tema.getTid();
	}

	public List<Foro> getForos() {
		if (request.getParameter("pg") != null) {
			try {
				pagina = Integer.parseInt(request.getParameter("pg"));
			} catch (NumberFormatException ex) {
				pagina = 1;
			}
		}
		log.info("pagina {}", pagina);
		List<Foro> rval = fdao.getForosConTema(tema, pagina, 20);
		lastPage = rval.size() < 20;
		return rval;
	}

}
