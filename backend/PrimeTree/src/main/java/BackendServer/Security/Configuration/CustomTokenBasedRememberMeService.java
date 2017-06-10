package BackendServer.Security.Configuration;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.crypto.codec.Utf8;

/**This class is a TokenRememberMeService which reads the Cookie-id-string out of a header token and provides a backdoor for swagger
 * @author Florian Kutz
 *
 */
public class CustomTokenBasedRememberMeService extends TokenBasedRememberMeServices{

    private static final String secretWordForSwaggerToAuthenticateAsAnUser = "I'mAnUser",
    		secretWordForSwaggerToAuthenticateAsAnAdmin="I'mAnAdmin";

	public CustomTokenBasedRememberMeService(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

	private final String HEADER_SECURITY_TOKEN = "X-API-Key";
	
    /**
     * Locates the Spring Security remember me token in the request header and returns its value.
     *
     * @param request the submitted request which is to be authenticated
     * @return the value of the request header (which was originally provided by the cookie - API expects it in header)
     */
    @Override 
    protected String extractRememberMeCookie(HttpServletRequest request) {
    	String token = request.getHeader(HEADER_SECURITY_TOKEN);
        return token;
    }

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices#decodeCookie(java.lang.String)
	 * This method provides a secret word for swagger so swagger can authenticate
	 */
	@Override
	protected String[] decodeCookie(String cookieValue) throws InvalidCookieException {
		if(cookieValue.equals(secretWordForSwaggerToAuthenticateAsAnUser)){
			String[] swaggerAuthenticationFields={
				"akessler",
				""+Long.MAX_VALUE,
				makeTokenSignature(Long.MAX_VALUE,
						"akessler", "123")
			};
			return swaggerAuthenticationFields;
		}else if(cookieValue.equals(secretWordForSwaggerToAuthenticateAsAnAdmin)){
			String[] swaggerAuthenticationFields={
					"mmustermann",
					""+Long.MAX_VALUE,
					makeTokenSignature(Long.MAX_VALUE,
							"mmustermann", "123")
				};
			return swaggerAuthenticationFields;
		}else{
			return super.decodeCookie(cookieValue);
		}
	}

}
