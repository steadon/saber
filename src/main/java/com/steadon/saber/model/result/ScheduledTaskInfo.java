package com.steadon.saber.model.result;

import com.steadon.saber.model.dao.NotificationRule;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ScheduledTaskInfo {
    private Integer id;
    private String ruleCode;
    private Channel channel;
    private Integer groupId;
    private String goal;
    private Long startTime;
    private Long endTime;
    private Long taskTime;
    private Integer type;
    private String createBy;
    private Long createTime;

    @Data
    @AllArgsConstructor
    public static class Channel {
        private Boolean feishu;
        private Boolean sms;
        private Boolean email;

        public Channel(NotificationRule notificationRule) {
            this.feishu = notificationRule.getFeishuStatus();
            this.sms = notificationRule.getSmsStatus();
            this.email = notificationRule.getEmailStatus();
        }
    }
}
