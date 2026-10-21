package org.example.insurance_policy_system.entity.claimmanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.insurance_policy_system.enums.claimmanagement.DocumentVerificationStatus;

@Entity
@Table(name = "claim_documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private  Long documentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", referencedColumnName = "claim_id")
    private ClaimMaster claim;

    @Column(name = "document_name", length = 150)
    private  String documentName;


    @Column(name = "file_path", length = 500)
    private  String  filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
private DocumentVerificationStatus verificationStatus;



}
