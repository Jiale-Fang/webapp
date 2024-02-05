package pers.fjl.healthcheck.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime accountCreated;
    private LocalDateTime accountUpdated;
}
