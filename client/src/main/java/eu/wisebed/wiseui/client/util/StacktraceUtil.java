package eu.wisebed.wiseui.client.util;

public class StacktraceUtil {

    private static final String DEFAULT = "<no stacktrace>";
    private static final String LINE_SEPARATOR = "\n";
    private static final String DOTS = "...";
    private static final int MAX_VISIBLE_ELEMENTS = 100;

    public static String stacktraceToString(final Throwable throwable) {
        if (throwable == null) return DEFAULT;

        String result = null;
        result += throwable.toString();
        result += LINE_SEPARATOR;

        int i = 0;
        for (StackTraceElement element : throwable.getStackTrace()) {
            if (i > MAX_VISIBLE_ELEMENTS) {
                result += DOTS;
                break;
            }
            result += element;
            result += LINE_SEPARATOR;
            i++;
        }

        return result.toString();
    }
}
