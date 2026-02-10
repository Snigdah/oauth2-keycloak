package com.oauth2.resource_server.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserContext {
    private String userId;
    private String username;
    private String email;
    private String employeeId;
    private String orgId;
    private String officeId;
    private List<String> userTerminalIp;
}
