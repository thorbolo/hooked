package org.wickedsource.hooked.plugins.filedata.params;

import java.util.List;

/**
 * @author Tom Hombergs <tom.hombergs@gmail.com>
 */
public interface ParameterizedPlugin {

    public List<? extends PluginParameter> getParameters();

}