package dev.riac.trapcount.report.trapcountreport.infrascructure.email;

import com.wildbit.java.postmark.Postmark;
import com.wildbit.java.postmark.client.ApiClient;
import com.wildbit.java.postmark.client.data.model.message.Message;
import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
import com.wildbit.java.postmark.client.exception.PostmarkException;
import dev.riac.trapcount.report.trapcountreport.application.email.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Log4j2
@Service
public class EmailServicePostmark implements EmailService {

    @Value("${postmark.api.client}")
    private String postmarkApiClient;

    @Value("${trapcount.subject.trapdata.email.ranch}")
    private String subjectEmailRanch;

    @Value("${trapcount.subject.trapdata.email.grower}")
    private String subjectEmailGrower;

    @Value("${trapcount.body.trapdata.email}")
    private String htmlBodyMessage;

    @Value("${trapcount.email.bcc}")
    private String emailBCC;

    public MessageResponse sendEmail(String receiver, String fileName, String growerName, String ranchName, String date, String sender) throws IOException, PostmarkException {

        log.debug("sendEmail...");
        File file = new File(fileName);
        ApiClient client = Postmark.getApiClient(postmarkApiClient);

        String subject = "";
        if(ranchName != null
            && date != null) {
            subject = String.format(subjectEmailRanch, growerName, ranchName, date);
        } else {
            subject = String.format(subjectEmailGrower, growerName);
        }

        Message message = new Message(sender, receiver, subject, htmlBodyMessage + subject);
        message.setBcc(emailBCC);

        message.addAttachment(file.getPath());

        log.debug("Send report email from: " + sender + " to: " + receiver);
        MessageResponse response = client.deliverMessage(message);

        log.debug("response.getMessage(): " + response.getMessage());
        log.debug("response.getMessageId(): " + response.getMessageId());
        return response;
    }
}
