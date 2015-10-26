/* Icaro Cloud Simulator (ICLOS).
   Copyright (C) 2015 DISIT Lab http://www.disit.org - University of Florence

   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. */

package org.cloudsimulator.viewer.compositecomponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;

import org.cloudsimulator.repository.DynamicAttributesRepository;
import org.cloudsimulator.repository.StaticAttributesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CCUtility {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CCUtility.class);
    private static final String TITLE_PANEL = "titlePanel";
    private static final String INDEX_PANEL = "indexPanel";
    private static final String INDEX_PANEL_PARENT = "indexPanelParent";
    private static final String INDEX_PANEL_GPARENT = "indexPanelGParent";
    private static final String INDEX_PANEL_GGPARENT = "indexPanelGGParent";
    private static final String IDCC = "idCC";
    private static final String IDCC_PARENT = "idCCParent";
    private static final String OBJECT_LINK_TO_PANEL = "objectLinkToPanel";
    private static final String BINDING_TO_PANEL = "bindingToPanel";
    private static final String REMOVE_ACTION = "removeAction";

    private static final String LIST_DROP_DOWN_MENU = "listDropDownMenu";
    private static final String LIST_INPUT_DATE = "listInputDate";
    private static final String LIST_INPUT_EMAIL = "listInputEmail";
    private static final String LIST_INPUT_IP = "listInputIP";
    private static final String LIST_INPUT_NUMBER = "listInputNumber";
    private static final String LIST_INPUT_SELECT_ONE_MENU = "listInputSelectOneMenu";
    private static final String LIST_INPUT_TEXT = "listInputText";

    private static final String LIST_INPUT_SELECT_ONE_MENU_DYNAMIC = "listInputSelectOneMenuDynamic";
    private static final String LIST_INPUT_SELECT_ONE_MENU_DYNAMIC_LISTENER = "listInputSelectOneMenuDynamicListener";

    private static final String FIRST_PANEL = "firstPanel.xhtml";
    private static final String SECOND_PANEL = "secondPanel.xhtml";
    private static final String THIRD_PANEL = "thirdPanel.xhtml";
    private static final String FORTH_PANEL = "forthPanel.xhtml";

    private static final String PREFIX_IDCC = "ccPanel";
    private static final String PREFIX_REMOVE_PANEL = "removePanel";
    private static final String PREFIX_GET = "get";
    private static final String PREFIX_CONTROLLER = "Controller";
    private static final String PREFIX_VIEWER = "Viewer";
    private static final String OPEN_LIST = "List[";

    private CCUtility() {
        // Not Called
    }

    @SuppressWarnings("el-syntax")
    private static void setOneDynamicAttribute(
            Map<String, ValueExpression> mapValueExpression, String nameEntity,
            String nameAttribute) {
        try {
            DynamicAttributesRepository.class.getMethod(PREFIX_GET
                    + nameEntity.substring(0, 1).toUpperCase()
                    + nameEntity.substring(1)
                    + nameAttribute.substring(0, 1).toUpperCase()
                    + nameAttribute.substring(1));
            mapValueExpression.put(
                    nameAttribute,
                    createValueExpression(
                            "#{"
                                    + DynamicAttributesRepository.class
                                            .getAnnotation(
                                                    javax.inject.Named.class)
                                            .value()
                                    + "."
                                    + nameEntity
                                    + nameAttribute.substring(0, 1)
                                            .toUpperCase()
                                    + nameAttribute.substring(1) + "}",
                            Object.class));
        } catch (NoSuchMethodException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    @SuppressWarnings("el-syntax")
    private static void setOneStaticAttribute(
            Map<String, ValueExpression> mapValueExpression, String nameEntity,
            String nameAttribute) {
        try {
            StaticAttributesRepository.class.getMethod(PREFIX_GET
                    + nameEntity.substring(0, 1).toUpperCase()
                    + nameEntity.substring(1)
                    + nameAttribute.substring(0, 1).toUpperCase()
                    + nameAttribute.substring(1));
            mapValueExpression.put(
                    nameAttribute,
                    createValueExpression(
                            "#{"
                                    + StaticAttributesRepository.class
                                            .getAnnotation(
                                                    javax.inject.Named.class)
                                            .value()
                                    + "."
                                    + nameEntity
                                    + nameAttribute.substring(0, 1)
                                            .toUpperCase()
                                    + nameAttribute.substring(1) + "}",
                            Object.class));
        } catch (NoSuchMethodException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    private static void setDefaultAttribute(
            Map<String, ValueExpression> mapValueExpression, String nameEntity) {
        setOneStaticAttribute(mapValueExpression, nameEntity, LIST_INPUT_DATE);
        setOneStaticAttribute(mapValueExpression, nameEntity, LIST_INPUT_EMAIL);
        setOneStaticAttribute(mapValueExpression, nameEntity, LIST_INPUT_IP);
        setOneStaticAttribute(mapValueExpression, nameEntity, LIST_INPUT_NUMBER);
        setOneStaticAttribute(mapValueExpression, nameEntity,
                LIST_INPUT_SELECT_ONE_MENU);
        setOneStaticAttribute(mapValueExpression, nameEntity, LIST_INPUT_TEXT);
        setOneDynamicAttribute(mapValueExpression, nameEntity,
                LIST_INPUT_SELECT_ONE_MENU_DYNAMIC_LISTENER);
        setOneDynamicAttribute(mapValueExpression, nameEntity,
                LIST_INPUT_SELECT_ONE_MENU_DYNAMIC);
    }

    @SuppressWarnings("el-syntax")
    public static void setFirstPanel(int indexPanel,
            HtmlPanelGroup htmlPanelParent, String nameEntity, String context) {
        Map<String, ValueExpression> mapValueExpression = new HashMap<String, ValueExpression>();
        final String idCC = PREFIX_IDCC
                + nameEntity.substring(0, 1).toUpperCase()
                + nameEntity.substring(1) + indexPanel;
        mapValueExpression.put(IDCC, createValueExpression(idCC, String.class));
        mapValueExpression
                .put(INDEX_PANEL,
                        createValueExpression(String.valueOf(indexPanel),
                                String.class));
        mapValueExpression.put(
                TITLE_PANEL,
                createValueExpression("# " + (indexPanel + 1) + " "
                        + nameEntity, String.class));
        mapValueExpression.put(
                REMOVE_ACTION,
                createValueExpression(
                        PREFIX_REMOVE_PANEL
                                + nameEntity.substring(0, 1).toUpperCase()
                                + nameEntity.substring(1), String.class));

        try {
            StaticAttributesRepository.class.getMethod(PREFIX_GET
                    + nameEntity.substring(0, 1).toUpperCase()
                    + nameEntity.substring(1)
                    + LIST_DROP_DOWN_MENU.substring(0, 1).toUpperCase()
                    + LIST_DROP_DOWN_MENU.substring(1));
            mapValueExpression.put(
                    LIST_DROP_DOWN_MENU,
                    createValueExpression(
                            "#{"
                                    + StaticAttributesRepository.class
                                            .getAnnotation(
                                                    javax.inject.Named.class)
                                            .value()
                                    + "."
                                    + nameEntity
                                    + LIST_DROP_DOWN_MENU.substring(0, 1)
                                            .toUpperCase()
                                    + LIST_DROP_DOWN_MENU.substring(1) + "}",
                            Object.class));
        } catch (NoSuchMethodException e) {
            LOGGER.info(e.getMessage(), e);
        }

        setDefaultAttribute(mapValueExpression, nameEntity);

        mapValueExpression.put(
                OBJECT_LINK_TO_PANEL,
                createValueExpression("#{" + context + PREFIX_CONTROLLER + "."
                        + nameEntity + OPEN_LIST + indexPanel + "]}",
                        Object.class));
        if ("businessConfiguration".equals(context)) {
            mapValueExpression.put(
                    OBJECT_LINK_TO_PANEL,
                    createValueExpression(
                            "#{" + context + PREFIX_CONTROLLER + "."
                                    + PREFIX_GET
                                    + context.substring(0, 1).toUpperCase()
                                    + context.substring(1) + "()." + nameEntity
                                    + OPEN_LIST + indexPanel + "]}",
                            Object.class));
        }
        mapValueExpression.put(
                BINDING_TO_PANEL,
                createValueExpression("#{" + context + PREFIX_VIEWER + "."
                        + nameEntity + "PanelBodyList[" + indexPanel + "]}",
                        Object.class));
        includeCompositeComponent(htmlPanelParent, "panelComponent/" + context,
                FIRST_PANEL, idCC, mapValueExpression);
    }

    @SuppressWarnings("el-syntax")
    public static void setSecondPanel(int indexPanel, int indexPanelParent,
            HtmlPanelGroup htmlPanelParent, String nameEntity,
            String nameEntityParent, String context) {
        Map<String, ValueExpression> mapValueExpression = new HashMap<String, ValueExpression>();
        final String idCC = PREFIX_IDCC
                + nameEntity.substring(0, 1).toUpperCase()
                + nameEntity.substring(1) + indexPanel;
        mapValueExpression.put(IDCC, createValueExpression(idCC, String.class));
        mapValueExpression.put(IDCC_PARENT,
                createValueExpression(htmlPanelParent.getId(), String.class));
        mapValueExpression
                .put(INDEX_PANEL,
                        createValueExpression(String.valueOf(indexPanel),
                                String.class));
        mapValueExpression.put(
                INDEX_PANEL_PARENT,
                createValueExpression(String.valueOf(indexPanelParent),
                        String.class));
        mapValueExpression.put(
                TITLE_PANEL,
                createValueExpression("# " + (indexPanel + 1) + " "
                        + nameEntity, String.class));
        mapValueExpression.put(
                REMOVE_ACTION,
                createValueExpression(
                        PREFIX_REMOVE_PANEL
                                + nameEntity.substring(0, 1).toUpperCase()
                                + nameEntity.substring(1)
                                + nameEntityParent.substring(0, 1)
                                        .toUpperCase()
                                + nameEntityParent.substring(1), String.class));

        try {
            StaticAttributesRepository.class.getMethod(PREFIX_GET
                    + nameEntity.substring(0, 1).toUpperCase()
                    + nameEntity.substring(1)
                    + nameEntityParent.substring(0, 1).toUpperCase()
                    + nameEntityParent.substring(1)
                    + LIST_DROP_DOWN_MENU.substring(0, 1).toUpperCase()
                    + LIST_DROP_DOWN_MENU.substring(1));
            mapValueExpression.put(
                    LIST_DROP_DOWN_MENU,
                    createValueExpression(
                            "#{"
                                    + StaticAttributesRepository.class
                                            .getAnnotation(
                                                    javax.inject.Named.class)
                                            .value()
                                    + "."
                                    + nameEntity
                                    + nameEntityParent.substring(0, 1)
                                            .toUpperCase()
                                    + nameEntityParent.substring(1)
                                    + LIST_DROP_DOWN_MENU.substring(0, 1)
                                            .toUpperCase()
                                    + LIST_DROP_DOWN_MENU.substring(1) + "}",
                            Object.class));
        } catch (NoSuchMethodException e) {
            LOGGER.info(e.getMessage(), e);
        }
        setDefaultAttribute(mapValueExpression, nameEntity);

        mapValueExpression.put(
                OBJECT_LINK_TO_PANEL,
                createValueExpression("#{" + context + PREFIX_CONTROLLER + "."
                        + nameEntityParent + OPEN_LIST + indexPanelParent
                        + "]." + nameEntity + OPEN_LIST + indexPanel + "]}",
                        Object.class));
        if ("businessConfiguration".equals(context)) {
            mapValueExpression.put(
                    OBJECT_LINK_TO_PANEL,
                    createValueExpression(
                            "#{" + context + PREFIX_CONTROLLER + "."
                                    + PREFIX_GET
                                    + context.substring(0, 1).toUpperCase()
                                    + context.substring(1) + "()."
                                    + nameEntityParent + OPEN_LIST
                                    + indexPanelParent + "]." + nameEntity
                                    + OPEN_LIST + indexPanel + "]}",
                            Object.class));
        }

        mapValueExpression.put(CCUtility.BINDING_TO_PANEL, CCUtility
                .createValueExpression("#{" + context + PREFIX_VIEWER + "."
                        + nameEntity
                        + nameEntityParent.substring(0, 1).toUpperCase()
                        + nameEntityParent.substring(1) + "PanelBodyList["
                        + indexPanelParent + "][" + indexPanel + "]}",
                        Object.class));

        includeCompositeComponent(htmlPanelParent, "panelComponent/" + context,
                SECOND_PANEL, idCC, mapValueExpression);
    }

    @SuppressWarnings("el-syntax")
    public static void setThirdPanel(int indexPanel, int indexPanelParent,
            int indexPanelGParent, HtmlPanelGroup htmlPanelParent,
            String nameEntity, String nameEntityParent,
            String nameEntityGParent, String context, String prefixRemBindDrop) {
        Map<String, ValueExpression> mapValueExpression = new HashMap<String, ValueExpression>();
        final String idCC = PREFIX_IDCC
                + nameEntity.substring(0, 1).toUpperCase()
                + nameEntity.substring(1) + indexPanel;
        mapValueExpression.put(IDCC, createValueExpression(idCC, String.class));
        mapValueExpression.put(IDCC_PARENT,
                createValueExpression(htmlPanelParent.getId(), String.class));
        mapValueExpression
                .put(INDEX_PANEL,
                        createValueExpression(String.valueOf(indexPanel),
                                String.class));
        mapValueExpression.put(
                INDEX_PANEL_PARENT,
                createValueExpression(String.valueOf(indexPanelParent),
                        String.class));
        mapValueExpression.put(
                INDEX_PANEL_GPARENT,
                createValueExpression(String.valueOf(indexPanelGParent),
                        String.class));
        mapValueExpression.put(
                TITLE_PANEL,
                createValueExpression("# " + (indexPanel + 1) + " "
                        + nameEntity, String.class));
        mapValueExpression
                .put(REMOVE_ACTION,
                        createValueExpression(
                                PREFIX_REMOVE_PANEL
                                        + nameEntity.substring(0, 1)
                                                .toUpperCase()
                                        + nameEntity.substring(1)
                                        + nameEntityParent.substring(0, 1)
                                                .toUpperCase()
                                        + nameEntityParent.substring(1)
                                        + prefixRemBindDrop.toUpperCase(),
                                String.class));

        try {
            StaticAttributesRepository.class.getMethod(PREFIX_GET
                    + nameEntity.substring(0, 1).toUpperCase()
                    + nameEntity.substring(1)
                    + nameEntityParent.substring(0, 1).toUpperCase()
                    + nameEntityParent.substring(1)
                    + prefixRemBindDrop.toUpperCase()
                    + LIST_DROP_DOWN_MENU.substring(0, 1).toUpperCase()
                    + LIST_DROP_DOWN_MENU.substring(1));
            mapValueExpression.put(
                    LIST_DROP_DOWN_MENU,
                    createValueExpression(
                            "#{"
                                    + StaticAttributesRepository.class
                                            .getAnnotation(
                                                    javax.inject.Named.class)
                                            .value()
                                    + "."
                                    + nameEntity
                                    + nameEntityParent.substring(0, 1)
                                            .toUpperCase()
                                    + nameEntityParent.substring(1)
                                    + prefixRemBindDrop.toUpperCase()
                                    + LIST_DROP_DOWN_MENU.substring(0, 1)
                                            .toUpperCase()
                                    + LIST_DROP_DOWN_MENU.substring(1) + "}",
                            Object.class));
        } catch (NoSuchMethodException e) {
            LOGGER.info(e.getMessage(), e);
        }
        setDefaultAttribute(mapValueExpression, nameEntity);

        mapValueExpression.put(
                OBJECT_LINK_TO_PANEL,
                createValueExpression("#{" + context + PREFIX_CONTROLLER + "."
                        + nameEntityGParent + OPEN_LIST + indexPanelGParent
                        + "]." + nameEntityParent + OPEN_LIST
                        + indexPanelParent + "]." + nameEntity + OPEN_LIST
                        + indexPanel + "]}", Object.class));
        if ("businessConfiguration".equals(context)) {
            mapValueExpression.put(
                    OBJECT_LINK_TO_PANEL,
                    createValueExpression(
                            "#{" + context + PREFIX_CONTROLLER + "."
                                    + PREFIX_GET
                                    + context.substring(0, 1).toUpperCase()
                                    + context.substring(1) + "()."
                                    + nameEntityGParent + OPEN_LIST
                                    + indexPanelGParent + "]."
                                    + nameEntityParent + OPEN_LIST
                                    + indexPanelParent + "]." + nameEntity
                                    + OPEN_LIST + indexPanel + "]}",
                            Object.class));
        }

        mapValueExpression.put(CCUtility.BINDING_TO_PANEL, CCUtility
                .createValueExpression("#{" + context + PREFIX_VIEWER + "."
                        + nameEntity
                        + nameEntityParent.substring(0, 1).toUpperCase()
                        + nameEntityParent.substring(1) + prefixRemBindDrop
                        + "PanelBodyList[" + indexPanelGParent + "]["
                        + indexPanelParent + "][" + indexPanel + "]}",
                        Object.class));

        includeCompositeComponent(htmlPanelParent, "panelComponent/" + context,
                THIRD_PANEL, idCC, mapValueExpression);
    }

    @SuppressWarnings("el-syntax")
    public static void setForthPanel(int indexPanel, int indexPanelParent,
            int indexPanelGParent, int indexPanelGGParent,
            HtmlPanelGroup htmlPanelParent, String nameEntity,
            String nameEntityParent, String nameEntityGParent,
            String nameEntityGGParent, String context, String prefixRemBindDrop) {
        Map<String, ValueExpression> mapValueExpression = new HashMap<String, ValueExpression>();
        final String idCC = PREFIX_IDCC
                + nameEntity.substring(0, 1).toUpperCase()
                + nameEntity.substring(1) + indexPanel;
        mapValueExpression.put(IDCC, createValueExpression(idCC, String.class));
        mapValueExpression.put(IDCC_PARENT,
                createValueExpression(htmlPanelParent.getId(), String.class));
        mapValueExpression
                .put(INDEX_PANEL,
                        createValueExpression(String.valueOf(indexPanel),
                                String.class));
        mapValueExpression.put(
                INDEX_PANEL_PARENT,
                createValueExpression(String.valueOf(indexPanelParent),
                        String.class));
        mapValueExpression.put(
                INDEX_PANEL_GPARENT,
                createValueExpression(String.valueOf(indexPanelGParent),
                        String.class));
        mapValueExpression.put(
                INDEX_PANEL_GGPARENT,
                createValueExpression(String.valueOf(indexPanelGGParent),
                        String.class));
        mapValueExpression.put(
                TITLE_PANEL,
                createValueExpression("# " + (indexPanel + 1) + " "
                        + nameEntity, String.class));
        mapValueExpression
                .put(REMOVE_ACTION,
                        createValueExpression(
                                PREFIX_REMOVE_PANEL
                                        + nameEntity.substring(0, 1)
                                                .toUpperCase()
                                        + nameEntity.substring(1)
                                        + nameEntityParent.substring(0, 1)
                                                .toUpperCase()
                                        + nameEntityParent.substring(1)
                                        + prefixRemBindDrop.toUpperCase(),
                                String.class));

        try {
            StaticAttributesRepository.class.getMethod(PREFIX_GET
                    + nameEntity.substring(0, 1).toUpperCase()
                    + nameEntity.substring(1)
                    + nameEntityParent.substring(0, 1).toUpperCase()
                    + nameEntityParent.substring(1)
                    + prefixRemBindDrop.toUpperCase()
                    + LIST_DROP_DOWN_MENU.substring(0, 1).toUpperCase()
                    + LIST_DROP_DOWN_MENU.substring(1));
            mapValueExpression.put(
                    LIST_DROP_DOWN_MENU,
                    createValueExpression(
                            "#{"
                                    + StaticAttributesRepository.class
                                            .getAnnotation(
                                                    javax.inject.Named.class)
                                            .value()
                                    + "."
                                    + nameEntity
                                    + nameEntityParent.substring(0, 1)
                                            .toUpperCase()
                                    + nameEntityParent.substring(1)
                                    + prefixRemBindDrop.toUpperCase()
                                    + LIST_DROP_DOWN_MENU.substring(0, 1)
                                            .toUpperCase()
                                    + LIST_DROP_DOWN_MENU.substring(1) + "}",
                            Object.class));
        } catch (NoSuchMethodException e) {
            LOGGER.info(e.getMessage(), e);
        }
        setDefaultAttribute(mapValueExpression, nameEntity);

        mapValueExpression.put(
                OBJECT_LINK_TO_PANEL,
                createValueExpression("#{" + context + PREFIX_CONTROLLER + "."
                        + nameEntityGGParent + OPEN_LIST + indexPanelGGParent
                        + "]." + nameEntityGParent + OPEN_LIST
                        + indexPanelGParent + "]." + nameEntityParent
                        + OPEN_LIST + indexPanelParent + "]." + nameEntity
                        + OPEN_LIST + indexPanel + "]}", Object.class));
        if ("businessConfiguration".equals(context)) {
            mapValueExpression.put(
                    OBJECT_LINK_TO_PANEL,
                    createValueExpression(
                            "#{" + context + PREFIX_CONTROLLER + "."
                                    + PREFIX_GET
                                    + context.substring(0, 1).toUpperCase()
                                    + context.substring(1) + "()."
                                    + nameEntityGGParent + OPEN_LIST
                                    + indexPanelGGParent + "]."
                                    + nameEntityGParent + OPEN_LIST
                                    + indexPanelGParent + "]."
                                    + nameEntityParent + OPEN_LIST
                                    + indexPanelParent + "]." + nameEntity
                                    + OPEN_LIST + indexPanel + "]}",
                            Object.class));
        }

        mapValueExpression.put(
                CCUtility.BINDING_TO_PANEL,
                CCUtility.createValueExpression("#{" + context + PREFIX_VIEWER
                        + "." + nameEntity
                        + nameEntityParent.substring(0, 1).toUpperCase()
                        + nameEntityParent.substring(1) + prefixRemBindDrop
                        + "PanelBodyList[" + indexPanelGGParent + "]["
                        + indexPanelGParent + "][" + indexPanelParent + "]["
                        + indexPanel + "]}", Object.class));

        includeCompositeComponent(htmlPanelParent, "panelComponent/" + context,
                FORTH_PANEL, idCC, mapValueExpression);
    }

    public static void includeCompositeComponent(final UIComponent parent,
            final String libraryName, final String resourceName,
            final String id,
            final Map<String, ValueExpression> mapValueExpression) {

        // Prepare.
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        FaceletContext faceletContext = (FaceletContext) context
                .getAttributes().get("javax.faces.FACELET_CONTEXT");

        // This basically creates <ui:component> based on <composite:interface>.
        Resource resource = application.getResourceHandler().createResource(
                resourceName, libraryName);
        UIComponent composite = application.createComponent(context, resource);
        // Mandatory for the case composite is part of
        // UIForm! Otherwise JSF can't find inputs.
        composite.setId(id);

        // This for set the attribute and the action
        for (Map.Entry<String, ValueExpression> entry : mapValueExpression
                .entrySet()) {
            composite.setValueExpression(entry.getKey(), entry.getValue());
        }

        // This basically creates <composite:implementation>.
        UIComponent implementation = application
                .createComponent(UIPanel.COMPONENT_TYPE);
        implementation.setRendererType("javax.faces.Group");
        composite.getFacets().put(UIComponent.COMPOSITE_FACET_NAME,
                implementation);

        // Now include the composite component file in the given parent.
        parent.getChildren().add(composite);
        // This makes #{cc} available.
        parent.pushComponentToEL(context, composite);

        try {
            faceletContext.includeFacelet(implementation, resource.getURL());
        } catch (IOException e) {
            throw new FacesException(e);
        } finally {
            parent.popComponentFromEL(context);
        }
    }

    public static MethodExpression createMethodExpression(
            final String expression, final Class<?> returnType,
            final Class<?>... parameterTypes) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext
                .getApplication()
                .getExpressionFactory()
                .createMethodExpression(facesContext.getELContext(),
                        expression, returnType, parameterTypes);
    }

    public static ValueExpression createValueExpression(
            final String valueExpression, final Class<?> valueType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext
                .getApplication()
                .getExpressionFactory()
                .createValueExpression(facesContext.getELContext(),
                        valueExpression, valueType);
    }

    public static UIComponent getComponent(final String idComponent,
            final HtmlPanelGroup panelBodyDataCenter) {
        for (UIComponent component : panelBodyDataCenter.getChildren()) {
            if (component.getClientId().contains(idComponent)) {
                return component;
            }
        }
        return null;
    }
}
