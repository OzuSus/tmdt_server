package com.TTLTTBDD.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String address;
    private String phone;
    private String avatar;
    private String email;
    private Integer roleId = 1;
}
