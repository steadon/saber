package com.steadon.saber.model.param;

import lombok.Data;

@Data
public class SearchScheduledTaskParam {
    private Channel channel;
    private Integer groupId;
    private Integer taskType;
    private Boolean isCreate;
    private Integer sortType;

    @Data
    public static class Channel {
        private Boolean feishu;
        private Boolean sms;
        private Boolean email;
    }
}