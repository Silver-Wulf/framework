/*
@VaadinApache2LicenseForJavaFiles@
 */
package com.vaadin.terminal.gwt.client.ui;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;

public abstract class VMediaBasePaintable extends VAbstractPaintableWidget {

    public static final String TAG_SOURCE = "src";

    public static final String ATTR_PAUSE = "pause";
    public static final String ATTR_PLAY = "play";
    public static final String ATTR_MUTED = "muted";
    public static final String ATTR_CONTROLS = "ctrl";
    public static final String ATTR_AUTOPLAY = "auto";
    public static final String ATTR_RESOURCE = "res";
    public static final String ATTR_RESOURCE_TYPE = "type";
    public static final String ATTR_HTML = "html";
    public static final String ATTR_ALT_TEXT = "alt";

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        getWidgetForPaintable().setControls(shouldShowControls(uidl));
        getWidgetForPaintable().setAutoplay(shouldAutoplay(uidl));
        getWidgetForPaintable().setMuted(isMediaMuted(uidl));

        // Add all sources
        for (int ix = 0; ix < uidl.getChildCount(); ix++) {
            UIDL child = uidl.getChildUIDL(ix);
            if (TAG_SOURCE.equals(child.getTag())) {
                getWidgetForPaintable().addSource(getSourceUrl(child),
                        getSourceType(child));
            }
        }
        setAltText(uidl);

        evalPauseCommand(uidl);
        evalPlayCommand(uidl);
    }

    protected boolean shouldShowControls(UIDL uidl) {
        return uidl.getBooleanAttribute(ATTR_CONTROLS);
    }

    private boolean shouldAutoplay(UIDL uidl) {
        return uidl.getBooleanAttribute(ATTR_AUTOPLAY);
    }

    private boolean isMediaMuted(UIDL uidl) {
        return uidl.getBooleanAttribute(ATTR_MUTED);
    }

    private boolean allowHtmlContent(UIDL uidl) {
        return uidl.getBooleanAttribute(ATTR_HTML);
    }

    private void evalPlayCommand(UIDL uidl) {
        if (uidl.hasAttribute(ATTR_PLAY)) {
            getWidgetForPaintable().play();
        }
    }

    private void evalPauseCommand(UIDL uidl) {
        if (uidl.hasAttribute(ATTR_PAUSE)) {
            getWidgetForPaintable().pause();
        }
    }

    @Override
    public VMediaBase getWidgetForPaintable() {
        return (VMediaBase) super.getWidgetForPaintable();
    }

    /**
     * @param uidl
     * @return the URL of a resource to be used as a source for the media
     */
    private String getSourceUrl(UIDL uidl) {
        String url = getConnection().translateVaadinUri(
                uidl.getStringAttribute(VMediaBasePaintable.ATTR_RESOURCE));
        if (url == null) {
            return "";
        }
        return url;
    }

    /**
     * @param uidl
     * @return the mime type of the media
     */
    private String getSourceType(UIDL uidl) {
        return uidl.getStringAttribute(VMediaBasePaintable.ATTR_RESOURCE_TYPE);
    }

    private void setAltText(UIDL uidl) {
        String alt = uidl.getStringAttribute(VMediaBasePaintable.ATTR_ALT_TEXT);

        if (alt == null || "".equals(alt)) {
            alt = getWidgetForPaintable().getDefaultAltHtml();
        } else if (!allowHtmlContent(uidl)) {
            alt = Util.escapeHTML(alt);
        }
        getWidgetForPaintable().setAltText(alt);
    }

}
