package com.winshare.demo.rest.wx;

public enum EventTypeEnums {
    ADD_EXTERNAL_CONTACT("change_external_contact","add_external_contact","添加企业客户事件"),
    EDIT_EXTERNAL_CONTACT("change_external_contact","edit_external_contact","编辑企业客户事件"),
    DEL_EXTERNAL_CONTACT("change_external_contact","del_external_contact","删除企业客户事件");
    private String eventType;
    private String eventName;
    private String desc;
    EventTypeEnums(String eventType,String eventName, String desc){
        this.eventType = eventType;
        this.eventName = eventName;
        this.desc  = desc;
    }


    public String getEventType() {
        return eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDesc() {
        return desc;
    }
}
