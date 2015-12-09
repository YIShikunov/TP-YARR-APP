package archon.tp_yarr_app.utils;

import com.fasterxml.jackson.databind.JsonNode;

import net.dean.jraw.http.AuthenticationMethod;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.models.meta.JsonProperty;

import java.util.Date;

public class OAuthDataWrapper extends OAuthData {
    public OAuthDataWrapper() {
        super(AuthenticationMethod.APP, null);
    }

    public String access_token;
    public String token_type;
    public int expires_in;
    public String scope;
    public String refresh_token;


    @Override
    public String getAccessToken() {
        return access_token;
    }
    @Override
    public String getTokenType() {
        return token_type;
    }
    @Override
    public Date getExpirationDate() {
        Date tokenExpiration = new Date();
        // Add the time the token expires
        tokenExpiration.setTime(tokenExpiration.getTime() + expires_in * 1000);
        return tokenExpiration;
    }
    @Override
    public String[] getScopes() {
        return scope.split(",");
    }
    @Override
    public String getRefreshToken() {
        return refresh_token;
    }
}
