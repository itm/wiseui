/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer Technology Institute (RACTI)
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
package eu.wisebed.wiseui.shared.exception;

public class WisemlException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 4313625292150681976L;

    private String stacktraceString;

    public WisemlException() {

    }

    public WisemlException(final Throwable throwable) {
        super(throwable);
    }

    public WisemlException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public WisemlException(final String message, final Throwable throwable, final String stacktraceString) {
        super(message, throwable);
        this.stacktraceString = stacktraceString;
    }

    public String getStacktraceString() {
        return stacktraceString;
    }
}
