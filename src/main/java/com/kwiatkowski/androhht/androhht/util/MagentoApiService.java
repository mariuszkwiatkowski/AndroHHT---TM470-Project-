package com.kwiatkowski.androhht.androhht.util;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */


public class MagentoApiService extends DefaultApi10a {


    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return null;
    }
}
