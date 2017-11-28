package com.aks.mindspace.mindspace;

/**
 * Created by 123 on 10/16/2017.
 */

public class emp {
    String empid, empname, empemailid, empdep, empimage, empno, empgender,emprating;
    public emp(){
        this.empid="000";
        this.empname="xyz";
        this.empemailid="xyz@gmail.com";
        this.empdep="sale";
        this.empimage="default";
        this.empno="0123456789";
        this.empgender="female";
        this.emprating="5";
    }
    public emp(String empid,String empname,String empemailid,String empdep,String empimage,String empno,String empgender,String emprating){
        this.empid=empid;
        this.empname=empname;
        this.empemailid=empemailid;
        this.empdep=empdep;
        this.empimage=empimage;
        this.empno=empno;
        this.empgender=empgender;
        this.emprating=emprating;
    }
}
