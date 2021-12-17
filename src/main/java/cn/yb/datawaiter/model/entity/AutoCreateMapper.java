package cn.yb.datawaiter.model.entity;

import java.util.stream.Stream;

/**
 * 自动创建Mapper
 */
public enum  AutoCreateMapper {

    SelectById ("SelectById","根据ID查找"),
    SelectAll ("SelectAll","查找所有"),
    SelectCount ("SelectCount","查找个数"),
    SelectPagination ("SelectPagination","分页查找" ),
    DeleteByPo ("DeleteByPo","根据对象删除" ),
    SavePo ("SavePo","保存对象" ),
    UpdatePo("UpdatePo","修改对象");

    public String label;
    public String id;
    // 构造方法
     AutoCreateMapper(String id, String label) {
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

    public static AutoCreateMapper fromString(String actionString) {
        return Stream.of(AutoCreateMapper.values())
                .filter(action -> actionString.equalsIgnoreCase(action.name()))
                .findFirst()
                .orElse(null);
    }
}
