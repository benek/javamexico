package org.javamexico.site.pages.bolsa;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.BolsaTrabajoDao;
import org.javamexico.entity.bolsa.Oferta;
import org.javamexico.entity.bolsa.Tag;
import org.javamexico.site.base.Pagina;

import java.util.List;

/** Pagina para ver los detalles de una oferta de trabajo.
 *
 * Date: Dec 8, 2010 Time: 12:57:13 PM
 *
 * @author Enrique Zamudio
 */
public class Ver extends Pagina {

    @Inject @Service("bolsaDao")
    private BolsaTrabajoDao bdao;
    @Property private Oferta oferta;
    @Property private Oferta otraof;
    @SuppressWarnings("unused")
    @Property private Tag tag;

    Object onActivate(String ids) {
        int ofid = 0;
        if (ids.indexOf('-') > 0) {
            ofid = Integer.parseInt(ids.substring(0, ids.indexOf('-')));
        } else {
            ofid = Integer.parseInt(ids);
        }
        if (ofid > 0) {
            oferta = bdao.getOferta(ofid);
        }
        if (oferta == null) {
            return Index.class;
        }
        return null;
    }

    Object onPassivate() {
        if (oferta == null) {
            return null;
        }
        return oferta.getOfid();
    }

    public boolean isOfertaVotada() {
        return oferta != null && getUserExists() && bdao.findVoto(getUser(), oferta) != null;
    }

    public void onActionFromVoteOfertaUp() {
        bdao.votaOferta(getUser(), oferta, true);
    }
    public void onActionFromVoteOfertaDown() {
        bdao.votaOferta(getUser(), oferta, false);
    }

    public List<Oferta> getMisOfertas() {
        return bdao.getOfertasConTagsUsuario(getUser().getTags());
    }
    public List<Oferta> getOfertasSimilares() {
        if (oferta.getTags().size() == 1) {
            return bdao.getOfertasConTag(oferta.getTags().iterator().next());
        } else {
            return bdao.getOfertasConTags(oferta.getTags());
        }
    }

    public boolean isOfertaDistinta() {
        return otraof == null || otraof.getOfid() != oferta.getOfid();
    }

}