package com.example.restfulwebservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable(value = "id") Integer id) {
        User user = service.findOne(id);

        if( user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return user;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody User user) {

        if(user == null) {
            throw new UserEmptyException(String.format("user empty"));
        }
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable(value = "id") int id) {
        User user = service.deleteById(id);

        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

    }

    @PutMapping("/users")
    public void updateUser(@RequestBody User userForm) {
        User user = service.updateById(userForm);

        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", userForm.getId()));
        }

    }
}
