package br.com.diegonassula.findandreplace;

import br.com.diegonassula.findandreplace.service.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FindAndReplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindAndReplaceApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(FileService fileService) {
        return args -> {
            fileService.call();
        };
    }

}
