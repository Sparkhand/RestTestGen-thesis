package io.resttestgen.core.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.resttestgen.core.datatype.HttpStatusCode;
import io.resttestgen.core.openapi.Operation;
import io.resttestgen.core.testing.coverage.ParameterElementWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EndpointsCollection {
    private Map<String, PathRepresentation> paths = new HashMap<>();

    public PathRepresentation getOrCreatePath(String path) {
        return paths.computeIfAbsent(path, k -> new PathRepresentation());
    }

    public void setPathDocumented(String path, boolean isDocumented) {
        getOrCreatePath(path).isPathDocumented = isDocumented;
    }

    public void setPathTested(String path, boolean isTested) {
        getOrCreatePath(path).isPathTested = isTested;
    }

    public void setOperationDocumented(String path, Operation operation, boolean isDocumented) {
        getOrCreatePath(path).getOrCreateOperation(operation).isOperationDocumented = isDocumented;
    }

    public void setOperationTested(String path, Operation operation, boolean isTested) {
        getOrCreatePath(path).getOrCreateOperation(operation).isOperationTested = isTested;
    }

    public void setStatusCodeDocumented(String path, Operation operation, HttpStatusCode statusCode, boolean isDocumented) {
        getOrCreatePath(path).getOrCreateOperation(operation).getOrCreateStatusCode(statusCode).isStatusCodeDocumented = isDocumented;
    }

    public void setStatusCodeTested(String path, Operation operation, HttpStatusCode statusCode, boolean isTested) {
        getOrCreatePath(path).getOrCreateOperation(operation).getOrCreateStatusCode(statusCode).isStatusCodeTested = isTested;
    }

    public void setParameterDocumented(String path, Operation operation, HttpStatusCode statusCode, ParameterElementWrapper parameter, boolean isDocumented) {
        getOrCreatePath(path).getOrCreateOperation(operation).getOrCreateStatusCode(statusCode).getOrCreateParameter(parameter).isParameterDocumented = isDocumented;
    }

    public void setParameterTested(String path, Operation operation, HttpStatusCode statusCode, ParameterElementWrapper parameter, boolean isTested) {
        getOrCreatePath(path).getOrCreateOperation(operation).getOrCreateStatusCode(statusCode).getOrCreateParameter(parameter).isParameterTested = isTested;
    }

    public void setParameterValueDocumented(String path, Operation operation, HttpStatusCode statusCode, ParameterElementWrapper parameter, Object value, boolean isDocumented) {
        getOrCreatePath(path).getOrCreateOperation(operation).getOrCreateStatusCode(statusCode).getOrCreateParameter(parameter).getOrCreateParameterValue(value).isValueDocumented = isDocumented;
    }

    public void setParameterValueTested(String path, Operation operation, HttpStatusCode statusCode, ParameterElementWrapper parameter, Object value, boolean isTested) {
        getOrCreatePath(path).getOrCreateOperation(operation).getOrCreateStatusCode(statusCode).getOrCreateParameter(parameter).getOrCreateParameterValue(value).isValueTested = isTested;
    }

    class PathRepresentation {
        private boolean isPathDocumented;
        private boolean isPathTested;
        private Set<OperationRepresentation> operations = new HashSet<>();

        private OperationRepresentation getOrCreateOperation(Operation operation) {
            return operations.stream()
                    .filter(opRep -> opRep.operation.equals(operation))
                    .findFirst()
                    .orElseGet(() -> {
                        OperationRepresentation newOpRep = new OperationRepresentation();
                        newOpRep.operation = operation;
                        operations.add(newOpRep);
                        return newOpRep;
                    });
        }

        class OperationRepresentation {
            private Operation operation;
            private boolean isOperationDocumented;
            private boolean isOperationTested;
            private Set<StatusCodeRepresentation> statusCodes = new HashSet<>();

            private StatusCodeRepresentation getOrCreateStatusCode(HttpStatusCode statusCode) {
                return statusCodes.stream()
                        .filter(statusCodeRep -> statusCodeRep.statusCode.equals(statusCode))
                        .findFirst()
                        .orElseGet(() -> {
                            StatusCodeRepresentation newStatusCodeRep = new StatusCodeRepresentation();
                            newStatusCodeRep.statusCode = statusCode;
                            statusCodes.add(newStatusCodeRep);
                            return newStatusCodeRep;
                        });
            }

            class StatusCodeRepresentation {
                private HttpStatusCode statusCode;
                private boolean isStatusCodeDocumented;
                private boolean isStatusCodeTested;
                private Set<ParameterRepresentation> parameters = new HashSet<>();

                private ParameterRepresentation getOrCreateParameter(ParameterElementWrapper parameter) {
                    return parameters.stream()
                            .filter(paramRep -> paramRep.parameter.equals(parameter))
                            .findFirst()
                            .orElseGet(() -> {
                                ParameterRepresentation newParamRep = new ParameterRepresentation();
                                newParamRep.parameter = parameter;
                                parameters.add(newParamRep);
                                return newParamRep;
                            });
                }

                class ParameterRepresentation {
                    private ParameterElementWrapper parameter;
                    private boolean isParameterDocumented;
                    private boolean isParameterTested;
                    private Set<ParameterValueRepresentation> parameterValues = new HashSet<>();

                    private ParameterValueRepresentation getOrCreateParameterValue(Object value) {
                        return parameterValues.stream()
                                .filter(paramValueRep -> paramValueRep.value.equals(value))
                                .findFirst()
                                .orElseGet(() -> {
                                    ParameterValueRepresentation newParamValueRep = new ParameterValueRepresentation();
                                    newParamValueRep.value = value;
                                    parameterValues.add(newParamValueRep);
                                    return newParamValueRep;
                                });
                    }

                    class ParameterValueRepresentation {
                        private Object value;
                        private boolean isValueDocumented;
                        private boolean isValueTested;
                    }
                }
            }
        }
    }

    public JsonObject generateJsonRepresentation() {
        JsonObject jsonRoot = new JsonObject();

        for (Map.Entry<String, PathRepresentation> entry : paths.entrySet()) {
            String path = entry.getKey();
            PathRepresentation pathRep = entry.getValue();
            JsonObject pathJson = new JsonObject();
            pathJson.addProperty("documented", pathRep.isPathDocumented);
            pathJson.addProperty("tested", pathRep.isPathTested);

            JsonArray operationsArray = new JsonArray();

            for (PathRepresentation.OperationRepresentation operationRep : pathRep.operations) {
                JsonObject operationJson = new JsonObject();
                operationJson.addProperty("opId", operationRep.operation.getOperationId());
                operationJson.addProperty("method", operationRep.operation.getMethod().toString());
                operationJson.addProperty("documented", operationRep.isOperationDocumented);
                operationJson.addProperty("tested", operationRep.isOperationTested);

                JsonArray statusCodesArray = new JsonArray();
                for (PathRepresentation.OperationRepresentation.StatusCodeRepresentation statusCodeRep : operationRep.statusCodes) {
                    JsonObject statusCodeJson = new JsonObject();
                    statusCodeJson.addProperty("code", statusCodeRep.statusCode.getCode());
                    statusCodeJson.addProperty("documented", statusCodeRep.isStatusCodeDocumented);
                    statusCodeJson.addProperty("tested", statusCodeRep.isStatusCodeTested);

                    JsonArray parametersArray = new JsonArray();
                    for (PathRepresentation.OperationRepresentation.StatusCodeRepresentation.ParameterRepresentation parameterRep : statusCodeRep.parameters) {
                        JsonObject parameterJson = new JsonObject();
                        parameterJson.addProperty("name", parameterRep.parameter.getParameter().getName().toString());
                        parameterJson.addProperty("documented", parameterRep.isParameterDocumented);
                        parameterJson.addProperty("tested", parameterRep.isParameterTested);

                        JsonArray valuesArray = new JsonArray();
                        for (PathRepresentation.OperationRepresentation.StatusCodeRepresentation.ParameterRepresentation.ParameterValueRepresentation valueRep : parameterRep.parameterValues) {
                            JsonObject valueJson = new JsonObject();
                            valueJson.addProperty("value", valueRep.value.toString());
                            valueJson.addProperty("documented", valueRep.isValueDocumented);
                            valueJson.addProperty("tested", valueRep.isValueTested);
                            valuesArray.add(valueJson);
                        }
                        parameterJson.add("values", valuesArray);
                        parametersArray.add(parameterJson);
                    }
                    statusCodeJson.add("parameters", parametersArray);
                    statusCodesArray.add(statusCodeJson);
                }
                operationJson.add("statusCodes", statusCodesArray);
                operationsArray.add(operationJson);
            }

            pathJson.add("operations", operationsArray);
            jsonRoot.add(path, pathJson);
        }

        return jsonRoot;
    }
}