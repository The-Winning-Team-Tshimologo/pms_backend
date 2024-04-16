package com.thewinningteam.pms.response;

import com.thewinningteam.pms.model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private Long id;
    private String email;
    private String roles;
    private String token;



}
