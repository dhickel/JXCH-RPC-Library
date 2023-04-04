package io.mindspice.util;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;


public class CertPairStore {
    private final KeyStore ks;

    public CertPairStore() throws IllegalStateException {
        try {
            ks = KeyStore.getInstance("JKS");
            ks.load(null, null); // Needs called, but doesn't need any arguments passed
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to initialize keystore"); // Should never happen
        }
    }

    public KeyStore getKeyStore() { return ks; }

    public void addKey(String alias, String[] certPaths)
            throws IllegalStateException, IllegalArgumentException {
        addKey(alias, certPaths[0], certPaths[1]);
    }

    public void addKey(String alias, String certPath, String keyPath)
            throws IllegalStateException, IllegalArgumentException {

        try {
            // Register Bouncy Castle provider if it's not already set
            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(Files.newInputStream(Path.of(certPath)));
            PrivateKey privateKey = readPrivateKeyFromFile(Path.of(keyPath));

            // Load keys into keystore, using existing one, if one has been passed
            ks.setKeyEntry(alias, privateKey, "".toCharArray(), new Certificate[]{certificate});
        } catch (CertificateException e) {
            throw new IllegalArgumentException("Invalid certificate, failed to load", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Certificate File Not Found", e);
        } catch (KeyStoreException e) {
            throw new IllegalStateException("Object state invalid, no keystore", e); // Should never happen
        }
    }

    private PrivateKey readPrivateKeyFromFile(Path keyPath) throws IOException {
        try (FileReader fileReader = new FileReader(keyPath.toFile());
             PEMParser pemParser = new PEMParser(fileReader)) {

            Object pemObject = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (pemObject instanceof PEMKeyPair) {
                return converter.getPrivateKey(((PEMKeyPair) pemObject).getPrivateKeyInfo());
            } else if (pemObject instanceof PrivateKeyInfo) {
                return converter.getPrivateKey((PrivateKeyInfo) pemObject);
            } else {
                throw new IllegalArgumentException("Unsupported private key format");
            }
        }
    }
}