package com.db.pratice.controller;

import com.db.pratice.model.Role;
import com.db.pratice.model.User;
import com.db.pratice.repository.RoleRepository;
import com.db.pratice.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RestController is commonly used to define Restfull endpoint using HttpMethods(get,post,put,etc,)
 * This class is used to perform the CRUD operations on H2 DB.
 */

@RestController
@SecurityRequirement(name = "basicAuth")
public class Controller {
    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    /**
     * This method is used to add the UserDetails
     * @param user requested from the User
     * @return user
     */
    @PostMapping("/addUser")
    public ResponseEntity<Object> addUser(@RequestBody @Valid User user){
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if(optionalUser.isPresent()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User is Already Created with this "+user.getId());
        }
        else {
            userRepository.save(user);
            logger.info("USER IS ADD");
            return ResponseEntity.ok().body(user);
        }
    }
    @PostMapping()
    public ResponseEntity<Object> addRole(@RequestBody Role role){
        return ResponseEntity.ok().body(roleRepository.save(role));
    }
    @GetMapping("/getRoles")
        public List<Role> getRoles(){
            return roleRepository.findAll();
        }
    }
    /**
     * GET the Details of the request user
     * @return getAllUserDetails using repository method findAll()
     */
    @GetMapping("/getAllUsersNames")
    public List<String> getAllUsers(@RequestParam int id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Role> roles =user.getRole();
            String roleName="";
            for (Role role : roles) {
                //System.out.println(role.getRoleName());
                roleName=role.getRoleName();
            }
            if(roleName.equals("ADMINISTRATOR") || roleName.equals("SENIOUR_EMPLOYEE")){
                List<User> users = userRepository.findAll();
                List<String> usernames = users.stream()
                        .map(User::getUsername)
                        .collect(Collectors.toList());
                return usernames;
            }
            return null;
        }
        else {
            return null;
        }
    }


    /**
     * This method is used to get the details of requested user.
     * @param id request from the user
     * @return user if present else throw EntityNotFoundException
     * @exception EntityNotFoundException if user is not present
     */
    @GetMapping("/getRequestedUser/{id}")
    public ResponseEntity<Object> getRequestedUser(@PathVariable int id,@RequestParam String username,String password){
        try {
            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isPresent()) {
                User user=userOptional.get();
                if((username).equals(user.getUsername()) && password.equals(user.getPassword())){
                    logger.info("Received the user details");
                    return ResponseEntity.status(HttpStatus.OK).body(user);
                }
                else {
                    logger.info("Credentails are inValid");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please, Enter valid Credentails");
                }
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
    @DeleteMapping("/deleteMyAccount/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id,@RequestParam String username,String password){
        try{
            Optional<User> userOptional = userRepository.findById(id);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
                    userRepository.deleteById(id);
                    return ResponseEntity.ok().body("Deleted");
                }
                else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enter inValid Credentails");
                }
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
     * @param id of the user to be delete
     * @param employeeId which is id of the Employee Who is deleting
     * @return ResponseStatus
     */
    //DeletionByAdmin
    @DeleteMapping("/deleteBy/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id,@RequestParam int employeeId){
        try{
            Optional<User> userOptional = userRepository.findById(id);
            if(userOptional.isPresent()){
                //To check whether he the SeniourEmployee or not
                Optional<User> employeeOptional = userRepository.findById(employeeId);

                //for getting the RoleName
                User user = employeeOptional.get();
                List<Role> roles =user.getRole();
                String roleName="";
                for (Role role : roles) {
                    //System.out.println(role.getRoleName());
                    roleName=role.getRoleName();
                }
                System.out.println(roleName);
                if(roleName.equals("SENIOUR_EMPLOYEE")|| roleName.equals("ADMIN")){
                    userRepository.deleteById(id);
                    return ResponseEntity.ok().body("Deleted the user: "+user.getUsername());
                }
                else{
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Only ADMIN can able to Delete the User");
                }
            }
            else {
                throw new EntityNotFoundException();
            }
        }
        catch(EntityNotFoundException e){
            logger.error("No User");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user");
        }
    }

    /**
     * @param id
     * @return user updated details if the user is present else throws EntityNotFoundException
     * @exception EntityNotFoundException if user is not present
     */
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<Object> updateDetails(@PathVariable int id,@RequestParam String username,String password ,@RequestBody User userUpdate) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
                    user.setPassword(userUpdate.getPassword());
                    user.setUsername(userUpdate.getUsername());
                    user.setRole(userUpdate.getRole());
                    userRepository.save(user);
                    return ResponseEntity.ok().body("Updated the details of the User.");
                }
                else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Enter inValid Credentails");
                }

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

    //for all USERS
    @GetMapping("/getAllUsers")
    public List<User> getUsers(){
        return userRepository.findAll();
    }


}
