package com.api.movie_api.Controllers;

import com.api.movie_api.Entities.Client;
import com.api.movie_api.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Autowired
    private ClientService clientService;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/signup")
    public Client signup(@RequestParam String username, @RequestParam String password) {
        return clientService.signup(username, password);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/login")
    public Client login(@RequestParam String username, @RequestParam String password) {
        System.out.println("username: " + username + " password: " + password);
        return clientService.login(username, password);
    }

    @GetMapping("/client/{id}")
    public Client getClientById(
            @PathVariable("id") Long id
    ) {
        return clientService.getClientById(id);
    }
}
