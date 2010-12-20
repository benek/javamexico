package org.javamexico.site.components;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;
import org.apache.tapestry5.BindingConstants;
import org.javamexico.site.base.Pagina;

/**
 * Layout component for pages of application JavaMexico.
 */
@Import(stylesheet="context:layout/layout.css")
public class Layout extends Pagina {
    /** The page title, for the <title> element and the <h1> element. */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    @SuppressWarnings("unused")
    @Property private String title;

    @Property private String pageName;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @SuppressWarnings("unused")
    @Property private String sidebarTitle;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    @SuppressWarnings("unused")
    @Property private Block sidebar;

    @Inject
    private ComponentResources resources;
    @Inject
    private Request req;

    public String getClassForPageName()
    {
      return resources.getPageName().equalsIgnoreCase(pageName)
             ? "current_page_item"
             : null;
    }

    public String[] getPageNames()
    {
      return new String[] { "Index", "Foros", "Preguntas", "Bolsa" };
    }

    void onActionFromLogout() {
    	Session sesion = req.getSession(false);
    	if (sesion != null) {
    		sesion.invalidate();
    	}
    }

}
