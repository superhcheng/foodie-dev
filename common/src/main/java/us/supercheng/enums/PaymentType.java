package us.supercheng.enums;

public enum PaymentType {

    Alipay(1, "Alipay"),
    WechatPay(2, "Wechat Pay");

    public Integer type;
    public String value;

    PaymentType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}