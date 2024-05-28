package org.example.design_pattern.behavior.chainOfResponsibility;

//抽象處理者：大領導類
abstract class Leader {
    private Leader next;
    private String name;
    public Leader(String name){
        this.name = name;
    }
    public void setNext(Leader next) {
        this.next = next;
    }
    public Leader getNext() {
        return next;
    }
    public void leaveSuccessed(int LeaveDays){
        System.out.println(this.name + " approve your " + LeaveDays + " days leave.");
    }
    public void leaveFailed(){
        System.out.println("There are too many days of leave, no one approved the leave slip!");
    }
    // 處理請求的方法
    public abstract void handleRequest(int LeaveDays);
}
