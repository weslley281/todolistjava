package br.com.weslleyferraz.todolistjava.filter;

import br.com.weslleyferraz.todolistjava.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var serveletPath = request.getServletPath();
        if (serveletPath.equals("/tasks/")) {
            var authorization = request.getHeader("Authorization");

            System.out.println("Authorazation: " + authorization);
            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            var authString = new String(authDecode);

            System.out.println("AuthEncoded: " + authEncoded);
            System.out.println("AuthDecoded: " + authDecode);
            System.out.println("AuthString: " + authString);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            System.out.printf("Usuário: %s, Senha: %s\n", username, password);

            var user = this.userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401, "Usuário sem autorização");
            } else {
                var passwordVerify = BCrypt.checkpw(password, user.getPassword());
                if (passwordVerify) {
                    request.setAttribute("userId", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Usuário sem autorização");
                }
            }
        }else {
            filterChain.doFilter(request, response);
        }
    }
}
