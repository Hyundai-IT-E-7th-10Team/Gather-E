package com.kosa.gather_e.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.kosa.gather_e.auth.vo.UserVO;
import org.springframework.stereotype.Component;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
public class GoogleAuthService  {

    public UserVO getUser(String idTokenString) throws Exception  {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList("356799775533-tluao51i97147innj7vnmfed2kqj02df.apps.googleusercontent.com"))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            return UserVO.builder()
                    .userNickname(name)
                    .userEmail(email)
                    .userName(name)
                    .userProfileImg(pictureUrl)
                    .build();
        } else {
            System.out.println("Invalid ID token.");
            throw new GeneralSecurityException();
        }
    }
}
