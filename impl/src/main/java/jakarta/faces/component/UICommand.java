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

import com.sun.faces.api.component.UICommandImpl;

import jakarta.el.MethodExpression;
import jakarta.faces.application.Application;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;
import jakarta.faces.event.FacesEvent;
import jakarta.faces.render.Renderer;

/**
 * <strong>UICommand</strong> is a {@link UIComponent} that represents a user interface component which, when activated
 * by the user, triggers an application specific "command" or "action". Such a component is typically rendered as a push
 * button, a menu item, or a hyperlink.
 *
 * <p>
 * When the <code>decode()</code> method of this {@link UICommand}, or its corresponding {@link Renderer}, detects that
 * this control has been activated, it will queue an {@link ActionEvent}. Later on, the <code>broadcast()</code> method
 * will ensure that this event is broadcast to all interested listeners.
 * </p>
 *
 * <p>
 * Listeners will be invoked in the following order:
 * <ol>
 *   <li>{@link ActionListener}s, in the order in which they were registered.
 *   <li>The "actionListener" {@link MethodExpression} (which will cover the "actionListener" that was set as a
 *       <code>MethodBinding</code>).
 *   <li>The default {@link ActionListener}, retrieved from the {@link Application} - and therefore, any attached "action"
 *       {@link MethodExpression}.
 * </ol>
 *
 * <p>
 * By default, the <code>rendererType</code> property must be set to "<code>jakarta.faces.Button</code>". This value can
 * be changed by calling the <code>setRendererType()</code> method.
 * </p>
 */
public class UICommand extends UIComponentBase implements ActionSource2 {

    // ------------------------------------------------------ Manifest Constants

    /**
     * The standard component type for this component.
     */
    public static final String COMPONENT_TYPE = "jakarta.faces.Command";

    /**
     * The standard component family for this component.
     */
    public static final String COMPONENT_FAMILY = "jakarta.faces.Command";

    /**
     * Properties that are tracked by state saving.
     */
    enum PropertyKeys {
        value, immediate, methodBindingActionListener, actionExpression,
    }

    UICommandImpl uiCommandImpl;

    // ------------------------------------------------------------ Constructors

    /**
     * Create a new {@link UICommand} instance with default property values.
     */
    public UICommand() {
        super(new UICommandImpl());
        setRendererType("jakarta.faces.Button");
        this.uiCommandImpl = (UICommandImpl) getUiComponentBaseImpl();
        uiCommandImpl.setPeer(this);
    }

    // -------------------------------------------------------------- Properties

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    // ------------------------------------------------- ActionSource/ActionSource2 Properties

    /**
     * The immediate flag.
     */
    @Override
    public boolean isImmediate() {
        return uiCommandImpl.isImmediate();
    }

    @Override
    public void setImmediate(boolean immediate) {
        uiCommandImpl.setImmediate(immediate);
    }

    /**
     * Returns the <code>value</code> property of the <code>UICommand</code>. This is most often rendered as a label.
     *
     * @return The object representing the value of this component.
     */
    public Object getValue() {
        return uiCommandImpl.getValue();
    }

    /**
     * Sets the <code>value</code> property of the <code>UICommand</code>. This is most often rendered as a label.
     *
     * @param value the new value
     */
    public void setValue(Object value) {
        uiCommandImpl.setValue(value);
    }

    // ---------------------------------------------------- ActionSource / ActionSource2 Methods

    @Override
    public MethodExpression getActionExpression() {
        return uiCommandImpl.getActionExpression();
    }

    @Override
    public void setActionExpression(MethodExpression actionExpression) {
        uiCommandImpl.setActionExpression(actionExpression);
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void addActionListener(ActionListener listener) {
        uiCommandImpl.addActionListener(listener);
    }

    @Override
    public ActionListener[] getActionListeners() {
        return uiCommandImpl.getActionListeners();
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void removeActionListener(ActionListener listener) {
        uiCommandImpl.removeActionListener(listener);
    }

    // ----------------------------------------------------- UIComponent Methods

    /**
     * In addition to to the default {@link UIComponent#broadcast} processing, pass the {@link ActionEvent} being broadcast
     * to the method referenced by <code>actionListener</code> (if any), and to the default {@link ActionListener}
     * registered on the {@link jakarta.faces.application.Application}.
     *
     * @param event {@link FacesEvent} to be broadcast
     *
     * @throws AbortProcessingException Signal the Jakarta Faces implementation that no further processing on the
     * current event should be performed
     * @throws IllegalArgumentException if the implementation class of this {@link FacesEvent} is not supported by this
     * component
     * @throws NullPointerException if <code>event</code> is <code>null</code>
     */
    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        uiCommandImpl.broadcast(event);
    }

    /**
     * Intercept <code>queueEvent</code> and take the following action. If the event is an <code>{@link ActionEvent}</code>,
     * obtain the <code>UIComponent</code> instance from the event. If the component is an <code>{@link ActionSource}</code>
     * obtain the value of its "immediate" property. If it is true, mark the phaseId for the event to be
     * <code>PhaseId.APPLY_REQUEST_VALUES</code> otherwise, mark the phaseId to be <code>PhaseId.INVOKE_APPLICATION</code>.
     * The event must be passed on to <code>super.queueEvent()</code> before returning from this method.
     *
     */
    @Override
    public void queueEvent(FacesEvent event) {
        uiCommandImpl.queueEvent(event);
    }

}
