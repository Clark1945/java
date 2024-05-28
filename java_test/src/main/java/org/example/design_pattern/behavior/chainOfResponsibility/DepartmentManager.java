package org.example.design_pattern.behavior.chainOfResponsibility;

//實體處理者2：部門經理
class DepartmentManager extends Leader {
    public DepartmentManager(){
        super("DepartmentManager");
    }
    public void handleRequest(int LeaveDays) {
        if(LeaveDays <= 10) {
            this.leaveSuccessed(LeaveDays);
        }
        else {
            if(getNext() != null)  {
                getNext().handleRequest(LeaveDays);
            }
            else {
                this.leaveFailed();
            }
        }
    }
}
