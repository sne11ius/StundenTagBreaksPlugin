package nu.wasis.stunden.plugins;

import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import nu.wasis.stunden.exception.InvalidConfigurationException;
import nu.wasis.stunden.model.Day;
import nu.wasis.stunden.model.Entry;
import nu.wasis.stunden.model.WorkPeriod;
import nu.wasis.stunden.plugin.ProcessPlugin;
import nu.wasis.stunden.plugins.tagbreaks.config.StundenTagBreaksPluginConfiguration;

import org.apache.log4j.Logger;

@PluginImplementation
public class StundenTagBreaksPlugin implements ProcessPlugin {

	private static final Logger LOG = Logger.getLogger(StundenTagBreaksPlugin.class);
	
	@Override
	public WorkPeriod process(WorkPeriod workPeriod, Object configuration) {
		LOG.info("Tagging breaks...");
		if (null == configuration || !(configuration instanceof StundenTagBreaksPluginConfiguration)) {
			throw new InvalidConfigurationException("Configuration null or wrong type. You probably need to fix your configuration file.");
		}
		final StundenTagBreaksPluginConfiguration myConfig = (StundenTagBreaksPluginConfiguration) configuration;
		final List<String> breakNames = myConfig.getBreakNames();
		for (final Day day : workPeriod.getDays()) {
			for (final Entry entry : day.getEntries()) {
				if (breakNames.contains(entry.getProject().getName())) {
					LOG.debug("Tagging entry: " + entry);
					entry.setBreak(true);
				}
			}
		}
		
		LOG.info("...done");
		return workPeriod;
	}

	@Override
	public Class<?> getConfigurationClass() {
		return StundenTagBreaksPluginConfiguration.class;
	}

}
