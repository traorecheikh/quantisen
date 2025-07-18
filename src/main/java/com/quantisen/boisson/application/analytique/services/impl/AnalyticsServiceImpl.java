package com.quantisen.boisson.application.analytique.services.impl;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.quantisen.boisson.application.analytique.dtos.*;
import com.quantisen.boisson.application.analytique.services.AnalyticsService;
import com.quantisen.boisson.domaine.stockage.enums.TypeMouvement;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequestScoped
public class AnalyticsServiceImpl implements AnalyticsService {

    @Inject
    private EntityManager em;

    @Override
    public DashboardStatisticsDto getDashboardStatistics() {
        // Total beverages
        int totalBeverages = ((Long) em.createQuery("SELECT COUNT(b) FROM Boisson b").getSingleResult()).intValue();
        // Total stock (sum of quantiteActuelle for vendable lots)
        int totalStock = ((Number) em.createQuery("SELECT COALESCE(SUM(l.quantiteActuelle), 0) FROM Lot l WHERE l.vendable = true").getSingleResult()).intValue();
        // Low stock alerts (number of boissons below threshold)
        int lowStockAlerts = ((Number) em.createQuery(
                "SELECT COUNT(b) FROM Boisson b WHERE (SELECT COALESCE(SUM(l.quantiteActuelle), 0) FROM Lot l WHERE l.boisson.id = b.id AND l.vendable = true) < b.seuil"
        ).getSingleResult()).intValue();
        // Total movements
        int totalMovements = ((Long) em.createQuery("SELECT COUNT(m) FROM Mouvement m").getSingleResult()).intValue();
        // Total users
        int totalUsers = ((Long) em.createQuery("SELECT COUNT(u) FROM CompteUtilisateur u").getSingleResult()).intValue();
        // Total value (sum of quantiteActuelle * prix for vendable lots)
        Double totalValue = (Double) em.createQuery(
                "SELECT COALESCE(SUM(l.quantiteActuelle * b.prix), 0) FROM Lot l JOIN l.boisson b WHERE l.vendable = true"
        ).getSingleResult();
        // Stock alerts (list)
        List<StockAlertDto> stockAlerts = getStockAlerts();
        return new DashboardStatisticsDto(
                totalBeverages,
                totalStock,
                lowStockAlerts,
                totalMovements,
                totalUsers,
                totalValue != null ? totalValue : 0.0,
                stockAlerts
        );
    }

    @Override
    public WeeklyStockMovementDto getWeeklyStockMovement() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate weekStart = today.minusDays(6);

            List<String> weekDates = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                weekDates.add(weekStart.plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            List<Integer> entriesData = new ArrayList<>();
            List<Integer> exitsData = new ArrayList<>();
            List<Integer> adjustmentsData = new ArrayList<>();

            for (String date : weekDates) {
                entriesData.add(getMovementCountForDate(date, "ENTREE"));
                exitsData.add(getMovementCountForDate(date, "SORTIE"));
                adjustmentsData.add(getMovementCountForDate(date, "AJUSTEMENT"));
            }

            List<WeeklyDatasetDto> datasets = List.of(
                    new WeeklyDatasetDto("Entrées", entriesData, "ENTREE", "#10b981"),
                    new WeeklyDatasetDto("Sorties", exitsData, "SORTIE", "#ef4444"),
                    new WeeklyDatasetDto("Ajustements", adjustmentsData, "AJUSTEMENT", "#f59e0b")
            );

            int totalEntries = entriesData.stream().mapToInt(Integer::intValue).sum();
            int totalExits = exitsData.stream().mapToInt(Integer::intValue).sum();
            int totalAdjustments = adjustmentsData.stream().mapToInt(Integer::intValue).sum();

            return new WeeklyStockMovementDto(weekDates, datasets, totalEntries, totalExits, totalAdjustments);
        } catch (Exception e) {
            throw new RuntimeException("Error generating weekly stock movement", e);
        }
    }

    private int getMovementCountForDate(String date, String type) {
        return ((Long) em.createQuery(
                "SELECT COUNT(m) FROM Mouvement m WHERE m.type = :type AND to_char(CAST(m.dateMouvement AS timestamp), 'YYYY-MM-DD') = :date"
        ).setParameter("type", TypeMouvement.valueOf(type)).setParameter("date", date).getSingleResult()).intValue();
    }

    @Override
    public List<MovementTrendDto> getMovementTrends(String period) {
        List<MovementTrendDto> trends = new ArrayList<>();
        trends.add(getTrendForPeriod("weekly", "Cette Semaine"));
        trends.add(getTrendForPeriod("monthly", "Ce Mois"));
        trends.add(getTrendForPeriod("yearly", "Cette Année"));
        return trends;
    }

    private MovementTrendDto getTrendForPeriod(String periodType, String label) {
        LocalDate today = LocalDate.now();
        LocalDate startDate, previousStartDate, previousEndDate;

        switch (periodType) {
            case "monthly":
                startDate = today.withDayOfMonth(1);
                previousStartDate = startDate.minusMonths(1);
                previousEndDate = startDate.minusDays(1);
                break;
            case "yearly":
                startDate = today.withDayOfYear(1);
                previousStartDate = startDate.minusYears(1);
                previousEndDate = startDate.minusDays(1);
                break;
            default: // weekly
                startDate = today.minusDays(today.getDayOfWeek().getValue() - 1);
                previousStartDate = startDate.minusWeeks(1);
                previousEndDate = startDate.minusDays(1);
                break;
        }

        int currentMovements = getMovementCountBetweenDates(startDate, today);
        int previousMovements = getMovementCountBetweenDates(previousStartDate, previousEndDate);

        String trend = "STABLE";
        if (currentMovements > previousMovements) {
            trend = "UP";
        } else if (currentMovements < previousMovements) {
            trend = "DOWN";
        }

        return new MovementTrendDto(label, currentMovements, trend);
    }


    private int getMovementCountBetweenDates(LocalDate startDate, LocalDate endDate) {
        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return ((Long) em.createQuery(
                "SELECT COUNT(m) FROM Mouvement m WHERE m.dateMouvement >= :startDate AND m.dateMouvement <= :endDate"
        ).setParameter("startDate", startDateStr).setParameter("endDate", endDateStr).getSingleResult()).intValue();
    }


    @Override
    public List<TopBeverageDto> getBeveragePerformance() {
        try {
            List<Object[]> results = em.createQuery(
                    "SELECT b.id, b.nom, COUNT(m.id), SUM(lo.quantite), SUM(lo.quantite * b.prix) " +
                            "FROM Mouvement m JOIN m.ligneOperations lo JOIN lo.lot l JOIN l.boisson b " +
                            "GROUP BY b.id, b.nom " +
                            "ORDER BY COUNT(m.id) DESC",
                    Object[].class
            ).getResultList();

            AtomicInteger rank = new AtomicInteger(1);
            return results.stream()
                    .map(row -> new TopBeverageDto(
                            (Long) row[0],
                            rank.getAndIncrement(),
                            (String) row[1],
                            ((Long) row[2]).intValue(),
                            ((Number) row[3]).intValue(),
                            ((Number) row[4]).doubleValue()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error getting beverage performance", e);
        }
    }

    @Override
    public InventoryAnalyticsDto getInventoryAnalytics() {
        try {
            List<Object[]> results = em.createQuery(
                    "SELECT b.nom, SUM(l.quantiteActuelle * b.prix) " +
                            "FROM Lot l JOIN l.boisson b " +
                            "WHERE l.vendable = true " +
                            "GROUP BY b.nom",
                    Object[].class
            ).getResultList();

            double totalValue = results.stream().mapToDouble(row -> (Double) row[1]).sum();
            String[] colors = {"#3498db", "#f1c40f", "#2ecc71", "#e74c3c", "#9b59b6"};
            AtomicInteger colorIndex = new AtomicInteger(0);

            List<StockDistributionDto> stockDistribution = results.stream()
                    .map(row -> new StockDistributionDto(
                            (String) row[0],
                            ((Double) row[1] / totalValue) * 100,
                            (Double) row[1],
                            colors[colorIndex.getAndIncrement() % colors.length]
                    ))
                    .collect(Collectors.toList());

            return new InventoryAnalyticsDto(stockDistribution, null, null);
        } catch (Exception e) {
            throw new RuntimeException("Error generating inventory analytics", e);
        }
    }

    @Override
    public byte[] exportDashboardExcel(String type) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            switch (type.toLowerCase()) {
                case "analytics":
                    exportAnalyticsToExcel(workbook);
                    break;
                case "movements":
                    exportMovementsToExcel(workbook);
                    break;
                case "inventory":
                    exportInventoryToExcel(workbook);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid export type: " + type);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting to Excel", e);
        }
    }

    private void exportAnalyticsToExcel(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Analytics");

        // Weekly Stock Movement
        WeeklyStockMovementDto weeklyMovement = getWeeklyStockMovement();
        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Weekly Stock Movement");

        Row dateHeader = sheet.createRow(rowNum++);
        dateHeader.createCell(0).setCellValue("Date");
        dateHeader.createCell(1).setCellValue("Entries");
        dateHeader.createCell(2).setCellValue("Exits");
        dateHeader.createCell(3).setCellValue("Adjustments");

        for (int i = 0; i < weeklyMovement.getWeekDates().size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(weeklyMovement.getWeekDates().get(i));
            row.createCell(1).setCellValue(weeklyMovement.getDatasets().get(0).getData().get(i));
            row.createCell(2).setCellValue(weeklyMovement.getDatasets().get(1).getData().get(i));
            row.createCell(3).setCellValue(weeklyMovement.getDatasets().get(2).getData().get(i));
        }
    }

    private void exportMovementsToExcel(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Movements");

        // Beverage Performance
        List<TopBeverageDto> performance = getBeveragePerformance();
        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Beverage Performance");

        Row perfHeader = sheet.createRow(rowNum++);
        perfHeader.createCell(0).setCellValue("Rank");
        perfHeader.createCell(1).setCellValue("Name");
        perfHeader.createCell(2).setCellValue("Total Movements");
        perfHeader.createCell(3).setCellValue("Total Quantity");
        perfHeader.createCell(4).setCellValue("Revenue Impact");

        for (TopBeverageDto beverage : performance) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(beverage.getRank());
            row.createCell(1).setCellValue(beverage.getName());
            row.createCell(2).setCellValue(beverage.getTotalMovements());
            row.createCell(3).setCellValue(beverage.getTotalQuantity());
            row.createCell(4).setCellValue(beverage.getRevenueImpact());
        }
    }

    private void exportInventoryToExcel(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Inventory");

        // Inventory Analytics
        InventoryAnalyticsDto analytics = getInventoryAnalytics();
        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Inventory Analytics");

        Row invHeader = sheet.createRow(rowNum++);
        invHeader.createCell(0).setCellValue("Category");
        invHeader.createCell(1).setCellValue("Percentage");
        invHeader.createCell(2).setCellValue("Value");

        for (StockDistributionDto distribution : analytics.getStockDistribution()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(distribution.getCategory());
            row.createCell(1).setCellValue(String.format("%.2f%%", distribution.getPercentage()));
            row.createCell(2).setCellValue(distribution.getValue());
        }
    }

    @Override
    public byte[] exportDashboardPdf(String type) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            switch (type.toLowerCase()) {
                case "analytics":
                    exportAnalyticsToPdf(document);
                    break;
                case "movements":
                    exportMovementsToPdf(document);
                    break;
                case "inventory":
                    exportInventoryToPdf(document);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid export type: " + type);
            }
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting to PDF", e);
        }
    }

    private void exportAnalyticsToPdf(Document document) {
        document.add(new Paragraph("Analytics Report"));

        // Weekly Stock Movement
        WeeklyStockMovementDto weeklyMovement = getWeeklyStockMovement();
        document.add(new Paragraph("Weekly Stock Movement"));
        PdfPTable weeklyTable = new PdfPTable(4);
        weeklyTable.addCell("Date");
        weeklyTable.addCell("Entries");
        weeklyTable.addCell("Exits");
        weeklyTable.addCell("Adjustments");

        for (int i = 0; i < weeklyMovement.getWeekDates().size(); i++) {
            weeklyTable.addCell(weeklyMovement.getWeekDates().get(i));
            weeklyTable.addCell(String.valueOf(weeklyMovement.getDatasets().get(0).getData().get(i)));
            weeklyTable.addCell(String.valueOf(weeklyMovement.getDatasets().get(1).getData().get(i)));
            weeklyTable.addCell(String.valueOf(weeklyMovement.getDatasets().get(2).getData().get(i)));
        }
        document.add(weeklyTable);
    }

    private void exportMovementsToPdf(Document document) {
        document.add(new Paragraph("Movements Report"));

        // Beverage Performance
        List<TopBeverageDto> performance = getBeveragePerformance();
        document.add(new Paragraph("Beverage Performance"));
        PdfPTable performanceTable = new PdfPTable(5);
        performanceTable.addCell("Rank");
        performanceTable.addCell("Name");
        performanceTable.addCell("Total Movements");
        performanceTable.addCell("Total Quantity");
        performanceTable.addCell("Revenue Impact");

        for (TopBeverageDto beverage : performance) {
            performanceTable.addCell(String.valueOf(beverage.getRank()));
            performanceTable.addCell(beverage.getName());
            performanceTable.addCell(String.valueOf(beverage.getTotalMovements()));
            performanceTable.addCell(String.valueOf(beverage.getTotalQuantity()));
            performanceTable.addCell(String.valueOf(beverage.getRevenueImpact()));
        }
        document.add(performanceTable);
    }

    private void exportInventoryToPdf(Document document) {
        document.add(new Paragraph("Inventory Report"));

        // Inventory Analytics
        InventoryAnalyticsDto analytics = getInventoryAnalytics();
        document.add(new Paragraph("Inventory Distribution"));
        PdfPTable inventoryTable = new PdfPTable(3);
        inventoryTable.addCell("Category");
        inventoryTable.addCell("Percentage");
        inventoryTable.addCell("Value");

        for (StockDistributionDto distribution : analytics.getStockDistribution()) {
            inventoryTable.addCell(distribution.getCategory());
            inventoryTable.addCell(String.format("%.2f%%", distribution.getPercentage()));
            inventoryTable.addCell(String.valueOf(distribution.getValue()));
        }
        document.add(inventoryTable);
    }

    // Other methods from the original implementation that are not directly related to the new endpoints
    // can be kept here if they are still needed for other parts of the application.
    // For brevity, they are omitted from this example.
    @Override
    public DailyMovementsDto getDailyMovements(String start, String end) {
        return new DailyMovementsDto(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public List<StockAlertDto> getStockAlerts() {
        List<Object[]> results = em.createQuery(
                "SELECT b.nom, COALESCE(SUM(l.quantiteActuelle), 0), b.seuil " +
                        "FROM Boisson b LEFT JOIN Lot l ON l.boisson.id = b.id AND l.vendable = true " +
                        "GROUP BY b.id, b.nom, b.seuil", Object[].class
        ).getResultList();
        List<StockAlertDto> alerts = new ArrayList<>();
        for (Object[] row : results) {
            String name = (String) row[0];
            int current = ((Number) row[1]).intValue();
            int threshold = ((Number) row[2]).intValue();
            String severity;
            if (current == 0) {
                severity = "CRITICAL";
            } else if (current < threshold) {
                severity = "LOW";
            } else {
                severity = "OK";
            }
            alerts.add(new StockAlertDto(name, current, threshold, severity));
            System.out.println("Stock Alert - Beverage: " + name + ", Current: " + current + ", Threshold: " + threshold);
        }
        return alerts;
    }

    @Override
    public List<ExpirationAlertDto> getExpirationAlerts() {
        return new ArrayList<>();
    }

    @Override
    public List<AccountActivityDto> getUserActivity() {
        return new ArrayList<>();
    }

    @Override
    public RevenueMetricsDto getRevenueMetrics() {
        return new RevenueMetricsDto();
    }
}
