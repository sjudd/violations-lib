package se.bjurr.violations.lib.parsers;

import static se.bjurr.violations.lib.model.SEVERITY.ERROR;
import static se.bjurr.violations.lib.model.SEVERITY.INFO;
import static se.bjurr.violations.lib.model.SEVERITY.WARN;
import static se.bjurr.violations.lib.model.Violation.violationBuilder;
import static se.bjurr.violations.lib.parsers.ViolationParserUtils.findIntegerAttribute;
import static se.bjurr.violations.lib.parsers.ViolationParserUtils.getAttribute;
import static se.bjurr.violations.lib.parsers.ViolationParserUtils.getChunks;
import static se.bjurr.violations.lib.parsers.ViolationParserUtils.getIntegerAttribute;
import static se.bjurr.violations.lib.reports.Parser.LINT;

import java.util.ArrayList;
import java.util.List;
import se.bjurr.violations.lib.model.SEVERITY;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.util.Optional;

public class LintParser implements ViolationsParser {
  @Override
  public List<Violation> parseReportOutput(String string) throws Exception {
    List<Violation> violations = new ArrayList<>();
    List<String> files = getChunks(string, "<file", "</file>");
    for (String fileChunk : files) {
      String filename = getAttribute(fileChunk, "name");
      List<String> issues = getChunks(fileChunk, "<issue", "/>");
      for (String issueChunk : issues) {
        Integer line = getIntegerAttribute(issueChunk, "line");
        Optional<Integer> charAttrib = findIntegerAttribute(issueChunk, "char");
        String severity = getAttribute(issueChunk, "severity");

        String message = getAttribute(issueChunk, "reason");
        String evidence = getAttribute(issueChunk, "evidence").trim();
        violations.add( //
            violationBuilder() //
                .setParser(LINT) //
                .setStartLine(line) //
                .setColumn(charAttrib.orNull()) //
                .setFile(filename) //
                .setSeverity(toSeverity(severity)) //
                .setMessage(message + ": " + evidence) //
                .build() //
            );
      }
    }
    return violations;
  }

  public SEVERITY toSeverity(String severity) {
    if (severity.equalsIgnoreCase("ERROR")) {
      return ERROR;
    }
    if (severity.equalsIgnoreCase("WARNING")) {
      return WARN;
    }
    return INFO;
  }
}
