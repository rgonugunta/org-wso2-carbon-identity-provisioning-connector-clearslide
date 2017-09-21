package org.wso2.carbon.identity.provisioning.connector.clearslide;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.provisioning.IdentityProvisioningConstants;
import org.wso2.carbon.identity.provisioning.IdentityProvisioningException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ClearslideProvisioningConnectorConfig implements Serializable {

    private static final long serialVersionUID = -4476579393653814433L;

    private static final Log log = LogFactory.getLog(ClearslideProvisioningConnectorConfig.class);
    private Properties configs;

    /**
     * @param configs
     */
    public ClearslideProvisioningConnectorConfig(Properties configs) {
        this.configs = configs;
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getRequiredAttributeNames() {
        List<String> requiredAttributeList = new ArrayList<String>();
        String requiredAttributes = this.configs
                .getProperty(ClearslideConnectorConstants.PropertyConfig.REQUIRED_FIELDS);
        if (StringUtils.isNotBlank(requiredAttributes)) {
            requiredAttributeList = Arrays.asList(requiredAttributes
                    .split(IdentityProvisioningConstants.PropertyConfig.DELIMATOR));
        }
        return requiredAttributeList;
    }

    /**
     * @return
     * @throws IdentityProvisioningException
     */
    public String getUserIdClaim() throws IdentityProvisioningException {
        String userIDClaim = this.configs
                .getProperty(ClearslideConnectorConstants.PropertyConfig.USER_ID_CLAIM);
        if (StringUtils.isBlank(userIDClaim)) {
            log.error("Required claim for user id is not defined in config");
            throw new IdentityProvisioningException(
                    "Required claim for user id is not defined in config");
        }
        if (log.isDebugEnabled()) {
            log.debug("Mapped claim for UserId is : " + userIDClaim);
        }
        return userIDClaim;
    }

    /**
     * @param key
     * @return
     */
    public String getValue(String key) {
        return this.configs.getProperty(key);
    }
}