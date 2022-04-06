package com.joe.entity;

public class Employee {
    public int empid;
    public int deptno;

    public Employee(int empid, int deptno) {
        this.empid = empid;
        this.deptno = deptno;
    }

    public int getEmpid() {
        return empid;
    }

    public void setEmpid(int empid) {
        this.empid = empid;
    }

    public int getDeptno() {
        return deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }
}