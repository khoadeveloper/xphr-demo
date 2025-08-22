# How security works

### Add spring security dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Enable Security
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
```

`@EnableWebSecurity` is to enable spring security in our application
`@EnableMethodSecurity` is to enable method level security, will talk about it later.

### Security configration
```java
@Bean
public UserDetailsService userDetailsService(PasswordEncoder encoder) {

    UserDetails admin = User.withUsername("admin")
            .password(encoder.encode("123456789"))  // <-- Encode the password
            .roles("ADMIN")
            .build();

    UserDetails employee = User.withUsername("employee1")
            .password(encoder.encode("123456789"))  // <-- Encode the password
            .roles("EMPLOYEE")
            .build();

    UserDetails auditor = User.withUsername("auditor")
            .password(encoder.encode("123456789"))  // <-- Encode the password
            .roles("AUDITOR")
            .build();

    return new InMemoryUserDetailsManager(admin, employee, auditor);
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/report").authenticated() // Force this endpoint to be secured
                    .requestMatchers("/**").permitAll() // For the rest permit all access
            )
            .formLogin(withDefaults());

    return http.build();
}
```

I created 3 users with 3 roles:
- ADMIN
- EMPLOYEE
- AUDITOR

The purpose of AUDITOR is to demonstrate the method level authentication, AUDITOR is not supposed to access `/report` endpoint

### Method level authentication

```java
@GetMapping("")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public ModelAndView report(Principal principal)
```

This @PreAuthorize annotation will block/allow access based on condition defined. Now it is hasAnyRole(), which mean only user with role ADMIN or EMPLOYEE can access this page.
If AUDITOR try to access, we get 403 response.

Another thing is that Spring will automatically inject Principal object for us, which will contain information about logged user, we cna use that for further process.