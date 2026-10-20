
package org.example.insurance_policy_system.serviceimpl.kyc;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

import org.example.insurance_policy_system.dto.kyc.*;
import org.example.insurance_policy_system.entity.customer.Customer;
import org.example.insurance_policy_system.entity.kyc.*;
import org.example.insurance_policy_system.repository.customer.CustomerRepository;
import org.example.insurance_policy_system.repository.kyc.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class KycService {

    private final KycRequestRepository requests;
    private final KycDocumentRepository documents;
    private final KycAuditLogRepository logs;
    private final CustomerRepository customers;

    @Value("${kyc.upload-dir:uploads/kyc}")
    private String uploadDir;

    @Value("${kyc.max-file-size-bytes:5242880}")
    private long maxFileSize;

    private static final Set<String> TYPES = Set.of(
            "AADHAAR",
            "PAN",
            "PASSPORT",
            "DRIVING LICENSE",
            "VOTER ID",
            "ADDRESS PROOF",
            "INCOME PROOF",
            "PASSPORT SIZE PHOTO",
            "SIGNATURE",
            "ELECTRICITY BILL",
            "WATER BILL",
            "BANK STATEMENT",
            "GAS CONNECTION BILL",
            "GOVERNMENT IDENTITY CARD"
    );

    private static final Pattern PAN =
            Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");

    public KycService(
            KycRequestRepository r,
            KycDocumentRepository d,
            KycAuditLogRepository l,
            CustomerRepository c) {

        requests = r;
        documents = d;
        logs = l;
        customers = c;
    }

    private RuntimeException notFound(String s) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, s);
    }

    private KycRequest get(Long id) {
        return requests.findById(id)
                .orElseThrow(() -> notFound("KYC request not found: " + id));
    }

    private void audit(
            KycRequest k,
            String action,
            Long actor,
            String remarks) {

        KycAuditLog l = new KycAuditLog();
        l.setKyc(k);
        l.setAction(action);
        l.setPerformedBy(actor);
        l.setRemarks(remarks);

        logs.save(l);
    }

    public List<KycRequest> all() {
        return requests.findAll();
    }

    public KycRequest byId(Long id) {
        return get(id);
    }

    public KycRequest create(KycCreateRequest in) {

        Customer c = customers.findById(in.customerId())
                .orElseThrow(() ->
                        notFound("Customer not found: " + in.customerId()));

        Optional<KycRequest> active =
                requests.findFirstByCustomer_CustomerIdOrderByIdDesc(
                        c.getCustomerId());

        if (active.isPresent()
                && !"Rejected".equals(active.get().getStatus())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An active KYC request already exists for this customer");
        }

        KycRequest k = new KycRequest();

        k.setCustomer(c);
        k.setKycNumber(
                "KYC" + System.currentTimeMillis()
                        + UUID.randomUUID().toString()
                        .substring(0, 4).toUpperCase());

        k.setKycDate(
                in.kycDate() == null ? LocalDate.now() : in.kycDate());

        k.setRemarks(in.remarks());

        k = requests.save(k);

        audit(k, "KYC_CREATED", null, in.remarks());

        return k;
    }

    public KycRequest update(Long id, KycCreateRequest in) {

        KycRequest k = get(id);

        if ("Verified".equals(k.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Verified KYC cannot be edited");
        }

        if (in.remarks() != null) {
            k.setRemarks(in.remarks());
        }

        if (in.kycDate() != null) {
            k.setKycDate(in.kycDate());
        }

        audit(k, "KYC_UPDATED", null, in.remarks());

        return requests.save(k);
    }

    public void delete(Long id) {
        KycRequest k = get(id);
        requests.delete(k);
    }

    public KycRequest updateIdentity(
            Long id,
            KycIdentityRequest in) {

        KycRequest k = get(id);

        if ("Verified".equals(k.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Verified KYC cannot be edited");
        }

        if (in.aadhaarNumber() != null
                && !in.aadhaarNumber().isBlank()) {

            String n = in.aadhaarNumber().trim();

            if (requests.findAll().stream()
                    .anyMatch(x -> !x.getId().equals(id)
                            && n.equals(x.getAadhaarNumber()))) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Aadhaar number already exists");
            }

            k.setAadhaarNumber(n);
        }

        if (in.aadhaarName() != null) {
            k.setAadhaarName(in.aadhaarName());
        }

        if (in.aadhaarDateOfBirth() != null) {
            k.setAadhaarDateOfBirth(in.aadhaarDateOfBirth());
        }

        if (in.aadhaarAddress() != null) {
            k.setAadhaarAddress(in.aadhaarAddress());
        }

        if (in.panNumber() != null && !in.panNumber().isBlank()) {

            String n = in.panNumber().trim().toUpperCase(Locale.ROOT);

            if (requests.findAll().stream()
                    .anyMatch(x -> !x.getId().equals(id)
                            && n.equalsIgnoreCase(x.getPanNumber()))) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "PAN number already exists");
            }

            k.setPanNumber(n);
            k.setPanStatus("Pending");
        }

        if (in.panHolderName() != null) {
            k.setPanHolderName(in.panHolderName());
        }

        k = requests.save(k);

        audit(k, "IDENTITY_DETAILS_UPDATED", null,
                "Identity fields updated");

        return k;
    }

    public KycDocument upload(
            Long kycId,
            String type,
            String number,
            MultipartFile file) throws IOException {

        KycRequest k = get(kycId);

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A document file is required");
        }

        if (file.getSize() > maxFileSize) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "File exceeds configured maximum size");
        }

        String t = type.trim().toUpperCase(Locale.ROOT);

        if (!TYPES.contains(t)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported document type");
        }

        String ext = "";

        String original = Objects.requireNonNullElse(
                file.getOriginalFilename(), "document");

        int dot = original.lastIndexOf('.');

        if (dot >= 0) {
            ext = original.substring(dot).toLowerCase(Locale.ROOT);
        }

        if (!Set.of(".pdf", ".jpg", ".jpeg", ".png").contains(ext)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only PDF, JPG, JPEG and PNG files are allowed");
        }

        String normalized = number.trim();

        if (normalized.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Document number is required");
        }

        if ("AADHAAR".equals(t)
                && !normalized.matches("[0-9]{12}")) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Aadhaar must contain exactly 12 digits");
        }

        if ("PAN".equals(t)
                && !PAN.matcher(
                normalized.toUpperCase(Locale.ROOT)).matches()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "PAN format is invalid");
        }

        if (documents.existsByDocumentNumber(normalized)
                || ("AADHAAR".equals(t)
                && requests.existsByAadhaarNumber(normalized))
                || ("PAN".equals(t)
                && requests.existsByPanNumberIgnoreCase(normalized))) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Document number already exists");
        }

        Path base = Paths.get(uploadDir).toAbsolutePath().normalize();

        Files.createDirectories(base);

        String safeName = UUID.randomUUID() + ext;
        Path dest = base.resolve(safeName).normalize();

        if (!dest.startsWith(base)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid file path");
        }

        file.transferTo(dest);

        KycDocument d = new KycDocument();

        d.setKyc(k);
        d.setDocumentType(t);
        d.setDocumentNumber(normalized);
        d.setFileName(Paths.get(original).getFileName().toString());
        d.setFilePath(dest.toString());
        d.setRemarks("Uploaded");

        d = documents.save(d);

        if ("AADHAAR".equals(t)) {
            k.setAadhaarNumber(normalized);
        }

        if ("PAN".equals(t)) {
            k.setPanNumber(normalized.toUpperCase(Locale.ROOT));
        }

        requests.save(k);

        audit(k, "DOCUMENT_UPLOADED:" + t, null, d.getFileName());

        return d;
    }

    public List<KycDocument> docs(Long id) {
        get(id);
        return documents.findByKyc_Id(id);
    }

    public KycDocument document(Long id) {
        return documents.findById(id)
                .orElseThrow(() ->
                        notFound("KYC document not found: " + id));
    }

    public KycDocument documentDecision(
            Long id,
            boolean verified,
            Long actor,
            String remarks) {

        KycDocument d = document(id);

        d.setVerificationStatus(verified ? "Verified" : "Rejected");
        d.setRemarks(remarks);

        KycDocument saved = documents.save(d);

        audit(
                d.getKyc(),
                verified ? "DOCUMENT_VERIFIED" : "DOCUMENT_REJECTED",
                actor,
                remarks);

        return saved;
    }

    public KycDocument deleteDocument(Long id) {

        KycDocument d = document(id);

        try {
            Files.deleteIfExists(Paths.get(d.getFilePath()));
        } catch (IOException ignored) {
            // Ignore file deletion errors.
        }

        documents.delete(d);

        audit(d.getKyc(), "DOCUMENT_DELETED", null, d.getDocumentType());

        return d;
    }

    public KycRequest approve(Long id, Long actor, String remarks) {

        KycRequest k = get(id);
        List<KycDocument> ds = documents.findByKyc_Id(id);

        if (ds.isEmpty()
                || ds.stream().anyMatch(
                d -> !"Verified".equals(d.getVerificationStatus()))) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "All uploaded KYC documents must be verified before approval");
        }

        k.setStatus("Verified");
        k.setVerifiedBy(actor);
        k.setVerifiedDate(LocalDateTime.now());
        k.setRemarks(remarks);

        k = requests.save(k);

        audit(k, "KYC_APPROVED", actor, remarks);

        return k;
    }

    public KycRequest reject(
            Long id,
            Long actor,
            String reason) {

        KycRequest k = get(id);

        if (reason == null || reason.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rejection reason is required");
        }

        k.setStatus("Rejected");
        k.setVerifiedBy(actor);
        k.setVerifiedDate(LocalDateTime.now());
        k.setRemarks(reason);

        k = requests.save(k);

        audit(k, "KYC_REJECTED", actor, reason);

        return k;
    }

    public KycRequest resubmit(
            Long id,
            Long actor,
            String remarks) {

        KycRequest k = get(id);

        if (!"Rejected".equals(k.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only rejected KYC can be re-submitted");
        }

        k.setStatus("Re-Submitted");
        k.setVerifiedBy(null);
        k.setVerifiedDate(null);
        k.setRemarks(remarks);

        k = requests.save(k);

        audit(k, "KYC_RESUBMITTED", actor, remarks);

        return k;
    }

    public KycRequest ckyc(
            Long customerId,
            String ckycNumber,
            boolean found) {

        KycRequest k = requests
                .findFirstByCustomer_CustomerIdOrderByIdDesc(customerId)
                .orElseThrow(() ->
                        notFound("KYC request not found for customer"));

        k.setCkycNumber(ckycNumber);
        k.setCkycStatus(found ? "Verified" : "Not Found");
        k.setCkycVerificationDate(LocalDateTime.now());

        k = requests.save(k);

        audit(
                k,
                "CKYC_LOOKUP_" + k.getCkycStatus(),
                null,
                "Local/manual CKYC result; external registry integration is not configured");

        return k;
    }

    public List<KycAuditLog> audit(Long id) {
        get(id);
        return logs.findByKyc_IdOrderByActionDateDesc(id);
    }

    public Map<String, Object> dashboard() {

        List<KycRequest> all = requests.findAll();

        Map<String, Object> m = new LinkedHashMap<>();

        m.put("totalKycRequests", all.size());

        m.put("pendingVerification",
                all.stream()
                        .filter(k -> "Pending".equals(k.getStatus()))
                        .count());

        m.put("verifiedKyc",
                all.stream()
                        .filter(k -> "Verified".equals(k.getStatus()))
                        .count());

        m.put("rejectedKyc",
                all.stream()
                        .filter(k -> "Rejected".equals(k.getStatus()))
                        .count());

        m.put("resubmittedKyc",
                all.stream()
                        .filter(k -> "Re-Submitted".equals(k.getStatus()))
                        .count());

        m.put("ckycVerified",
                all.stream()
                        .filter(k -> "Verified".equals(k.getCkycStatus()))
                        .count());

        return m;
    }

    public List<KycRequest> byStatus(String status) {

        return requests.findAll().stream()
                .filter(k -> k.getStatus().equalsIgnoreCase(status))
                .toList();
    }
}
