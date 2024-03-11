package com.api.movie_api.Controllers;

import com.api.movie_api.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password) {
        return clientService.signup(username, password);
    }

    @GetMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return clientService.login(username, password);
    }
}
