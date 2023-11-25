package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.config.JwtTokenUtil;
import hu.bme.aut.haulagecompany.model.Role;
import hu.bme.aut.haulagecompany.model.User;
import hu.bme.aut.haulagecompany.model.dto.AuthResponseDTO;
import hu.bme.aut.haulagecompany.model.dto.UserDTO;
import hu.bme.aut.haulagecompany.model.dto.UserLoginDTO;
import hu.bme.aut.haulagecompany.model.dto.UserRegDTO;
import hu.bme.aut.haulagecompany.model.mapper.UserMapper;
import hu.bme.aut.haulagecompany.repository.RoleRepository;
import hu.bme.aut.haulagecompany.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public ResponseEntity<String> register(UserRegDTO userDTO) {

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if(roleRepository.findByName("USER") == null){
            init();
        }
        user.addRole(roleRepository.findByName(userDTO.getRole()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<?> login(UserLoginDTO userDTO) {
        if(roleRepository.findByName("USER") == null){
            init();
        }
        if (!userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username not found");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getUsername(), userDTO.getPassword())
            );
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            User userDB = userRepository.getUserByUsername(userDTO.getUsername());
            String accessToken = jwtTokenUtil.generateAccessToken(user, userDB.getId());
            userDB.setEntryDate(new Timestamp(System.currentTimeMillis()));
            userRepository.save(userDB);
            AuthResponseDTO response = new AuthResponseDTO(userDB.getId(), accessToken);

            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(usersToUserDTOs(users));
    }

    private List<UserDTO> usersToUserDTOs(List<User> users){
        List<UserDTO> DTOs = new ArrayList<>();
        for(User u : users){
            DTOs.add(UserMapper.INSTANCE.toUserDTO(u));
        }
        return DTOs;
    }

    public ResponseEntity<?> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return ResponseEntity.ok(UserMapper.INSTANCE.toUserDTO(user.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }


    public ResponseEntity<String> deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    public ResponseEntity<UserDTO> updateUserRoles(Long id, List<String> roles) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User validUser = user.get();
            validUser.setRoles(new ArrayList<>());
            for(String r : roles){
                validUser.addRole(roleRepository.findByName(r));
            }
            return ResponseEntity.ok(UserMapper.INSTANCE.toUserDTO(userRepository.save(validUser)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public ResponseEntity<Boolean> checkUserNameExist(String name) {
        return ResponseEntity.ok(userRepository.existsByUsername(name));
    }

    private void init() {
        Role userRole = new Role(); userRole.setName("USER"); userRole.setDescription("User role");
        roleRepository.save(userRole);

        Role adminRole = new Role(); adminRole.setName("ADMIN"); adminRole.setDescription("Admin role");
        roleRepository.save(adminRole);

        User userTest = new User();
        userTest.setName("test"); userTest.setUsername("test1"); userTest.setPassword(passwordEncoder.encode("testuser")); userTest.addRole(userRole);
        userRepository.save(userTest);

        User userAdminTest = new User();
        userAdminTest.setName("test"); userAdminTest.setUsername("test3"); userAdminTest.setPassword(passwordEncoder.encode("testuserad")); userAdminTest.addRole(adminRole);
        userRepository.save(userAdminTest);
    }
}