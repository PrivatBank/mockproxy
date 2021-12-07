package com.pb.mockproxy.littleproxy.mitm;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Parameter object holding personal informations given to a SSLEngineSource.
 * 
 * XXX consider to inline within the interface SslEngineSource, if MITM is core
 */
public class Authority {

    private final File keyStoreDir;

    private final String alias;

    private final char[] password;

    private final String commonName;

    private final String organization;

    private final String organizationalUnitName;

    private final String certOrganization;

    private final String certOrganizationalUnitName;

    /**
     * Create a parameter object with example certificate and certificate
     * authority informations
     */
    public Authority() {
        try {
            URL resource = getClass().getClassLoader().getResource("keystore");
            keyStoreDir = Paths.get(resource.toURI()).toFile();
            alias = "MockProxy-cert"; // proxy id
            password = "Pess ortum in berolinum!".toCharArray();
            organization = "MockProxy"; // proxy name
            commonName = organization + ", proxy works as man in the middle";
            organizationalUnitName = "Certificate Authority";
            certOrganization = organization; // proxy name
            certOrganizationalUnitName = organization + ", proxy for trace and mocking origins responses";
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Create a parameter object with the given certificate and certificate
     * authority informations
     */
    public Authority(File keyStoreDir, String alias, char[] password,
            String commonName, String organization,
            String organizationalUnitName, String certOrganization,
            String certOrganizationalUnitName) {
        super();
        this.keyStoreDir = keyStoreDir;
        this.alias = alias;
        this.password = password;
        this.commonName = commonName;
        this.organization = organization;
        this.organizationalUnitName = organizationalUnitName;
        this.certOrganization = certOrganization;
        this.certOrganizationalUnitName = certOrganizationalUnitName;
    }

    public File aliasFile(String fileExtension) {
        return new File(keyStoreDir, alias + fileExtension);
    }

    public String alias() {
        return alias;
    }

    public char[] password() {
        return password;
    }

    public String commonName() {
        return commonName;
    }

    public String organization() {
        return organization;
    }

    public String organizationalUnitName() {
        return organizationalUnitName;
    }

    public String certOrganisation() {
        return certOrganization;
    }

    public String certOrganizationalUnitName() {
        return certOrganizationalUnitName;
    }

}
