package com.pspvin.mishra3.vineet.qr.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/qr")
@CrossOrigin("*")
public class QRAuthController {

    // Temporary in-memory store: Map<String, String> -> sessionId: status (PENDING/APPROVED)
    private final Map<String, String> sessionStore = new ConcurrentHashMap<>();

    // 1. Generate unique session ID and return QR code image bytes
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] generateQRCode() throws Exception {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, "PENDING");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(sessionId, BarcodeFormat.QR_CODE, 200, 200);
        
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    // 2. Check login status (Polled by React frontend)
    @GetMapping("/status/{sessionId}")
    public ResponseEntity<Map<String, String>> checkStatus(@PathVariable String sessionId) {
        String status = sessionStore.getOrDefault(sessionId, "EXPIRED");
        return ResponseEntity.ok(Map.of("status", status));
    }

    // 3. Mobile/Second device calls this endpoint to approve login
    @PostMapping("/authorize")
    public ResponseEntity<Map<String, String>> authorizeQR(@RequestBody Map<String, String> request) {
        String sessionId = request.get("sessionId");
        if (sessionStore.containsKey(sessionId)) {
            sessionStore.put(sessionId, "APPROVED");
            return ResponseEntity.ok(Map.of("message", "Login approved successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid session ID"));
    }
}

