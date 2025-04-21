package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.exception.PasswordValidationException;
import com.TTLTTBDD.server.exception.UserAlreadyExistsException;
import com.TTLTTBDD.server.exception.UserNotVerifiedException;
import com.TTLTTBDD.server.models.dto.UserDTO;
import com.TTLTTBDD.server.models.entity.Cart;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.models.entity.Verifytoken;
import com.TTLTTBDD.server.repositories.*;
import com.TTLTTBDD.server.utils.loadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerifyTokenRepository tokenRepository;

    private loadFile loadFile = new loadFile();
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(int id){
        return userRepository.findUsersById(id).map(this::convertToDTO);
    }

    public Optional<UserDTO> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            if (!user.get().getStatus()) {
                throw new UserNotVerifiedException("Tài khoản chưa được xác thực. Vui lòng kiểm tra email.");
            }
            return Optional.of(convertToDTO(user.get()));
        }
        return Optional.empty();
    }

    public UserDTO register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Username hoặc email đã tồn tại.");
        }
        validatePassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(false);
        User savedUser = userRepository.save(user);

        Cart newCart = new Cart();
        newCart.setIdUser(savedUser);
        cartRepository.save(newCart);

        String token = UUID.randomUUID().toString();
        Verifytoken verificationToken = new Verifytoken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(savedUser, token);

        return convertToDTO(savedUser);
    }

    public UserDTO updateUserInfoAccount(UserDTO userDTO) {
            User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("Ko tìm thấy user"));
            user.setUsername(userDTO.getUsername());
            user.setFullname(userDTO.getFullname());
            user.setAddress(userDTO.getAddress());
            user.setPhone(userDTO.getPhone());
            user.setEmail(userDTO.getEmail());

            userRepository.save(user);
            return convertToDTO(user);

    }

    public UserDTO updateUserAvata(UserDTO userDTO, MultipartFile avataFile) {
        try {
            if (avataFile != null && !avataFile.isEmpty()) {
                String avatarPath = loadFile.saveFile(avataFile);
                userDTO.setAvata(avatarPath);
            }
            User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("Ko tìm thấy user"));
            user.setAvata(userDTO.getAvata());

            userRepository.save(user);
            return convertToDTO(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }

    public UserDTO updateUser(UserDTO userDTO, MultipartFile avataFile) {
        try {
            // Kiểm tra nếu có avatar mới, nếu không thì không thay đổi avatar trong DB
            if (avataFile != null && !avataFile.isEmpty()) {
                String avatarPath = loadFile.saveFile(avataFile);  // Lưu avatar mới
                userDTO.setAvata(avatarPath);  // Cập nhật avatar trong DTO
            }

            // Tìm người dùng trong DB và cập nhật thông tin
            User user = userRepository.findById(userDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

            // Cập nhật các thông tin khác của người dùng
            user.setUsername(userDTO.getUsername());
            user.setFullname(userDTO.getFullname());
            user.setAddress(userDTO.getAddress());
            user.setPhone(userDTO.getPhone());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());

            // Nếu avatar không được thay đổi, không cần set lại avatar trong DB
            if (userDTO.getAvata() != null) {
                user.setAvata(userDTO.getAvata());
            }

            // Lưu người dùng đã được cập nhật
            userRepository.save(user);

            // Trả về UserDTO đã cập nhật
            return convertToDTO(user);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tải lên avatar", e);
        }
    }

    @Transactional
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Xóa các bản ghi trong bảng favorite trước
        favoriteRepository.deleteAllByIdUser(user);

        // Tìm Cart của User theo ID
        Optional<Cart> cartOptional = cartRepository.findByIdUser_Id(user.getId());
        if (cartOptional.isPresent()) {
            // Xóa CartDetail liên quan đến Cart
            cartDetailRepository.deleteAllByIdCart(cartOptional.get());

            // Xóa Cart
            cartRepository.delete(cartOptional.get());
        }

        // Cuối cùng xóa User
        userRepository.delete(user);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(user.getRole())
                .avata(user.getAvata())
                .status(user.getStatus())
                .build();
    }
    private void validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password.length() < 8) {
            errors.add("mật khẩu phải tối thiểu 8 kí tự");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Mật khẩu phải chứa ít nhất 1 kí tự in hoa");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add("Mật khẩu phải chứa ít nhất 1 chữ số");
        }
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            errors.add("Mật khẩu phải chứa ít nhất 1 kí tự đặt biệt");
        }
        if (!errors.isEmpty()) {
            throw new PasswordValidationException(errors);
        }
    }

}
