package cn.sancell.xingqiu.homeuser.afterSale.address;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ApplyMultipleEntity<T> implements MultiItemEntity {

    public static final int HEADER = 1;
    public static final int CONTENT = 2;
    public static final int NOT_PS = 3;//不可以配送
    public static final int NOT_PS_TITLE = 4;//不可以配送title
    public int itemType;
    public T data;

    public ApplyMultipleEntity(int type, T data) {
        itemType = type;
        this.data = data;
    }

    public ApplyMultipleEntity(T data) {
        itemType = CONTENT;
        this.data = data;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
