package io.resttestgen.implementation.writer;

import com.google.gson.JsonObject;
import io.resttestgen.boot.Configuration;
import io.resttestgen.core.Environment;
import io.resttestgen.core.testing.Coverage;
import io.resttestgen.core.testing.coverage.CoverageManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoverageReportWriter {

    private final CoverageManager coverageManager;
    private final Configuration configuration = Environment.getInstance().getConfiguration();

    public CoverageReportWriter(CoverageManager coverageManager){
        this.coverageManager = coverageManager;
    }

    public String getOutputFormatName() {
        return "CoverageReports";
    }

    public String getCoverageReportTemplatePath() {
        return "coverage-report-template.html";
    }

    private String getOutputPath(){
        return configuration.getOutputPath() + configuration.getTestingSessionName() + "/" + getOutputFormatName() + "/";
    }

    public void write() throws IOException {
        File file = new File(getOutputPath());

        boolean created = file.mkdirs();
        if (!created && !file.exists()) {
            throw new IOException("Failed to create output directory: " + getOutputPath());
        }

        // Create the output file
        FileWriter writer = new FileWriter(getOutputPath() + "CoverageStats.json");
        JsonObject jsonRoot = new JsonObject();

        // Read the HTML template file
        File templateFile = new File(getCoverageReportTemplatePath());
        StringBuilder templateContent = new StringBuilder();
        try (java.util.Scanner scanner = new java.util.Scanner(templateFile)) {
            while (scanner.hasNextLine()) {
                templateContent.append(scanner.nextLine()).append("\n");
            }
        }

        // Replace the placeholders with the data
        String htmlContent = templateContent.toString();
        htmlContent = htmlContent.replace("{{API_NAME}}", configuration.getApiUnderTest());
        htmlContent = htmlContent.replace("{{TESTING_SESSION_ID}}", configuration.getTestingSessionName());
        
        LocalDateTime timestamp = configuration.getTimestamp();
        htmlContent = htmlContent.replace("{{TIMESTAMP}}", timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Loop through the coverage objects and generate the report
        for(Coverage coverage: coverageManager.getCoverages()){
            FileWriter singleCoverageReportWriter = new FileWriter(getOutputPath() + coverage.getClass().getSimpleName() + (".json"));
            singleCoverageReportWriter.write(String.valueOf(coverage.getReportAsJsonObject()));
            singleCoverageReportWriter.close();

            JsonObject raw = new JsonObject();
            raw.addProperty("documented", coverage.getToTest());
            raw.addProperty("documentedTested", coverage.getNumOfTestedDocumented());
            raw.addProperty("notDocumentedTested", coverage.getNumOfTestedNotDocumented());

            JsonObject simpleCoverageReport = new JsonObject();
            simpleCoverageReport.add("raw",raw);
            simpleCoverageReport.addProperty("rate", coverage.getCoverage().toString());

            jsonRoot.add(coverage.getClass().getSimpleName(), simpleCoverageReport);

            htmlContent = htmlContent.replace("//{{JSONDATA}}",
                "'" + coverage.getClass().getSimpleName() + "': " + coverage.getReportAsJsonObject().toString() + ",\n" + "//{{JSONDATA}}");
        }

        writer.write(jsonRoot.toString());
        writer.close();

        // Write the HTML report
        htmlContent = htmlContent.replace("//{{JSONDATA}}", "'CoverageStats': "+ jsonRoot.toString()) + "\n";
        
        FileWriter htmlWriter = new FileWriter(getOutputPath() + "coverage-report.html");
        htmlWriter.write(htmlContent);
        htmlWriter.close();
    }
}
