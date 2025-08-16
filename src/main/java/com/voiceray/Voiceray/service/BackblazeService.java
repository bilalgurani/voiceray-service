package com.voiceray.Voiceray.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.http.*;

import java.util.Map;
import java.util.UUID;

@Service
public class BackblazeService {
    private static final String B2_API_URL = "https://api.backblazeb2.com/b2api/v2/";
    private final String keyId;
    private final String applicationKey;
    private final RestTemplate restTemplate = new RestTemplate();

    // Removed fixed bucketId - will be passed per upload
    public BackblazeService(
            @Value("${backblaze.keyId}") String keyId,
            @Value("${backblaze.applicationKey}") String applicationKey) {
        this.keyId = keyId;
        this.applicationKey = applicationKey;
    }

    public String uploadFile(MultipartFile file, String bucketName, String bucketId) throws IOException {
        // 1. Authenticate
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBasicAuth(keyId, applicationKey);
        ResponseEntity<BackblazeAuthResponse> authResponse = restTemplate.exchange(
                B2_API_URL + "b2_authorize_account",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders),
                BackblazeAuthResponse.class
        );

        // 2. Get bucket-specific upload URL
        BucketUploadData uploadData = getBucketUploadData(authResponse.getBody(), bucketId);

        // 3. Prepare upload headers
        String fileName = UUID.randomUUID() + "_" + sanitizeFilename(file.getOriginalFilename());
        HttpHeaders uploadHeaders = new HttpHeaders();
        uploadHeaders.set("Authorization", uploadData.authorizationToken);
        uploadHeaders.set("X-Bz-File-Name", fileName);
        uploadHeaders.set("X-Bz-Content-Sha1", "do_not_verify");
        uploadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 4. Upload file
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), uploadHeaders);
        restTemplate.postForEntity(
                uploadData.uploadUrl,
                requestEntity,
                BackblazeUploadResponse.class
        );

        // 5. Return public URL
        return generatePublicUrl(authResponse.getBody(), bucketId, bucketName, fileName);
    }

    private BucketUploadData getBucketUploadData(BackblazeAuthResponse auth, String bucketId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", auth.getAuthorizationToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = Map.of("bucketId", bucketId);
        String url = auth.getApiUrl() + "/b2api/v2/b2_get_upload_url";
        ResponseEntity<UploadUrlResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                UploadUrlResponse.class
        );

        return new BucketUploadData(
                response.getBody().getUploadUrl(),
                response.getBody().getAuthorizationToken()
        );
    }

    private String generatePublicUrl(BackblazeAuthResponse auth, String bucketId, String bucketName, String fileName) {
        String baseUrl = auth.getDownloadUrl().endsWith("/")
                ? auth.getDownloadUrl()
                : auth.getDownloadUrl() + "/";
        return baseUrl + "file/" + bucketName + "/" + fileName;
    }

    private String sanitizeFilename(String filename) {
        if (filename == null) return "file";
        return filename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    private static class BucketUploadData {
        final String uploadUrl;
        final String authorizationToken;

        BucketUploadData(String uploadUrl, String authorizationToken) {
            this.uploadUrl = uploadUrl;
            this.authorizationToken = authorizationToken;
        }
    }

    public static class BackblazeAuthResponse {
        private String apiUrl;
        private String authorizationToken;
        private String downloadUrl;

        public String getApiUrl() { return apiUrl; }
        public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }
        public String getAuthorizationToken() { return authorizationToken; }
        public void setAuthorizationToken(String authorizationToken) { this.authorizationToken = authorizationToken; }
        public String getDownloadUrl() { return downloadUrl; }
        public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    }

    public static class UploadUrlResponse {
        private String uploadUrl;
        private String authorizationToken;

        public String getUploadUrl() { return uploadUrl; }
        public void setUploadUrl(String uploadUrl) { this.uploadUrl = uploadUrl; }
        public String getAuthorizationToken() { return authorizationToken; }
        public void setAuthorizationToken(String authorizationToken) { this.authorizationToken = authorizationToken; }
    }

    public static class BackblazeUploadResponse {
        // Can be empty if not used
    }
}
