package cn.yb.datawaiter.jdbc.model;



import cn.yb.datawaiter.exception.GlobRuntimeException;

import java.util.ArrayList;
import java.util.List;

public abstract class ParamsTool {

    private  String sql;
    private  char[] symbols;
    public final static  char SMBOL_EQ = '?';
    public final static  char  SMBOL_LIKE = '#';
    public  ParamsTool(String sql){
        this.sql = sql;
        this.symbols =new char[0];
    }

    public  ParamsTool(String sql,char[] symbols){
        this.sql = sql;
        this.symbols = symbols != null ? symbols:new char[0];
    }

    public  Object[] getParams(){
        List<Object> params  = new ArrayList<>();

        for(char ch :sql.toCharArray()){
            switch (ch){
                case SMBOL_EQ:
                    params.add(getEqParam(params.size()));
                    break;
                case SMBOL_LIKE:
                    params.add(getLikeParam(params.size()));
                    break;
                default:
                    for (int i = 0; i < this.symbols.length; i++) {
                        char symbol =  this.symbols[i];
                        if(ch == symbol){
                            Object parma = getCustomParam(params.size(),i,symbol);
                            if(parma == null){
                                throw  new GlobRuntimeException("自定义参数必须要有返回值");
                            }
                            params.add(parma);
                        }
                    }
                    break;
            }

        }
        return  params.toArray();
    }
    public  String  getResultSQL(){
        List<Character> list = new ArrayList<>();
        sql = sql.replaceAll(SMBOL_LIKE+"","?");
        for (char symbol : symbols) {
            sql = sql.replaceAll(symbol+"","?");
        }
        return  sql;
    }

    /**
     * ? 情况
     * @param index
     * @return
     */
    protected abstract Object getEqParam(int index);

    /**
     * # 情况
     * @param index
     * @return
     */
    protected abstract String getLikeParam(int index);

    /**
     * 自定义参数
     * @param index
     * @param symbolIndex
     * @param symbol
     * @return
     */
    protected abstract Object getCustomParam(int index, int symbolIndex, char symbol);

}
