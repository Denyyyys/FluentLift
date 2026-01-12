package com.denyyyys.fluentLift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deepl.api.DeepLClient;
import com.deepl.api.TextResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeepLTranslateService {

    private DeepLClient deepLClient;

    @Value("${DEEPL_API_KEY}")
    private String deeplApiKey;

    public String translateEnToPl(String text) throws Exception {
        return this.translate("en", "pl", text);

    }

    public String translateEnToUk(String text) throws Exception {
        return this.translate("en", "uk", text);
    }

    private String translate(String sourceLang, String targetLang, String text) throws Exception {
        if (text.isBlank() || text == null) {
            return text;
        }

        deepLClient = new DeepLClient(deeplApiKey);

        TextResult result = deepLClient.translateText(text, sourceLang, targetLang);
        return result.getText();
    }
}
