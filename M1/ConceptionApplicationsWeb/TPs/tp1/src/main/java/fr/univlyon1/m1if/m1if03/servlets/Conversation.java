package fr.univlyon1.m1if.m1if03.servlets;

import fr.univlyon1.m1if.m1if03.classes.Message;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette servlet gère la conversation dans le chat.
 * Elle récupère les nouveaux messages et affiche la liste des messages.
 */
@WebServlet(name = "Conversation", value = "/conversation")
public class Conversation extends HttpServlet {
    private final List<Message> messages = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Génère "à la main" la page HTML de réponse
        PrintWriter out = response.getWriter();
        out.write("<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>messages</title>\n" +
                "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "    <meta http-equiv=\"refresh\" content=\"5\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table>");
        for(Message m: messages) {
            out.println("        <tr><td><em>" + m.getUser() + "</em>:</td><td>" + m.getText() + "</td></tr>");
        }
        out.write("    </table>\n" +
                "</body>\n" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // crée un nouveau message et l'ajoute à la liste
        Message m = new Message(request.getParameter("login"), request.getParameter("text"));
        messages.add(m);
        // Reprend le comportement des requêtes GET
        doGet(request, response);
    }
}
