package estevao.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "estevao.market.model") // para enxergar a classe de model (entidades)
@ComponentScan(basePackages = {"estevao.*"}) // enxergar todos os pacotes como componentes
@EnableJpaRepositories(basePackages = {"estevao.market.repository"}) // enxergar as interfaces de repository
@EnableTransactionManagement
public class MarketApplication {
	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123"));
		SpringApplication.run(MarketApplication.class, args);
	}

}
