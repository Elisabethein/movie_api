package com.api.movie_api.Controllers;

import com.api.movie_api.Entities.Client;
import com.api.movie_api.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Signup a new client
     * @param username - username of the client
     * @param password - password of the client
     * @return the client object
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/signup")
    public ResponseEntity<Client> signup(@RequestParam String username, @RequestParam String password) {
        try {
            Client client = clientService.signup(username, password);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Login a client
     * @param username - username of the client
     * @param password - password of the client
     * @return the client object
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/login")
    public ResponseEntity<Client> login(@RequestParam String username, @RequestParam String password) {
        try {
            Client client = clientService.login(username, password);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get a client by id
     * @param id - id of the client
     * @return the client object
     */
    @GetMapping("/client/{id}")
    public Client getClientById(
            @PathVariable("id") Long id
    ) {
        return clientService.getClientById(id);
    }
}
