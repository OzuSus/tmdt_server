package com.TTLTTBDD.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private String fullname;
    private String address;
    private String phone;
    private String email;
    private Boolean role = false;
    private String avatar;
    private Boolean status = false;

}
