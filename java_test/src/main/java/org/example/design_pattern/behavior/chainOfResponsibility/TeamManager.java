package org.example.design_pattern.behavior.chainOfResponsibility;

//實體處理者1：團隊經理
class TeamManager extends Leader {
    public TeamManager(){
        super("TeamManager");
    }
    public void handleRequest(int LeaveDays) {
        if(LeaveDays <= 7) {
            this.leaveSuccessed(LeaveDays);
        }
        else {
            if (getNext() != null) {
                getNext().handleRequest(LeaveDays);
            }
            else {
                this.leaveFailed();
            }
        }
    }
}
