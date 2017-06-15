package backendServer.security.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**This Class overrides the default LogoutSuccessHandler so the Client isn't redirected to a html page by the BackendServer 
 * after a successful logout.*/
public class MyLogoutSuccessHandler extends
SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		System.out.println("onLogoutSuccess(...)");
		response.addHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Headers", "x-author");
		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Content-Type", "application/json");
		request.logout();
	}

}
