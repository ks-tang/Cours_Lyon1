package fr.univlyon1.m1if.m1if03.controller;

import fr.univlyon1.m1if.m1if03.model.Message;
import fr.univlyon1.m1if.m1if03.dao.MessageDao;
import fr.univlyon1.m1if.m1if03.dto.message.MessageDtoMapper;
import fr.univlyon1.m1if.m1if03.model.operations.MessageResource;
import fr.univlyon1.m1if.m1if03.dto.message.MessageResponseDto;
import fr.univlyon1.m1if.m1if03.dto.message.MessageRequestDto;
import fr.univlyon1.m1if.m1if03.utils.UrlUtils;
import fr.univlyon1.m1if.m1if03.utils.ContentNegotiationHelper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import javax.naming.InvalidNameException;
import javax.naming.NameNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MessagesController", urlPatterns = {"/messages", "/messages/*"})
public class MessagesController extends HttpServlet {
    private MessageDtoMapper messageMapper;
    private MessageResource messageResource;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        MessageDao messageDao = (MessageDao) config.getServletContext().getAttribute("messageDao");
        messageMapper = new MessageDtoMapper(config.getServletContext());
        messageResource = new MessageResource(messageDao);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String[] url = UrlUtils.getUrlParts(request);

        switch (url.length) {
            case 1: // Création d'un utilisateur
                // TODO Parsing des paramètres "old school" ; sera amélioré dans la partie négociation de contenus...
                MessageRequestDto message = (MessageRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, MessageRequestDto.class);
                try {
                    int messageId = messageResource.createMessage(Integer.toString(message.getSalon()), message.getAuthor(), message.getText());
                    response.setHeader("Location", "messages/" + messageId);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } catch (IllegalArgumentException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("directory", "message");
        String[] url = UrlUtils.getUrlParts(request);

        if (url.length == 1) { // Renvoie la liste de tous les utilisateurs
            List<Integer> messages = messageResource.readAll();
            List<String> messagesList = new ArrayList<>();
            for(Integer m : messages) {
                messagesList.add("messages/" + m.toString());
            }
            request.setAttribute("list", messagesList);
            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
            return;
        }
        try {
            Message message = messageResource.readOne(url[1]);
            MessageResponseDto messageDto = messageMapper.toDto(message);
            switch (url.length) {
                case 2: // Renvoie un DTO d'utilisateur (avec toutes les infos le concernant pour pouvoir le templater dans la vue)
                    request.setAttribute("dto", messageDto);
                    // Transfère la gestion de l'interface à une JSP
                    // Utilise un RequestDispatcher pour "transférer" la requête à un autre objet, en interne du serveur.
                    // Ceci n'est pas une redirection HTTP ; le client n'est pas informé de cette redirection et "voit" toujours la même URL.
                    // Note :
                    //     il existe deux méthodes pour transférer une requête (et une réponse) à l'aide d'un RequestDispatcher : include et forward
                    //     voir les différences ici : https://docs.oracle.com/javaee/6/tutorial/doc/bnagi.html
                    //request.getRequestDispatcher("/WEB-INF/components/message.jsp").include(request, response);
                    request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                    break;
                case 3: // Renvoie une propriété d'un utilisateur
                    switch (url[2]) {
                        case "author":
                            request.setAttribute("dto", new MessageResponseDto(messageDto.getAuthor(), null, null));
                            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                            break;
                        case "salon":
                            request.setAttribute("dto", new MessageResponseDto(null, messageDto.getSalon(), null));
                            //request.getRequestDispatcher("/WEB-INF/components/message.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                            break;
                        case "text":
                            request.setAttribute("dto", new MessageResponseDto(null, null, messageDto.getText()));
                            //request.getRequestDispatcher("/WEB-INF/components/message.jsp").include(request, response);
                            request.setAttribute("htmlPath", "/WEB-INF/components/users.jsp");
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    break;
                default: // Redirige vers l'URL qui devrait correspondre à la sous-propriété demandée (qu'elle existe ou pas ne concerne pas ce contrôleur)
                    // Construction de la fin de l'URL vers laquelle rediriger
                    String urlEnd = UrlUtils.getUrlEnd(request, 3);
                    switch (url[2]) {
                        case "author":
                            response.sendRedirect("users/" + message.getAuthor() + urlEnd);
                        case "memberOfSalons":
                            response.sendRedirect("salons/" + message.getSalon() + urlEnd);
                            break;
                        default:
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    }
            }
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (NameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le message " + url[1] + " n'existe pas.");
        } catch (InvalidNameException ignored) {
            // Ne devrait pas arriver car les paramètres sont déjà des Strings
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        String login = url[1];
        // TODO Parsing des paramètres "old school" ; sera amélioré dans la partie négociation de contenus...
        MessageRequestDto message = (MessageRequestDto) ContentNegotiationHelper.getDtoFromRequest(request, MessageRequestDto.class);

        if (url.length == 2) {
            try {
                messageResource.updateMessage(login, message.getText());
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (NameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (InvalidNameException ignored) {
                // Ne devrait pas arriver car les paramètres sont déjà des Strings
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] url = UrlUtils.getUrlParts(request);
        String login = url[1];
        if (url.length == 2) {
            try {
                messageResource.deleteMessage(login);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (IllegalArgumentException ex) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (NameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Le message " + login + " n'existe pas.");
            } catch (InvalidNameException ignored) {
                // Ne devrait pas arriver car les paramètres sont déjà des Strings
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }




}
