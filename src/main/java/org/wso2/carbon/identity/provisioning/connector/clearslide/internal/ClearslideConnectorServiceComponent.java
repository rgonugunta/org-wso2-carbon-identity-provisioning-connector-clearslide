package org.wso2.carbon.identity.provisioning.connector.clearslide.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.provisioning.AbstractProvisioningConnectorFactory;
import org.wso2.carbon.identity.provisioning.connector.clearslide.ClearslideProvisioningConnectorFactory;

public class ClearslideConnectorServiceComponent {

    private static Log log = LogFactory.getLog(ClearslideConnectorServiceComponent.class);

    protected void activate(ComponentContext context) {

        if (log.isDebugEnabled()) {
            log.debug("Activating SalesforceConnectorServiceComponent");
        }

        try {
            ClearslideProvisioningConnectorFactory clearslideProvisioningConnectorFactory = new
                    ClearslideProvisioningConnectorFactory();

            context.getBundleContext().registerService(
                    AbstractProvisioningConnectorFactory.class.getName(),
                    clearslideProvisioningConnectorFactory, null);
            if (log.isDebugEnabled()) {
                log.debug("Clearslide Identity Provisioning Connector bundle is activated");
            }
        } catch (Throwable e) {
            log.error(" Error while activating Salesforce Identity Provisioning Connector ", e);
        }
    }
}
