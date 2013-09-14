package nu.wasis.stunden.plugins;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import nu.wasis.stunden.exception.InvalidConfigurationException;
import nu.wasis.stunden.model.Day;
import nu.wasis.stunden.model.Entry;
import nu.wasis.stunden.model.WorkPeriod;
import nu.wasis.stunden.plugin.ProcessPlugin;
import nu.wasis.stunden.plugins.removebreaks.config.StundenRemoveBreaksPluginConfiguration;

@PluginImplementation
public class StundenRemoveBreaksPlugin implements ProcessPlugin {

	private static final Logger LOG = Logger.getLogger(StundenRemoveBreaksPlugin.class);
	
	@Override
	public WorkPeriod process(WorkPeriod workPeriod, Object configuration) {
		LOG.info("Removing breaks...");
		if (null == configuration || !(configuration instanceof StundenRemoveBreaksPluginConfiguration)) {
			throw new InvalidConfigurationException("Configuration null or wrong type. You probably need to fix your configuration file.");
		}
		final StundenRemoveBreaksPluginConfiguration myConfig = (StundenRemoveBreaksPluginConfiguration) configuration;
		final List<String> breakNames = myConfig.getBreakNames();
		final WorkPeriod clearedWorkPeriod = new WorkPeriod();
		for (final Day oldDay : workPeriod.getDays()) {
			final List<Entry> clearedEntries = new LinkedList<>();
			for (final Entry entry : oldDay.getEntries()) {
				if (!breakNames.contains(entry.getProject().getName())) {
					clearedEntries.add(entry);
				} else {
					LOG.debug("Removing entry: " + entry);
				}
			}
			clearedWorkPeriod.getDays().add(new Day(oldDay.getDate(), clearedEntries));
		}
		
		LOG.info("...done");
		return clearedWorkPeriod;
	}

	@Override
	public Class<?> getConfigurationClass() {
		return StundenRemoveBreaksPluginConfiguration.class;
	}

}
