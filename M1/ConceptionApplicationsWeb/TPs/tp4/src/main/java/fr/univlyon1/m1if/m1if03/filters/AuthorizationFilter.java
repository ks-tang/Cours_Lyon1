package fr.univlyon1.m1if.m1if03.filters;

import fr.univlyon1.m1if.m1if03.model.Salon;
import fr.univlyon1.m1if.m1if03.model.User;
import fr.univlyon1.m1if.m1if03.model.Message;
import fr.univlyon1.m1if.m1if03.dao.SalonDao;
import fr.univlyon1.m1if.m1if03.dao.MessageDao;
import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Filtre d'autorisation basique.<br>
 * Vérifie que l'utilisateur authentifié a le droit d'accéder à certaines ressources.
 * Renvoie un code 403 (Forbidden) sinon.
 *
 * @author Lionel Médini
 */
@WebFilter
public class AuthorizationFilter extends HttpFilter {

    public void init(FilterConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String[] url = UrlUtils.getUrlParts(request);
        SalonDao salonDao = (SalonDao) this.getServletContext().getAttribute("salonDao");
        MessageDao messageDao = (MessageDao) this.getServletContext().getAttribute("messageDao");
        UserDao userDao = (UserDao) this.getServletContext().getAttribute("userDao");

        if(url.length >= 2 && (url[1].equals("login") || url[1].equals("logout"))) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if(url.length >= 2) {
                User user = (User)(userDao.findOne((String)request.getAttribute("loggedUser")));
                request.setAttribute("user", user);
                List<String> userSalonMember = parseString((String)request.getAttribute("loggedUserSalons"));
                List<String> userSalonOwned = parseString((String)request.getAttribute("loggedUserOwnedSalons"));
                request.setAttribute("userSalonMember", userSalonMember);
                request.setAttribute("userSalonOwned", userSalonOwned);

                switch (url[0]) {
                    case "users":
                        List<String> listUserCase = new ArrayList<String>();
                        listUserCase.add("name");
                        listUserCase.add("ownedSalons");
                        if(!request.getMethod().equals("GET") || user.getLogin().equals(url[1]) || (url.length >= 3 && listUserCase.contains(url[2]))) {
                            chain.doFilter(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Utilisateur et login ne correspondent pas");
                        }
                        break;
                    
                    case "salons":
                        Salon salon = salonDao.findOne(Integer.valueOf(url[1]));
                        if ((request.getMethod().equals("GET") && userSalonMember.contains(salon.getId().toString())) || 
                                (salon.getOwner().equals(user.getLogin()) && !(request.getMethod().equals("GET")))) {
                            chain.doFilter(request, response);
                        } else {
                            if(request.getMethod().equals("GET")) {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas membre de ce salon.");
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'êtes pas propriétaire de ce salon.");
                            }
                        }
                        break;
                    case "messages":
                        Message message = messageDao.findOne(Integer.valueOf(url[1]));
                        if(request.getMethod().equals("GET")) {
                            try {
                                Salon salonMessage = salonDao.findOne(message.getSalon());
                                if(salonMessage.getOwner().equals(user.getLogin()) || salonMessage.hasMember(user.getLogin())) {
                                    chain.doFilter(request, response);
                                } else {
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Vous n'appartenez pas à ce salon.");
                                }
                            } catch (NameNotFoundException e) {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Salon est introuvable");
                            } catch (InvalidNameException | NumberFormatException e) {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Nom du salon est invalide");
                            }
                        } else {
                            if(user.getLogin().equals(message.getAuthor())) {
                                    chain.doFilter(request, response);
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Ce message ne vous appartient pas");
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else {
               chain.doFilter(request, response);
            }       
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ce salon n'existe pas.");
        } catch (InvalidNameException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, request.getParameter("salon") + " n'est pas valide.");
        }
    }

    private static List<String> parseString(String toParse) {
        List<String> res = new ArrayList<>();
        if(!toParse.isEmpty()) {
            Collections.addAll(res, toParse.split(","));
        }
        return res;
    }

}
