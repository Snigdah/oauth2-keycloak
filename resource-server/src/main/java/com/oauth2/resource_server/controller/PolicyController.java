package com.oauth2.resource_server.controller;

import com.oauth2.resource_server.dto.EditPolicyDTO;
import com.oauth2.resource_server.dto.PolicyDTO;
import com.oauth2.resource_server.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {
    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<String> createPolicy(@RequestBody PolicyDTO dto) {
        String message = policyService.createPolicy(dto);
        return ResponseEntity.ok(message);
    }

    @GetMapping
    public ResponseEntity<List<PolicyRepresentation>> listPolicies() {
        List<PolicyRepresentation> policies = policyService.listPolicies();
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/{name}")
    public ResponseEntity<PolicyRepresentation> getPolicyByName(@PathVariable("name") String name) {
        PolicyRepresentation policy = policyService.getPolicyByName(name);
        return policy != null ? ResponseEntity.ok(policy) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<String> updatePolicy(@PathVariable("name") String name,
                                               @Valid @RequestBody EditPolicyDTO dto) {
        String message = policyService.updatePolicy(name, dto);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deletePolicy(@PathVariable("name") String name) {
        String message = policyService.deletePolicy(name);
        return ResponseEntity.ok(message);
    }
}
