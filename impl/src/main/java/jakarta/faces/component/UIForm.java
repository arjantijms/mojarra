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

import com.sun.faces.api.component.UIFormImpl;

import jakarta.faces.FacesException;
import jakarta.faces.component.visit.VisitCallback;
import jakarta.faces.component.visit.VisitContext;
import jakarta.faces.context.FacesContext;

/**
 * <p>
 * <strong class="changed_modified_2_1">UIForm</strong> is a {@link UIComponent} that represents an input form to be
 * presented to the user, and whose child components represent (among other things) the input fields to be included when
 * the form is submitted.
 * </p>
 *
 * <p>
 * By default, the <code>rendererType</code> property must be set to "<code>jakarta.faces.Form</code>". This value can
 * be changed by calling the <code>setRendererType()</code> method.
 * </p>
 */
public class UIForm extends UIComponentBase implements NamingContainer, UniqueIdVendor {

    // ------------------------------------------------------ Manifest Constants

    /**
     * <p>
     * The standard component type for this component.
     * </p>
     */
    public static final String COMPONENT_TYPE = "jakarta.faces.Form";

    /**
     * <p>
     * The standard component family for this component.
     * </p>
     */
    public static final String COMPONENT_FAMILY = "jakarta.faces.Form";

    UIFormImpl uiFormImpl;

    // ------------------------------------------------------------ Constructors

    /**
     * <p>
     * Create a new {@link UIForm} instance with default property values.
     * </p>
     */
    public UIForm() {
        super(new UIFormImpl());
        setRendererType("jakarta.faces.Form");
        this.uiFormImpl = (UIFormImpl) getUiComponentBaseImpl();
        uiFormImpl.setPeer(this);
    }

    // -------------------------------------------------------------- Properties

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    /**
     * <p>
     * <span class="changed_modified_2_1">Returns</span> the current value of the <code>submitted</code> property. The
     * default value is <code>false</code>. See {@link #setSubmitted} for details.
     * </p>
     *
     * <p class="changed_modified_2_1">
     * This property must be kept as a transient property using the {@link UIComponent#getTransientStateHelper}.
     * </p>
     *
     * @return <code>true</code> if the form was submitted, <code>false</code> otherwise.
     */
    public boolean isSubmitted() {
        return uiFormImpl.isSubmitted();
    }

    /**
     * <p>
     * <span class="changed_modified_2_1">If</span> <strong>this</strong> <code>UIForm</code> instance (as opposed to other
     * forms in the page) is experiencing a submit during this request processing lifecycle, this method must be called,
     * with <code>true</code> as the argument, during the {@link UIComponent#decode} for this <code>UIForm</code> instance.
     * If <strong>this</strong> <code>UIForm</code> instance is <strong>not</strong> experiencing a submit, this method must
     * be called, with <code>false</code> as the argument, during the {@link UIComponent#decode} for this
     * <code>UIForm</code> instance.
     * </p>
     *
     * <p>
     * The value of a <code>UIForm</code>'s submitted property must not be saved as part of its state.
     * </p>
     *
     * <p class="changed_modified_2_1">
     * This property must be kept as a transient property using the {@link UIComponent#getTransientStateHelper}.
     * </p>
     *
     * @param submitted the new value of the submitted flag.
     */
    public void setSubmitted(boolean submitted) {
        uiFormImpl.setSubmitted(submitted);
    }

    /**
     * Is the id prepended.
     *
     * @return <code>true</code> if it is, <code>false</code> otherwise.
     */
    public boolean isPrependId() {
        return uiFormImpl.isPrependId();
    }

    /**
     * Set whether the id should be prepended.
     *
     * @param prependId <code>true</code> if it is, <code>false</code> otherwise.
     */
    public void setPrependId(boolean prependId) {
        uiFormImpl.setPrependId(prependId);
    }

    // ----------------------------------------------------- UIComponent Methods

    /**
     * <p>
     * Override {@link UIComponent#processDecodes} to ensure that the form is decoded <strong>before</strong> its children.
     * This is necessary to allow the <code>submitted</code> property to be correctly set.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processDecodes(FacesContext context) {
        uiFormImpl.processDecodes(context);
    }

    /**
     * <p class="changed_modified_2_3">
     * Override {@link UIComponent#processValidators} to ensure that the children of this <code>UIForm</code> instance are
     * only processed if {@link #isSubmitted} returns <code>true</code>.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     * @see jakarta.faces.event.PreValidateEvent
     * @see jakarta.faces.event.PostValidateEvent
     */
    @Override
    public void processValidators(FacesContext context) {
        uiFormImpl.processValidators(context);
    }

    /**
     * <p>
     * Override {@link UIComponent#processUpdates} to ensure that the children of this <code>UIForm</code> instance are only
     * processed if {@link #isSubmitted} returns <code>true</code>.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processUpdates(FacesContext context) {
        uiFormImpl.processUpdates(context);
    }

    /**
     * <p class="changed_modified_2_2">
     * Generate an identifier for a component. The identifier will be prefixed with UNIQUE_ID_PREFIX, and will be unique
     * within this component-container. Optionally, a unique seed value can be supplied by component creators which should
     * be included in the generated unique id.
     * </p>
     * <p class="changed_added_2_2">
     * If the <code>prependId</code> property has the value <code>false</code>, this method must call
     * <code>createUniqueId</code> on the next ancestor <code>UniqueIdVendor</code>.
     * </p>
     *
     * @param context FacesContext
     * @param seed an optional seed value - e.g. based on the position of the component in the VDL-template
     * @return a unique-id in this component-container
     */
    @Override
    public String createUniqueId(FacesContext context, String seed) {
        return uiFormImpl.createUniqueId(context, seed);
    }

    /**
     * <p>
     * Override the {@link UIComponent#getContainerClientId} to allow users to disable this form from prepending its
     * <code>clientId</code> to its descendent's <code>clientIds</code> depending on the value of this form's
     * {@link #isPrependId} property.
     * </p>
     */
    @Override
    public String getContainerClientId(FacesContext context) {
        return uiFormImpl.getContainerClientId(context);
    }

    /**
     * @see UIComponent#visitTree
     */
    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        return uiFormImpl.visitTree(context, callback);
    }

    /**
     * @see UIComponent#invokeOnComponent
     */
    @Override
    public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException {
        return uiFormImpl.invokeOnComponent(context, clientId, callback);
    }


}
