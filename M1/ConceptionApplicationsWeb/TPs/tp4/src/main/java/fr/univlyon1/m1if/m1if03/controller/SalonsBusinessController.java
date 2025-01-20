package fr.univlyon1.m1if.m1if03.controller;

import fr.univlyon1.m1if.m1if03.dao.UserDao;
import fr.univlyon1.m1if.m1if03.dto.salon.SalonBusinessRequestDto;
import fr.univlyon1.m1if.m1if03.dao.MessageDao;
import fr.univlyon1.m1if.m1if03.dao.SalonDao;
import fr.univlyon1.m1if.m1if03.model.Salon;
import fr.univlyon1.m1if.m1if03.model.User;
import fr.univlyon1.m1if.m1if03.model.operations.SalonBusiness;
import fr.univlyon1.m1if.m1if03.model.operations.SalonResource;
import fr.univlyon1.m1if.m1if03.utils.ChatonsM1if03JwtHelper;
import fr.univlyon1.m1if.m1if03.utils.ContentNegotiationHelper;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InvalidNameException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;

import java.io.IOException;

/**
 * Contrôleur délégué aux CU concernant les opérations métier sur les salons.
 *
 */
@WebServlet(name = "SalonsBusinessController", urlPatterns = {})
public class SalonsBusinessController extends HttpServlet {
    private SalonBusiness salonBusiness;
    private SalonResource salonResource;
    private SalonDao salonDao;

    //<editor-fold desc="Méthode de gestion du cycle de vie">
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        UserDao userDao = (UserDao) config.getServletContext().getAttribute("userDao");
        MessageDao messageDao = (MessageDao) config.getServletContext().getAttribute("messageDao");
        salonDao = (SalonDao) config.getServletContext().getAttribute("salonDao");
        salonBusiness = new SalonBusiness(userDao, messageDao);
        salonResource = new SalonResource(salonDao, userDao);
    }
    //</editor-fold>

    //<editor-fold desc="Méthode de service">
    /**
     * Appelle l'opération demandée à la fin de l'URL de la requête.
     * @param request  Voir doc...
     * @param response Voir doc...
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] url = UrlUtils.getUrlParts(request);
        SalonBusinessRequestDto business = (SalonBusinessRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, SalonBusinessRequestDto.class);
        try {
            Salon salon = salonResource.readOne(url[1]);
            salonBusiness.addMember(salon, business.getUser());
            String token = ChatonsM1if03JwtHelper.generateToken(salon.getOwner(), 
                            ChatonsM1if03JwtHelper.setSalon(salonDao.findByMember(salon.getOwner())), 
                            ChatonsM1if03JwtHelper.setSalon(salonDao.findByOwner(salon.getOwner())), request);
            response.setHeader("Authorization", token);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (InvalidNameException ignored) {
            // Ne devrait pas arriver car les paramètres sont déjà des Strings
        } catch (NameAlreadyBoundException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Le utilisateur " + business.getUser() + " est déjà membre du salon.");
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "L'utilisateur " + business.getUser() + " n'existe pas.");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Méthode de service">
    /**
     * Appelle l'opération demandée à la fin de l'URL de la requête.
     * @param request  Voir doc...
     * @param response Voir doc...
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // TODO : See if the parsing could be better
        String[] url = UrlUtils.getUrlParts(request);
        try {
            request.setAttribute("messages", salonBusiness.getSalonMessages(salonResource.readOne(url[1])));
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le salon " + url[1] + " n'existe pas.");
        } catch (InvalidNameException e) {
            // Ignored, shoudn't happen
        }
        request.setAttribute("htmlPath", "/WEB-INF/components/salon/salonContent.jsp");
    }
    //</editor-fold>

    //<editor-fold desc="Méthode de service">
    /**
     * Appelle l'opération demandée à la fin de l'URL de la requête.
     * @param request  Voir doc...
     * @param response Voir doc...
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // TODO : Parse better
        String userToDelete = request.getParameter("user");
        String[] url = UrlUtils.getUrlParts(request);

        try {
            Salon salon = salonResource.readOne(url[1]);
            salonBusiness.removeMember(salon, userToDelete);
            User user = (User)request.getAttribute("user");
            String token = ChatonsM1if03JwtHelper.generateToken(user.getLogin(), 
            ChatonsM1if03JwtHelper.setSalon(salonDao.findByMember(user.getLogin())), 
            ChatonsM1if03JwtHelper.setSalon(salonDao.findByOwner(user.getLogin())), request);
            response.setHeader("Authorization", token);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (InvalidNameException e) {
            // Ignored
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le salon " + url[1] + " n'existe pas.");
        }
    }
    //</editor-fold>
}
