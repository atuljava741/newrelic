package com.newrelic;

import com.newrelic.utils.Utils;

public class NRConfig {

    public final String newRelicToken;

    private boolean defaultInteractions = true;
    private boolean analyticsEvents = true;
    private boolean crashReportingEnabled = true;
    private boolean interactionTracking = true;
    private boolean loggingEnabled = true;

    public NRConfig(String newRelicToken) {
        if (Utils.isEmptyOrNull(newRelicToken))
            throw new IllegalArgumentException("New Relic Token can not be null or empty");

        this.newRelicToken = newRelicToken;
    }

    /**
     * Enable or disable agent logging.
     * @param isEnable for enable/disable
     * @return NRConfig
     */
    public NRConfig withLoggingEnabled(boolean isEnable) {
        this.loggingEnabled = isEnable;
        return this;
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public NRConfig withCrashReporting(boolean isEnable) {
        this.crashReportingEnabled = isEnable;
        return this;
    }

    public boolean isCrashReportingEnabled() {
        return this.crashReportingEnabled;
    }

    public NRConfig withDefaultInteractions(boolean isEnable) {
        this.defaultInteractions = isEnable;
        return this;
    }

    public boolean isDefaultInteractions() {
        return this.defaultInteractions;
    }

    /**
     * Enable or disable collection of event data.
     * @param isEnable for enable/disable
     * @return NRConfig
     */
    public NRConfig withAnalyticsEvents(boolean isEnable) {
        this.analyticsEvents = isEnable;
        return this;
    }

    public boolean isAnalyticsEvents() {
        return this.analyticsEvents;
    }

    public NRConfig withInteractionTracking(boolean isEnable) {
        this.interactionTracking = isEnable;
        return this;
    }

    public boolean isInteractionTracking() {
        return this.interactionTracking;
    }
}
