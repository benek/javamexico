package org.javamexico.site.pages.bolsa;

import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.BolsaTrabajoDao;
import org.javamexico.entity.bolsa.Oferta;
import org.javamexico.entity.bolsa.Tag;
import org.javamexico.site.base.Pagina;

import java.util.Date;
import java.util.List;

/** Pagina principal del modulo de bolsa de trabajo.
 * 
 * @author Enrique Zamudio
 */
@IncludeStylesheet("context:layout/preguntas.css")
public class Index extends Pagina {

    @Inject @Service("bolsaDao")
    private BolsaTrabajoDao bdao;
    @Property private Oferta oferta;
    @Property private Tag tag;
    @Property private String stag;

    /** Esto se invoca cuando trae contexto la liga a esta pagina */
    void onActivate(String value) {
        stag = value;
    }

    public List<Oferta> getOfertas() {
        if (stag == null) {
            return bdao.getOfertasRecientes(new Date());
        } else {
            return bdao.getOfertasConTag(stag);
        }
    }

    public List<Tag> getPopTags() {
        return bdao.getTagsPopulares(10);
    }

    public List<Oferta> getMisOfertas() {
        return bdao.getOfertasConTagsUsuario(getUser().getTags());
    }

}
