package org.javamexico.dao;

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

    /** Devuelve las ofertas mas recientes (primero la mas reciente hacia la mas antigua) por fecha de alta. */
	public List<Oferta> getOfertasRecientes(int cuantas);
    /** Devuelve las ofertas con fecha de expiracion mas proxima (la mas proxima a expirar primero). */
    public List<Oferta> getOfertasPorExpirar(int cuantas);

    /** Devuelve las ofertas que tengan el tag especificado. */
	public List<Oferta> getOfertasConTag(Tag tag);
    /** Devuelve las ofertas que tengan especificado un tag con el nombre especificado. */
    public List<Oferta> getOfertasConTag(String tag);

    /** Devuelve las ofertas que tengan uno o varios de los tags especificados. */
	public List<Oferta> getOfertasConTags(Set<Tag> tags);

    /** Devuelve las ofertas que tienen tags con nombres iguales a los tags de usuario especificados. */
	public List<Oferta> getOfertasConTagsUsuario(Set<TagUsuario> tags);

    /** Devuelve la oferta con la llave indicada. */
    public Oferta getOferta(int ofid);

    /** Registra un voto de un usuario por una oferta; puede ser positivo o negativo. */
	public VotoOferta votaOferta(Usuario autor, Oferta oferta, boolean up);

    /** Busca el voto emitido por un usuario para una oferta. */
	public VotoOferta findVoto(Usuario user, Oferta oferta);

    /** Devuelve una lista de todas las empresas registradas en el sistema. */
    public List<Empresa> getEmpresas();

    public void update(Empresa e);

    public void addTag(String tag, Oferta oferta);
    public List<Tag> findMatchingTags(String parcial);
    public List<Tag> getTagsPopulares(int max);

    /** Devuelve todas las ofertas de una empresa, filtrando por publicadas o sin filtrar. */
    public List<Oferta> getOfertas(Empresa emp, boolean pubsOnly);

}
