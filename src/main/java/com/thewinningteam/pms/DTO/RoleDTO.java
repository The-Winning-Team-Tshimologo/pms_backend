
package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.ERole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO {
    private Long roleId;
    @Enumerated(EnumType.STRING)
    private ERole name;
}