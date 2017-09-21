package org.wso2.carbon.identity.provisioning.connector.clearslide;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.provisioning.AbstractOutboundProvisioningConnector;
import org.wso2.carbon.identity.provisioning.AbstractProvisioningConnectorFactory;
import org.wso2.carbon.identity.provisioning.IdentityProvisioningException;

public class ClearslideProvisioningConnectorFactory extends AbstractProvisioningConnectorFactory {

    private static final Log log = LogFactory.getLog(ClearslideProvisioningConnectorFactory.class);
    private static final String CLEARSLIDE = "clearslide";

    @Override
    protected AbstractOutboundProvisioningConnector buildConnector(
            Property[] provisioningProperties) throws IdentityProvisioningException {
        ClearslideProvisioningConnector salesforceConnector = new ClearslideProvisioningConnector();
        salesforceConnector.init(provisioningProperties);

        if (log.isDebugEnabled()) {
            log.debug("Clearslide provisioning connector created successfully.");
        }

        return salesforceConnector;
    }

    @Override
    public String getConnectorType() {
        return CLEARSLIDE;
    }
}
