package com.github.arena.challenges.weakmdparser;

public class MarkdownParser {

    /**
     * Parses a given markdown string into HTML.
     *
     * @param markdown the markdown text to be parsed
     * @return the HTML representation of the markdown text
     */
    public String parse(String markdown) {
        String[] lines = markdown.split("\n");
        StringBuilder result = new StringBuilder();
        boolean activeList = false;

        for (String line : lines) {
            String parsedLine = parseLine(line);

            if (parsedLine.startsWith("<li>") && !activeList) {
                activeList = true;
                result.append("<ul>");
            }

            if (!parsedLine.startsWith("<li>") && activeList) {
                activeList = false;
                result.append("</ul>");
            }

            result.append(parsedLine);
        }

        if (activeList) {
            result.append("</ul>");
        }

        return result.toString();
    }

    /**
     * Parses a single line of markdown text.
     *
     * @param line the markdown line to be parsed
     * @return the HTML representation of the markdown line
     */
    private String parseLine(String line) {
        String parsedLine = parseHeader(line);

        if (parsedLine == null) {
            parsedLine = parseListItem(line);
        }

        if (parsedLine == null) {
            parsedLine = parseParagraph(line);
        }

        return parsedLine;
    }

    /**
     * Parses a markdown header line.
     *
     * @param markdown the markdown header line to be parsed as an HTML header
     * @return the HTML representation of the header, or null if the line is not a header
     */
    private String parseHeader(String markdown) {
        int headerLevel = countHashSymbols(markdown);

        if (headerLevel == 0) {
            return null;
        }

        String tagName = "h" + headerLevel;
        String content = markdown.substring(headerLevel + 1).trim();

        return "<" + tagName + ">" + content + "</" + tagName + ">";
    }

    /**
     * Counts the number of leading hash symbols in a markdown line.
     *
     * @param line the line of text
     * @return the number of leading hash symbols
     */
    private int countHashSymbols(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == '#') {
            count++;
        }
        return count;
    }

    /**
     * Parses a markdown list item.
     *
     * @param markdown the markdown line to be parsed as a list item
     * @return the HTML representation of the list item, or null if the line is not a list item
     */
    private String parseListItem(String markdown) {
        if (markdown.startsWith("* ")) {
            String listItemContent = parseTextStyles(markdown.substring(2));
            return "<li>" + listItemContent + "</li>";
        }

        return null;
    }

    /**
     * Parses a markdown paragraph.
     *
     * @param markdown the markdown line to be parsed as a paragraph
     * @return the HTML representation of the paragraph
     */
    private String parseParagraph(String markdown) {
        return "<p>" + parseTextStyles(markdown) + "</p>";
    }

    /**
     * Parses text styles in a markdown string.
     * Currently supports bold and italic styles.
     *
     * @param markdown the markdown text to be parsed for text styles
     * @return the HTML representation of the styled text
     */
    private String parseTextStyles(String markdown) {
        String boldPattern = "__(.+)__";
        String boldReplacement = "<strong>$1</strong>";
        String result = markdown.replaceAll(boldPattern, boldReplacement);

        String italicPattern = "_(.+)_";
        String italicReplacement = "<em>$1</em>";
        return result.replaceAll(italicPattern, italicReplacement);
    }
}

