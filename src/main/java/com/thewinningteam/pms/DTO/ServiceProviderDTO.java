package com.thewinningteam.pms.DTO;

import com.thewinningteam.pms.model.AcceptanceStatus;
import com.thewinningteam.pms.model.Profile;
import com.thewinningteam.pms.model.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceProviderDTO {
    private Long userId;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
    private int rating;
    private RoleDTO role;
    private AddressDTO address;
    private byte[]  profilePicture;

    private byte[] identityDocument;
    private byte[] bankStatement;
    private byte[] qualifications;
    private byte[] CriminalRecord;
    private byte[] resume;

    @Enumerated(EnumType.STRING)
    private AcceptanceStatus acceptanceStatus;
    private ProfileDTO profile;
    private List<ReviewDTO> reviews;
    private String bankName ;
    private Long accountNumber;
    private String typeOfAccount;
    private Long branchCode;
}
