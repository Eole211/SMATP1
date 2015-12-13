/**
 * Created by Léo on 02-11-15.
 * Messages
 */
public class Msg {

   
    public enum Request{ASK};
    public enum Action{MOVE};


    private Agent m_emitter;
    private Agent m_dest;
    private Request m_request ;
    private Action m_action;
    private Case m_info;



    public Msg(Agent emit, Agent dest,Request request,Action action, Case info  ){
        m_emitter=emit;
        m_dest=dest;
        m_request=request;
        m_action=action;
        m_info=info;
    }





    
    
    
     /**
     * @return the m_request
     */
    public Request getRequest() {
        return m_request;
    }

    /**
     * @return the m_action
     */
    public Action getAction() {
        return m_action;
    }

    /**
     * @return the m_info
     */
    public Case getInfo() {
        return m_info;
    }

    /**
     * @return the m_dest
     */
    public Agent getDest() {
        return m_dest;
    }

    /**
     * @param m_dest the m_dest to set
     */
    public void setDest(Agent m_dest) {
        this.m_dest = m_dest;
    }

}
