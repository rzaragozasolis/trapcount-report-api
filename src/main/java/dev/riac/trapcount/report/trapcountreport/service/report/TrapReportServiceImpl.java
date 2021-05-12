package dev.riac.trapcount.report.trapcountreport.service.report;

import com.wildbit.java.postmark.client.data.model.message.MessageResponse;
import com.wildbit.java.postmark.client.exception.PostmarkException;
import dev.riac.trapcount.report.trapcountreport.application.email.EmailService;
import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MapEntity;
import dev.riac.trapcount.report.trapcountreport.domain.report.entity.RanchEntity;
import dev.riac.trapcount.report.trapcountreport.domain.report.model.ReceiverEnum;
import dev.riac.trapcount.report.trapcountreport.domain.report.model.TrapDataResponse;
import dev.riac.trapcount.report.trapcountreport.domain.report.repository.RanchRepository;
import dev.riac.trapcount.report.trapcountreport.domain.report.repository.TrapRepository;
import dev.riac.trapcount.report.trapcountreport.service.MapService;
import dev.riac.trapcount.report.trapcountreport.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TrapReportServiceImpl implements ITrapReportService {

    private final TrapRepository trapRepository;
    private final RanchRepository ranchRepository;
    private final EmailService emailService;
    private final MemberService memberService;
    private final MapService mapService;
    private final IGenerateReport iGenerateReportTrapData;

    @Value("${report.filename.trapdata}")
    private String filename;

    @Value("${trapcount.skip.send.email}")
    private Boolean skipSendEmail;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public MessageResponse reportByRanch(Long ranchKey, ReceiverEnum receiver, String startDate, String sender) throws IOException, PostmarkException, ParseException {

        String email = "";
        RanchEntity ranchEntity = ranchRepository.findById(ranchKey).orElseThrow(() -> new BadRequestException("No Ranch found"));

        switch (receiver){
            case CONSULTANT:
                email = memberService.getEmailAddressConsultant(ranchEntity.getFieldConsultantMemberKey());
                break;
            case GROWER:
                email = ranchEntity.getGroweremail();
                break;
            case BOTH:
                String emailConsultant = memberService.getEmailAddressConsultant(ranchEntity.getFieldConsultantMemberKey());
                email = emailConsultant + "; " + ranchEntity.getGroweremail();
            default:
                break;
        }

        Date parsedDate = dateFormat.parse(startDate);
        Object[] trapDataResponse = trapRepository.getAllTrapDataByRanch(ranchKey, parsedDate);

        List<TrapDataResponse> responseList = mapObjectToTrapDataResponse(trapDataResponse);

        Map<RanchEntity, List<TrapDataResponse>> mapDataReport = new TreeMap<>();
        mapDataReport.put(ranchEntity, responseList);

        Optional<MapEntity> maybeImageMap = mapService.getResourseImageMap(ranchKey);

        String fileReportName = filename + "-" + ranchEntity.getGrowername() + " - " + ranchEntity.getRanchName() + ".pdf";

        // Generate PDF
        ByteArrayInputStream bis = iGenerateReportTrapData.generateReport(mapDataReport, fileReportName, maybeImageMap, true);

        if (skipSendEmail)
            return null;
        return emailService.sendEmail(email,
                                        fileReportName,
                                        ranchEntity.getGrowername(),
                                        ranchEntity.getRanchName(),
                                        getFormatDate(ranchEntity.getLastmodified()),
                                        sender);
    }

    @Override
    public MessageResponse reportByGrower(String growerName, Long consultantKey, String startDate, String sender) throws IOException, PostmarkException, ParseException {

        List<RanchEntity> ranchesKey = ranchRepository.getRanchesKeyByGrowerNameAndFieldConsultantMemberKey(growerName, consultantKey);
        Date parsedDate = dateFormat.parse(startDate);

        Map<RanchEntity, List<TrapDataResponse>> mapDataReport = new TreeMap<>();

        ranchesKey.forEach(r -> {
            Object[] trapDataResponse = trapRepository.getAllTrapDataByRanch(r.getRanchKey(), parsedDate);
            mapDataReport.put(r, mapObjectToTrapDataResponse(trapDataResponse));
        });

        String fileReportName = filename + "-" + growerName + ".pdf";

        // Generate PDF
        ByteArrayInputStream bis = iGenerateReportTrapData.generateReport(mapDataReport, fileReportName, Optional.empty(), false);

        String email = memberService.getEmailAddressConsultant(consultantKey);

        if (skipSendEmail)
            return null;
        return emailService.sendEmail(email,
                                        fileReportName,
                                        growerName,
                                        null,
                                        null,
                                        sender);
    }

    private String getFormatDate(String s) {

        SimpleDateFormat originalDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat resultDate = new SimpleDateFormat("MM-dd-yyyy");
        String dateString = "";
        try {
            Date dateValue = originalDate.parse(s);
            dateString = resultDate.format(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }

        return dateString;
    }


    private List<TrapDataResponse> mapObjectToTrapDataResponse(Object[] data) {

        List<TrapDataResponse> responseList = new ArrayList<>();

        for (Object datum : data) {

            Object[] d = (Object[]) datum;

            TrapDataResponse response = TrapDataResponse.builder()
                    .weekNumber((Integer) d[0])
                    .weekStart((Timestamp) d[1])
                    .trapId((String) d[2])
                    .trapKey((BigInteger) d[3])
                    .trapType((String) d[4])
                    .trapCount((Integer) d[5])
                    .insectType((String) d[6])
                    .blockName((String) d[7])
                    .build();
            responseList.add(response);
        }

        return responseList.stream()
                .sorted(Comparator.comparing(TrapDataResponse::getTrapType))
                .collect(Collectors.toList());
    }
}
