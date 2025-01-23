package fr.univlyon1.m1if.m1if03.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import fr.univlyon1.m1if.m1if03.utils.BufferlessHttpServletResponseWrapper;

/**
 * Filtre qui gère la négotiation de contenu, après la redirection par le controleur de ressource.
 *
 */
@WebFilter
public class ContentNegotiationFilter extends HttpFilter {

    /**
     * Vérifie qu'une requête est authentifiée, sauf requêtes autorisées.
     * @param request Voir doc...
     * @param response Voir doc...
     * @param chain Voir doc...
     * @throws IOException Voir doc...
     * @throws ServletException Voir doc...
     */
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse wrapper = new BufferlessHttpServletResponseWrapper(response);
        super.doFilter(request, wrapper, chain);
        if(request.getMethod().equals("GET") && response.getStatus() == HttpServletResponse.SC_OK) {
            String accept = request.getHeader("Accept");
            response.setHeader("Content-Type", accept);
            if (accept.equals("application/xml")) {
                XmlMapper mapper = new XmlMapper();
                if(request.getAttribute("dto") != null) {
                    mapper.writeValue(response.getWriter(), request.getAttribute("dto"));
                } else if(request.getAttribute("list") != null) {
                    mapper.writeValue(response.getWriter(), request.getAttribute("list"));
                }
            } else if (accept.equals("application/json")) {
                ObjectMapper mapper = new ObjectMapper();
                if(request.getAttribute("dto") != null) {
                    mapper.writeValue(response.getWriter(), request.getAttribute("dto"));
                } else if(request.getAttribute("list") != null) {
                    mapper.writeValue(response.getWriter(), request.getAttribute("list"));
                }
            } else if (accept.equals("text/html")) {
                request.getRequestDispatcher((String)request.getAttribute("htmlPath")).forward(request, response);
            }
            
        }
    }
}
