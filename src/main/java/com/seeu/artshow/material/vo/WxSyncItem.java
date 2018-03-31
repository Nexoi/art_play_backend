package com.seeu.artshow.material.vo;

public class WxSyncItem {

    public enum TYPE{
        ADD,    // 增添到公众号
        UPDATE, // 更新到公众号
    }
    public enum STATUS{
        ING,
        FINISH,
    }
    private Long itemId;
    private String itemName;
    // 机制 1 使用
    private TYPE type;
    // 机制 2 使用
    private STATUS status;
    private String message; // 正确／错误信息都在这里面，string

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
