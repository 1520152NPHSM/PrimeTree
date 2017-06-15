package backendServer.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import backendServer.user.service.MyUserDetailsServiceImpl;

/**This class configures Spring Security
* @author Florian Kutz
*
*/
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired private CustomTokenBasedRememberMeService tokenBasedRememberMeService;

	@Autowired private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;

	private final String tokenKey = "X-API-Key";

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Override protected void configure(HttpSecurity http) throws Exception {

		http.addFilterBefore(new MyUsernamePasswordAuthenticationFilter(authenticationManager(), loginSuccessHandler()), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(rememberMeAuthenticationFilter(), BasicAuthenticationFilter.class );
		
        http

        	.csrf()

        		.disable()

            .authorizeRequests()

            .antMatchers(HttpMethod.PUT, "/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        	.antMatchers("/user/login").anonymous()
            .antMatchers("/", "/home").permitAll()
            .antMatchers("/listings/inactive").hasAuthority("ADMIN")
            .antMatchers("/users").hasAuthority("ADMIN")
            .antMatchers("/user/{id}/admin").hasAuthority("ADMIN")

                .anyRequest().authenticated()

                .and()

            .formLogin()

                .loginPage("/login")
                .permitAll()
                .loginProcessingUrl("/user/login")
                
                .successHandler(loginSuccessHandler())
              .failureHandler(loginFailureHandler())

                .permitAll().and()
            	.logout()
            	.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
            	.permitAll()
                .logoutSuccessHandler(logoutSuccessHandler())
                ;
        
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

            http.rememberMe().rememberMeServices(tokenBasedRememberMeService);
        http.cors();

    }

	@Bean public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception{
		 return new RememberMeAuthenticationFilter(authenticationManager(), tokenBasedRememberMeService());
	 }
	
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new MyLoginSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler loginFailureHandler() {
		return new MyLoginFailureHandler();
	}
	
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new MyLogoutSuccessHandler();
	}

	 @Autowired
	protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {

		 auth

		 	.userDetailsService(userDetailsService());

		 auth.authenticationProvider(rememberMeAuthenticationProvider);

	 }

	 

	 @Bean @Override public AuthenticationManager authenticationManagerBean() throws Exception {

		 return super.authenticationManagerBean();

	 }
	 
	 @Bean
	 @Primary
   public UserDetailsService userDetailsService(){
   	return new MyUserDetailsServiceImpl();
   }
	 
	@Bean 
	public CustomTokenBasedRememberMeService tokenBasedRememberMeService(){
		CustomTokenBasedRememberMeService service = new CustomTokenBasedRememberMeService(tokenKey, userDetailsService());
		service.setAlwaysRemember(true);
		service.setCookieName(tokenKey);
		return service;
	}
	
	@Bean 
	RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
		return new RememberMeAuthenticationProvider(tokenKey);
	}
	
	@Bean
	AuthenticationEntryPoint authenticationEntryPoint(){
		return new MyAuthenticationEntryPoint();
	}

}
