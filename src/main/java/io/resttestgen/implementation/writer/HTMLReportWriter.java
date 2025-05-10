package io.resttestgen.implementation.writer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.resttestgen.boot.Configuration;
import io.resttestgen.core.datatype.parameter.Parameter;
import io.resttestgen.core.datatype.parameter.attributes.ParameterType;
import io.resttestgen.core.datatype.parameter.leaves.LeafParameter;
import io.resttestgen.core.helper.CoverageCollection;
import io.resttestgen.core.openapi.Operation;
import io.resttestgen.core.testing.Coverage;
import io.resttestgen.core.testing.TestInteraction;
import io.resttestgen.core.testing.TestSequence;
import io.resttestgen.core.testing.coverage.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Generates HTML reports with test sequence data and coverage information.
 * Handles file operations, template generation, and JSON data injection.
 */
public class HTMLReportWriter {
    // Path constants for templates and resources
    private static final String REPORT_RESOURCES_TEMPLATE_FOLDER = "src/main/resources/report-resources/";
    private static final String COVERAGE_REPORT_TEMPLATE = "coverage-report-template.html";
    private static final String REPORT_OUTPUT_FILENAME = "coverage-report.html";
    private static final String CSS_SUBFOLDER = "css/";
    private static final String JS_SUBFOLDER = "js/";
    private static final String JS_CONSTANTS_FILENAME = "constants.js";

    // HTML template placeholders
    private static final String API_NAME_PLACEHOLDER = "{{API_NAME}}";
    private static final String SESSION_ID_PLACEHOLDER = "{{TESTING_SESSION_ID}}";
    private static final String TIMESTAMP_PLACEHOLDER = "{{TIMESTAMP}}";
    private static final String TEST_SEQUENCE_JSON_DATA_PLACEHOLDER = "//{{TEST-SEQUENCE-JSON-DATA}}";

    private final Configuration configuration;
    private CoverageCollection coverageCollection;
    private final Gson gson;

    /**
     * Creates a new HTMLReportWriter with dependency injection for configuration.
     *
     * @param configuration The application configuration
     * @throws IllegalArgumentException If configuration is null
     */
    public HTMLReportWriter(Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null");
        this.coverageCollection = new CoverageCollection();
        this.gson = new Gson();
    }

    /**
     * Copy a file or directory to the output location.
     *
     * @param sourceTarget Source file or directory
     * @param destTarget Destination file or directory
     * @throws IOException If an I/O error occurs
     * @throws IllegalArgumentException If any parameter is null
     */
    private void copyFileOrDir(File sourceTarget, File destTarget) throws IOException {
        Objects.requireNonNull(sourceTarget, "Source target cannot be null");
        Objects.requireNonNull(destTarget, "Destination target cannot be null");

        if (!sourceTarget.exists()) {
            throw new IOException("Source file or directory does not exist: " + sourceTarget.getPath());
        }

        if (!destTarget.exists()) {
            if (!destTarget.mkdirs()) {
                throw new IOException("Failed to create destination directory: " + destTarget.getPath());
            }
        }

        String[] files = sourceTarget.list();
        if (files == null) {
            throw new IOException("Failed to list files in: " + sourceTarget.getPath());
        }

        for (String f : files) {
            File source = new File(sourceTarget, f);
            File dest = new File(destTarget, f);
            if (source.isDirectory()) {
                copyFileOrDir(source, dest);
            } else {
                try (InputStream in = new FileInputStream(source);
                     OutputStream out = new FileOutputStream(dest)) {
                    byte[] buf = new byte[4096]; // Larger buffer for efficiency
                    int length;
                    while ((length = in.read(buf)) > 0) {
                        out.write(buf, 0, length);
                    }
                }
            }
        }
    }

    /**
     * Populates coverage collection from coverage manager data.
     *
     * @param coverageManager The coverage manager containing coverage data
     * @return The populated coverage collection
     * @throws IllegalArgumentException If coverage manager is null
     */
    public CoverageCollection populateCoverageCollection(CoverageManager coverageManager) {
        Objects.requireNonNull(coverageManager, "Coverage manager cannot be null");

        List<Coverage> coverages = coverageManager.getCoverages();

        // Extract single coverages from list
        PathCoverage pathCoverage = null;
        OperationCoverage operationCoverage = null;
        StatusCodeCoverage statusCodeCoverage = null;
        ParameterCoverage parameterCoverage = null;
        ParameterValueCoverage parameterValueCoverage = null;

        for (Coverage coverage : coverages) {
            if (coverage instanceof PathCoverage) {
                pathCoverage = (PathCoverage) coverage;
            } else if (coverage instanceof OperationCoverage) {
                operationCoverage = (OperationCoverage) coverage;
            } else if (coverage instanceof StatusCodeCoverage) {
                statusCodeCoverage = (StatusCodeCoverage) coverage;
            } else if (coverage instanceof ParameterCoverage) {
                parameterCoverage = (ParameterCoverage) coverage;
            } else if (coverage instanceof ParameterValueCoverage) {
                parameterValueCoverage = (ParameterValueCoverage) coverage;
            }
        }

        this.coverageCollection = new CoverageCollection(
                pathCoverage != null ? pathCoverage.getDocumentedPaths() : new HashSet<>(),
                pathCoverage != null ? pathCoverage.getTestedPaths() : new HashSet<>(),
                operationCoverage != null ? operationCoverage.getDocumentedOperations() : new HashSet<>(),
                operationCoverage != null ? operationCoverage.getTestedOperations() : new HashSet<>(),
                statusCodeCoverage != null ? statusCodeCoverage.getDocumentedStatusCodes() : new HashMap<>(),
                statusCodeCoverage != null ? statusCodeCoverage.getTestedStatusCodes() : new HashMap<>(),
                parameterCoverage != null ? parameterCoverage.getDocumentedParameters() : new HashMap<>(),
                parameterCoverage != null ? parameterCoverage.getTestedParameters() : new HashMap<>(),
                parameterValueCoverage != null ? parameterValueCoverage.getDocumentedValues() : new HashMap<>(),
                parameterValueCoverage != null ? parameterValueCoverage.getTestedValues() : new HashMap<>()
        );

        return this.coverageCollection;
    }

    /**
     * Creates all required report resource files.
     *
     * @throws IOException If an I/O error occurs
     */
    public void createReportResourcesFiles() throws IOException {
        // Create output directory if it doesn't exist
        Files.createDirectories(getOutputPath());

        // Fill the HTML template
        this.fillHTMLTemplate();

        // Copy CSS resources
        this.copyFileOrDir(
                getCSSTemplateFolder().toFile(),
                getCSSOutputFolder().toFile()
        );

        // Copy JS resources
        this.copyFileOrDir(
                getJSTemplateFolder().toFile(),
                getJSOutputFolder().toFile()
        );
    }

    /**
     * Gets the base output path for the report.
     *
     * @return Path object representing the output directory
     */
    private Path getOutputPath() {
        return Paths.get(configuration.getOutputPath(), configuration.getTestingSessionName());
    }

    /**
     * Gets the report resources template folder path.
     *
     * @return Path object for the template folder
     */
    private Path getReportResourcesTemplateFolder() {
        return Paths.get(REPORT_RESOURCES_TEMPLATE_FOLDER);
    }

    /**
     * Gets the report resources output folder path.
     *
     * @return Path object for the output folder
     */
    private Path getReportResourcesOutputFolder() {
        return getOutputPath().resolve("report-resources");
    }

    /**
     * Gets the HTML template file path.
     *
     * @return Path object for the HTML template
     */
    private Path getHTMLTemplateFile() {
        return getReportResourcesTemplateFolder().resolve(COVERAGE_REPORT_TEMPLATE);
    }

    /**
     * Gets the HTML output file path.
     *
     * @return Path object for the HTML output file
     */
    private Path getHTMLOutputFile() {
        return getOutputPath().resolve(REPORT_OUTPUT_FILENAME);
    }

    /**
     * Gets the CSS template folder path.
     *
     * @return Path object for the CSS template folder
     */
    private Path getCSSTemplateFolder() {
        return getReportResourcesTemplateFolder().resolve(CSS_SUBFOLDER);
    }

    /**
     * Gets the CSS output folder path.
     *
     * @return Path object for the CSS output folder
     */
    private Path getCSSOutputFolder() {
        return getReportResourcesOutputFolder().resolve(CSS_SUBFOLDER);
    }

    /**
     * Gets the JS template folder path.
     *
     * @return Path object for the JS template folder
     */
    private Path getJSTemplateFolder() {
        return getReportResourcesTemplateFolder().resolve(JS_SUBFOLDER);
    }

    /**
     * Gets the JS output folder path.
     *
     * @return Path object for the JS output folder
     */
    private Path getJSOutputFolder() {
        return getReportResourcesOutputFolder().resolve(JS_SUBFOLDER);
    }

    /**
     * Gets the JS constants output file path.
     *
     * @return Path object for the JS constants file
     */
    private Path getJSConstantsOutputFile() {
        return getJSOutputFolder().resolve(JS_CONSTANTS_FILENAME);
    }

    /**
     * Fills the HTML template with basic information like API name and timestamp.
     *
     * @throws IOException If an I/O error occurs
     */
    public void fillHTMLTemplate() throws IOException {
        Path htmlTemplatePath = getHTMLTemplateFile();
        Path htmlOutputPath = getHTMLOutputFile();

        // Create parent directories if they don't exist
        Files.createDirectories(htmlOutputPath.getParent());

        String htmlContent = new String(Files.readAllBytes(htmlTemplatePath));
        htmlContent = htmlContent.replace(API_NAME_PLACEHOLDER, configuration.getApiUnderTest());
        htmlContent = htmlContent.replace(SESSION_ID_PLACEHOLDER, configuration.getTestingSessionName());

        LocalDateTime timestamp = configuration.getTimestamp();
        htmlContent = htmlContent.replace(TIMESTAMP_PLACEHOLDER,
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        try (FileWriter htmlWriter = new FileWriter(htmlOutputPath.toFile())) {
            htmlWriter.write(htmlContent);
        }
    }

    /**
     * Injects test sequence data into the JS constants file.
     *
     * @param testSequences List of test sequences to report
     * @throws IOException If an I/O error occurs
     * @throws IllegalArgumentException If testSequences is null
     */
    public void injectTestSequenceData(List<TestSequence> testSequences) throws IOException {
        Objects.requireNonNull(testSequences, "Test sequences cannot be null");

        Path jsConstantsPath = getJSConstantsOutputFile();
        String content = new String(Files.readAllBytes(jsConstantsPath));

        // Find placeholder position
        int placeholderPos = content.indexOf(TEST_SEQUENCE_JSON_DATA_PLACEHOLDER);
        if (placeholderPos == -1) {
            throw new IOException("Could not find test sequence placeholder in JS constants file");
        }

        // Use StringBuilder for efficient string manipulation
        StringBuilder newContent = new StringBuilder(content.substring(0, placeholderPos));

        // Process each test sequence
        for (TestSequence testSequence : testSequences) {
            processTestSequence(testSequence, newContent);
        }

        // Append the rest of the file
        newContent.append(content.substring(placeholderPos + TEST_SEQUENCE_JSON_DATA_PLACEHOLDER.length()));

        // Write to file
        try (FileWriter writer = new FileWriter(jsConstantsPath.toFile())) {
            writer.write(newContent.toString());
        }
    }

    /**
     * Processes a single test sequence and adds its JSON representation to the StringBuilder.
     *
     * @param testSequence The test sequence to process
     * @param contentBuilder The StringBuilder to append to
     */
    private void processTestSequence(TestSequence testSequence, StringBuilder contentBuilder) {
        List<TestInteraction> interactions = testSequence.getTestInteractions();
        if (interactions == null || interactions.isEmpty()) {
            return;
        }

        for (int i = 0; i < interactions.size(); i++) {
            TestInteraction interaction = interactions.get(i);
            JsonObject data = createInteractionJsonData(testSequence, interaction, i);
            contentBuilder.append(gson.toJson(data)).append(",\n");
        }
    }

    /**
     * Creates a JSON object for a test interaction.
     *
     * @param testSequence The parent test sequence
     * @param interaction The test interaction
     * @param interactionIndex The index of the interaction in the sequence
     * @return A JsonObject containing the interaction data
     */
    private JsonObject createInteractionJsonData(TestSequence testSequence, TestInteraction interaction, int interactionIndex) {
        JsonObject data = new JsonObject();

        // Add basic sequence information
        data.addProperty("generator", testSequence.getGenerator());
        data.addProperty("generatedAt", testSequence.getGeneratedAt().toString());
        data.add("testResults", gson.toJsonTree(testSequence.getTestResults()));
        data.add("tags", gson.toJsonTree(testSequence.getTags()));
        data.addProperty("belongsToInteraction", testSequence.getName());
        data.addProperty("interactionIndex", interactionIndex);

        // Get operation
        Operation operation = interaction.getFuzzedOperation();
        if (operation == null) {
            operation = interaction.getReferenceOperation();
        }

        // Add request/response information
        populateRequestResponseData(data, interaction, operation);

        // Add parameters information
        if (operation != null) {
            data.add("requestParameters", createParametersJsonArray(operation));
        }

        return data;
    }

    /**
     * Populates request and response data in the JSON object.
     *
     * @param data The JSON object to populate
     * @param interaction The test interaction containing request/response data
     * @param operation The operation associated with the interaction
     */
    private void populateRequestResponseData(JsonObject data, TestInteraction interaction, Operation operation) {
        // Request URL
        data.addProperty("requestURL", interaction.getRequestURL());
        data.addProperty("requestURLIsDocumented",
                operation != null && coverageCollection.isPathDocumented(operation.getEndpoint()));
        data.addProperty("requestURLIsTested",
                operation != null && coverageCollection.isPathTested(operation.getEndpoint()));

        // Request method
        data.addProperty("requestMethod", interaction.getRequestMethod().toString());
        data.addProperty("requestMethodIsDocumented",
                operation != null && coverageCollection.isOperationDocumented(operation));
        data.addProperty("requestMethodIsTested",
                operation != null && coverageCollection.isOperationTested(operation));

        // Response status code
        data.addProperty("responseStatusCode", interaction.getResponseStatusCode().toString());
        data.addProperty("responseStatusCodeIsDocumented",
                operation != null && coverageCollection.isStatusCodeDocumented(operation, interaction.getResponseStatusCode()));
        data.addProperty("responseStatusCodeIsTested",
                operation != null && coverageCollection.isStatusCodeTested(operation, interaction.getResponseStatusCode()));
    }

    /**
     * Creates a JSON array of parameters for an operation.
     *
     * @param operation The operation containing parameters
     * @return A JsonArray containing parameter data
     */
    private JsonArray createParametersJsonArray(Operation operation) {
        JsonArray parameters = new JsonArray();

        for (Parameter parameter : operation.getAllRequestParameters()) {
            if (!(parameter instanceof LeafParameter)) {
                continue;
            }

            LeafParameter leafParam = (LeafParameter) parameter;
            if (leafParam.getType() != ParameterType.BOOLEAN && !leafParam.isEnum()) {
                continue;
            }

            JsonObject parameterData = createParameterJsonObject(operation, leafParam);
            parameters.add(parameterData);
        }

        return parameters;
    }

    /**
     * Creates a JSON object for a single parameter.
     *
     * @param operation The operation containing the parameter
     * @param parameter The parameter to create JSON for
     * @return A JsonObject containing parameter data
     */
    private JsonObject createParameterJsonObject(Operation operation, LeafParameter parameter) {
        ParameterElementWrapper parameterWrapper = new ParameterElementWrapper(parameter);
        Object concreteValue = parameter.getConcreteValue();

        // Parameter data
        JsonObject parameterData = new JsonObject();
        parameterData.addProperty("name", parameter.getName().toString());
        parameterData.addProperty("isDocumented",
                coverageCollection.isParameterDocumented(operation, parameterWrapper));
        parameterData.addProperty("isTested",
                coverageCollection.isParameterTested(operation, parameterWrapper));

        // Parameter value data
        JsonObject valueData = new JsonObject();
        valueData.addProperty("value", concreteValue != null ? concreteValue.toString() : "null");
        valueData.addProperty("isDocumented",
                coverageCollection.isParameterValueDocumented(operation, parameterWrapper, concreteValue));
        valueData.addProperty("isTested",
                coverageCollection.isParameterValueTested(operation, parameterWrapper, concreteValue));

        parameterData.add("parameterValue", valueData);
        return parameterData;
    }
}