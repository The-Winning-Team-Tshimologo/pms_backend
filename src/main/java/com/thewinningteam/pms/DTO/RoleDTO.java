
package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleDTO {
    private Long roleId;
    private ERole name;
}