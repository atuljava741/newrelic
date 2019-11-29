package com.newrelic;

import android.content.Context;

import com.newrelic.utils.Utils;

import java.io.IOException;
import java.util.Map;

public class NRSdk {

    // logger
    private static final NRLog logger = NRLog.getLogger(NRSdk.class.getSimpleName());

    // holder  instance
    private static NRInstance nrInstance;

    private NRSdk() {

    }

    public static void init(final Context context, final String newRelicToken) {
        // config
        init(context, new NRConfig(newRelicToken));
    }

    public static void init(final Context context, final NRConfig config) {
        try {
            nrInstance = NRInstance.getInstance(context, config);
        } catch (Exception e) {
            logger.error("error in init()", e.toString());
            nrInstance = null;
        }
    }

    private static boolean isSdkInitialized() {
        if (null == nrInstance) {
            logger.error("NRSdk not initialized. You must call NRSdk.init() ");
            return false;
        }

        return true;
    }

    /**
     * create a New Relic-monitored interaction / Start an interaction trace /Track a method as an interaction
     *
     * @param actionName The name you want to give to the interaction
     * @return interaction ID number which can be used for ending the interaction at a certain point
     */
    public static String startInteraction(String actionName) {
        if (!isSdkInitialized()) {
            return null;
        }

        if (Utils.isEmptyOrNull(actionName)) {
            logger.error("error in startInteraction()", "actionName is empty OR null");
            return null;
        }

        try {
            return nrInstance.startInteraction(actionName);
        } catch (Exception e) {
            logger.error("error in startInteraction()", e.toString());
            return null;
        }
    }

    /**
     * Ends a custom interaction.
     * This call has no effect if the interaction has already ended.
     *
     * @param interactionID ID for the interaction you want to end
     */
    public static void endInteraction(String interactionID) {
        if (!isSdkInitialized()) {
            return;
        }

        if (Utils.isEmptyOrNull(interactionID)) {
            logger.error("error in endInteraction()", "interactionID is empty OR null");
            return;
        }

        try {
            nrInstance.endInteraction(interactionID);
        } catch (Exception e) {
            logger.error("error in endInteraction()", e.toString());
        }
    }

    /**
     * Set a new name for an interaction that is already being tracked by New Relic
     *
     * @param interactionName name you want to give to the interaction
     *                        <p>
     *                        For example, you have an interaction that is being reported under a single activity name, like FragmentActivity,
     *                        or under an obfuscated name, like baseclass.a, and you want to rename the interaction to be more descriptive,.
     *                        You could use setInteractionName at the beginning of each onCreate() method to change the name.
     */
    public static void setInteractionName(String interactionName) {
        if (!isSdkInitialized()) {
            return;
        }

        if (Utils.isEmptyOrNull(interactionName)) {
            logger.error("error in setInteractionName()", "interactionName is empty OR null");
            return;
        }

        try {
            nrInstance.setInteractionName(interactionName);
        } catch (Exception e) {
            logger.error("error in setInteractionName()", e.toString());
        }
    }

    /**
     * Records a custom New Relic Mobile event
     *
     * @param eventType       Required. The type of event.
     * @param eventName       Optional. Use this parameter to name the event.
     * @param eventAttributes Optional. A map that includes a list of attributes
     * @return true if the event is recorded successfully, or false if not.
     */
    public static boolean recordCustomEvent(String eventType, String eventName, Map<String, Object> eventAttributes) {
        if (!isSdkInitialized()) {
            return false;
        }

        if (Utils.isEmptyOrNull(eventType)) {
            logger.error("error in recordCustomEvent()", "eventType is empty OR null");
            return false;
        }

        try {
            return nrInstance.recordCustomEvent(eventType, eventName, eventAttributes);
        } catch (Exception e) {
            logger.error("error in recordCustomEvent()", e.toString());
            return false;
        }
    }

    /**
     * Records a MobileBreadcrumb event, useful for crash analysis.
     *
     * @param eventName       Required. The name you want to give to the breadcrumb event.
     * @param eventAttributes Optional. A map that includes a list of attributes of the breadcrumb event.
     * @return true if the event is recorded successfully, or false if not.
     */
    public static boolean recordBreadcrumb(String eventName, Map<String, Object> eventAttributes) {
        if (!isSdkInitialized()) {
            return false;
        }

        if (Utils.isEmptyOrNull(eventName)) {
            logger.error("error in recordBreadcrumb()", "eventName is empty OR null");
            return false;
        }

        try {
            return nrInstance.recordBreadcrumb(eventName, eventAttributes)
                    ;
        } catch (Exception e) {
            logger.error("error in recordBreadcrumb()", e.toString());
            return false;
        }
    }

    /**
     * Throws a demo run-time exception named java.lang.RuntimeException to test New Relic crash reporting.
     */
    public static void crashNow() {
        if (!isSdkInitialized()) {
            return;
        }

        try {
            nrInstance.crashNow();
        } catch (Exception e) {
            logger.error("error in crashNow()", e.toString());
        }
    }

    /**
     * Throws a demo run-time exception named java.lang.RuntimeException to test New Relic crash reporting.
     *
     * @param message Optional. A message attached to the exception.
     */
    public static void crashNow(String message) {
        if (!isSdkInitialized()) {
            return;
        }

        if (Utils.isEmptyOrNull(message)) {
            logger.error("error in crashNow()", "message is empty OR null");
            return;
        }


        try {
            nrInstance.crashNow(message);
        } catch (Exception e) {
            logger.error("error in crashNow()", e.toString());
        }
    }

    /**
     * @return Returns ID for current session
     */
    public static String currentSessionId() {
        if (!isSdkInitialized()) {
            return null;
        }

        try {
            return nrInstance.currentSessionId();
        } catch (Exception e) {
            logger.error("error in currentSessionId()", e.toString());
            return null;
        }
    }

    /**
     * Tracks networks requests.
     *
     * @param url           Required. The URL of the request.
     * @param httpMethod    Required. The HTTP method used, such as GET or POST.
     * @param statusCode    Required. The statusCode of the HTTP response, such as 200 for OK.
     * @param startTime     Required. The start time of the request in milliseconds since the epoch.
     * @param endTime       Required. The end time of the request in milliseconds since the epoch.
     * @param bytesSent     Required. The number of bytes sent in the request.
     * @param bytesReceived Required. The number of bytes received in the response.
     * @param responseBody  Optional. The response body of the HTTP response. The response body will be truncated and included in an HTTP Error metric if the HTTP transaction is an error.
     */
    public static void noticeHttpTransaction(String url, String httpMethod, int statusCode, long startTime, long endTime, long bytesSent, long bytesReceived, String responseBody) {
        if (!isSdkInitialized()) {
            return;
        }

        if (Utils.isEmptyOrNull(url)) {
            logger.error("error in noticeHttpTransaction()", "url is empty OR null");
            return;
        }

        if (Utils.isEmptyOrNull(httpMethod)) {
            logger.error("error in noticeHttpTransaction()", "httpMethod must not be empty or null");
            return;
        }

        try {
            nrInstance.noticeHttpTransaction(url, httpMethod, statusCode, startTime, endTime, bytesSent, bytesReceived, responseBody);
        } catch (Exception e) {
            logger.error("error in noticeHttpTransaction()", e.toString());
        }

        /*Example
        public class CustomHttpMetricsLogger implements Interceptor {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //collect request start time
                long t1 = System.nanoTime();
                //get the size of the request body
                long requestSize = null == request.body() ? 0 : request.body().contentLength();
                //proceed with the request
                Response response = chain.proceed(request);
                //capture the time when response returns
                long t2 = System.nanoTime();
                long responseSize = null == response.body() ? 0 : response.body().contentLength();
                //tell new relic to notice this request
                NewRelic.noticeHttpTransaction(request.urlString(), request.method(), response.code(), t1, t2, requestSize, responseSize);
                //return response for processing
                return response;
            }
        }*/

    }

    public static boolean recordHandledException(Exception exceptionToHandle) {
        return recordHandledException(exceptionToHandle, null);
    }

    /**
     * Records a handled exception.
     *
     * @param exceptionToHandle   Required. The exception object that was thrown.
     * @param exceptionAttributes Optional. Map of attributes that give context.
     * @return true if the handled exception was recorded successfully, or false if not.
     */
    public static boolean recordHandledException(Exception exceptionToHandle, Map<String, Object> exceptionAttributes) {

        if (!isSdkInitialized()) {
            return false;
        }

        if (null == exceptionToHandle) {
            logger.error("error in recordHandledException()", "exceptionToHandle is null");
            return false;
        }

        try {
            return nrInstance.recordHandledException(exceptionToHandle, exceptionAttributes);
        } catch (Exception e) {
            logger.error("error in recordHandledException()", e.toString());
            return false;
        }

    }

    /**
     * Sets the event harvest cycle length.
     *
     * @param maxBufferTimeInSec Required. The maximum time (in seconds) that the agent should store events in memory. The default value harvest cycle length is 600 seconds.
     */
    public static void setMaxEventBufferTime(int maxBufferTimeInSec) {
        if (!isSdkInitialized()) {
            return;
        }

        if (maxBufferTimeInSec < 60 || maxBufferTimeInSec > 600) {
            logger.error("setMaxEventBufferTime()", "maxEventBufferTime value should be minimum 60 seconds OR maximum 600 seconds");
            return;
        }

        try {
            nrInstance.setMaxEventBufferTime(maxBufferTimeInSec);
        } catch (Exception e) {
            logger.error("error in setMaxEventBufferTime()", e.toString());
        }
    }

    /**
     * Sets the maximum size of the event pool.
     * By default, New Relic for Mobile collects a maximum of 1,000 events per harvest cycle (cycles are ten minutes long by default).
     *
     * @param maxEventPoolSize Required. Maximum size of event pool.
     */
    public static void setMaxEventPoolSize(int maxEventPoolSize) {
        if (!isSdkInitialized()) {
            return;
        }

        if (maxEventPoolSize <= 0 || maxEventPoolSize > 1000) {
            logger.error("setMaxEventPoolSize()", "maxEventPoolSize value should be minimum 1 OR maximum 1000");
            return;
        }

        try {
            nrInstance.setMaxEventPoolSize(maxEventPoolSize);
        } catch (Exception e) {
            logger.error("error in setMaxEventPoolSize()", e.toString());
        }
    }

    /**
     * Set a custom user identifier value to associate user sessions with analytics events and attributes.
     *
     * @param userId Required. Sets user ID.
     * @return true if it succeeds, or false if it doesn't.
     */
    public static boolean setUserId(String userId) {
        if (!isSdkInitialized()) {
            return false;
        }

        if (Utils.isEmptyOrNull(userId)) {
            logger.error("error in setUserId()", "userId is empty OR null");
            return false;
        }

        try {
            return nrInstance.setUserId(userId);
        } catch (Exception e) {
            logger.error("error in setUserId()", e.toString());
            return false;
        }
    }


    /**
     * Records network failures.
     *
     * @param url              Required. The URL of the request.
     * @param httpMethod       Required. The HTTP method used, such as GET or POST.
     * @param startTime        Required. The start time of the request in milliseconds since the epoch.
     * @param endTime          Required. The end time of the request in milliseconds since the epoch.
     * @param exceptionFailure exception that occurred
     */
    public static void noticeNetworkFailure(String url, String httpMethod, long startTime, long endTime, Exception exceptionFailure) {
        if (!isSdkInitialized()) {
            return;
        }

        if (Utils.isEmptyOrNull(url)) {
            logger.error("error in noticeNetworkFailure()", "url is empty OR null");
            return;
        }

        if (Utils.isEmptyOrNull(httpMethod)) {
            logger.error("error in noticeNetworkFailure()", "httpMethod is empty OR null");
            return;
        }

        if (null == exceptionFailure) {
            logger.error("exceptionFailure is null");
            return;
        }

        try {
            nrInstance.noticeNetworkFailure(url, httpMethod, startTime, endTime, exceptionFailure);
        } catch (Exception e) {
            logger.error("exception in noticeNetworkFailure()", e.toString());
        }
    }
}
