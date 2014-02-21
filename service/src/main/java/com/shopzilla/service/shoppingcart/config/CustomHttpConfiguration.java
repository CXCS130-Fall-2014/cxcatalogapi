package com.shopzilla.service.shoppingcart.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.yammer.dropwizard.config.HttpConfiguration;
import com.yammer.dropwizard.config.LoggingConfiguration;
import com.yammer.dropwizard.config.RequestLogConfiguration;
import com.yammer.dropwizard.util.Duration;
import com.yammer.dropwizard.util.Size;

/**
 * Provides default values for HttpConfiguration.
 * We can always override this values in service.yaml.
 *
 * User: Oleksandr Murha
 * Date: 04/09/2013
 */
public class CustomHttpConfiguration extends HttpConfiguration {

    public class CustomRequestLogConfiguration extends RequestLogConfiguration{
        public class CustomConsoleConfiguration extends LoggingConfiguration.ConsoleConfiguration {
            private boolean enabled = false;

            @Override
            public boolean isEnabled() {
                return enabled;
            }
        }

        @JsonProperty("console")
        private CustomConsoleConfiguration console = new CustomConsoleConfiguration();

        @Override
        public CustomConsoleConfiguration getConsoleConfiguration() {
            return console;
        }
    }

    private String rootPath = "/services/shoppingcart/v1/*";
    private int port = 7500;
    private int adminPort = 7500;
    private int maxThreads = 100;
    private int minThreads = 10;
    private ConnectorType connectorType = ConnectorType.BLOCKING;
    private Duration maxIdleTime = Duration.seconds(5);
    private int acceptorThreads = 3;
    private int acceptorThreadPriorityOffset = 0;
    private int acceptQueueSize = 100;
    private int maxBufferCount = 1024;
    private Size requestBufferSize = Size.kilobytes(32);
    private Size requestHeaderBufferSize = Size.kilobytes(6);
    private Size responseBufferSize = Size.kilobytes(32);
    private Size responseHeaderBufferSize = Size.kilobytes(6);
    private boolean reuseAddress = true;
    private Optional<Duration> soLingerTime = Optional.fromNullable(Duration.seconds(1));
    private int lowResourcesConnectionThreshold = 25000;
    private Duration lowResourcesMaxIdleTime = Duration.seconds(5);
    private Duration shutdownGracePeriod = Duration.seconds(2);
    private boolean useForwardedHeaders = true;
    private boolean useDirectBuffers = true;

    @JsonProperty("requestLog")
    private CustomRequestLogConfiguration requestLog = new CustomRequestLogConfiguration();

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getAdminPort() {
        return adminPort;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public int getMaxThreads() {
        return maxThreads;
    }

    @Override
    public int getMinThreads() {
        return minThreads;
    }

    @Override
    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    @Override
    public int getAcceptorThreads() {
        return acceptorThreads;
    }

    @Override
    public int getAcceptorThreadPriorityOffset() {
        return acceptorThreadPriorityOffset;
    }

    @Override
    public int getAcceptQueueSize() {
        return acceptQueueSize;
    }

    @Override
    public int getMaxBufferCount() {
        return maxBufferCount;
    }

    @Override
    public ConnectorType getConnectorType() {
        return connectorType;
    }

    @Override
    public Size getRequestBufferSize() {
        return requestBufferSize;
    }

    @Override
    public Size getRequestHeaderBufferSize() {
        return requestHeaderBufferSize;
    }

    @Override
    public Size getResponseBufferSize() {
        return responseBufferSize;
    }

    @Override
    public Size getResponseHeaderBufferSize() {
        return responseHeaderBufferSize;
    }

    @Override
    public boolean isReuseAddressEnabled() {
        return reuseAddress;
    }

    @Override
    public Optional<Duration> getSoLingerTime() {
        return soLingerTime;
    }

    @Override
    public int getLowResourcesConnectionThreshold() {
        return lowResourcesConnectionThreshold;
    }

    @Override
    public Duration getLowResourcesMaxIdleTime() {
        return lowResourcesMaxIdleTime;
    }

    @Override
    public Duration getShutdownGracePeriod() {
        return shutdownGracePeriod;
    }

    @Override
    public boolean useForwardedHeaders() {
        return useForwardedHeaders;
    }

    @Override
    public boolean useDirectBuffers() {
        return useDirectBuffers;
    }

    @Override
    public RequestLogConfiguration getRequestLogConfiguration() {
        return requestLog;
    }
}
