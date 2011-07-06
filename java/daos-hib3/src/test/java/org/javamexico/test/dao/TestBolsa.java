package org.javamexico.test.dao;

import org.javamexico.dao.BolsaTrabajoDao;
import org.javamexico.entity.bolsa.Empresa;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/** Pruebas del DAO de bolsa de trabajo.
 *
 * Date: Dec 8, 2010 Time: 11:29:58 AM
 *
 * @author Enrique Zamudio
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/org/javamexico/test/dao/spring.xml"})
public class TestBolsa {
    @Resource(name="bolsaDao")
    private BolsaTrabajoDao bdao;

    //Activar esta prueba solamente para ponerle password "prueba" a todas las empresas
    @Test
    public void cambiaPasswords() {
        List<Empresa> todas = bdao.getEmpresas();
        for (Empresa e : todas) {
            e.setPassword("prueba");
            bdao.update(e);
        }
    }

}
