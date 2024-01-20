package proj.java.spring.aop;

import lombok.Data;

@Data
public class TxInfo {
    private int id;
    private String txnNo;
    private String consumer;
    private String goods;
    private int amount;
}
