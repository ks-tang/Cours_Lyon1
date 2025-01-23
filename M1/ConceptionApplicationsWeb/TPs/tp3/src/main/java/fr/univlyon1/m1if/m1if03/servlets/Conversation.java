package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Message;
import fr.univlyon1.m1if.m1if03.classes.Salon;
//import fr.univlyon1.m1if.m1if03.daos.UserDao;
import fr.univlyon1.m1if.m1if03.exceptions.SalonNotFoundException;
import jakarta.servlet.ServletConfig;
//import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NameNotFoundException;
import java.io.IOException;
//import java.util.List;

import fr.univlyon1.m1if.m1if03.daos.SalonDao;
import fr.univlyon1.m1if.m1if03.daos.MessageDao;

/**
 * Cette servlet gère la conversation dans le chat.
 * Elle récupère les nouveaux messages et affiche la liste des messages.
 */
@WebServlet(name = "Conversation", value = "/conversation")
public class Conversation extends HttpServlet {
    //private List<Message> messages;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //ServletContext context = config.getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Place le salon dans un attribut de session
            request.getSession().setAttribute("salon", getSalon(request));
            // Transfère la gestion de l'interface à une JSP
            request.getRequestDispatcher("conversation.jsp").forward(request, response);
        } catch (SalonNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Salon salon = getSalon(request);
            MessageDao messagedao = (MessageDao) this.getServletContext().getAttribute("messageDao");

            // Crée un nouveau message et l'ajoute au salon
            Message m = new Message(request.getParameter("login"), request.getParameter("text"), request.getParameter("salon"));
            messagedao.createMessage(m);
            salon.addMessage(m);
            // Reprend le comportement des requêtes GET
            doGet(request, response);
        } catch (SalonNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Retrouve un salon dans la jungle des DAOs à partir des informations contenues dans la requête.
     * @param request Requête HTTP qui doit contenir soit des paramètres <code>owner</code> et <code>salon</code>,
     *                soit une session utilisateur avec un attribut <code>salon</code> déjà positionné
     * @return l'instance de <code>Salon</code> dont on veut la conversation
     * @throws SalonNotFoundException Si les paramètres n'existent pas, ne correspondent pas, et si aucun attribut de session n'est positionné
     */
    private Salon getSalon(HttpServletRequest request) throws SalonNotFoundException {
        SalonDao dao = (SalonDao) this.getServletContext().getAttribute("salonDao");
        Salon salon;
        try {
            salon = dao.findOne(request.getParameter("salon"));
            
        } catch (NameNotFoundException | NullPointerException e) {
            salon = (Salon) request.getSession().getAttribute("salon");
        }
        if(salon != null) {
            return salon;
        }
        throw new SalonNotFoundException("Impossible d'identifier le salon," +
                " soit parce que les paramètres passés à la requête ne le permettent pas, soit parce que ces paramètres sont absents.");
    }
}
