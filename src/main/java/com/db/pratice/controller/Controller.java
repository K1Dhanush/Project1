package com.db.pratice.controller;

import com.db.pratice.model.User;
import com.db.pratice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * RestController is commonly used to define Restfull endpoint using HttpMethods(get,post,put,etc,)
 * This class is used to perform the CRUD operations on H2 DB.
 */

@RestController
@org.springframework.stereotype.Controller
public class Controller {
    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/index")
    public String Page(){
        return "index";
    }

    /**
     * This method is used to add the UserDetails
     * @param user
     * @return user
     */
    @PostMapping("/addUser")
    public User addUser(@RequestBody User user){
        userRepository.save(user);
        logger.info("USER IS ADD");
        return user;
    }

    /**
     * GET the Details of the request user
     * @return getAllUserDetails using repository method findAll()
     */
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * This method is used to get the details of requested user.
     * @param id request from the user
     * @return user if present else throw EntityNotFoundException
     * @exception EntityNotFoundException if user is not present
     */
    @GetMapping("/getRequestedUser/{id}")
    public ResponseEntity<Object> getRequestedUser(@PathVariable int id){
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                logger.info("Received the user details");
                return ResponseEntity.ok().body(user);
            } else {
                throw new EntityNotFoundException();
            }
        }catch(EntityNotFoundException e1){
            logger.error("User is Not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is Not found with Id "+ id);
        }
    }

    /**
     * This method is used to Delete the user
     * @param id from the user
     * @return Deleted if user is present else throws EntityNotFoundException
     * @exception EntityNotFoundException if user is not present
     */
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id){
        try{
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                userRepository.deleteById(id);
                return ResponseEntity.ok().body("Deleted");
            }
            else {
                throw new EntityNotFoundException();
            }
        }catch(EntityNotFoundException e2){
            logger.error("No User");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user");
        }
    }

    /**
     * @param id
     * @param password
     * @param username
     * @return user updated details if the user is present else throws EntityNotFoundException
     * @exception EntityNotFoundException if user is not present
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateDetails(@PathVariable int id, @RequestParam String password,String username) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(password);
                user.setUsername(username);
                userRepository.save(user);
                return ResponseEntity.ok().body("Password updated successfully");
            } else {
                throw new EntityNotFoundException("User not found with ID: " + id);
            }
        } catch (EntityNotFoundException e) {
            logger.error("User Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the password");
        }
    }
}
