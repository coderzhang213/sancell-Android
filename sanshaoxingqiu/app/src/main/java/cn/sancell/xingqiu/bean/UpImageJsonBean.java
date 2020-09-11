package cn.sancell.xingqiu.bean;

public class UpImageJsonBean {

    public String imgWidth;

    public String imgHeight;

    public String imgType;

    public String fileSize;

    public UpImageJsonBean(String imgWidth, String imgHeight, String imgType, String fileSize) {
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.imgType = imgType;
        this.fileSize = fileSize;
    }
}
