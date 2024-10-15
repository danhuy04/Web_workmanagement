package com.example.Task_SpringBoot.controller.auth;

import com.example.Task_SpringBoot.Repositories.UserRepository;
import com.example.Task_SpringBoot.dto.AuthenticationRequest;
import com.example.Task_SpringBoot.dto.AuthenticationResponse;
import com.example.Task_SpringBoot.dto.SignupRequest;
import com.example.Task_SpringBoot.dto.UserDto;
import com.example.Task_SpringBoot.entities.User;
import com.example.Task_SpringBoot.services.auth.AuthService;
import com.example.Task_SpringBoot.services.jwt.UserService;
import com.example.Task_SpringBoot.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Lombok tự động tạo constructor với tất cả các tham số là final,
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    // Đăng ký người dùng mới
    @PostMapping("/signup") //Định nghĩa endpoint cho yêu cầu đăng ký.
    //Nhận yêu cầu đăng ký từ client, chuyển đổi thành đối tượng SignupRequest.
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        //authService.hasUserWithEmail để kiểm tra xem có người dùng nào đã sử dụng email đó hay chưa
        if (authService.hasUserWithEmail(signupRequest.getEmail()))
            // Nếu có, trả về mã lỗi NOT_ACCEPTABLE.
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists with this email");

        //Nếu email chưa tồn tại, gọi authService.signupUser để tạo người dùng mới và trả về UserDto.
        UserDto createUserDto = authService.signupUser(signupRequest);
        if (createUserDto == null)
            // Nếu người dùng không được tạo thành công, trả về mã lỗi BAD_REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");

        return ResponseEntity.status(HttpStatus.CREATED).body(createUserDto);
    }

    // Đăng nhập người dùng
    @PostMapping("/login") //Định nghĩa endpoint cho yêu cầu đăng nhập.
    // AuthenticationRequest chứa địa chỉ email và mật khẩu của người dùng muốn đăng nhập
    //@RequestBody: Annotation này cho biết rằng dữ liệu yêu cầu từ client sẽ được chuyển đổi thành đối tượng AuthenticationRequest
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            //Xác thực người dùng
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
            // Nếu thông tin đăng nhập không chính xác, ném ngoại lệ BadCredentialsException
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
                                    // userService.userDetailsService().loadUserByUsername để lấy thông tin người dùng từ email.
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());

                            //jwtUtil.generateToken để tạo JSON Web Token
        final String jwtToken = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        // Kiểm tra sự tồn tại của người dùng
        //kiểm tra xem đối tượng Optional<User> (optionalUser) có chứa một người dùng hay không.
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwtToken);
            authenticationResponse.setUserID(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }

        return authenticationResponse;
    }
}
