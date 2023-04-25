/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package jakarta.faces.component;

import static java.util.logging.Level.SEVERE;

import java.util.logging.Logger;

import com.sun.faces.api.component.UINamingContainerImpl;

import jakarta.faces.component.visit.VisitCallback;
import jakarta.faces.component.visit.VisitContext;
import jakarta.faces.context.FacesContext;

/**
 * <p>
 * <strong class="changed_modified_2_0">UINamingContainer</strong> is a convenience base class for components that wish
 * to implement {@link NamingContainer} functionality.
 * </p>
 */

public class UINamingContainer extends UIComponentBase implements NamingContainer, UniqueIdVendor, StateHolder {

    // ------------------------------------------------------ Manifest Constants

    private static Logger LOGGER = Logger.getLogger("jakarta.faces.component", "jakarta.faces.LogStrings");

    /**
     * The standard component type for this component.
     */
    public static final String COMPONENT_TYPE = "jakarta.faces.NamingContainer";

    /**
     * The standard component family for this component.
     */
    public static final String COMPONENT_FAMILY = "jakarta.faces.NamingContainer";

    /**
     * <p class="changed_added_2_0">
     * The context-param that allows the separator char for clientId strings to be set on a per-web application basis.
     * </p>
     *
     * @since 2.0
     */
    public static final String SEPARATOR_CHAR_PARAM_NAME = "jakarta.faces.SEPARATOR_CHAR";


    UINamingContainerImpl uiNamingContainerImpl;

    // ------------------------------------------------------------ Constructors


    /**
     * <p>
     * Create a new {@link UINamingContainer} instance with default property values.
     * </p>
     */
    public UINamingContainer() {
        super(new UINamingContainerImpl());
        setRendererType(null);
        this.uiNamingContainerImpl = (UINamingContainerImpl) getUiComponentBaseImpl();
        uiNamingContainerImpl.setPeer(this);
    }


    // -------------------------------------------------------------- Properties

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * <p class="changed_added_2_0">
     * Return the character used to separate segments of a clientId. The implementation must determine if there is a
     * &lt;<code>context-param</code>&gt; with the value given by the value of the symbolic constant
     * {@link #SEPARATOR_CHAR_PARAM_NAME}. If there is a value for this param, the first character of the value must be
     * returned from this method. Otherwise, the value of the symbolic constant {@link NamingContainer#SEPARATOR_CHAR} must
     * be returned.
     * </p>
     *
     * @param context the {@link FacesContext} for the current request
     * @return the separator char.
     * @since 2.0
     */
    public static char getSeparatorChar(FacesContext context) {
        if (context == null) {
            if (LOGGER.isLoggable(SEVERE)) {
                LOGGER.log(SEVERE, "UINamingContainer.getSeparatorChar() called with null FacesContext. This indicates a SEVERE error. Returning {0}",
                        SEPARATOR_CHAR);
            }

            return SEPARATOR_CHAR;
        }

        Character separatorChar = (Character) context.getAttributes().get(SEPARATOR_CHAR_PARAM_NAME);
        if (separatorChar == null) {
            String initParam = context.getExternalContext().getInitParameter(SEPARATOR_CHAR_PARAM_NAME);
            separatorChar = SEPARATOR_CHAR;
            if (initParam != null) {
                initParam = initParam.trim();
                if (initParam.length() != 0) {
                    separatorChar = initParam.charAt(0);
                }
            }

            context.getAttributes().put(SEPARATOR_CHAR_PARAM_NAME, separatorChar);
        }

        return separatorChar;
    }

    /**
     * @return <code>true</code> if tree should be visited, <code>false</code> otherwise.
     * @see UIComponent#visitTree
     */
    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        return uiNamingContainerImpl.visitTree(context, callback);
    }

    @Override
    public String createUniqueId(FacesContext context, String seed) {
        return uiNamingContainerImpl.createUniqueId(context, seed);
    }

}
