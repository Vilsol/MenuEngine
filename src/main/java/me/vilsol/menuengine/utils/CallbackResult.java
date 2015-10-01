package me.vilsol.menuengine.utils;

public class CallbackResult {

    private boolean callItem = false;
    private boolean canHandle = false;

    public boolean callItem(){
        return callItem;
    }

    public void setCallItem(boolean callItem){
        this.callItem = callItem;
    }

    public boolean canHandle(){
        return canHandle;
    }

    public void setCanHandle(boolean canHandle){
        this.canHandle = canHandle;
    }

}
