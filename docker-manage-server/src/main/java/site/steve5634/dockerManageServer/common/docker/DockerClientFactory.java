package site.steve5634.dockerManageServer.common.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import site.steve5634.dockerManageServer.domain.DockerServer;

import java.time.Duration;

public class DockerClientFactory {

    public static DockerClient getClient(DockerServer server){
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(server.getHost())
                .withApiVersion(server.getVersion())
                .withDockerTlsVerify(Integer.valueOf(1).equals(server.getTlsVerify()))
                .withDockerCertPath(server.getCertPath())
                .withRegistryUrl(server.getRegistryUrl())
                .withRegistryUsername(server.getRegistryUsername())
                .withRegistryPassword(server.getRegistryPassword())
                .withRegistryEmail(server.getRegistryEmail())
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        return DockerClientImpl.getInstance(config, httpClient);
    }

}
