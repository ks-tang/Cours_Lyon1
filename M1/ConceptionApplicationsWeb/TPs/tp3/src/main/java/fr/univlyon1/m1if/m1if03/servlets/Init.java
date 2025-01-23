package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.daos.UserDao;
import fr.univlyon1.m1if.m1if03.daos.SalonDao;
import fr.univlyon1.m1if.m1if03.daos.MessageDao;
import fr.univlyon1.m1if.m1if03.daos.DemandeDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "Init", value = "/initial", loadOnStartup = 1)
public class Init extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        // Cette instruction doit toujours être au début de la méthode init() pour pouvoir accéder à l'objet config.
        super.init(config);

        //Récupère le contexte applicatif
        ServletContext context = config.getServletContext();

        // La map d'User est remplacée par un UserDao et placée dans le contexte
        context.setAttribute("userDao", new UserDao());
        context.setAttribute("salonDao", new SalonDao());
        context.setAttribute("messageDao", new MessageDao());
        context.setAttribute("demandeDao", new DemandeDao());
    }
}
