package cn.yb.datawaiter.model;

/**
 * 对象之间关系
 */
public enum PoRelation {

    no ("no","无"),
    association ("association","一对一"),
    collection ("collection","一对多" );

    public String label;
    public String id;
    // 构造方法
    private PoRelation(String id, String label) {
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
