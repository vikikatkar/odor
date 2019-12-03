package com.mga.vikram.odor;

public class Verifier extends Reporter {
    private static Verifier verifierInstance;
    private boolean isVerifier=false;

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

    public boolean isVerifier(){
        return isVerifier;
    }

}
