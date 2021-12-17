package cn.yb.datawaiter.model.entity;

public enum MapperCreateEnum {
    SELECT("SELECT","查询"),
    EDIT("EDIT","编辑");

    public String id;
    public String label;
    // 构造方法
    private MapperCreateEnum(String id, String label) {
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
