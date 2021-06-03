package com.switchfully.spectangular.services.mailing;

import com.switchfully.spectangular.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private SpringTemplateEngine thymeleafTemplateEngine;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;

    private MimeMessageHelper helper;

    @BeforeEach
    public void setUp() throws MessagingException {
        mimeMessage = new MimeMessage((Session)null);
        helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    }

    @Test
    void sendEmail_givenMimeMessage_thenSendMimeMessage() throws MessagingException {
        //GIVEN
        String recipient = "test@test.com";
        helper.setTo(recipient);
        //WHEN
        emailService.sendEmail(mimeMessage);
        //THEN
        verify(mailSender).send(any(MimeMessage.class));
        assertThat(mimeMessage.getRecipients(MimeMessage.RecipientType.TO)[0].toString()).isEqualTo(recipient);
    }
/*
    @Test
    void mailToResetPassword_givenUserAndUrl_thenMessageGetsSend() {
        //GIVEN
        User mockUser = mock(User.class);
        String url = "https://www.test.com";
        when(thymeleafTemplateEngine.process(any(TemplateSpec.class), any(Context.class))).thenReturn("");
        //WHEN
        emailService.mailToResetPassword(mockUser, url);
        //THEN
        verify(mailSender).send(any(MimeMessage.class));
    }
    */
}
