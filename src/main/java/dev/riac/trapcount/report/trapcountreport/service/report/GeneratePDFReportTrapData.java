package dev.riac.trapcount.report.trapcountreport.service.report;

import com.google.common.collect.Maps;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.RoundDotsBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import dev.riac.trapcount.report.trapcountreport.domain.report.entity.MapEntity;
import dev.riac.trapcount.report.trapcountreport.domain.report.entity.RanchEntity;
import dev.riac.trapcount.report.trapcountreport.domain.report.model.TrapCount;
import dev.riac.trapcount.report.trapcountreport.domain.report.model.TrapCountTemp;
import dev.riac.trapcount.report.trapcountreport.domain.report.model.TrapDataResponse;
import dev.riac.trapcount.report.trapcountreport.domain.report.model.TrapType;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.itextpdf.kernel.font.PdfFontFactory.createFont;

@Log4j2
@Component("generatorPdfReportTrapData")
public class GeneratePDFReportTrapData implements IGenerateReport<TrapDataResponse> {

    private static final String LANDSCAPE = "Landscape";
    private static final String PORTRAIT = "Portrait";
    private static final String NOT_DATA_FOUND = "Not Data Found";
    private final String CLASSPATH_LOGO = "classpath:gsl.png";
    private static final String PATTERN_MM_DD_YYYY = "yyyy-MM-dd";

    private final DateFormat dateFormat = new SimpleDateFormat(PATTERN_MM_DD_YYYY);
    private static final String TOTAL = "Total";
    private static final DeviceRgb WHITE_COLOR = WebColors.getRGBColor("#FFFFFF");

    public ByteArrayInputStream generateReport(Map<RanchEntity, List<TrapDataResponse>> dataReport, String fileName, Optional<MapEntity> maybeImageMap, boolean flagRanchReport) throws IOException {

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(fileName));
        Document doc = new Document(pdfDoc, PageSize.LETTER.rotate());

        Table tableHeader = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        // Logo Trap Count
        Image imgLogo = null;
        try {
            imgLogo = new Image(ImageDataFactory.create(CLASSPATH_LOGO)).scale(0.45f, 0.45f);
            if(flagRanchReport)
                tableHeader.addCell(new Cell(2,1).add(imgLogo).setWidth(70).setBorder(Border.NO_BORDER));
            else
                doc.add(imgLogo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (dataReport != null && !dataReport.isEmpty()) {

            dataReport.forEach((ranch, value) -> {

                if (value.size() > 0) {
                    List<String> headersWeekStart = getHeadersWeekStart(value);
                    if(flagRanchReport) {

                        Paragraph growerNameH1 = new Paragraph("Grower Name: " + ranch.getGrowername()).setBold();
                        Paragraph startDayH1 = new Paragraph("Start Day: " + getFormatDate(headersWeekStart.get(0))).setBold();
                        Paragraph ranchH1 = new Paragraph("Ranch: " + ranch.getRanchName()).setBold();
                        Paragraph currentDayH1 = new Paragraph("Current Day: " + getFormatDate(headersWeekStart.get(headersWeekStart.size() - 2))).setBold();

                        tableHeader.addCell(new Cell().add(growerNameH1).setBorder(Border.NO_BORDER));
                        tableHeader.addCell(new Cell().add(startDayH1).setBorder(Border.NO_BORDER));
                        tableHeader.addCell(new Cell().add(ranchH1).setBorder(Border.NO_BORDER));
                        tableHeader.addCell(new Cell().add(currentDayH1).setBorder(Border.NO_BORDER));
                        doc.add(tableHeader);

                    }else {
                        Text ranchTitle = new Text(ranch.getRanchName());

                        try {
                            ranchTitle.setFontColor(WebColors.getRGBColor("#3A5DD7"))
                                    .setFont(createFont(StandardFonts.HELVETICA_BOLD));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        doc.add(new Paragraph(ranchTitle));
                    }

                    doc.add(generateTable(value, headersWeekStart)).add(new Paragraph().setPaddingBottom(10));
                    if(maybeImageMap.isPresent()){
                        try {
                            MapEntity imageMap = maybeImageMap.get();
                            Image img = new Image(ImageDataFactory.create(imageMap.getFileName())).setAutoScale(true);


                            PageSize pageSize = (imageMap.getPageOrientation().equals(LANDSCAPE)) ? PageSize.LETTER.rotate() : PageSize.LETTER;
                            pdfDoc.setDefaultPageSize(pageSize);
                            doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                            doc.add(img);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }

                }else {
                    String messageNotFound = Optional.ofNullable(ranch)
                            .map(r -> NOT_DATA_FOUND + " for Ranch " + r.getRanchName())
                            .orElse(NOT_DATA_FOUND);
                    log.debug(messageNotFound);
                    if(flagRanchReport) {
                        doc.add(tableHeader);
                    }
                    doc.add(new Paragraph(messageNotFound).setBold());
                }
            });
            doc.close();
        } else {
            log.debug("generateReport... Not data found");
            doc.add(tableHeader);
            doc.add(new Paragraph(NOT_DATA_FOUND).setBold());
            doc.close();
        }
        return null;
    }

    private String getFormatDate(String s) {

        String dateString = "";
        try {
            Date dateValue = new SimpleDateFormat("yyyy-MM-dd").parse(s);
            dateString = new SimpleDateFormat("MM-dd-yyyy").format(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }

        return dateString;
    }

    private Table generateTable(List<TrapDataResponse> listTrapData, List<String> headersWeekStart) {

        Table table = new Table(UnitValue.createPercentArray(headersWeekStart.size() + 3)).useAllAvailableWidth();
        table.addHeaderCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addHeaderCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addHeaderCell(new Cell().add(new Paragraph(""))
                .setBorderTop(Border.NO_BORDER)
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER));
        if(headersWeekStart.size() < 4) {
            table.setWidth(headersWeekStart.size() * 100);
        }else if(headersWeekStart.size() < 7) {
            table.setWidth(headersWeekStart.size() * 70);
        } else if(headersWeekStart.size() < 12){
            table.setWidth(headersWeekStart.size() * 50);
        }


        headersWeekStart.forEach(header -> {
            Cell cell = null;
            try {
                cell = addHeaderCell(header);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            table.addHeaderCell(cell);
        });

        NavigableMap<TrapType, Map<String, Set<TrapCount>>> trapAsTree = groupByTrapTypeAndTrapIdAsTree(listTrapData);

        trapAsTree.forEach((key, setMap) -> {

            String inspectType = key.getInspectType();
            String trapType = key.getTrapType();

            Set<Integer> setTrapIds = getSetTrapIds(setMap);

            table.addCell(addCell(inspectType, setTrapIds.size()));
            table.addCell(addCell(trapType, setTrapIds.size()));

            Map<String, List<Integer>> sumList = new HashMap<>();

            AtomicInteger ordinal = new AtomicInteger(0);

            setTrapIds.forEach(id -> {
                int tmpId = ordinal.incrementAndGet();
                if (id.equals(Integer.MAX_VALUE)) {
                    table.addCell(addCellBold());
                } else {
                    table.addCell(addCellWithColorForID(id.toString(), WebColors.getRGBColor("#194CEA"), getBackgroundColor(tmpId)));
                }

                List<Integer> sumRow = new ArrayList<>();

                headersWeekStart.forEach(week -> {
                    if (setMap.containsKey(week)) {
                        Set<TrapCount> trapCounts = setMap.get(week);

                        Optional<TrapCount> maybeTrapCount = trapCounts.stream().filter(tr -> tr.getTrapId().equalsIgnoreCase(id.toString())).findAny();

                        if (maybeTrapCount.isPresent()) {
                            String pTrapCount = Optional.ofNullable(maybeTrapCount.get().getTrapCount())
                                    .map(v -> {
                                        if(sumList.containsKey(week)) {
                                            sumList.get(week).add(v);
                                        } else {
                                            List<Integer> list = new ArrayList<>();
                                            list.add(v);
                                            sumList.put(week, list);
                                        }
                                        sumRow.add(v);
                                        return v.toString();
                                    })
                                    .orElse("");
                            table.addCell(addCell(pTrapCount, getBackgroundColor(tmpId)).setBorder(Border.NO_BORDER));

                        } else {
                            if (id.equals(Integer.MAX_VALUE)) {
                                List<Integer> integers = Optional.ofNullable(sumList.get(week)).orElse(new ArrayList<>());
                                int sum = integers.stream().mapToInt(v -> Optional.ofNullable(v).orElse(0)).sum();
                                sumRow.add(sum);
                                table.addCell(addCellBoldBorderTop(String.valueOf(sum)));
                            } else {
                                table.addCell(addCell("", getBackgroundColor(tmpId)));
                            }
                        }
                    } else {
                        if (week.equals(TOTAL)) {
                            int sum = sumRow.stream().mapToInt(v -> Optional.ofNullable(v).orElse(0)).sum();
                            table.addCell(addCellBoldColumnTotal(String.valueOf(sum)));
                        }else {
                            if (id.equals(Integer.MAX_VALUE)) {
                                table.addCell(addCellTotals());
                            } else {
                                table.addCell(addCell("", getBackgroundColor(tmpId)));
                            }
                        }
                    }
                });
            });
        });

        return table;
    }

    private DeviceRgb getBackgroundColor(int tmpId) {
        return (tmpId % 2 != 0) ? WebColors.getRGBColor("#EEF7FF") : WHITE_COLOR;
    }

    private Set<Integer> getSetTrapIds ( Map<String, Set<TrapCount>> setMap) {

        Set<Integer> trapIds = new TreeSet<>();
        trapIds.add(Integer.MAX_VALUE);

        setMap.forEach((key, value) -> value.forEach(v -> trapIds.add(Integer.valueOf(v.getTrapId()))));

        return trapIds;
    }

    private List<String> getHeadersWeekStart(List<TrapDataResponse> listTrapData) {

        List<String> headers = listTrapData.stream()
                .map(t -> dateFormat.format(t.getWeekStart()))
                .distinct().sorted().collect(Collectors.toList());

        headers.add(TOTAL);

        return headers;
    }

    public TreeMap<TrapType, Map<String, Set<TrapCount>>>  groupByTrapTypeAndTrapIdAsTree(List<TrapDataResponse> listTrapData) {

        Map<TrapType, Map<String, Set<TrapCountTemp>>> resultMapTemp = listTrapData.stream()
                .collect(Collectors.groupingBy(t -> TrapType.builder().trapType(t.getTrapType()).inspectType(t.getInsectType()).build(),
                        Collectors.groupingBy(t -> dateFormat.format(t.getWeekStart()),
                                Collectors.mapping(t -> TrapCountTemp.builder()
                                        .trapId(t.getTrapId())
                                        .trapKey(t.getTrapKey())
                                        .trapCount(t.getTrapCount())
                                        .build(), Collectors.toSet()))));

        TreeMap<TrapType, Map<String, Set<TrapCount>>> resultData = new TreeMap<>();

        resultMapTemp.forEach((key, value1) -> {
            Map<String, Set<TrapCount>> temp = new TreeMap<>();

            value1.forEach((key1, value) -> {

                List<TrapCount> trapCountTemps = value.stream().collect(Collectors.groupingBy(TrapCountTemp::getTrapId)
                ).values().stream()
                        .map(countTemps -> countTemps.stream()
                                .reduce((f1, f2) -> TrapCountTemp.builder()
                                        .trapId(f1.getTrapId())
                                        .trapCount(f1.getTrapCount() + f2.getTrapCount())
                                        .build())
                        ).map(f -> {
                            TrapCountTemp tmp = Optional.ofNullable(f.get()).orElse(TrapCountTemp.builder().build());
                            return TrapCount.builder()
                                    .trapId(tmp.getTrapId())
                                    .trapCount(tmp.getTrapCount())
                                    .build();
                        })
                        .collect(Collectors.toList());

                temp.put(key1, new TreeSet<>(trapCountTemps));

            });
            resultData.put(key, temp);
        });

        return resultData;
    }

    // Generic function to construct a
    // new TreeMap from HashMap
    public static <K extends Comparable, V> TreeMap<K, V> convertToTreeMap(Map<K, V> hashMap)
    {
        // Create a new TreeMap
        TreeMap<K, V> treeMap = Maps.newTreeMap();

        // Pass the hashMap to putAll() method
        treeMap.putAll(hashMap);

        // Return the TreeMap
        return treeMap;
    }

    private Cell addCell(String content, int row) {
        Cell cell = new Cell(row, 1);
        cell.setBorderLeft(Border.NO_BORDER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setPadding(5);
        cell.add(new Paragraph(content).setFontSize(8).setBold().setTextAlignment(TextAlignment.CENTER));

        return cell;
    }

    private Cell addHeaderCell(String content) throws ParseException {

        Cell cell = new Cell();

        cell.setBackgroundColor(WebColors.getRGBColor("#b0c4de"));
        if (content.equals(TOTAL)) {
            cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));
            cell.add(new Paragraph(content).setFontSize(8).setBold()
            .setTextAlignment(TextAlignment.CENTER));
            return cell;
        }
        LocalDate localDate = LocalDate.parse(content);

        String month = localDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
        String day = String.valueOf(localDate.getDayOfMonth());
        cell.add(new Paragraph(month + "\n" + day)
                .setFontSize(8)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        return cell;
    }

    private Cell addCell(String content, DeviceRgb rgbColor) {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.add(new Paragraph(content).setFontSize(8)
        .setTextAlignment(TextAlignment.CENTER)
        ).setBorderLeft(Border.NO_BORDER)
        .setBorderRight(Border.NO_BORDER)
        .setBorderBottom(new RoundDotsBorder(1))
        .setBackgroundColor(rgbColor);

        return cell;
    }

    private Cell addCellTotals() {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.add(new Paragraph("").setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
        ).setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(1))
                .setBorderTop(new SolidBorder(1))
                .setBackgroundColor(WHITE_COLOR);

        return cell;
    }

    private Cell addBlockCell(DeviceRgb rgbColor) {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.add(new Paragraph("").setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
        ).setBorderLeft(new RoundDotsBorder(0.5f))
                .setBorderRight(new RoundDotsBorder(0.5f))
                .setBorderBottom(new RoundDotsBorder(0.5f))
                .setBackgroundColor(rgbColor);

        return cell;
    }

    private Cell addCellWithColor(String content, DeviceRgb rgbColor, DeviceRgb backgroundColor) {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.add(new Paragraph(content).setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
        ).setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderBottom(new RoundDotsBorder(1))
        .setFontColor(rgbColor)
        .setBackgroundColor(backgroundColor)
        .setWidth(6);

        return cell;
    }

    private Cell addCellWithColorForID(String content, DeviceRgb rgbColor, DeviceRgb backgroundColor) {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.add(new Paragraph(content).setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
        ).setBorderRight(new SolidBorder(1))
                .setBorderBottom(new RoundDotsBorder(1))
                .setFontColor(rgbColor)
                .setBackgroundColor(backgroundColor)
                .setWidth(6);

        return cell;
    }

    private Cell addCellBold() {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.add(new Paragraph(TOTAL).setFontSize(8).setBold()
        .setTextAlignment(TextAlignment.CENTER))
                .setBorderRight(new SolidBorder(1));

        return cell;
    }

    private Cell addCellBoldColumnTotal(String content) {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.add(new Paragraph(content).setFontSize(8).setBold()
                .setTextAlignment(TextAlignment.CENTER))
                .setBorderLeft(new SolidBorder(1))
        .setBorderRight(Border.NO_BORDER);

        return cell;
    }

    private Cell addCellBoldBorderTop(String content) {
        Cell cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.add(new Paragraph(content).setFontSize(8).setBold()
                .setTextAlignment(TextAlignment.CENTER))
        .setBorderLeft(Border.NO_BORDER)
        .setBorderRight(Border.NO_BORDER)
        .setBorderTop(new SolidBorder(1))
        .setBorderBottom(new SolidBorder(1));

        return cell;
    }

}
