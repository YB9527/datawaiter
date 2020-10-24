package cn.yb.datawaiter.exception;

public class CommonException extends Exception {

    private static final long serialVersionUID = 1L;

    // 提供无参数的构造方法
    public CommonException() {
    }

    // 提供一个有参数的构造方法，可自动生成
    public CommonException(String msg) {
        super(msg);// 把参数传递给Throwable的带String参数的构造方法
    }

}