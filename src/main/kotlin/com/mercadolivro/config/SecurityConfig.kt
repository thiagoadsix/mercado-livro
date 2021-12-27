package com.mercadolivro.config

import com.mercadolivro.enums.Role
import com.mercadolivro.repositories.CustomerRepository
import com.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.security.AuthorizationFilter
import com.mercadolivro.security.CustomAuthenticationEntryPoint
import com.mercadolivro.security.JwtUtil
import com.mercadolivro.services.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
  private val customerRepository: CustomerRepository,
  private val userDetailsCustomService: UserDetailsCustomService,
  private val jwtUtil: JwtUtil,
  private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) : WebSecurityConfigurerAdapter() {
  private val PUBLIC_POST_MATCHERS = arrayOf("/customers")

  private val PUBLIC_GET_MATCHERS = arrayOf(
    "/books"
  )

  private val ADMIN_MATCHERS = arrayOf("/admin/**")

  override fun configure(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(userDetailsCustomService).passwordEncoder(bCryptPasswordEncoder())
  }

  override fun configure(http: HttpSecurity) {
    http.cors().and().csrf().disable()
    http.authorizeRequests()
      .antMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
      .antMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
      .antMatchers(HttpMethod.GET, *PUBLIC_GET_MATCHERS).permitAll()
      .anyRequest()
      .authenticated()
    http.addFilter(AuthenticationFilter(authenticationManager(), customerRepository, jwtUtil))
    http.addFilter(AuthorizationFilter(authenticationManager(), jwtUtil, userDetailsCustomService))
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
  }

  override fun configure(web: WebSecurity) {
    web.ignoring()
      .antMatchers(
        "/v2/api-docs",
        "/configuration/ui",
        "/swagger-resources/**",
        "/configuration/**",
        "/swagger-ui.html",
        "/webjars/**"
      )
  }

  @Bean
  fun corsConfig(): CorsFilter {
    val source = UrlBasedCorsConfigurationSource()
    val config = CorsConfiguration()
    config.allowCredentials = true
    config.addAllowedOriginPattern("*")
    config.addAllowedHeader("*")
    config.addAllowedMethod("*")
    source.registerCorsConfiguration("/**", config)
    return CorsFilter(source)
  }

  @Bean
  fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }
}