package com.example.Task_SpringBoot.config;

import com.example.Task_SpringBoot.services.jwt.UserService;
import com.example.Task_SpringBoot.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Sử dụng đối tượng JwtUtil để làm việc với JWT token
    private final JwtUtil jwtUtil;

    // Dịch vụ lấy thông tin người dùng từ cơ sở dữ liệu
    private final UserService userService;

    // Đối tượng chứa thông tin người dùng
    private final UserDetails userDetails;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Lấy giá trị của header "Authorization" từ yêu cầu HTTP
        final String authHeader = request.getHeader("Authorization");
        final String jwt; // Biến để chứa JWT token
        final String userEmail; // Biến để chứa email người dùng trích xuất từ token

        // Kiểm tra nếu header không tồn tại hoặc không bắt đầu với "Bearer", thì bỏ qua filter này
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response); // Tiếp tục chuỗi bộ lọc mà không xử lý JWT
            return; // Kết thúc hàm để không tiếp tục xử lý
        }

        // Lấy JWT token từ header (bỏ qua phần "Bearer ")
        jwt = authHeader.substring(7);

        // Trích xuất tên người dùng (email) từ JWT token
        userEmail = jwtUtil.extractUserName(jwt);

        // Kiểm tra nếu email hợp lệ và chưa có xác thực trước đó
        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Kiểm tra nếu token hợp lệ bằng cách so sánh token với thông tin người dùng đã trích xuất
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                // Tạo một SecurityContext mới (khung chứa thông tin bảo mật)
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                // Tạo đối tượng UsernamePasswordAuthenticationToken để xác thực
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Đặt chi tiết của yêu cầu vào đối tượng xác thực (authentication)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Đặt đối tượng xác thực này vào SecurityContext
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context); // Cập nhật SecurityContextHolder với thông tin xác thực
            }
        }

        // Tiếp tục chuỗi bộ lọc (chuyển yêu cầu sang các filter tiếp theo)
        filterChain.doFilter(request, response);
    }
}
