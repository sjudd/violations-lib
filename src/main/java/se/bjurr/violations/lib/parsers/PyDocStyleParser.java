package se.bjurr.violations.lib.parsers;

import static java.lang.Integer.parseInt;
import static se.bjurr.violations.lib.model.SEVERITY.ERROR;
import static se.bjurr.violations.lib.model.Violation.violationBuilder;
import static se.bjurr.violations.lib.reports.Parser.PYDOCSTYLE;
import static se.bjurr.violations.lib.util.ViolationParserUtils.getParts;

import java.util.ArrayList;
import java.util.List;
import se.bjurr.violations.lib.model.Violation;
import se.bjurr.violations.lib.util.ViolationParserUtils;

public class PyDocStyleParser implements ViolationsParser {

  @Override
  public List<Violation> parseReportOutput(String string) throws Exception {
    List<Violation> violations = new ArrayList<>();
    boolean fileLine = true;
    List<String> lines = ViolationParserUtils.getLines(string);
    String filename = null;
    Integer line = null;
    for (String inputLine : lines) {
      if (fileLine) {
        List<String> parts = getParts(inputLine, "([^:]*)", "(\\d+)");
        filename = parts.get(0);
        line = parseInt(parts.get(1));
      } else {
        List<String> parts = getParts(inputLine, "([^:]*)", ":(.*)");
        String rule = parts.get(0);
        String message = parts.get(1);

        violations.add( //
            violationBuilder() //
                .setParser(PYDOCSTYLE) //
                .setStartLine(line) //
                .setFile(filename) //
                .setRule(rule) //
                .setSeverity(ERROR) //
                .setMessage(message) //
                .build() //
            );
      }
      fileLine = !fileLine;
    }
    return violations;
  }
}
