package dev.riac.trapcount.report.trapcountreport.controller;

import com.wildbit.java.postmark.client.exception.PostmarkException;
import dev.riac.trapcount.report.trapcountreport.domain.report.model.ReceiverEnum;
import dev.riac.trapcount.report.trapcountreport.service.report.ITrapReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class TrapReportController {

    private final ITrapReportService iTrapReportService;

    @Async
    @GetMapping("/ranch/{ranchKey}/{receiver}")
    public ResponseEntity reportByRanch(@PathVariable Long ranchKey,
                                        @PathVariable ReceiverEnum receiver,
                                        @RequestParam String startDate,
                                        @RequestParam String sender) throws ParseException, PostmarkException, IOException {
        iTrapReportService.reportByRanch(ranchKey, receiver, startDate, sender);

        return ResponseEntity.ok().build();
    }

    @Async
    @GetMapping("/grower/{consultantKey}")
    public ResponseEntity reportByGrower(@PathVariable Long consultantKey,
                                         @RequestParam String growerName,
                                         @RequestParam String startDate,
                                         @RequestParam String sender) throws ParseException, PostmarkException, IOException {


        iTrapReportService.reportByGrower(growerName, consultantKey, startDate, sender);

        return ResponseEntity.ok().build();
    }
}
