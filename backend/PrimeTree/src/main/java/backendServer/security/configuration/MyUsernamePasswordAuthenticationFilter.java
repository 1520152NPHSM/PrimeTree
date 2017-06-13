package backendServer.security.configuration;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

/**This class changes the login-method for the outside so the username and the password are transported in the request-body
 * @author Florian Kutz
 *
 */
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String jsonUsername;
	private String jsonPassword;

	@Override
    protected String obtainPassword(HttpServletRequest request) {
        String password = null; 

        if ("application/json".equals(request.getHeader("Content-Type"))) {
        	password=this.jsonPassword;
            
        }else{
            password = super.obtainPassword(request);
        }

        return password;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request){
        String username = null;

        if ("application/json".equals(request.getHeader("Content-Type"))) {
        	username=this.jsonUsername;
        }else{
            username = super.obtainUsername(request);
        }

        return username;
    }
    
    @Override
    public synchronized Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            try {
                /*
                 * HttpServletRequest can be read only once
                 */
                StringBuffer sb = new StringBuffer();
                String line = null;

                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }

                //json transformation
                ObjectMapper mapper = new ObjectMapper();
                JSONObject loginRequest = new JSONObject(sb.toString());
                this.jsonUsername = loginRequest.getString("username");
                this.jsonPassword = loginRequest.getString("password");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.attemptAuthentication(request, response);
    }
    
    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationSuccessHandler loginSuccessHandler){
    	this.setRequiresAuthenticationRequestMatcher(
	    new AntPathRequestMatcher("/user/login","POST"));
    	this.setAuthenticationManager(authenticationManager);
    	this.setAuthenticationSuccessHandler(loginSuccessHandler);
    	this.setAuthenticationFailureHandler(new MyLoginFailureHandler());
    	this.setUsernameParameter("username");
    	this.setPasswordParameter("password");
    }
	
}
