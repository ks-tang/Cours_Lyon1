package fr.univlyon1.m1if.m1if03.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Set;

import fr.univlyon1.m1if.m1if03.model.Salon;

/**
 * Classe qui centralise les opérations de validation et de génération d'un token "métier", c'est-à-dire dédié à cette application.
 *
 * @author Lionel Médini
 */
public final class ChatonsM1if03JwtHelper {
    private static final String SECRET = "monsecret2022";
    private static final String ISSUER = "Chatons M1IF03";
    private static final long LIFETIME = 1800000; // Durée de vie d'un token : 30 minutes ; vous pouvez le modifier pour tester
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    /**
     * Pour valider la règle <code>HideUtilityClassConstructorCheck</code> de CheckStyle...
     * Une classe utilitaire qui n'a que des méthodes statiques ne doit pas avoir de constructeur public, parce qu'il ne servirait à rien de l'instancier.
     */
    private ChatonsM1if03JwtHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * Vérifie l'authentification d'un utilisateur grâce à un token JWT.
     *
     * @param token le token à vérifier
     * @param req   la requête HTTP (nécessaire pour vérifier si l'origine de la requête est la même que celle du token
     * @return un booléen qui indique si le token est bien formé et valide (pas expiré) et si l'utilisateur est authentifié
     */
    public static String verifyToken(String token, @NotNull HttpServletRequest req) throws NullPointerException, JWTVerificationException {
        JWTVerifier authenticationVerifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .withAudience(getOrigin(req)) // Non-reusable verifier instance
                .build();

        authenticationVerifier.verify(token); // Lève une NullPointerException si le token n'existe pas, et une JWTVerificationException s'il est invalide
        DecodedJWT jwt = JWT.decode(token); // Pourrait lever une JWTDecodeException mais comme le token est vérifié avant, cela ne devrait pas arriver
        return jwt.getClaim("sub").asString();
    }

    /**
     * Vérifie dans le token si un user est membre d'un salon.
     *
     * @param token le token à vérifier
     * @param salonId l'id du salon dont on veut savoir si l'utilisateur est membre
     * @return un booléen indiquant si le token contient un booléen admin à true
     */
    public static boolean getMember(String token, String salonId) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).withClaimPresence("member").build();
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("member").asString().contains(salonId);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * Crée un token avec les caractéristiques de l'utilisateur.
     *
     * @param subject le login de l'utilisateur
     * @param member  la liste des salons dont l'utilisateur est member
     * @param req     la requête HTTP pour pouvoir en extraire l'origine avec getOrigin()
     * @return le token signé
     * @throws JWTCreationException si les paramètres ne permettent pas de créer un token
     */
    public static String generateToken(String subject, String member, String owned, HttpServletRequest req) throws JWTCreationException {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withAudience(getOrigin(req))
                .withClaim("member", member)
                .withClaim("owned", owned)
                .withExpiresAt(new Date(new Date().getTime() + LIFETIME))
                .sign(ALGORITHM);
    }

    /**
     * Renvoie l'URL d'origine du client, en fonction des headers de proxy (si existants) ou de l'URL de la requête sinon.
     *
     * @param request la requête HTTP
     * @return une String qui sera passée aux éléments de l'application pour générer les URL absolues.
     */
    private static String getOrigin(@NotNull HttpServletRequest request) {
        String origin = String.valueOf(request.getRequestURL()).substring(0, request.getRequestURL().lastIndexOf(request.getRequestURI()));
        if (request.getHeader("X-Forwarded-Host") != null && request.getHeader("X-Forwarded-Proto") != null && request.getHeader("X-Forwarded-Path") != null) {
            switch (request.getHeader("X-Forwarded-Proto")) {
                case "http":
                    origin = request.getHeader("X-Forwarded-Proto") + "://" +
                            (request.getHeader("X-Forwarded-Host").endsWith(":80") ? request.getHeader("X-Forwarded-Host").replace(":80", "") :
                                    request.getHeader("X-Forwarded-Host"));
                    break;
                case "https":
                    origin = request.getHeader("X-Forwarded-Proto") + "://" +
                            (request.getHeader("X-Forwarded-Host").endsWith(":443") ? request.getHeader("X-Forwarded-Host").replace(":443", "") :
                                    request.getHeader("X-Forwarded-Host"));
                default:
            }
            origin = origin + request.getHeader("X-Forwarded-Path");
        }
        return origin + request.getContextPath();
    }

    public static String setSalon(Set<Salon> salonsSet) {
        String res = "";
        int i = 0;
        for(Salon salon : salonsSet) {
            res += salon.getId().toString();
            if(i != salonsSet.size()) {
                res += ",";
            }
            i++;
        }
        return res;
    }
}
