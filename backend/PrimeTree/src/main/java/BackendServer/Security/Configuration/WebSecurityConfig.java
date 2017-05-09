package BackendServer.Security.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import BackendServer.User.Service.MyUserDetailsServiceImpl;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private AuthenticationProvider authenticationProvider;
	@Autowired
	MyLoginSuccessHandler mysuccessHandler;
	@Autowired
	private AuthenticationFailureHandler myFailureHandler;
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;
//	@Autowired
//	private UserDetailsService userDetailsService;
//	
//	public WebSecurityConfig(){
//		System.out.println("WebSecurityConfig()");
//	}

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		System.out.println("configure(HttpSecurity http)");
		
		  http.csrf().disable()
            .authorizeRequests()
            	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            	.antMatchers("/login").anonymous()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
            .and()
            	.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)//.accessDeniedHandler(accessDeniedHandler)
            .and()
            	.formLogin()
                .loginPage("/login")
                .permitAll()
                .loginProcessingUrl("/login")
                .permitAll()
                .successHandler(mysuccessHandler)
                .failureHandler(myFailureHandler)
                .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            	.permitAll()
                .logoutSuccessHandler(logoutSuccessHandler)
                ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
    	DaoAuthenticationProvider authenticationProvider =new DaoAuthenticationProvider();
    	authenticationProvider.setUserDetailsService(userDetailsService());
    	return authenticationProvider;
    }
    
    @Bean
    public UserDetailsService userDetailsService(){
    	return new MyUserDetailsServiceImpl();
    }
    
    @Bean
    MyLoginSuccessHandler mysuccessHandler(){
    	return new MyLoginSuccessHandler();
    }
    
    @Bean
    AuthenticationFailureHandler authenticationFailureHandler(){
    	return new MyLoginFailureHandler();
    }
    
    @Bean
    LogoutSuccessHandler logoutSuccessHandler(){
    	return new MyLogoutSuccessHandler();
    }
    
    @Bean
    AccessDeniedHandler accessDeniedHandler(){
    	return new MyAccessDeniedHandler();
    }
    
    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(){
    	return new MyAuthenticationEntryPoint();
    }
}