package io.resttestgen.implementation.strategy;

import io.resttestgen.core.Environment;
import io.resttestgen.core.openapi.Operation;
import io.resttestgen.core.testing.Strategy;
import io.resttestgen.core.testing.TestRunner;
import io.resttestgen.core.testing.TestSequence;
import io.resttestgen.core.testing.operationsorter.OperationsSorter;
import io.resttestgen.implementation.fuzzer.ErrorFuzzer;
import io.resttestgen.implementation.fuzzer.NominalFuzzer;
import io.resttestgen.implementation.operationssorter.GraphBasedOperationsSorter;
import io.resttestgen.implementation.oracle.StatusCodeOracle;
import io.resttestgen.implementation.writer.CoverageReportWriter;
import io.resttestgen.implementation.writer.HTMLReportWriter;
import io.resttestgen.implementation.writer.ReportWriter;
import io.resttestgen.implementation.writer.RestAssuredWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class NominalAndErrorStrategy extends Strategy {

    private static final Logger logger = LogManager.getLogger(NominalAndErrorStrategy.class);

    private static final int NOMINAL_TEST_COUNT = 20;
    private static final int ERROR_TEST_COUNT = 10;

    private final TestSequence globalNominalTestSequence = new TestSequence();

    public void start() {
        HTMLReportWriter htmlReportWriter = initializeHtmlReportWriter();
        List<TestSequence> testSequencesToReport = new ArrayList<>();

        executeNominalFuzzer(testSequencesToReport);
        globalNominalTestSequence.filterBySuccessfulStatusCode();

        executeErrorFuzzer(testSequencesToReport);
        writeCoverageReports(htmlReportWriter);
        injectTestSequenceData(htmlReportWriter, testSequencesToReport);
    }

    private HTMLReportWriter initializeHtmlReportWriter() {
        HTMLReportWriter htmlReportWriter = new HTMLReportWriter(Environment.getInstance().getConfiguration());
        try {
            htmlReportWriter.createReportResourcesFiles();
        } catch (IOException e) {
            logger.warn("Could not copy folders from template-resources", e);
        }
        return htmlReportWriter;
    }

    private void executeNominalFuzzer(List<TestSequence> testSequencesToReport) {
        OperationsSorter sorter = new GraphBasedOperationsSorter();
        while (!sorter.isEmpty()) {
            Operation operationToTest = sorter.getFirst();
            logger.debug("Testing operation " + operationToTest);
            NominalFuzzer nominalFuzzer = new NominalFuzzer(operationToTest);
            List<TestSequence> nominalSequences = nominalFuzzer.generateTestSequences(NOMINAL_TEST_COUNT);

            for (TestSequence testSequence : nominalSequences) {
                runAndEvaluateTestSequence(testSequence);
                writeTestSequenceReports(testSequence);
                testSequencesToReport.add(testSequence);
            }
            globalNominalTestSequence.append(nominalSequences);
            sorter.removeFirst();
        }
    }

    private void runAndEvaluateTestSequence(TestSequence testSequence) {
        TestRunner testRunner = TestRunner.getInstance();
        testRunner.run(testSequence);

        StatusCodeOracle statusCodeOracle = new StatusCodeOracle();
        statusCodeOracle.assertTestSequence(testSequence);
    }

    private void writeTestSequenceReports(TestSequence testSequence) {
        try {
            ReportWriter reportWriter = new ReportWriter(testSequence);
            reportWriter.write();
            RestAssuredWriter restAssuredWriter = new RestAssuredWriter(testSequence);
            restAssuredWriter.write();
        } catch (IOException e) {
            logger.warn("Could not write test sequence report to file.", e);
        }
    }

    private void executeErrorFuzzer(List<TestSequence> testSequencesToReport) {
        ErrorFuzzer errorFuzzer = new ErrorFuzzer(globalNominalTestSequence);
        List<TestSequence> errorFuzzerTestSequences = errorFuzzer.generateTestSequences(ERROR_TEST_COUNT);
        testSequencesToReport.addAll(errorFuzzerTestSequences);
    }

    private void writeCoverageReports(HTMLReportWriter htmlReportWriter) {
        htmlReportWriter.populateCoverageCollection(TestRunner.getInstance().getCoverage());
        try {
            CoverageReportWriter coverageReportWriter = new CoverageReportWriter(TestRunner.getInstance().getCoverage());
            coverageReportWriter.write();
        } catch (IOException e) {
            logger.warn("Could not write test coverage report to file.", e);
        }
    }

    private void injectTestSequenceData(HTMLReportWriter htmlReportWriter, List<TestSequence> testSequencesToReport) {
        logger.info("Injecting test sequences into JS constants file");
        try {
            htmlReportWriter.injectTestSequenceData(testSequencesToReport);
        } catch (IOException e) {
            logger.warn("Could not write test sequence data to JavaScript constants file.", e);
        }
        logger.info("DONE Injecting test sequences into JS constants file");
    }
}
