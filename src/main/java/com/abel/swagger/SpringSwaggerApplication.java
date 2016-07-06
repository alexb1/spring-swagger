package com.abel.swagger;

import com.abel.swagger.model.User;
import com.abel.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SpringSwaggerApplication {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (String... args) -> {
            userRepository.save(new User("user", "secret", "user@mail.org"));
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSwaggerApplication.class, args);
    }
}
