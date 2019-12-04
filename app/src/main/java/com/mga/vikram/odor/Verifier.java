package com.mga.vikram.odor;

public class Verifier extends Reporter {
    private static Verifier verifierInstance;
    private boolean isVerifier=false;
    private Report reportToVerify;

    public static Verifier getInstance() {
        if( null == verifierInstance){
            verifierInstance = new Verifier();
        }
        return verifierInstance;
    }

    private Verifier(){
        super();
    }

    public void enableVerifier(){
        isVerifier = true;
    }
    public void disableVerifier(){
        isVerifier = false;
    }

    public boolean isVerifier(){
        return isVerifier;
    }

    public void verifyReport(Report report){
        reportToVerify = report;
    }

    public Report getReportToVerify(){
        return reportToVerify;
    }
}
