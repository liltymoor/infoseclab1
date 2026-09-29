package lilty.infra.infoseclab1.utility;

import org.springframework.web.util.HtmlUtils;

public final class OutputSanitizer {
    private OutputSanitizer() {
    }

    public static String escape(String value) {
        if (value == null) {
            return null;
        }
        return HtmlUtils.htmlEscape(value);
    }
}
