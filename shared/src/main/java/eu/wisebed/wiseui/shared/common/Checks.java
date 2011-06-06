/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
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
package eu.wisebed.wiseui.shared.common;

import java.util.Collection;

/**
 * Utility class for common pre- and post-condition checks.
 * <p/>
 * The class provides two kinds of checks: Argument checks and general checks.
 * The difference is, that argument checks throw an {@link IllegalArgumentException},
 * while general checks throw a {@link RuntimeException}.
 *
 * @author Soenke Nommensen
 *
 * TODO: Show MessageBox when used in the client.
 */
public class Checks {

    /**
     * @param expression Boolean expression, which shall be tested.
     * @param message    Error message
     * @throws IllegalArgumentException
     */
    public static void checkArgument(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * @param argument Object reference, which shall be tested for Null.
     * @param message  Error message
     */
    public static void ifNullArgument(final Object argument, final String message) {
        checkArgument(argument != null, message);
    }

    /**
     * @param stringArgument String reference, which shall be tested for Null or empty.
     * @param message        Error message
     */
    public static void ifNullOrEmptyArgument(final String stringArgument, final String message) {
        checkArgument(stringArgument != null && !stringArgument.isEmpty(), message);
    }

    /**
     * @param collection Collection, which shall be tested for Null or empty.
     * @param message    Error message
     * @throws RuntimeException
     */
    @SuppressWarnings("rawtypes")
	public static void ifNullOrEmptyArgument(final Collection collection, final String message) {
        checkArgument(collection != null && !collection.isEmpty(), message);
    }

    /**
     * @param expression Boolean expression, which shall be tested.
     * @param message    Error message
     * @throws RuntimeException
     */
    public static void check(final boolean expression, final String message) throws RuntimeException {
        if (!expression) {
            throw new RuntimeException(message);
        }
    }

    /**
     * @param reference Object reference, which shall be tested for Null.
     * @param message   Error message
     * @throws RuntimeException
     */
    public static void ifNull(final Object reference, final String message) {
        check(reference != null, message);
    }

    /**
     * @param stringReference String reference, which shall be tested for Null or empty.
     * @param message         Error message
     * @throws RuntimeException
     */
    public static void ifNullOrEmpty(final String stringReference, final String message) {
        check(stringReference != null && !stringReference.isEmpty(), message);
    }

    /**
     * @param collection Collection, which shall be tested for Null or empty.
     * @param message    Error message
     * @throws RuntimeException
     */
    @SuppressWarnings("rawtypes")
	public static void ifNullOrEmpty(final Collection collection, final String message) {
        check(collection != null && !collection.isEmpty(), message);
    }
}
