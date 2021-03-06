package jscover.maven;

import jscover.Main;
import jscover.server.ConfigurationForServer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

@Mojo(name = "server", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public class ServerMojo extends JSCoverMojo {
    private ConfigurationForServer defaults = new ConfigurationForServer();

    @Parameter
    private int port = defaults.getPort();
    @Parameter
    private boolean includeUnloadedJS = defaults.isIncludeUnloadedJS();
    @Parameter
    private File documentRoot = defaults.getDocumentRoot();

    public void execute() throws MojoExecutionException, MojoFailureException {
        setSystemProperties();
        final ConfigurationForServer config = getConfigurationForServer();

        final Main main = new Main();
        Runnable jsCover = new Runnable() {
            public void run() {
                try {
                    main.initialize();
                    main.runServer(config);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread jsCoverThread = new Thread(jsCover);
        jsCoverThread.start();
        try {
            ServerTestRunner serverTestRunner = new ServerTestRunner(getWebClient(), getWebDriverRunner(), config, lineCoverageMinimum, branchCoverageMinimum, functionCoverageMinimum, reportLCOV, reportCoberturaXML);
            serverTestRunner.runTests(getTestFiles());
        } finally {
            stopWebClient();
            main.stop();
        }
    }

    private ConfigurationForServer getConfigurationForServer() throws MojoExecutionException {
        ConfigurationForServer config = new ConfigurationForServer();
        //Common parameters
        setCommonConfiguration(config);
        //Server parameters
        config.setDocumentRoot(documentRoot);
        config.setPort(port);
        config.setIncludeUnloadedJS(includeUnloadedJS);
        config.setReportDir(reportDir);
        return config;
    }
}
