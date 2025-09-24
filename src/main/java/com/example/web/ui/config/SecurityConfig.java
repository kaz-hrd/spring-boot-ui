package com.example.web.ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 認可設定
            .authorizeHttpRequests(authz -> authz
                // 静的リソースとログインページは認証不要
                .requestMatchers(
                    "/login", "/error", "/favicon.ico",
                    "/css/**", "/js/**", "/webjars/**", "/images/**",
                    "/actuator/health", "/actuator/info"
                ).permitAll()
                // ユーザー登録ページは認証後にアクセス可能
                .requestMatchers("/register/**").authenticated()
                // 管理者専用エンドポイント
                .requestMatchers("/admin/**", "/actuator/**").hasRole("ADMIN")
                // その他は認証必須
                .anyRequest().authenticated()
            )
            // フォーム認証設定
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/users", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            // ログアウト設定
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )
            // セッション管理設定
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
            )
            .sessionManagement(session -> session
                .sessionFixation().migrateSession()
                .invalidSessionUrl("/login?expired=true")
            )
            // セキュリティヘッダー設定
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.deny())  // クリックジャッキング対策
                .contentTypeOptions(contentTypeOptions -> {})  // MIME type sniffing対策
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)  // 1年間
                    .includeSubDomains(true)
                )
                .referrerPolicy(policy -> policy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            )
            // CSRF設定（本来は有効にすべきだが、この例では無効）
            .csrf(csrf -> csrf.disable())
            // 例外処理設定
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            );
        
        return http.build();
    }

    @Bean
    public org.springframework.security.core.session.SessionRegistry sessionRegistry() {
        return new org.springframework.security.core.session.SessionRegistryImpl();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .accountLocked(false)
            .accountExpired(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("USER", "ADMIN")
            .accountLocked(false)
            .accountExpired(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // 強度を12に設定
    }
}