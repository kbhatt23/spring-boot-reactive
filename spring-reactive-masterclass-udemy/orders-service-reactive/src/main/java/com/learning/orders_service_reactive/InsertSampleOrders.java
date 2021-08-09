package com.learning.orders_service_reactive;


import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

//disable this for postgres
//for postgres we create table manually from pgadmin
@Service
public class InsertSampleOrders implements CommandLineRunner{
	
	  @Value("classpath:h2/init.sql")
	    private Resource initSql;

	    @Autowired
	    private R2dbcEntityTemplate entityTemplate;

	    @Override
	    public void run(String... args) throws Exception {
	        String query = StreamUtils.copyToString(initSql.getInputStream(), StandardCharsets.UTF_8);
	        System.out.println(query);
	        this.entityTemplate
	                .getDatabaseClient()
	                .sql(query)
	                .then()
	                .subscribe();

	    }

//	@Autowired
//	private UsersRepository usersRepository;
//	
//	@Override
//	public void run(String... args) throws Exception {
//		log.info("run: Inserting users in Mongo DB");
//		
//		//just create users with no transaction
//		usersRepository.deleteAll()
//				.doOnSuccess(voidRes -> log.info("run: Removed old users"))
//		        .thenMany(Flux.range(1, 5))
//		        .map(Long::valueOf)
//		        .map(id -> new UserEntity(id, "user-"+id, id*100d, null))
//		        .flatMap(usersRepository :: save)
//		        .blockLast()
////		        .subscribe(saved -> log.info("run: saved user "+saved), 
////		        		err ->{},
////		        		() -> log.info("run: Sample users inserted succesfully")
////		        		);
//		        ;
//		
//	}

}
