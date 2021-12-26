package cn.yb.datawaiter.jdbc.model;

public enum CRUDEnum {

    SELECT("SELECT","查询"),
    INSERT("INSERT","增加"),
    DELETE("DELETE","删除" ),
    UPDATE("UPDATE","修改"),
    EDIT("EDIT","编辑");

    public String id;
    public String label;
    // 构造方法
    private CRUDEnum(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
