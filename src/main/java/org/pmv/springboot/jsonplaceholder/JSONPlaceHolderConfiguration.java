package org.pmv.springboot.jsonplaceholder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSONPlaceHolderConfiguration {

    @Bean("jsonplaceholder")
    CommandLineRunner runner (JSONPlaceHolderClient client){
        return args -> {
            System.out.println("JSONPlaceHolder: " + client.getPosts().size());
            System.out.println(client.getPost(5L));

        };
    }
}
