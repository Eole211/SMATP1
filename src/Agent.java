
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Léo on 02-11-15.
 */
public class Agent extends Thread {

    private Case m_termCase;
    private Case m_currentCase;
    private Grid m_grid;
    private String m_string;
    private boolean isSatisfied;

    Agent(Case currentCase, Case termCase, String string) {
        m_termCase = termCase;
        m_currentCase = currentCase;
        m_grid = Grid.getInstance();
        m_string = string;
        m_grid.getCases()[currentCase.m_x][currentCase.m_y].setAgent(this);
    }

    @Override
    public void run() {
        while (m_grid.m_nbAgents != m_grid.m_nbAgentSatisfied) {

            //Messages 
            List<Msg> msgs = LetterBox.getInstance().readMessages(this);
            boolean needToMove = false;
            for (Msg msg : msgs) {
                if (msg.getRequest() == Msg.Request.ASK) {
                    if (msg.getAction() == Msg.Action.MOVE) {
                        System.out.println("Needtomove action : " + this.getString());
                        if (msg.getInfo() == this.m_currentCase) {
                            needToMove = true;
                            break;
                        }
                    }
                }
            }

            //Si message BOUGE reçu ou pas encore à destinaion
            if (needToMove || !this.isSatisfied) {
                if (needToMove) {
                    System.out.println(" RealNeedtomove : " + this.getString());
                }

                Case[][] cases = m_grid.getCases();
                int x = m_currentCase.m_x;
                int y = m_currentCase.m_y;

                //Cases voisines vides
                List<Case> emptyNeighb = m_grid.getFreeNeighbors(x, y, true);

                //Case voisine vide rapprochant l'agent de la destination
                List<Case> goodCases = new LinkedList<>();
                int dist = Math.abs(this.m_termCase.m_x - x) + Math.abs(this.m_termCase.m_y - y);
                Case futureCase = null;

                //S'il y a des cases voisines libres
                if (!emptyNeighb.isEmpty()) {
                    //On regroupe les cases rapprochant l'agent de sa destination
                    for (Case c : emptyNeighb) {
                        int ddist = Math.abs(this.m_termCase.m_x - c.m_x) + Math.abs(this.m_termCase.m_y - c.m_y) - dist;
                        if (ddist < 0) {
                            goodCases.add(c);
                        }
                    }
                    Random rndGen = new Random();
                    int rnd;
                    //Si des cases vide rapprochent l'agent de sa dest, on en choisi une au hazard
                    if (!goodCases.isEmpty()) {
                        rnd = rndGen.nextInt(goodCases.size());
                        futureCase = goodCases.get(rnd);
                    } //Sinon on en choisit une parmis les cases vides ou on envoie un message
                    else {

                        //Déplacement vers une case vide
                        if (rndGen.nextBoolean()) {
                            rnd = rndGen.nextInt(emptyNeighb.size());
                            futureCase = emptyNeighb.get(rnd);
                        } else { //On envoie un message à un voisin
                            List<Case> fullNeighb = m_grid.getFreeNeighbors(x, y, false);
                            for (Case c : fullNeighb) {
                                int ddist = Math.abs(this.m_termCase.m_x - c.m_x) + Math.abs(this.m_termCase.m_y - c.m_y) - dist;
                                if (ddist < 0) {
                                    goodCases.add(c);
                                }
                            }
                            if (!goodCases.isEmpty()) {
                                rnd = rndGen.nextInt(goodCases.size());
                                Agent toMove = goodCases.get(rnd).getAgent();
                                if (toMove != null) {
                                    LetterBox.getInstance().sendMessage(new Msg(this, toMove, Msg.Request.ASK, Msg.Action.MOVE, goodCases.get(rnd)));
                                    System.out.println(this.m_string + " asked " + toMove.getString() + " to move");
                                }
                            }

                        }
                    }
                }
                if (futureCase == null) {
                    goodCases = m_grid.getFreeNeighbors(x, y, false);
                    Random rndGen = new Random();
                    if (!goodCases.isEmpty()) {
                        int rnd = rndGen.nextInt(goodCases.size());
                        //Envoyer message
                        Agent toMove = goodCases.get(rnd).getAgent();
                        if (toMove != null) {
                            LetterBox.getInstance().sendMessage(new Msg(this, toMove, Msg.Request.ASK, Msg.Action.MOVE, goodCases.get(rnd)));
                            System.out.println(this.m_string + " asked " + toMove.getString() + " to move");
                        }
                    }
                    //}
                } else {
                    //System.out.println("moved " + this.getName() + "from [" + m_currentCase.m_x + "-" + m_currentCase.m_y + "] to [" + futureCase.m_x + "-" + futureCase.m_y + "]");
                    move(futureCase);
                    if (futureCase == this.m_termCase) {
                        isSatisfied = true;
                        m_grid.m_nbAgentSatisfied++;
                    } else if (isSatisfied) {
                        isSatisfied = false;
                        m_grid.m_nbAgentSatisfied--;
                    }
                }

                if (MainWindow.getInstance() != null) {
                    MainWindow.getInstance().drawGrid(m_grid);
                }
            }
            //choisir une case où se déplacer
            //Se déplacer   
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        System.out.println("Puzzle finished");

    }

    public void move(Case cto) {
        m_grid.moveAgent(this.m_currentCase, cto);
        this.setCurrentCase(cto);
    }

    public Case chooseCase() {
        int dx = m_termCase.m_x - m_currentCase.m_x;
        int dy = m_termCase.m_y - m_currentCase.m_y;
        return null;
    }

    public Case getTermCase() {
        return m_termCase;
    }

    /**
     * @return the m_string
     */
    public String getString() {
        return m_string;
    }

    /**
     * @param m_currentCase the m_currentCase to set
     */
    public void setCurrentCase(Case m_currentCase) {
        this.m_currentCase = m_currentCase;
    }
}
