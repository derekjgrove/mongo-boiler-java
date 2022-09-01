package com.mdb.ps.services.kms;

import java.util.*;

public class KMSProvider {

    private final String KEYVAULT_NAMESPACE = "__encryption.__keyVault";
    private final String ACCESS_KEY_ID = "<ACCESS_KEY_ID>";
    private final String SECRET_ACCESS_KEY = "<SECRET_ACCESS_KEY>";
    private final String SESSION_TOKEN = "<SESSION_TOKEN>";
    private final String CLOUD_PROVIDER = "aws";
    private final String DEK_ID = "<DEK_ID>";

    private Map<String, Map<String, Object>> kmsProviders = new HashMap<String, Map<String, Object>>();
    private String keyvaultNamespace = "";
    private String dekId = "";
    
    
    
    public KMSProvider() {
        Map<String, Object> providerDetails = new HashMap<>();
        providerDetails.put("accessKeyId", ACCESS_KEY_ID);
        providerDetails.put("secretAccessKey", SECRET_ACCESS_KEY);
        providerDetails.put("sessionToken", SESSION_TOKEN);
        this.kmsProviders.put(CLOUD_PROVIDER, providerDetails);
        this.keyvaultNamespace = KEYVAULT_NAMESPACE;
        this.dekId = DEK_ID;
    }
    
    public KMSProvider(String cloudProvider, String accessKeyId, String secretAccessKey, String sessionToken, String keyvaultNamespace, String dekId) {
        Map<String, Object> providerDetails = new HashMap<>();
        providerDetails.put("accessKeyId", accessKeyId);
        providerDetails.put("secretAccessKey", secretAccessKey);
        providerDetails.put("sessionToken", sessionToken);
        this.kmsProviders.put(cloudProvider, providerDetails);
        this.keyvaultNamespace = keyvaultNamespace;
        this.dekId = dekId;
    }

    public Map<String, Map<String, Object>> getKMSProviders() {
        return this.kmsProviders;
    }

    public String getKeyvaultNamespace() {
        return this.keyvaultNamespace;
    }

    public String getDekId() {
        return this.dekId;
    }
}
