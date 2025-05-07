package com.example.beautifulweb.service;

import com.example.beautifulweb.config.RecaptchaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecaptchaService {

    private static final Logger logger = LoggerFactory.getLogger(RecaptchaService.class);

    @Autowired
    private RecaptchaConfig recaptchaConfig;

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyRecaptcha(String recaptchaResponse) {
        logger.info("Verifying reCAPTCHA response: {}", recaptchaResponse);
        if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
            logger.warn("reCAPTCHA response is empty or null");
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("secret", recaptchaConfig.getSecretKey());
        requestBody.put("response", recaptchaResponse);

        logger.info("Sending reCAPTCHA verification request to Google with secret: {}", recaptchaConfig.getSecretKey());
        try {
            Map<String, Object> response = restTemplate.postForObject(GOOGLE_RECAPTCHA_VERIFY_URL, requestBody,
                    Map.class);
            if (response == null) {
                logger.error("Failed to verify reCAPTCHA: Response from Google is null");
                return false;
            }

            logger.info("reCAPTCHA verification response: {}", response);
            Boolean success = (Boolean) response.get("success");
            if (success == null || !success) {
                logger.warn("reCAPTCHA verification failed. Error codes: {}", response.get("error-codes"));
                return false;
            }

            logger.info("reCAPTCHA verification successful");
            return true;
        } catch (Exception e) {
            logger.error("Error verifying reCAPTCHA: {}", e.getMessage(), e);
            return false;
        }
    }
}