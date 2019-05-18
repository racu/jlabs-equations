package com.example.jlabscomp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

//http://go-to-devoxx-with.events-jlabs.pl/apiInfo/ee490700-f89a-4f49-b341-814f6e901675
//token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlZTQ5MDcwMC1mODlhLTRmNDktYjM0MS04MTRmNmU5MDE2NzUiLCJpc3MiOiJqLWxhYnMiLCJleHAiOjE1NjEzMzQ0MDB9.UAoDK-doFovWHy4HV4uoLWydZ3LraQ1sUwsHukRTY6g

//fix cache
//convert directly to json string
//try to cache all 3 chars long equations, substring eq(indexof '=')
//warm up


@SpringBootApplication
public class JlabscompApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(JlabscompApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		ConfigurableApplicationContext ctx = app.run(args);
		//SpringApplication.run(JlabscompApplication.class, args);
	}

}
