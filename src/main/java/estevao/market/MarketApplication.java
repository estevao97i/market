package estevao.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync // possibilitar assincronia no código
@EntityScan(basePackages = "estevao.market.model") // para enxergar a classe de model (entidades)
@ComponentScan(basePackages = {"estevao.*"}) // enxergar todos os pacotes como componentes
@EnableJpaRepositories(basePackages = {"estevao.market.repository"}) // enxergar as interfaces de repository
@EnableTransactionManagement
public class MarketApplication implements AsyncConfigurer {
	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123"));
		SpringApplication.run(MarketApplication.class, args);
	}

	// cria tarefas concorrentes (async)
	@Override
	@Bean
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(500);
		executor.setCorePoolSize(10);
		executor.setThreadNamePrefix("Assyncronous Thread");
		executor.initialize();
		return executor;
	}

}
