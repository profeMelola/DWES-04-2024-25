package es.daw.jakarta.services;

import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;

public class LoginServiceImpl implements LoginService{

    @Override
    public Optional<String> getUserName(HttpServletRequest request) {

        HttpSession session = request.getSession();
        
        String username = (String) session.getAttribute("username");

        if (username != null)
            return Optional.of(username);

        return Optional.empty();

    }

    @Override
    public Optional<String> getAtributo(HttpServletRequest request, String nombreAtributo) {
        HttpSession session = request.getSession();
        String atributo = (String) session.getAttribute(nombreAtributo);

        if (atributo != null)
            return Optional.of(atributo);

        return Optional.empty();

    }
    
    
}
