package com.example.jlabscomp.jlabsgateway;

import com.example.jlabscomp.EquationsDto;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class LocalServerGateway {

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlZTQ5MDcwMC1mODlhLTRmNDktYjM0MS04MTRmNmU5MDE2NzUiLCJpc3MiOiJqLWxhYnMiLCJleHAiOjE1NjEzMzQ0MDB9.UAoDK-doFovWHy4HV4uoLWydZ3LraQ1sUwsHukRTY6g";

    @Autowired
    RestTemplate restTemplate;


//    HttpHeaders getEquationsHeaders;
//    HttpHeaders submitAnswersHeaders;
//    @PostConstruct
//    public void init(){
//        getEquationsHeaders = new HttpHeaders();
//        getEquationsHeaders.setBearerAuth(token);
//
//        submitAnswersHeaders = new HttpHeaders();
//        submitAnswersHeaders.setBearerAuth(token);
//        submitAnswersHeaders.setContentType(MediaType.APPLICATION_JSON);
//    }

    public EquationsDto retrieveTestCase() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<EquationsDto> response = restTemplate.exchange("http://localhost:8080/getEquations", HttpMethod.GET, entity, EquationsDto.class);
        return response.getBody();
    }

    public HttpStatus submitTestCaseAnswers(List<String[]> testCasesAnswers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String[]>> entity = new HttpEntity<>(testCasesAnswers, headers);
        ResponseEntity<String> response = restTemplate.exchange( "http://localhost:8080/submitSolutions", HttpMethod.POST, entity, String.class);
        return response.getStatusCode();
    }

    public HttpStatus submitTestCaseAnswers(String testCasesAnswers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(testCasesAnswers, headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/submitSolutions", HttpMethod.POST, entity, String.class);
        return response.getStatusCode();
    }
}
