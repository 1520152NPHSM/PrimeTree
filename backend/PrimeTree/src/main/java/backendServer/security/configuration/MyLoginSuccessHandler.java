package backendServer.security.configuration;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import backendServer.listings.Constants;
import backendServer.user.User;
import backendServer.user.service.UserManager;

/**This Class overrides the default AuthenticationSuccessHandler so the Client isn't redirected to a html page by the BackendServer 
 * after a successful authentication.*/
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired 
	UserManager userManager;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse httpServletResponse, Authentication arg2)
			throws IOException, ServletException {
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName() + " hat sich mit Spring Security eingeloggt");
		httpServletResponse.addHeader("Access-Control-Allow-Origin", arg0.getHeader("origin"));
		httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		httpServletResponse.addHeader("Content-Type", "application/json");
		
		PrintWriter writer=httpServletResponse.getWriter();
		User user=userManager.loadUserByUsername(arg2.getName());
		writer.write(user.toJSON().toString());
		writer.flush();
		writer.close();
	}

}
