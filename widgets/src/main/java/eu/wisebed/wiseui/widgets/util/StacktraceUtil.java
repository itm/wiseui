/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.widgets.util;

public class StacktraceUtil {

    private static final String DEFAULT = "<no stacktrace>";
    private static final String LINE_SEPARATOR = "\n";
    private static final String DOTS = "...";
    private static final int MAX_VISIBLE_ELEMENTS = 100;

    public static String stacktraceToString(final Throwable throwable) {
        if (throwable == null) return DEFAULT;

        String result = throwable.toString();
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

        return result;
    }
}
