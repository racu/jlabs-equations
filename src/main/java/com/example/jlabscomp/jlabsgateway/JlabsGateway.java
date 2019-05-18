package com.example.jlabscomp.jlabsgateway;

import com.example.jlabscomp.EquationsDto;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
public class JlabsGateway {

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlZTQ5MDcwMC1mODlhLTRmNDktYjM0MS04MTRmNmU5MDE2NzUiLCJpc3MiOiJqLWxhYnMiLCJleHAiOjE1NjEzMzQ0MDB9.UAoDK-doFovWHy4HV4uoLWydZ3LraQ1sUwsHukRTY6g";


    public EquationsDto retrieveTestCase(String env) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        String fooResourceUrl = "http://go-to-devoxx-with.events-jlabs.pl/game/"+env;
        ResponseEntity<EquationsDto> response = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, entity, EquationsDto.class);
        return response.getBody();
    }

    public void submitTestCaseAnswers(List<String[]> testCasesAnswers, String env) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String[]>> entity = new HttpEntity<>(testCasesAnswers, headers);

        String fooResourceUrl = "http://go-to-devoxx-with.events-jlabs.pl/game/"+env;
        ResponseEntity<String> response = restTemplate.exchange(fooResourceUrl, HttpMethod.POST, entity, String.class);
        System.out.println(response.getStatusCode());
    }
}
