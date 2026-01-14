package com.example.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenClaimResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String employeeId;
    private List<String> userTerminalIP;
    private String orgId;
    private String officeId;
    private String screenLockTime;
    private String fullName;
    private String clickStreamTrack;
    private List<String> realmRoles;
}
