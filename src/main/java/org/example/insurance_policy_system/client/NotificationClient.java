//package org.example.insurance_policy_system.client;
//
//import org.example.insurance_policy_system.dto.claimmanagement.client.ClaimNotificationDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//@FeignClient(name = "notification-service", url = "${services.notification.url}")
//public interface NotificationClient {
//
//    @PostMapping("/internal/notifications/claims")
//    void sendClaimNotification(
//            @RequestBody ClaimNotificationDto request);
//}