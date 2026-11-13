package org.mitre.boogie.cli;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import javax.xml.stream.XMLStreamException;

import org.mitre.tdp.boogie.arinc.ArincVersion;

/**
 * Command-line entry point for compact views of ARINC 424 records in XML or fixed-width text.
 */
public final class BoogieCli {

  /**
   * Version used for fixed-width text when {@code -v} is omitted. V22 also reads most older files, as its fields are a superset.
   */
  static final ArincVersion DEFAULT_VERSION = ArincVersion.V22;

  private static final String HELP = """
      Usage: boogie -f FILE -c CLASS [-i IDENTIFIER] [-v ARINC_VERSION] [-n COUNT]

      Print compact views of ARINC 424 records from XML or legacy fixed-width text.
      The format is detected from the content; gzip-compressed input is accepted.

        -f, --file FILE              Input file; use - for standard input
        -c, --class CLASS            Record class, e.g. Airport, Runway, Approach (case-insensitive)
        -i, --id IDENTIFIER          Exact identifier match (case-insensitive)
        -v, --arinc-version VERSION  ARINC 424 version for text files, e.g. V18 or 22 (default V22)
        -n, --limit COUNT            Stop after COUNT matches
            --list-records          List record classes for XML and for the text version
        -h, --help                  Show this help

      Examples:
        boogie -f file.xml -c Airport -i KBOS
        boogie -f cifp.dat -v V18 -c Approach -i KBOS
        boogie -f LidoMIT.dat.gz -c Airway -i J75
        cat file.xml | boogie -f - -c Waypoint -n 5

      -i also matches a record's airport or AIRPORT/IDENTIFIER, so -c Runway -i KBOS lists KBOS
      runways and -c Approach -i KBOS/I04R selects one approach.
      Missing values display as -. Diagnostics go to stderr.
      Exit codes: 0 success, 1 input/output error, 2 usage error.
      """;

  private BoogieCli() {
  }

  /**
   * Runs the command and exits with its status.
   *
   * @param args command-line arguments; see {@code boogie --help}
   */
  public static void main(String[] args) {
    System.exit(run(args, System.in, System.out, System.err));
  }

  /**
   * Executes the command without closing caller-owned standard streams.
   *
   * @param args   command-line arguments; see {@code boogie --help}
   * @param stdin  input read when the file is {@code -}
   * @param stdout destination for record output
   * @param stderr destination for diagnostics
   * @return 0 for success, 1 for input/output errors, 2 for usage errors
   */
  public static int run(String[] args, InputStream stdin, PrintStream stdout, PrintStream stderr) {
    Options options;
    try {
      options = Options.parse(args);
    } catch (IllegalArgumentException e) {
      stderr.println("boogie: " + e.getMessage());
      stderr.println("Try 'boogie --help' for usage.");
      return 2;
    }

    if (options.help) {
      stdout.print(HELP);
      return stdout.checkError() ? 1 : 0;
    }
    if (options.listRecords) {
      stdout.println("XML: " + String.join(", ", RecordSummary.PROFILES) + " (other element names: generic view)");
      stdout.println("Text (" + options.version + "): " + String.join(", ", ArincTextReader.classes(options.version)));
      return stdout.checkError() ? 1 : 0;
    }

    try {
      if (options.file.equals("-")) {
        return view(stdin, options, stdout, stderr);
      }
      try (InputStream input = Files.newInputStream(Path.of(options.file))) {
        return view(input, options, stdout, stderr);
      }
    } catch (ArincTextReader.UnknownClassException e) {
      stderr.println("boogie: " + e.getMessage());
      return 2;
    } catch (IOException | XMLStreamException | InvalidPathException e) {
      stderr.println("boogie: " + RecordSummary.clean(e.getMessage()));
      return 1;
    }
  }

  private static int view(InputStream source, Options options, PrintStream stdout, PrintStream stderr)
      throws IOException, XMLStreamException {
    InputStream input = decompress(new BufferedInputStream(source));
    boolean xml = isXml(input);
    ArincTextReader.Selection selection = xml ? null : ArincTextReader.select(options.version, options.record);
    long[] matches = {0};
    int malformed = 0;

    if (xml) {
      XmlRecordReader.read(input, RecordSummary.elementNames(options.record), (record, port) -> {
        if (options.identifier != null
            && RecordSummary.identifiers(record, port).stream().noneMatch(options.identifier::equalsIgnoreCase)) {
          return true;
        }
        return print(RecordSummary.format(record, port), matches, options, stdout);
      });
    } else {
      malformed = ArincTextReader.read(
          input,
          options.version,
          selection,
          options.identifier,
          record -> print(ArincTextSummary.format(record), matches, options, stdout)
      );
    }

    if (stdout.checkError()) {
      stderr.println("boogie: could not write output.");
      return 1;
    }
    if (malformed > 0) {
      stderr.println("boogie: skipped " + malformed + " " + selection.label()
          + " line(s) that were not " + selection.spec().recordLength() + " characters long.");
    }
    if (matches[0] == 0) {
      stderr.println("boogie: no matching " + RecordSummary.clean(options.record) + " records"
          + (options.identifier == null ? "" : " for identifier " + RecordSummary.clean(options.identifier))
          + (xml ? "." : " in ARINC " + options.version + " text."));
    }
    return 0;
  }

  private static boolean print(String text, long[] matches, Options options, PrintStream stdout) {
    if (matches[0] > 0) {
      stdout.println();
    }
    stdout.println(text);
    matches[0]++;
    return !stdout.checkError() && matches[0] < options.limit;
  }

  /**
   * Unwraps gzip input while preserving support for {@link InputStream#mark(int)} and {@link InputStream#reset()} during
   * format detection.
   */
  private static InputStream decompress(BufferedInputStream input) throws IOException {
    input.mark(4);
    byte[] magic = input.readNBytes(4);
    input.reset();

    if (magic.length >= 2 && (magic[0] & 0xff) == 0x1f && (magic[1] & 0xff) == 0x8b) {
      return new BufferedInputStream(new GZIPInputStream(input, 1 << 16), 1 << 16);
    }
    if (magic.length == 4 && Arrays.equals(magic, new byte[] {'P', 'K', 3, 4})) {
      throw new IOException("ZIP archives are not supported; extract the file first.");
    }
    return input;
  }

  /**
   * Returns whether the first character after an optional byte-order mark and whitespace is {@code <}.
   */
  private static boolean isXml(InputStream input) throws IOException {
    int limit = 8192;
    input.mark(limit);
    try {
      for (int i = 0; i < limit; i++) {
        int next = input.read();
        if (next < 0) {
          return false;
        }
        if (next == 0xEF || next == 0xBB || next == 0xBF || Character.isWhitespace(next)) {
          continue;
        }
        return next == '<';
      }
      return false;
    } finally {
      input.reset();
    }
  }

  private record Options(String file, String record, String identifier, long limit, ArincVersion version, boolean help,
      boolean listRecords) {

    static Options parse(String[] args) {
      String file = null;
      String record = null;
      String identifier = null;
      long limit = Long.MAX_VALUE;
      ArincVersion version = DEFAULT_VERSION;
      boolean help = false;
      boolean listRecords = false;

      for (int i = 0; i < args.length; i++) {
        String option = args[i];
        if (option.equals("-h") || option.equals("--help")) {
          help = true;
          continue;
        }
        if (option.equals("--list-records")) {
          listRecords = true;
          continue;
        }
        if (!option.equals("-f") && !option.equals("--file")
            && !option.equals("-c") && !option.equals("--class")
            && !option.equals("-r") && !option.equals("--record")
            && !option.equals("-v") && !option.equals("--arinc-version")
            && !option.equals("-i") && !option.equals("--id")
            && !option.equals("-n") && !option.equals("--limit")) {
          throw new IllegalArgumentException("unknown option: " + RecordSummary.clean(option));
        }
        if (++i == args.length || (args[i].startsWith("-") && !args[i].equals("-"))) {
          throw new IllegalArgumentException("missing value for " + option);
        }
        String value = args[i];
        if (value.isBlank()) {
          throw new IllegalArgumentException("empty value for " + option);
        }

        switch (option) {
          case "-f", "--file" -> file = value;
          case "-c", "--class", "-r", "--record" -> record = value;
          case "-v", "--arinc-version" -> version = parseVersion(value);
          case "-i", "--id" -> identifier = value;
          case "-n", "--limit" -> {
            try {
              limit = Long.parseLong(value);
              if (limit < 1) {
                throw new NumberFormatException();
              }
            } catch (NumberFormatException e) {
              throw new IllegalArgumentException("--limit must be a positive integer.");
            }
          }
          default -> throw new AssertionError(option);
        }
      }

      if (!help && !listRecords && (file == null || record == null)) {
        throw new IllegalArgumentException("--file and --class are required.");
      }
      return new Options(file, record, identifier, limit, version, help, listRecords);
    }

    /**
     * Accepts enum names without regard to case, with or without the leading {@code V}, e.g. {@code V18}, {@code v22_nav}
     * or {@code 21}.
     */
    private static ArincVersion parseVersion(String value) {
      String name = value.strip().toUpperCase(Locale.ROOT);
      return ArincVersion.parse(name.startsWith("V") ? name : "V" + name)
          .orElseThrow(() -> new IllegalArgumentException(
              "unknown ARINC version: " + RecordSummary.clean(value) + "; choose from "
                  + String.join(", ", Arrays.stream(ArincVersion.values()).map(Enum::name).toList()) + "."
          ));
    }
  }
}
