package org.example.design_pattern.behavior.chainOfResponsibility;

//實體處理者3：院长类
class TechnicalDirector extends Leader {
    public TechnicalDirector(){
        super("TechnicalDirector");
    }
    public void handleRequest(int LeaveDays) {
        if(LeaveDays <= 15)  {
            this.leaveSuccessed(LeaveDays);
        }
        else {
            if(getNext() != null) {
                getNext().handleRequest(LeaveDays);
            }
            else {
                this.leaveFailed();
            }
        }
    }
}
