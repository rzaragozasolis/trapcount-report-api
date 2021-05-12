package dev.riac.trapcount.report.trapcountreport.service.report;

import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
import com.wildbit.java.postmark.client.exception.PostmarkException;
import dev.riac.trapcount.report.trapcountreport.domain.report.model.ReceiverEnum;

import java.io.IOException;
import java.text.ParseException;

public interface ITrapReportService {

    MessageResponse reportByRanch(Long ranchKey, ReceiverEnum receiver, String startDate, String sender) throws IOException, PostmarkException, ParseException;
    MessageResponse reportByGrower(String growerName, Long consultantKey, String startDate, String sender) throws IOException, PostmarkException, ParseException;
}
