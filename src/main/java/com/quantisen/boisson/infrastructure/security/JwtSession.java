package com.quantisen.boisson.infrastructure.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@ApplicationScoped
@Named
public class JwtSession {

    private static final String PRIVATE_KEY_PEM = """
            -----BEGIN PRIVATE KEY-----
            MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDIbzMT20tu8+06
            cFQlJ0GQ7mgiQA6VEfDAa95zk9jPzOPzJzCIJFM6cRicMkxWp6OnaF17ZlcAqeSz
            8mIG4fO3Wu4FrKI1c5ZAWsRsGg5ooMcl6cnE9HgfmHLkDtvlzIwHWpV0C5LbAsep
            LwcPtplHF1vx+EZNG+HyrwGPB2WL8imuTgm55EKZXiWlDGtu5E1cixfZ8d3VMvQg
            lCC4zJe68f+hl31zW6grSGQPxoIX48YYpO2iZUJHZI7ArP8BpFNfjWrKuRPzSS2K
            JH5MzKN0AoggtNSVqkhcxCqYC2WturGioF3mXtMsYqo/UXXVnGuz8jRMYqaJvb5/
            xsl1RXHzAgMBAAECggEAEemEzjalXaOKpP5/4+1yWcJB588DXprCrLeXIuC+GggC
            ARYUq2ivpW6RAqMow6pp9jPd/YFZBMlUvn4f+OR9fs/aAK4mYDzVsHz2xoBnnuhr
            aRMlFycP4HxOcYd1wGomGMwlb2r6CKeNcgGuvF6XASLwcnCRJnf3mn69rBYUTaFs
            pETfhoyrgLMyTKFjSVrlkrlVx7sIy6BYtAeYnvWwwyU3pejXO5XRvkT4glgH+SFY
            sZqJKz++3dHOgE2Ov3jGjeOU2ZNs5y4FMI7qVbivhhmR94YG84OUUawmqevbc9HU
            +wxQ1ZVyepyg34hP633wG6+xN/yM1lcljboYoDaQyQKBgQDlXIdHBYHhI2tJXjK4
            MkHIEC4AFOa1bC5k437O1CrDBnnycdqSQadV1C0zGpuAPPjpoySjZvehO0T5QU6M
            rHO+FW5/79UV4MmtRgGTb8vC5j89p7OMqB2Q/EqnoY7chOVScPCYqIyymDKrnAyb
            YiTtLCrSdjb/X0dJl07m6zLhawKBgQDftplrudFOYUAwqm9BmmJMwS5/im/kkNw3
            OFl8WsQCZzwkVRw0738gkq4srqfDNHUK0pA/I6lF6s8fVL/dqRUEDMD5l8bpc4eM
            oBFWFZWrKEH5GmVRn1R4JawEln3uxtL2RF/D4FXhQW7ku5exQ2ZRvLZvLL9evmnJ
            F0+p9y9rmQKBgQCz67uquOVDiRdWPHCpEbOMjzSml77LPou+VL9a3BCXYHM0/p8o
            8BumNoUme47UMABKttG85L8IWYgnx1UpAPbie8lszuRm/yj1pd8Cm0ZKI3gkk3ND
            uXVIEME3coKw9gc2fqYd+so0JzRIbmdtVLYG3wjQhC9scH9peSpJ9uK49QKBgGsk
            7T3m9RPxB+lG95oX8tSIAVkcWJLDvnTEDDe+0PnFr8xG5weDXQOgvMKxs6+ORDA2
            wNc8oSnXV/PNh0cGYAZchWAFDUNqDZWQZD0ygUhEM7S2q6f4A2C3yjc4TIA7wXYp
            MSSDIR6NVCtAnVpMyYiENWvxpoYd9FRXUdQ5iwChAoGAJ7UlohHGC2E5tyYDE4L2
            Wt1PO3RiakuXtRymht2cEBd1/nP2KDnZjEHJGyR0+tSMyRXAqIe4BIpbsQ+Ov2P1
            R5Hv671MtVUjPC7PEAWBPlQsQxhjbEmdR/dhjx+4n1K8XYos9qWa8RAbi9zjIJ7z
            l8MV3Tt81VlmhKqert7no0U=
            -----END PRIVATE KEY-----
            """;

    public String generateToken(CompteUtilisateur utilisateur, long expirationSeconds) throws Exception {
        RSAPrivateKey privateKey = getPrivateKeyFromPem(PRIVATE_KEY_PEM);
        JWSSigner signer = new RSASSASigner(privateKey);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(utilisateur.getEmail())
                .claim("id", utilisateur.getId())
                .claim("email", utilisateur.getEmail())
                .claim("role", utilisateur.getRole().name())
                .claim("firstName", utilisateur.getFirstName())
                .claim("lastName", utilisateur.getLastName())
                .claim("isActive", utilisateur.isActive())
                .claim("isFirstLogin", utilisateur.isFirstLogin())
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plusSeconds(expirationSeconds)))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new com.nimbusds.jose.JWSHeader(JWSAlgorithm.RS256),
                claimsSet
        );
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private RSAPrivateKey getPrivateKeyFromPem(String pem) throws Exception {
        pem = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(pem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }
}
