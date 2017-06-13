package backendServer.security.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**This Class overrides the default AccessDeniedHandler so the Client isn't redirected to a html page by the BackendServer 
 * after a denied access to a method*/
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		System.out.println("commence()");
		response.addHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Headers", "x-authors");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(authException.getMessage());
	}

}
