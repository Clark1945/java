package org.example.design_pattern.behavior.chainOfResponsibility;

public class Resign {
    public static void main(String[] args) {
        //組裝責任練
        Leader teamManager = new TeamManager();
        Leader departmentManager = new DepartmentManager();
        Leader technicalDirector = new TechnicalDirector();
        teamManager.setNext(departmentManager);
        departmentManager.setNext(technicalDirector);
        //提交請求
        teamManager.handleRequest(8);
    }
}

//Handler：定義一個處理請求的接口，包含抽象處理方法及後續方法。
//ConcreteHandler：實作Handler的處理方法，判斷是否可以處理這次的請求，可以則處理，不行則往後傳。

//優點
//1. 降低物件之間的耦合。
//2. 增強系統的擴充性，可以根據需求增加新的處理類，滿足開閉原則。
//3. 增強靈活性，可以動態的組成責任鏈。
//4. 簡化物件之間的連接性。
//5. 每個類別只處理自己該做的事，不處理就給別人做，符合單一職責原則。
//缺點
//1. 不能保證會被處理，可能到最後都沒有類別處理請求。
//2. 較長的責任鏈可能會影響系統效能？
//3. 增加Client的複雜性。

//1. 動態的指定一組物件處理請求，或添加新的處理者。
//2. 不確定由誰處理時，向多個處理者發送一個請求。

//而兩者的差別在於粒度的不同，Commande Pattern比較偏向將一個物件原本的method拆解成一個個的Command，
// 而Chain of Responsibility的Handler粒度更大一點，更像是一個個完整邏輯的物件。
