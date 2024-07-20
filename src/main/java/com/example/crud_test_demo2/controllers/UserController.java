package com.example.crud_test_demo2.controllers;

import com.example.crud_test_demo2.entities.User;
import com.example.crud_test_demo2.repositories.UserRepository;
import com.example.crud_test_demo2.services.UserService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public @ResponseBody User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/")
    public @ResponseBody List<User> getList() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody User getWithId(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @PutMapping("/{id}")
    public @ResponseBody User update(@PathVariable Integer id, @RequestBody @NotNull User user){
        user.setId(id);
        return userRepository.save(user);
    }

    @PutMapping("/{id}/activation")
    public @ResponseBody User putActive(@PathVariable Integer id, @RequestParam Boolean working){
        return userService.setUserActivationStatus(id, working);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }
}