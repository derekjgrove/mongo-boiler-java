package com.mdb.ps.services.kms;

import java.util.*;



public class KMSProviderSingleton {

    private static KMSProvider instance;
  
    private KMSProviderSingleton() {}


    private static KMSProvider createInstance(String cloudProvider, String accessKeyId, String secretAccessKey, String sessionToken, String keyvaultNamespace, String dekId, UUID dekUUID) {
        return new KMSProvider(cloudProvider, accessKeyId, secretAccessKey, sessionToken, keyvaultNamespace, dekId, dekUUID);
    }
    

    public static KMSProvider getInstance(String cloudProvider, String accessKeyId, String secretAccessKey, String sessionToken, String keyvaultNamespace, String dekId, UUID dekUUID) {

        if(instance == null) {
            instance = createInstance(cloudProvider, accessKeyId, secretAccessKey, sessionToken, keyvaultNamespace, dekId, dekUUID);
        }

        return instance;
    }

    private static KMSProvider createInstance() {
        return new KMSProvider();
    }
    

    public static KMSProvider getInstance() {

        if(instance == null) {
            instance = createInstance();
        }

        return instance;
    }

    
}
