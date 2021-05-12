package dev.riac.trapcount.report.trapcountreport.application.email;

import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
import com.wildbit.java.postmark.client.exception.PostmarkException;

import java.io.IOException;

public interface EmailService {

    MessageResponse sendEmail(String receiver, String fileName, String growerName, String ranchName, String date, String sender) throws IOException, PostmarkException;
}
