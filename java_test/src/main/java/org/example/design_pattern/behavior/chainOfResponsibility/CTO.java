package org.example.design_pattern.behavior.chainOfResponsibility;

//實體處理者4：技術長
class CTO extends Leader{
    public CTO(){
        super("CTO");
    }
    public void handleRequest(int LeaveDays) {
        if(LeaveDays <= 20)  {
            this.leaveSuccessed(LeaveDays);
        }
        else {
            if(getNext()!=null) {
                getNext().handleRequest(LeaveDays);
            }
            else {
                this.leaveFailed();
            }
        }
    }
}
