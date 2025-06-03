package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.exception.PasswordValidationException;
import com.TTLTTBDD.server.exception.UserAlreadyExistsException;
import com.TTLTTBDD.server.exception.UserNotVerifiedException;
import com.TTLTTBDD.server.models.dto.UserDTO;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.models.entity.Verifytoken;
import com.TTLTTBDD.server.repositories.UserRepository;
import com.TTLTTBDD.server.repositories.VerifyTokenRepository;
import com.TTLTTBDD.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private VerifyTokenRepository verifyTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/id")
    public Optional<UserDTO> getUserById(@RequestParam int id) {
        return userService.getUserById(id);
    }

    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            Optional<UserDTO> userDTO = userService.login(username, password);
            if (userDTO.isPresent()) {
                return ResponseEntity.ok(userDTO.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tên đăng nhập hoặc mật khẩu.");
            }
        } catch (UserNotVerifiedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống.");
        }
    }


    @CrossOrigin(origins = {"http://localhost:3000"})
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.register(user));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "user đã tồn tại"));
        } catch (PasswordValidationException e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("error", "Mật khẩu không hợp lệ");
            response.put("details", e.getErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "internal_error"));
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<?> checkUserExist(
            @RequestParam String username,
            @RequestParam String email) {

        boolean usernameExists = userRepository.findByUsername(username).isPresent();
        boolean emailExists = userRepository.findByEmail(email).isPresent();

        Map<String, Object> response = new HashMap<>();
        response.put("usernameExists", usernameExists);
        response.put("emailExists", emailExists);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-password")
    public ResponseEntity<?> validatePassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");

        try {
            userService.validatePassword(password);
            return ResponseEntity.ok(Map.of("valid", true));
        } catch (PasswordValidationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "errors", e.getErrors()
            ));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam int idUser,
            @RequestParam String oldPass,
            @RequestParam String newPass) {

        boolean result = userService.changePassword(idUser, oldPass, newPass);

        if (!result) {
            return ResponseEntity.badRequest().body("User không tồn tại hoặc Mật khẩu cũ ko hợp lệ");
        }

        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }

    @PutMapping("/updateInfoAccount")
    public UserDTO updateUser(@RequestParam("id") Integer id, @RequestParam("username") String username, @RequestParam("fullname") String fullname, @RequestParam("address") String address, @RequestParam("phone") String phone, @RequestParam("email") String email) {
        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .address(address)
                .phone(phone)
                .email(email)
                .build();
        return userService.updateUserInfoAccount(userDTO);
    }

    @PutMapping("/updateAvatar")
    public UserDTO updateUser(@RequestParam("id") Integer id, @RequestParam("avatarFile") MultipartFile avatarFile) {
        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .build();
        return userService.updateUserAvatar(userDTO, avatarFile);
    }

    @PutMapping("/update")
    public UserDTO updateUser(
            @RequestParam("id") Integer id,
            @RequestParam("username") String username,
            @RequestParam("fullname") String fullname,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("role") Integer role,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {

        UserDTO userDTO = UserDTO.builder()
                .id(id)
                .username(username)
                .fullname(fullname)
                .address(address)
                .phone(phone)
                .email(email)
                .roleId(role)
                .build();

        return userService.updateUser(userDTO, avatarFile);
    }


    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "User with ID " + id + " has been deleted.";
    }

    @GetMapping("/verify")
    public RedirectView verifyAccount(@RequestParam("token") String token) {
        Optional<Verifytoken> optionalToken = verifyTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return new RedirectView("/verify-failed.html");
        }

        Verifytoken verificationToken = optionalToken.get();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new RedirectView("/verify-expired.html");
        }

        User user = verificationToken.getUser();
        user.setStatus(true);
        userRepository.save(user);

        verifyTokenRepository.delete(verificationToken);

        return new RedirectView("/verify_success.html?username=" + user.getUsername());
    }

    @GetMapping("/regular")
    public List<UserDTO> getAllRegularUsers() {
        return userService.getAllRegularUsers();
    }

    @GetMapping("/regular/monthly-stats")
    public Map<String, Long> getRegularUsersByMonth() {
        return userService.getRegularUsersByMonth();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String username, @RequestParam String email) {
        try {
            userService.forgotPassword(username, email);
            return ResponseEntity.ok("Mật khẩu mới đã được gửi đến email của bạn.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống.");
        }
    }

}
