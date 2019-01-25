package com.tomowang.rundeck.plugins;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty;
import com.dtolabs.rundeck.plugins.descriptions.TextArea;
import com.dtolabs.rundeck.plugins.notification.NotificationPlugin;
import com.taobao.api.ApiException;

import java.util.Map;

@Plugin(service = "Notification", name = "dingtalk")
@PluginDescription(title = "DingTalk", description = "DingTalk robot webhook trigger")
public class DingTalkNotificationPlugin implements NotificationPlugin {
    @PluginProperty(name = "access_token", title = "Access Token", required = true)
    private String access_token;

    @PluginProperty(name = "title", title = "Title")
    private String title;

    @PluginProperty(name = "text", title = "Body", description = "Subset of Markdown")
    @TextArea
    private String text;

    /**
     * Post a notification for the given trigger, dataset, and configuration
     *
     * @param trigger       event type causing notification
     * @param executionData execution data
     * @param config        notification configuration
     * @return true if successul
     */
    public boolean postNotification(String trigger, Map executionData, Map config) {
        String url = "https://oapi.dingtalk.com/robot/send?access_token=" + access_token;
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(text);
        request.setMarkdown(markdown);
        try {
            OapiRobotSendResponse response = client.execute(request);
            if (!response.isSuccess()) {
                return false;
            }
        } catch (ApiException e) {
            return false;
        }
        return true;
    }
}