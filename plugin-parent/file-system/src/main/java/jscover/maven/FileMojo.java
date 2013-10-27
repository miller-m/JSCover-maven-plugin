package jscover.maven;

import jscover.Main;
import jscover.filesystem.ConfigurationForFS;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//@Mojo(name = "file", requiresDirectInvocation = true)
@Mojo(name = "file", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public class FileMojo extends JSCoverMojo {
    private ConfigurationForFS defaults = new ConfigurationForFS();

    @Parameter
    private File destDir = defaults.getDestDir();
    @Parameter
    protected final List<String> excludeArgs = new ArrayList<String>();

    public void execute() throws MojoExecutionException, MojoFailureException {
        setSystemProperties();
        final ConfigurationForFS config = getConfigurationForFS();

        Main main = new Main();
        try {
            main.initialize();
            main.runFileSystem(config);
        } catch (IOException e) {
            throw new MojoExecutionException("Problem initialising JSCover", e);
        }

    }

    private ConfigurationForFS getConfigurationForFS() {
        ConfigurationForFS config = new ConfigurationForFS();
        //Common parameters
        setCommonConfiguration(config);
        //File-System parameters
        config.setDestDir(destDir);
        for (String excludeArg : excludeArgs) {
            if (excludeArg.startsWith(ConfigurationForFS.EXLCUDE_PREFIX)) {
                config.addExclude(excludeArg);
            } else if (excludeArg.startsWith(ConfigurationForFS.EXLCUDE_REG_PREFIX)) {
                config.addExcludeReg(excludeArg);
            }
        }
        return config;
    }

}
