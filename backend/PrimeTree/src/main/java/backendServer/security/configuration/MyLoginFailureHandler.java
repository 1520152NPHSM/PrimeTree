package backendServer.security.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


/**This Class overrides the default AuthenticationFailureHandler so the Client isn't redirected to a html page by the BackendServer 
 * after a failed authentication.*/
public class MyLoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
			throws IOException, ServletException {
		System.out.println("LoginFailure");
		System.out.println(authenticationException.getMessage());
		response.addHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Headers", "x-authors");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(authenticationException.getMessage());
	}

}
