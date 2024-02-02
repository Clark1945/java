package proj.java.spring.aop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import proj.java.spring.aop.TxInfo;

import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class TxService {
    private HashMap<String,TxInfo> txInfoMap = new HashMap<>();

    public String createTx(TxInfo txInfo){
        txInfoMap.put(txInfo.getTxnNo(),txInfo);
        log.info("建立訂單成功");
        return "Success";
    }

    public TxInfo getTx(String txnNo) {
        log.info("取得訂單成功");
        return txInfoMap.get(txnNo);
    }

    public String updateTx(TxInfo txInfo) {
        txInfoMap.replace(txInfo.getTxnNo(),txInfo);
        log.info("更新訂單成功");
        return "Success";
    }

    public String deleteTx(String txnNo) {
        txInfoMap.remove(txnNo);
        log.info("刪除訂單成功");
        return "Success";
    }
}
