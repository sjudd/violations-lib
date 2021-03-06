package se.bjurr.violations.lib;

import static org.assertj.core.api.Assertions.assertThat;
import static se.bjurr.violations.lib.TestUtils.getRootFolder;
import static se.bjurr.violations.lib.ViolationsApi.violationsApi;
import static se.bjurr.violations.lib.model.SEVERITY.ERROR;
import static se.bjurr.violations.lib.model.SEVERITY.INFO;
import static se.bjurr.violations.lib.model.SEVERITY.WARN;
import static se.bjurr.violations.lib.reports.Parser.RESHARPER;

import java.util.List;
import org.junit.Test;
import se.bjurr.violations.lib.model.Violation;

public class ResharperTest {

  @Test
  public void testThatViolationsCanBeParsed() {
    String rootFolder = getRootFolder();

    List<Violation> actual =
        violationsApi() //
            .withPattern(".*/resharper/.*\\.xml$") //
            .inFolder(rootFolder) //
            .findAll(RESHARPER) //
            .violations();

    assertThat(actual) //
        .hasSize(4);

    assertThat(actual.get(0).getReporter()) //
        .isEqualTo(RESHARPER.name());

    assertThat(actual.get(0).getMessage()) //
        .isEqualTo(
            "Using directive is not required by the code and can be safely removed. Redundancies in Code. Redundant using directive. For more info, visit http://confluence.jetbrains.net/display/ReSharper/Redundant+using+directive");
    assertThat(actual.get(0).getRule().get()) //
        .isEqualTo("RedundantUsingDirective");
    assertThat(actual.get(0).getFile()) //
        .isEqualTo("MyLibrary/Class1.cs");
    assertThat(actual.get(0).getSeverity()) //
        .isEqualTo(WARN);

    assertThat(actual.get(1).getMessage()) //
        .isEqualTo(
            "Join declaration and assignment. Common Practices and Code Improvements. Join local variable declaration and assignment");
    assertThat(actual.get(1).getRule().get()) //
        .isEqualTo("JoinDeclarationAndInitializer");
    assertThat(actual.get(1).getFile()) //
        .isEqualTo("MyLibrary/Class1.cs");
    assertThat(actual.get(1).getSeverity()) //
        .isEqualTo(INFO);

    assertThat(actual.get(2).getMessage()) //
        .isEqualTo(
            "Using directive is not required by the code and can be safely removed. Redundancies in Code. Redundant using directive. For more info, visit http://confluence.jetbrains.net/display/ReSharper/Redundant+using+directive");
    assertThat(actual.get(2).getRule().get()) //
        .isEqualTo("RedundantUsingDirective");
    assertThat(actual.get(2).getFile()) //
        .isEqualTo("MyLibrary/Properties/AssemblyInfo.cs");
    assertThat(actual.get(2).getSeverity()) //
        .isEqualTo(WARN);

    assertThat(actual.get(3).getMessage()) //
        .isEqualTo("Cannot resolve symbol 'GetError'. C# Compiler Errors. CSharpErrors");
    assertThat(actual.get(3).getRule().get()) //
        .isEqualTo("CSharpErrors");
    assertThat(actual.get(3).getFile()) //
        .isEqualTo("MyLibrary/Class1.cs");
    assertThat(actual.get(3).getSeverity()) //
        .isEqualTo(ERROR);
  }
}
