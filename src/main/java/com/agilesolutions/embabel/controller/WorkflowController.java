package com.agilesolutions.embabel.controller;

import com.agilesolutions.embabel.model.DraftPost;
import com.agilesolutions.embabel.model.HumanReview;
import com.agilesolutions.embabel.model.PortfolioRefreshRequest;
import com.agilesolutions.embabel.service.PortfolioAgentService;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.core.AgentProcess;
import com.embabel.agent.core.AgentProcessRepository;
import com.embabel.agent.core.hitl.Awaitable;
import com.embabel.agent.core.hitl.FormBindingRequest;
import com.embabel.agent.core.hitl.FormResponse;
import com.embabel.ux.form.FormSubmission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workflows")
@Tag(name = "Portfolio Assets reporting",
        description = "Provides endpoints for generating performance reports for portfolio assets.")
public class WorkflowController {

    private final AgentPlatform agentPlatform;
    private final PortfolioAgentService portfolioAgentService;
    private final AgentProcessRepository agentProcessRepository;

    /**
     * Generates a performance report for a given portfolio name by invoking the agentic logic through the TradingAgent.
     *
     * @param request Portfolio request containing the portfolio name
     * @return ResponseEntity with entity information or appropriate error status
     */
    @PostMapping("/refresh")
    @Operation(
            summary     = "Refresh portfolio report",
            description = "Refreshes the portfolio report for a given portfolio name and returns the performance markdown report."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully refreshed portfolio report"),
            @ApiResponse(responseCode = "404", description = "Portfolio not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public DraftPost refreshPortfolio(
            @RequestBody PortfolioRefreshRequest request) {

        return portfolioAgentService.generateReport(request);
    }

    /**
     * Return the current state of an AgentProcess.
     *
     * GET /api/agent/processes/{processId}
     */
    @Operation(
            summary     = "Get the current state of an AgentProcess",
            description = "Retrieves the current state of an AgentProcess by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved process state"),
            @ApiResponse(responseCode = "404", description = "Process not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{processId}")
    public ResponseEntity<ProcessStatusResponse> getProcess(
            @PathVariable String processId) {

        AgentProcess process =
                agentProcessRepository.findById(processId);

        if (process == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                createProcessStatusResponse(process)
        );
    }

    /**
     * Confirms and feeds form data back to a suspended agent pipeline
     */
    @Operation(
            summary     = "Submit form confirmation for a suspended agent process",
            description = "Submits form confirmation for a suspended agent process and updates the pipeline."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully submitted form confirmation"),
            @ApiResponse(responseCode = "404", description = "Process not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/processes/{processId}/submit-form")
    public ResponseEntity<?> submitFormConfirmation(
            @PathVariable String processId,
            @RequestParam String awaitableId,
            @RequestBody FormSubmissionRequest request) {

        /*
         * ------------------------------------------------------------
         * 1. Retrieve the EXISTING AgentProcess
         * ------------------------------------------------------------
         *
         * We do NOT create a new AgentProcess here.
         */
        AgentProcess process =
                agentProcessRepository.findById(processId);

        if (process == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            "PROCESS_NOT_FOUND",
                            "Agent process '" + processId + "' was not found"
                    ));
        }

        /*
         * ------------------------------------------------------------
         * 2. Find the FormBindingRequest stored on the Blackboard
         * ------------------------------------------------------------
         *
         * WaitFor.formSubmission(...) creates a FormBindingRequest.
         *
         * Embabel puts the Awaitable on the process Blackboard while
         * the process is waiting.
         */
        FormBindingRequest<?> formRequest =
                findFormRequest(process, awaitableId);

        if (formRequest == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            "AWAITABLE_NOT_FOUND",
                            "Form awaitable '" + awaitableId +
                                    "' was not found on process '" +
                                    processId + "'"
                    ));
        }

        /*
         * ------------------------------------------------------------
         * 3. Validate that the submitted form belongs to the
         *    FormBindingRequest we found.
         * ------------------------------------------------------------
         */
        String expectedFormId =
                formRequest.getPayload().getId();

        if (!expectedFormId.equals(request.formId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(
                            "FORM_ID_MISMATCH",
                            "Expected formId '" + expectedFormId +
                                    "' but received '" +
                                    request.formId() + "'"
                    ));
        }

        /*
         * ------------------------------------------------------------
         * 4. Create Embabel FormSubmission
         * ------------------------------------------------------------
         *
         * FormSubmission contains the raw values submitted by the
         * frontend.
         */
        FormSubmission formSubmission =
                new FormSubmission(
                        request.formId(),
                        request.values(),
                        UUID.randomUUID().toString(),
                        Instant.now()
                );

        /*
         * ------------------------------------------------------------
         * 5. Create Embabel FormResponse
         * ------------------------------------------------------------
         *
         * awaitableId MUST be the ID of the existing
         * FormBindingRequest.
         */
        FormResponse formResponse =
                new FormResponse(
                        UUID.randomUUID().toString(),
                        awaitableId,
                        formSubmission,
                        false,
                        Instant.now()
                );

        /*
         * ------------------------------------------------------------
         * 6. Feed the response into the suspended process
         * ------------------------------------------------------------
         *
         * THIS is the important Embabel operation.
         *
         * FormBindingRequest.onResponse(...) binds the submitted
         * form to the output class specified when WaitFor.formSubmission(...) was created..
         */
        formRequest.onResponse(
                formResponse,
                process
        );

        /*
         * ------------------------------------------------------------
         * 7. Resume THE SAME AgentProcess
         * ------------------------------------------------------------
         *
         * We don't start the agent again.
         *
         * The existing blackboard and execution state are retained.
         */
        AgentProcess resumedProcess =
                process.run();

        /*
         * ------------------------------------------------------------
         * 8. Persist the updated process
         * ------------------------------------------------------------
         */
        agentProcessRepository.update(resumedProcess);

        /*
         * ------------------------------------------------------------
         * 9. Return the new state
         * ------------------------------------------------------------
         */
        return ResponseEntity.ok(
                createProcessStatusResponse(resumedProcess)
        );

    }

    /**
     * Find a specific FormBindingRequest on the process Blackboard.
     */
    private FormBindingRequest<?> findFormRequest(
            AgentProcess process,
            String awaitableId) {

        List<FormBindingRequest> requests =
                process.objectsOfType(FormBindingRequest.class);

        return requests.stream()
                .filter(request ->
                        request.getId().equals(awaitableId))
                .findFirst()
                .orElse(null);
    }


    /**
     * Convert AgentProcess state to an API response.
     */
    private ProcessStatusResponse createProcessStatusResponse(
            AgentProcess process) {

        FormBindingRequest<?> pendingForm =
                findPendingForm(process);

        return new ProcessStatusResponse(
                process.getId(),
                process.getStatus().name(),
                process.getFinished(),
                pendingForm == null
                        ? null
                        : createFormResponse(pendingForm)
        );
    }

    /**
     * Find the current pending form.
     *
     * If your agent can only have one WaitFor.formSubmission()
     * at a time, this is sufficient.
     */
    private FormBindingRequest<?> findPendingForm(
            AgentProcess process) {

        List<FormBindingRequest> requests =
                process.objectsOfType(FormBindingRequest.class);

        return requests.stream()
                .filter(request ->
                        !request.getValidationErrors().isEmpty()
                                || request.getId() != null)
                .reduce((first, second) -> second)
                .orElse(null);
    }


    /**
     * Convert Embabel's FormBindingRequest into a frontend-friendly
     * representation.
     */
    private PendingFormResponse createFormResponse(
            FormBindingRequest<?> request) {

        return new PendingFormResponse(
                request.getId(),
                request.getPayload().getId(),
                request.getOutputClass().getName(),
                request.getBindingName(),
                request.getPayload().toString()
        );
    }

    /*
     * ================================================================
     * DTOs
     * ================================================================
     */

    public record FormSubmissionRequest(
            String formId,
            Map<String, Object> values
    ) {
    }


    public record ProcessStatusResponse(
            String processId,
            String status,
            boolean finished,
            PendingFormResponse pendingForm
    ) {
    }


    public record PendingFormResponse(
            String awaitableId,
            String formId,
            String outputClass,
            String bindingName,
            String form
    ) {
    }


    public record ErrorResponse(
            String code,
            String message
    ) {
    }


}