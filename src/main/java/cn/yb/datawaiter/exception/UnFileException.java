package cn.yb.datawaiter.exception;

public class UnFileException extends Exception {



    // 提供无参数的构造方法
    public UnFileException() {
    }

    // 提供一个有参数的构造方法，可自动生成
    public UnFileException(String msg) {
        super(msg);// 把参数传递给Throwable的带String参数的构造方法
    }

}