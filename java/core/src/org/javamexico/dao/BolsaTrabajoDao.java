package org.javamexico.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.javamexico.entity.TagUsuario;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.bolsa.Empresa;
import org.javamexico.entity.bolsa.Oferta;
import org.javamexico.entity.bolsa.Tag;
import org.javamexico.entity.bolsa.VotoOferta;

/** Define el comportamiento del DAO para el modulo de Bolsa de Trabajo.
 * 
 * @author Enrique Zamudio
 */
public interface BolsaTrabajoDao {

	public List<Oferta> getOfertasRecientes(Date desde);

	public List<Oferta> getOfertasConTag(Tag tag);
    public List<Oferta> getOfertasConTag(String tag);

	public List<Oferta> getOfertasConTags(Set<Tag> tags);

	public List<Oferta> getOfertasConTagsUsuario(Set<TagUsuario> tags);

    public Oferta getOferta(int ofid);

	public VotoOferta votaOferta(Usuario autor, Oferta oferta, boolean up);

	public VotoOferta findVoto(Usuario user, Oferta oferta);

    /** Devuelve una lista de todas las empresas registradas en el sistema. */
    public List<Empresa> getEmpresas();

    public void update(Empresa e);

    public void addTag(String tag, Oferta oferta);
    public List<Tag> findMatchingTags(String parcial);
    public List<Tag> getTagsPopulares(int max);

}
