package org.example.insurance_policy_system.serviceimpl.claimmanagement;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.example.insurance_policy_system.entity.claimmanagement.ClaimSettlement;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class SettlementAdviceServiceImpl {

    private final ClaimSettlementServiceImpl settlementService;

    public byte[] generatePdf(Long settlementId) {

        ClaimSettlement settlement =
                settlementService.getPaidForAdvice(settlementId);

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            Document document = new Document();

            PdfWriter.getInstance(document, output);
            document.open();

            document.add(new Paragraph(
                    "INSURANCE CLAIM SETTLEMENT ADVICE",
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD, 16)
            ));

            document.add(new Paragraph(
                    "Advice Number: "
                            + settlement.getSettlementAdviceNumber()));

            document.add(new Paragraph(
                    "Claim ID: "
                            + settlement.getClaimId().getClaimId()));

            document.add(new Paragraph(
                    "Settlement ID: "
                            + settlement.getSettlementId()));

            document.add(new Paragraph(
                    "Approved Amount: INR "
                            + settlement.getApprovedAmount()));

            document.add(new Paragraph(
                    "Payment Mode: "
                            + settlement.getPaymentMode()));

            document.add(new Paragraph(
                    "Transaction Reference: "
                            + settlement.getTransactionReference()));

            document.add(new Paragraph(
                    "Settlement Date: "
                            + settlement.getSettlementDate()));

            document.add(new Paragraph(
                    "Settlement Status: PAID"));

            document.add(new Paragraph(
                    "RazorpayX Test Mode - No real funds transferred."));

            document.close();

            return output.toByteArray();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to generate settlement advice", e);
        }
    }
}