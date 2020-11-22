package cn.yb.datawaiter.crawler;

public interface RequestCallback {
    public <T> void call(T t);
}
