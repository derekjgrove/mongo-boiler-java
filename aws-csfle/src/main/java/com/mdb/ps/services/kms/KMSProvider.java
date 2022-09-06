package com.mdb.ps.services.kms;
import java.util.*;

public class KMSProvider {
    private final String KEYVAULT_NAMESPACE = "__encryption.__keyVault";
        private final String ACCESS_KEY_ID = "<ACCESS_KEY_ID>";
        private final String SECRET_ACCESS_KEY = "<SECRET_ACCESS_KEY>";
        private final String SESSION_TOKEN = "<SESSION_TOKEN>";
        private final String CLOUD_PROVIDER = "aws";
        private final String DEK_ID = "<DEK_ID>";
        private final UUID DEK_UUID = UUID.fromString("<DEK_UUID>");

        private Map<String, Map<String, Object>> kmsProviders = new HashMap<String, Map<String, Object>>();
        private String keyvaultNamespace = "";
        private String dekId = "";
        private UUID dekUUID;
        
        
        
        KMSProvider() {
            Map<String, Object> providerDetails = new HashMap<>();
            providerDetails.put("accessKeyId", ACCESS_KEY_ID);
            providerDetails.put("secretAccessKey", SECRET_ACCESS_KEY);
            providerDetails.put("sessionToken", SESSION_TOKEN);
            this.kmsProviders.put(CLOUD_PROVIDER, providerDetails);
            this.keyvaultNamespace = KEYVAULT_NAMESPACE;
            this.dekId = DEK_ID;
            this.dekUUID = DEK_UUID;
        }
        
        KMSProvider(String cloudProvider, String accessKeyId, String secretAccessKey, String sessionToken, String keyvaultNamespace, String dekId, UUID dekUUID) {
            Map<String, Object> providerDetails = new HashMap<>();
            providerDetails.put("accessKeyId", accessKeyId);
            providerDetails.put("secretAccessKey", secretAccessKey);
            providerDetails.put("sessionToken", sessionToken);
            this.kmsProviders.put(cloudProvider, providerDetails);
            this.keyvaultNamespace = keyvaultNamespace;
            this.dekId = dekId;
            this.dekUUID = dekUUID;
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

        public UUID getDekUUID() {
            return this.dekUUID;
        }
}
