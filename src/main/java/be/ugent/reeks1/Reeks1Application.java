package be.ugent.reeks1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("be.ugent.reeks1.repository")
@EntityScan("be.ugent.reeks1.components")
public class Reeks1Application {
	public static void main(String[] args) {
		SpringApplication.run(Reeks1Application.class, args);
	}

}
