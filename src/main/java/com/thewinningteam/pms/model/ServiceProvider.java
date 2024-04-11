package com.thewinningteam.pms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProvider extends User {
    @Lob
    @Column(name = "identity_document", columnDefinition = "LONGBLOB")
    private byte[] identityDocument;

    @Lob
    @Column(name = "bank_statement", columnDefinition = "LONGBLOB")
    private byte[] bankStatement;

    @Lob
    @Column(name = "qualifications", columnDefinition = "LONGBLOB")
    private byte[] qualifications;

    @Lob
    @Column(name = "criminal_record", columnDefinition = "LONGBLOB")
    private byte[] CriminalRecord;

    @Lob
    @Column(name = "resume", columnDefinition = "LONGBLOB")
    private byte[] resume;

    @Enumerated(EnumType.STRING)
    @Column(name = "acceptance_status")
    private AcceptanceStatus acceptanceStatus = AcceptanceStatus.PENDING;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<Review> reviews;

    private String bankName ;

    private Long accountNumber;

    private String typeOfAccount;

    private Long branchCode;

}


enum AcceptanceStatus {
    PENDING,
    REJECTED,
    ACCEPTED
}
