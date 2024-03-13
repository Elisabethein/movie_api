package com.api.movie_api.Services;

import com.api.movie_api.Entities.Client;
import com.api.movie_api.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Signup a new client
     * @param username - username of the client
     * @param password - password of the client
     * @return the client object
     */
    public Client signup(String username, String password) {
        Client withUsername = clientRepository.findByUsername(username);
        // Check if the username already exists
        if (withUsername != null) {
            throw new IllegalStateException("Username already exists");
        }
        // Hash the password
        String hashedPassword = passwordEncoder.encode(password);
        Client client = new Client(username, hashedPassword);
        clientRepository.save(client);
        return client;
    }

    /**
     * Login a client
     * @param username - username of the client
     * @param password - password of the client
     * @return the client object
     */
    public Client login(String username, String password) {
        Client client = clientRepository.findByUsername(username);
        // Check the username
        if (client == null) {
            throw new IllegalStateException("User does not exist");
        }
        // Check the password
        if (!passwordEncoder.matches(password, client.getPassword())) {
            throw new IllegalStateException("Invalid password");
        }
        return client;
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }
}
