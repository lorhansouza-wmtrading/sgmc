package br.com.mam.sgmc.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/keycloak")
public class KeycloakController {
    @Autowired
    private RestClient restClient;

    @GetMapping
    public String getKeycloakToken(){
        return this.restClient
            .get()
            .uri("http://localhost:8089/keycloak")
            .retrieve()
            .body(String.class);
    }
}
