package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(User user, String token) {
        String subject = "Xác thực tài khoản của bạn";
        String confirmationUrl = "http://localhost:8080/api/users/verify?token=" + token;

        String content = "<div style=\"font-family: Arial, sans-serif; padding: 20px; border: 1px solid #eee; max-width: 600px; margin: auto; background: #ffffff;\">\n" +
                "  <div style=\"text-align: center;\">\n" +
                "    <img src='https://res.cloudinary.com/duztah40b/image/upload/v1745154145/logo_p2qp8w.jpg' alt='Logo' style='height: 100px;'>\n" +
                "    <h2 style=\"color: #333;\">Xác thực tài khoản</h2>\n" +
                "    <p style=\"color: #555;\">Xin chào <strong>" + user.getFullname() + "</strong>,</p>\n" +
                "    <p style=\"color: #555;\">Cảm ơn bạn đã đăng ký. Vui lòng nhấn nút bên dưới để xác thực tài khoản.</p>\n" +
                "    <a href=\"" + confirmationUrl + "\" style=\"display: inline-block; padding: 12px 24px; background-color: #007BFF; color: white; text-decoration: none; border-radius: 5px; margin-top: 20px;\">Xác thực ngay</a>\n" +
                "    <p style=\"color: #aaa; margin-top: 30px; font-size: 12px;\">Nếu bạn không yêu cầu đăng ký, hãy bỏ qua email này.</p>\n" +
                "  </div>\n" +
                "</div>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendForgotPasswordEmail(User user) {
        String subject = "Xác nhận thay đổi mật khẩu người dùng";

        String content = "<div style=\"font-family: Arial, sans-serif; padding: 20px; border: 1px solid #eee; max-width: 600px; margin: auto; background: #ffffff;\">\n" +
                "  <div style=\"text-align: center;\">\n" +
                "    <img src='https://res.cloudinary.com/duztah40b/image/upload/v1745154145/logo_p2qp8w.jpg' alt='Logo' style='height: 100px;'>\n" +
                "    <h2 style=\"color: #333;\">Xác nhận thay đổi mật khẩu</h2>\n" +
                "    <p style=\"color: #555;\">Xin chào <strong>" + user.getFullname() + "</strong>,</p>\n" +
                "    <p style=\"color: #555;\">Bạn đã yêu cầu thay đổi mật khẩu. Dưới đây là mật khẩu mới của bạn</p>\n" +
                "    <p style=\"color: #555; font-weight: bold;\">" + "123" + user.getUsername() + user.getEmail() + "</p>\n" +
                "    <p style=\"color: #aaa; margin-top: 30px; font-size: 12px;\">Liên hệ chúng tôi nếu có vần đề cần hỗ trợ.</p>\n" +
                "  </div>\n" +
                "</div>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
