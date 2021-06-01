package com.switchfully.spectangular.services.mailing;

import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.dtos.CoachRequestDto;
import com.switchfully.spectangular.exceptions.UnableToSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private static final String SENDER_MAIL = "spectangular.codecoach@gmail.com";
    private static final String REGARDS = "Kind regards,";
    private static final String SENDER_NAME = "The CodeCoach Team";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine) {
        this.mailSender = mailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Async
    void sendEmail(MimeMessage email) {
        mailSender.send(email);
    }

    public void mailToResetPassword(User user, String url) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("user", user);
        templateModel.put("url", url + "/reset-password");
        sign(templateModel);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("reset-password-template.html", thymeleafContext);
        sendHtmlMessage(user.getEmail(), "Password Reset Request", htmlBody);
    }

    public void mailForRegistering(User user) {
        Context thymeleafContext = prepareMessageContent("user", user);
        String htmlBody = thymeleafTemplateEngine.process("register-template.html", thymeleafContext);
        sendHtmlMessage(user.getEmail(), "Welcome to CodeCoach!", htmlBody);
    }

    public void mailForCoachRequest(User user, CoachRequestDto dto) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("user", user);
        templateModel.put("coachRequest", dto);
        sign(templateModel);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("admin-coach-request-template.html", thymeleafContext);
        sendHtmlMessage(SENDER_MAIL, "New Coach Request", htmlBody);
    }

    public void mailForBecomingCoach(User user) {
        Context thymeleafContext = prepareMessageContent("user", user);
        String htmlBody = thymeleafTemplateEngine.process("became-coach-template.html", thymeleafContext);
        sendHtmlMessage(user.getEmail(), "You're now a Coach!", htmlBody);
    }

    public void mailForSessionRequest(Session session) {
        mailCoacheeForSessionRequest(session);
        mailCoachForSessionRequest(session);
    }

    private void mailCoacheeForSessionRequest(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coachee-session-request-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoachee().getEmail(), "Your Session Request", htmlBody);
    }

    private void mailCoachForSessionRequest(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coach-session-request-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoach().getEmail(), "New Session Request", htmlBody);
    }

    public void mailCoacheeForAcceptedSession(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coachee-session-accepted-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoachee().getEmail(), "Session Request Accepted", htmlBody);
    }

    public void mailCoacheeForDeclinedSession(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coachee-session-declined-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoachee().getEmail(), "Session Request Declined", htmlBody);
    }

    public void mailCoacheeForSessionCancelledByCoach(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coachee-session-cancelled-by-coach-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoachee().getEmail(), "Session Cancelled", htmlBody);
    }

    public void mailCoachForSessionCancelledByCoachee(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coach-session-cancelled-by-coachee-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoach().getEmail(), "Session Cancelled", htmlBody);
    }

    public void mailCoachForSessionRequestCancelled(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coach-session-request-cancelled-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoach().getEmail(), "Session Request Cancelled", htmlBody);
    }

    public void mailForAutomaticallyDeclinedSession(Session session) {
        mailCoacheeForAutomaticallyDeclinedSession(session);
        mailCoachForAutomaticallyDeclinedSession(session);
    }

    private void mailCoacheeForAutomaticallyDeclinedSession(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coachee-session-automatically-declined-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoachee().getEmail(), "Session Automatically Declined", htmlBody);
    }

    private void mailCoachForAutomaticallyDeclinedSession(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coach-session-automatically-declined-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoach().getEmail(), "Session Automatically Declined", htmlBody);
    }

    public void mailForAskingFeedback(Session session) {
        mailCoacheeForAskingFeedback(session);
        mailCoachForAskingFeedback(session);
    }

    private void mailCoacheeForAskingFeedback(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coachee-session-feedback-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoachee().getEmail(), "Give Session Feedback", htmlBody);
    }

    private void mailCoachForAskingFeedback(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coach-session-feedback-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoach().getEmail(), "Give Session Feedback", htmlBody);
    }

    public void mailCoacheeForReceivedFeedback(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coachee-session-feedback-received-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoachee().getEmail(), "Session Feedback Received", htmlBody);
    }

    public void mailCoachForReceivedFeedback(Session session) {
        Context thymeleafContext = prepareMessageContent("session", session);
        String htmlBody = thymeleafTemplateEngine.process("coach-session-feedback-received-template.html", thymeleafContext);
        sendHtmlMessage(session.getCoach().getEmail(), "Session Feedback Received", htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(SENDER_MAIL);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            sendEmail(message);
        } catch(MessagingException ex) {
            throw new UnableToSendEmailException();
        }
    }

    private Context prepareMessageContent(String key, Object object) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put(key, object);
        sign(templateModel);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        return thymeleafContext;
    }

    private void sign(Map<String, Object> templateModel) {
        templateModel.put("regards", REGARDS);
        templateModel.put("senderName", SENDER_NAME);
    }
}
