package jscover.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import static jscover.maven.TestType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.fail;

public class TestTypeTest {
    @Test
    public void shouldReturnCorrectRunner() throws MojoExecutionException {
        assertThat((JasmineHtmlReporterWebDriverRunner)JasmineHtmlReporter.getWebDriverRunner(), isA(JasmineHtmlReporterWebDriverRunner.class));
        assertThat((JasmineTrivialReporterWebDriverRunner)JasmineTrivialReporter.getWebDriverRunner(), isA(JasmineTrivialReporterWebDriverRunner.class));
        assertThat((QUnitWebDriverRunner)QUnit.getWebDriverRunner(), isA(QUnitWebDriverRunner.class));
        assertThat((Jasmine2DefaultReporterWebDriverRunner)Jasmine2.getWebDriverRunner(), isA(Jasmine2DefaultReporterWebDriverRunner.class));
    }

    @Test
    public void shouldThrowExceptionForCustom() throws MojoExecutionException {
        try {
            Custom.getWebDriverRunner();
            fail("Expected exception");
        } catch(MojoExecutionException e) {
            assertThat(e.getMessage(), equalTo("Please provide a custom test type class that implements jscover.maven.WebDriverRunner"));
        }
    }
}