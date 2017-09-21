package org.wso2.carbon.identity.provisioning.connector.clearslide;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.provisioning.AbstractOutboundProvisioningConnector;
import org.wso2.carbon.identity.provisioning.IdentityProvisioningException;
import org.wso2.carbon.identity.provisioning.ProvisionedIdentifier;
import org.wso2.carbon.identity.provisioning.ProvisioningEntity;
import org.wso2.carbon.identity.provisioning.ProvisioningEntityType;
import org.wso2.carbon.identity.provisioning.ProvisioningOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ClearslideProvisioningConnector extends AbstractOutboundProvisioningConnector {

    private static final Log logger = LogFactory.getLog(ClearslideProvisioningConnector.class);

    public void init(Property[] properties) throws IdentityProvisioningException {

    }

    public ProvisionedIdentifier provision(ProvisioningEntity provisioningEntity) throws IdentityProvisioningException {

        String provisionedId = null;

        if(provisioningEntity.isJitProvisioning() && !isJitProvisioningEnabled()) {
            logger.info("JIT provisioning disabled for ClearSlide connector");
            return null;
        }

        if(provisioningEntity.getEntityType() == ProvisioningEntityType.USER) {
            if (provisioningEntity.getOperation() == ProvisioningOperation.DELETE) {
                // get the provisionedId back from ClearSlide
                deleteUser(provisioningEntity);
            } else if(provisioningEntity.getOperation() == ProvisioningOperation.POST) {
                // get the provisionedId back from ClearSlide
                createUser(provisioningEntity);
            } else {
                logger.warn("Unsupported provisioning operation.");
            }
        } else {
            logger.warn("Unsupported provisioning entity type.");
        }

        // creates a provisioned identifier for the provisioned user.
        ProvisionedIdentifier identifier = new ProvisionedIdentifier();
        identifier.setIdentifier(provisionedId);
        return identifier;

    }

    private void createUser(ProvisioningEntity provisioningEntity) {
    }

    private void deleteUser(ProvisioningEntity provisioningEntity) throws IdentityProvisioningException {

        JSONObject entity = new JSONObject();
        try {
            entity.put("email", provisioningEntity.getEntityName());
            update(provisioningEntity.getIdentifier().getIdentifier(), entity);
        } catch (JSONException e) {
            logger.error("Error while creating JSON body");
            throw new IdentityProvisioningException(e);
        }
    }

    private void update(String provisionedId, JSONObject entity) throws IdentityProvisioningException {

        try {

            PostMethod patch = new PostMethod(this.getUserObjectEndpoint() + provisionedId) {
                @Override
                public String getName() {
                    return "DELETE";
                }
            };

            //setAuthorizationHeader(patch);
            patch.setRequestEntity(new StringRequestEntity(entity.toString(), "application/json",
                    null));

            try {
                HttpClient httpclient = new HttpClient();
                httpclient.executeMethod(patch);
                if (patch.getStatusCode() == HttpStatus.SC_OK
                        || patch.getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                    logger.debug("HTTP status " + patch.getStatusCode() + " updating user "
                            + provisionedId + "\n\n");
                } else {
                    logger.error("recieved response status code :" + patch.getStatusCode()
                            + " text : " + patch.getStatusText());
                    logger.debug("Error response : " + readResponse(patch));
                }

            } catch (IOException e) {
                logger.error("Error in invoking provisioning request");
                throw new IdentityProvisioningException(e);
            } finally {
                patch.releaseConnection();
            }

        } catch (UnsupportedEncodingException e) {
            logger.error("Error in encoding provisioning request");
            throw new IdentityProvisioningException(e);
        }
    }

    private String getUserObjectEndpoint() {
        return "https://dev.clearslideng.com/public/users/";
    }

    private String readResponse(PostMethod post) throws IOException {
        InputStream is = post.getResponseBodyAsStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();
    }
}
