package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.dto.UserDTO;
import hu.bme.aut.haulagecompany.model.dto.UserLoginDTO;
import hu.bme.aut.haulagecompany.model.dto.UserRegDTO;
import hu.bme.aut.haulagecompany.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/checkName/{name}")
    public ResponseEntity<Boolean> checkUserNameExist(@PathVariable String name){
        return userService.checkUserNameExist(name);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserRoles(@PathVariable Long id, @RequestBody List<String> roles){
        return userService.updateUserRoles(id, roles);
    }
}
