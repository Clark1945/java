package proj.java.spring.aop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import proj.java.spring.aop.TxInfo;
import proj.java.spring.aop.service.TxService;

@RestController
public class TxController {

    @Autowired
    private TxService txService;
    @PostMapping("/Tx")
    public String createTx(@RequestBody TxInfo txInfo) {
        return txService.createTx(txInfo);
    }
    @GetMapping("/Tx")
    public TxInfo getTx(@RequestParam("txnNo") String txnNo) {
        return txService.getTx(txnNo);
    }
    @PutMapping("/Tx")
    public String updateTx(@RequestBody TxInfo txInfo) {
        return txService.updateTx(txInfo);
    }
    @DeleteMapping("/Tx")
    public String deleteTx(@RequestParam("txnNo") String txnNo) {
        return txService.deleteTx(txnNo);
    }
}
