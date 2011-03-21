package eu.wisebed.wiseui.domain;

import eu.wisebed.wiseui.shared.dto.Dto;

import java.io.Serializable;

/**
 * Common interface that encompasses all business objects.
 *
 * @author Soenke Nommensen
 */
public interface Bo extends Serializable {

    /**
     * Converts a business object to a data transport object.
     *
     * @return The corresponding data transport object
     */
    Dto toDto();
}
