/**
 * Created by Léo on 02-11-15.
 */
public class Case
{
    int m_x;
    int m_y;
    String m_stringToDraw=" ";

    private Agent m_agent=null;
    Case(int x, int y){
        m_x=x;
        m_y=y;
    }

    public void setAgent(Agent a){
        if(a!=null)
            m_stringToDraw=a.getString();
        else
            m_stringToDraw="";
        m_agent=a;
    }

    public Agent getAgent(){
        return m_agent;
    }
    
   String getStringToDraw(){
       return m_stringToDraw;
   }


}
