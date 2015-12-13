import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * Created by Léo on 02-11-15.
 * Grille
 */
public class Grid {
    final  int m_sizex=5;
    final  int m_sizey=5;
    int m_nbAgents=0;
    int m_nbAgentSatisfied=0;
    List<Agent> m_lAg= new LinkedList<Agent>();
    private Case[][] m_cases = new Case[m_sizex][m_sizey];
    private static Grid m_instance=null;
    private final  Object m_sem=new Object(); 


    
    private Grid(){
        m_instance=this;
        for(int i=0;i<m_sizex;i++){
            for(int j=0;j<m_sizey;j++){
                getCases()[i][j]=new Case(i,j);
            }
        }
    }

    /**
     * singleton
     * @return 
     */
    public static Grid getInstance(){
        if(m_instance==null){
            m_instance=new Grid();
           // System.out.println("newgrid !");
        }
        return m_instance;
    }


/**
 * bouge l'agent situé à la première case vers la deuxième case
 * @param c1
 * 1ère case
 * @param c2
 * 2ème case
 */
    public void moveAgent(Case c1, Case c2){
        synchronized(m_sem){
            if(c1.getAgent()!=null && c2.getAgent()==null){
            //if(c1.getAgent()!=null){
                c1.getAgent().setCurrentCase(c2);
                m_cases[c2.m_x][c2.m_y].setAgent(c1.getAgent());
               m_cases[c1.m_x][c1.m_y].setAgent(null);
            }
            //}
        }
              
        
    }
    
    /**
     * Mise en route de tout les agents
     */
    public void startAgents() {
        for (Agent a : this.m_lAg) {
            a.start();
        }
    }
    
    /**
     * Ajout d'un agent
     * @param s
     * lettre de l'agent
     * @return
     * l'agent
     * @throws Exception 
     */
    public Agent addAgent(String s) throws Exception{
        if(m_lAg.size()<m_sizex*m_sizey-1 ) {
            Random rnd = new Random();
            int x, y, rx, ry;
            do {
                x = rnd.nextInt(m_sizex);
                y = rnd.nextInt(m_sizey);
            }
            while (m_cases[x][y].getAgent() != null);
            m_nbAgents++;
            // Parcourir agents et vérifier destination pas déjà prise.
            boolean endloop;
            do {
                rx = rnd.nextInt(m_sizex);
                ry = rnd.nextInt(m_sizey);
                endloop = true;
                for (Agent a : m_lAg) {
                    if (a.getTermCase().m_x == rx && a.getTermCase().m_y == ry) {
                        endloop = false;
                        break;
                    }
                }
            }
            while (!endloop);
                   
                Timer timer = new Timer();
                Agent a=new Agent(m_cases[x][y], m_cases[rx][ry],s);
                m_lAg.add(a);
                return a;
        }
        else{
            throw new Exception("Grid : plus de place pour nouvel agent !");
    
        }
                    
    }
/**
 * Retourne les cases voisines occupées ou les cases voisines libres autour de la case
 * @param x
 * position sur l'axe x de la case
 * @param y
 * position sur l'axe y de la case
 * @param free
 * true : cases occupées, false cases libres
 * @return 
 */
    public List<Case> getFreeNeighbors(int x, int y,boolean free){
        Case[][] cases=m_cases;
         List<Case> emptyNeighb = new LinkedList<>();
          if(x>0){
                 if( (cases[x-1][y].getAgent()== null)==free ){
                     emptyNeighb.add(cases[x-1][y]);
                 }
             }
             if(x<this.m_sizex-1){
                 if((cases[x+1][y].getAgent() == null)==free){
                     emptyNeighb.add(cases[x+1][y]);
                 }
             }
             if(y>0){
                 if((cases[x][y-1].getAgent()==null )==free){
                     emptyNeighb.add(cases[x][y-1]);
                 }
             }
             if(y<this.m_sizey-1){
                 if((cases[x][y+1].getAgent()==null )==free){
                     emptyNeighb.add(cases[x][y+1]);
                 }
             }
             return emptyNeighb;
    }

    public Case[][] getCases() {
        return m_cases;
    }
}