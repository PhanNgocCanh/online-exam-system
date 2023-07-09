package com.doantotnghiep.service.impl;

import com.doantotnghiep.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService implements IMailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Override

    public boolean sendSimpleMail(String receiver) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setSubject("Thông báo thông tin tài khoản");
            helper.setFrom(sender);
            helper.setTo(receiver);

            boolean html = true;
            String successMessage = "<b>Chào mừng bạn đến với hệ thống thi trắc nghiệm trực tuyến </b>"+
                    "<p>Tên đăng nhập của bạn là: "+receiver+"</p>"+
                    "<p>Mật khẩu: 1</p>"+
                    "<p style='color:red;'>Bạn có thể đổi mật khẩu sau khi đăng nhập lần đầu</p>";
            helper.setText(successMessage,html);
            mailSender.send(message);

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
