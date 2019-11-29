package com.newrelic;

import android.app.Application;
import android.content.Context;

import com.newrelic.agent.android.metric.MetricUnit;
import com.newrelic.utils.Utils;
import com.newrelic.agent.android.NewRelic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NRInstance {

    // logger
    private static final NRLog logger = NRLog.getLogger(NRInstance.class.getSimpleName());

    public static NRInstance instance;

    /* Application context */
    private final Context context;

    /* config */
    private NRConfig config;

    private boolean initialized = false;


    /**
     * Gets static instance of BNR Instance, creates if needed.
     *
     * @param context Context
     * @param config  NRConfig
     * @return NRInstance
     */
    public static NRInstance getInstance(Context context, NRConfig config)  {

        synchronized (NRInstance.class) {
            if (null == instance) {
                instance = new NRInstance(context, config);
            }
        }

        // Override config
        instance.config = config;

        return instance;
    }

    static NRInstance getInstance() {
        return instance;
    }

    private NRInstance(Context context, NRConfig config)  {

        Context ctx = context.getApplicationContext(); // Always grab Application Context only
        // Ensure you got Application context
        if (!(ctx instanceof Application)) {
            throw new IllegalStateException("Context failed to cast to ApplicationContext");
        }

        this.config = config;
        this.context = context;
        init(this);
    }

    private void init(NRInstance self) {
        // Check if already initialized
        if (isInitialized()) {
            logger.error("NRSdk is already initialized, please don't call init() again.");
            return;
        }

        // go about your business set things up!
        try {

            NewRelic.withApplicationToken(self.config.newRelicToken)
                    .withDefaultInteractions(self.config.isDefaultInteractions())
                    .withCrashReportingEnabled(self.config.isCrashReportingEnabled())
                    .withLoggingEnabled(self.config.isLoggingEnabled())
                    .withAnalyticsEvents(self.config.isAnalyticsEvents())
                    .withInteractionTracing(self.config.isInteractionTracking())
                    .start(this.context);

            initialized = true;

            logger.info("NRSdk initialized");

        } catch (Exception e) {
            logger.error("error in init()", e.toString());
        }
    }

    private boolean isInitialized() {
        return initialized;
    }

    /**
     * create a New Relic-monitored interaction / Start an interaction trace /Track a method as an interaction
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/start-interaction
     */
    String startInteraction(String actionName) {
        return NewRelic.startInteraction(actionName);  // NewRelic.startInteraction("RefreshContacts");
    }

    /**
     * Ends a custom interaction.
     * This call has no effect if the interaction has already ended.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/end-interaction
     */
    void endInteraction(String interactionID) {
        NewRelic.endInteraction(interactionID);
    }

    /**
     * Set a new name for an interaction that is already being tracked by New Relic
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/set-interaction-name
     *
     * @param interactionName name you want to give to the interaction
     *                        <p>
     *                        For example, you have an interaction that is being reported under a single activity name, like FragmentActivity,
     *                        or under an obfuscated name, like baseclass.a, and you want to rename the interaction to be more descriptive,.
     *                        You could use setInteractionName at the beginning of each onCreate() method to change the name.
     */
    void setInteractionName(String interactionName) {
        //Rename the in-flight interaction
        /*NewRelic.setInteractionName("Display MyCustomInteraction");*/
        NewRelic.setInteractionName(interactionName);
    }

    /**
     * Records a MobileBreadcrumb event, useful for crash analysis.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/recordbreadcrumb
     */
    boolean recordBreadcrumb(String eventName, Map<String, Object> eventAttributes) {

        return NewRelic.recordBreadcrumb(eventName, eventAttributes);
    }

    /**
     * Records a custom New Relic Mobile event
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/recordcustomevent-android-sdk-api
     */
    boolean recordCustomEvent(String eventType, String eventName, Map<String, Object> eventAttributes) {
        return NewRelic.recordCustomEvent(eventType, eventName, eventAttributes);
    }

    /**
     * Throws a demo run-time exception named java.lang.RuntimeException to test New Relic crash reporting.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/crashnow-android-sdk-api
     */
    void crashNow() {
        NewRelic.crashNow();
    }

    /**
     * Throws a demo run-time exception named java.lang.RuntimeException to test New Relic crash reporting.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/crashnow-android-sdk-api
     */
    void crashNow(String message) {
        NewRelic.crashNow(message);
    }

    /**
     * ref -https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/currentsessionid-android-sdk-api
     */
    String currentSessionId() {
        return NewRelic.currentSessionId();
    }

    /**
     * Tracks networks requests.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/notice-http-transaction
     */
    void noticeHttpTransaction(String url, String httpMethod, int statusCode, long startTime, long endTime, long bytesSent, long bytesReceived, String responseBody) {
        if (Utils.isEmptyOrNull(responseBody)) {
            NewRelic.noticeHttpTransaction(url, httpMethod, statusCode, startTime, endTime, bytesSent, bytesReceived);
        } else {
            NewRelic.noticeHttpTransaction(url, httpMethod, statusCode, startTime, endTime, bytesSent, bytesReceived, responseBody);
        }
    }

    /**
     * Records a handled exception.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/recordhandledexception-android-sdk-api
     */
    boolean recordHandledException(Exception exceptionToHandle, Map<String, Object> exceptionAttributes) {
        if (null == exceptionAttributes) {
            exceptionAttributes = new HashMap();
        }
        return NewRelic.recordHandledException(exceptionToHandle, exceptionAttributes);
    }

    /**
     * Sets the event harvest cycle length.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/set-max-event-buffer-time
     */
    void setMaxEventBufferTime(int maxBufferTimeInSec) {
        NewRelic.setMaxEventBufferTime(maxBufferTimeInSec);
    }

    /**
     * Sets the maximum size of the event pool.
     * By default, New Relic for Mobile collects a maximum of 1,000 events per harvest cycle (cycles are ten minutes long by default).
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/set-max-event-pool-size
     */
    void setMaxEventPoolSize(int maxSize) {
        NewRelic.setMaxEventPoolSize(maxSize);
    }

    /**
     * Set a custom user identifier value to associate user sessions with analytics events and attributes.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/set-user-id
     */
    boolean setUserId(String userId) {
        return NewRelic.setUserId(userId); //NewRelic.setUserId("SampleUserName");
    }

    /**
     * Records network failures.
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/notice-network-failure
     */
    void noticeNetworkFailure(String url, String httpMethod, long startTime, long endTime, Exception exception) {
        NewRelic.noticeNetworkFailure(url, httpMethod, startTime, endTime, exception);
    }

    /**
     * Record custom metrics (arbitrary numerical data).
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/recordmetric-android-sdk-api
     */
    void recordMetric(String name, String category) {
        NewRelic.recordMetric(name, category);
    }

    /**
     * Record custom metrics (arbitrary numerical data).
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/recordmetric-android-sdk-api
     */
    void recordMetric(String name, String category, double value) {
        NewRelic.recordMetric(name, category, value);
    }

    /**
     * Record custom metrics (arbitrary numerical data).
     * ref - https://docs.newrelic.com/docs/mobile-monitoring/new-relic-mobile-android/android-sdk-api/recordmetric-android-sdk-api
     */
    void recordMetric(String name, String category, int count, double totalValue, double exclusiveValue) {
        NewRelic.recordMetric(name, category, count, totalValue, exclusiveValue);
    }

    void recordMetric(String name, String category, int $count, double totalValue, double exclusiveValue, MetricUnit countUnit, MetricUnit valueUnit) {

    }

}
