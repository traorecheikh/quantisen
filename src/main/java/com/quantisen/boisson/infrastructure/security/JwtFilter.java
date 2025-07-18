package com.quantisen.boisson.infrastructure.security;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    private static final String PUBLIC_KEY_PEM = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyG8zE9tLbvPtOnBUJSdB
            kO5oIkAOlRHwwGvec5PYz8zj8ycwiCRTOnEYnDJMVqejp2hde2ZXAKnks/JiBuHz
            t1ruBayiNXOWQFrEbBoOaKDHJenJxPR4H5hy5A7b5cyMB1qVdAuS2wLHqS8HD7aZ
            Rxdb8fhGTRvh8q8Bjwdli/Iprk4JueRCmV4lpQxrbuRNXIsX2fHd1TL0IJQguMyX
            uvH/oZd9c1uoK0hkD8aCF+PGGKTtomVCR2SOwKz/AaRTX41qyrkT80ktiiR+TMyj
            dAKIILTUlapIXMQqmAtlrbqxoqBd5l7TLGKqP1F11Zxrs/I0TGKmib2+f8bJdUVx
            8wIDAQAB
            -----END PUBLIC KEY-----
            """;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (path.equals("utilisateurs/login") || (path.equals("utilisateurs/register"))) {
            return;
        }
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abort(requestContext);
            return;
        }

        String token = authHeader.substring("Bearer ".length());
        try {
            SignedJWT jwt = SignedJWT.parse(token);

            RSAPublicKey publicKey = getPublicKeyFromPem(PUBLIC_KEY_PEM);

            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            if (!jwt.verify(verifier)) {
                abort(requestContext);
                return;
            }

            Date exp = jwt.getJWTClaimsSet().getExpirationTime();
            if (exp == null || new Date().after(exp)) {
                abort(requestContext);
                return;
            }
            String role = (String) jwt.getJWTClaimsSet().getClaim("role");
            if (role == null || !(role.equals("GERANT") || role.equals("EMPLOYE") || role.equals("LIVREUR"))) {
                abort(requestContext);
            }

        } catch (Exception e) {
            abort(requestContext);
        }

    }

    private void abort(ContainerRequestContext ctx) {
        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    private RSAPublicKey getPublicKeyFromPem(String pem) throws Exception {
        pem = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(pem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(spec);
    }
}
