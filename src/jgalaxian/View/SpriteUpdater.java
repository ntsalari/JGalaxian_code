package jgalaxian.View;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpriteUpdater extends Thread implements Closeable {

    private boolean esegui = true;
    private boolean inPausa = false;
    private VSprite[] tutti;

    public SpriteUpdater() {
        super("Sprites");
        this.setPriority(Thread.MAX_PRIORITY - 1);
        VFinestra f = VFinestra.getInstance();
        VSprite[] p1 = f.getAmbiente().getSprites();
        VSprite[] p2 = f.getAnimati().getSprites();
        tutti = new VSprite[200];
        int k = 0;
        for (VSprite s : p1) {
            if (s != null) {
                tutti[k] = s;
            } else {
                break;
            }
            k++;
        }
        for (VSprite s : p2) {
            if (s != null) {
                tutti[k] = s;
            } else {
                break;
            }
            k++;
        }
    }

    @Override
    public void run() {
        VFinestra f = VFinestra.getInstance();
        VSfondo s = f.getSfondo();
        VDemo d = VDemo.getInstance();
        View view = View.getInstance();

        Logger l = Logger.getLogger(this.getClass().getName());
        long start = System.nanoTime();
        long end = start + 1;
        long sleep;
        long aggiornaSfondo;
        try {
            aggiornaSfondo = System.nanoTime();
            Runnable aggiornamentoSchermo = new Runnable() {
                @Override
                public void run() {
                    f.repaint();
                }
            }; 
            while (esegui) {
                sleep = 16666666 - (end - start);
                if (sleep > 0) {
                    java.util.concurrent.TimeUnit.NANOSECONDS.sleep(sleep); //60 fps
                } else {
                    l.log(Level.WARNING, "Il thread principale ha laggato!!");
                }
                start = System.nanoTime();
                if (view.giocoIniziato()) {
                    if (!inPausa) {
                        view.movimentoNavetta();
                        view.avanzaAnimazioni();
                        for (VSprite i : tutti) {
                            if (i != null) {
                                i.aggiornaStato();
                            }
                        }
                    } else {
                        f.getAnimati().animaElem();
                    }
                } else {
                    d.nextAnim();
                }
                if (start - aggiornaSfondo > 140000000) {
                    aggiornaSfondo = start;
                    s.nextAnim();
                }
                
                javax.swing.SwingUtilities.invokeLater(aggiornamentoSchermo);
                end = System.nanoTime();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(VFinestra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
	public boolean inPausa(){
		return inPausa;
	}

    public void pausa(int provenienza) {
        View view = View.getInstance();

        inPausa = !inPausa;

        View.getInstance().log("PAUSA IN SPRITE UPDATER -- getGiocatoreMorto : " + view.getGiocatoreMorto());

        switch (provenienza) {
            case Costanti.INIZIO_CAMBIO_LIVELLO:
                //view.pausaEffettiSonori();
                View.getInstance().log("Musica background in pausa per INIZIO_CAMBIO_LIVELLO in SpriteUpdater");
                view.pausaMusicaBackground();
                break;

            case Costanti.FINE_CAMBIO_LIVELLO:
                //view.terminaPausaEffettiSonori();
                View.getInstance().log("RESET Musica background per FINE_CAMBIO_LIVELLO in SpriteUpdater");
                view.resetMusicaBackground(false);
                break;

            case Costanti.PAUSA_TASTO_GUI:
                if (inPausa && view.giocoIniziato()) {
                    view.pausaEffettiSonori();
                    View.getInstance().log("Musica background in pausa per PAUSA_TASTO_GUI in SpriteUpdater");
                    view.pausaMusicaBackground();
                } else if (!inPausa && view.giocoIniziato()) {
                    view.terminaPausaEffettiSonori();
                    View.getInstance().log("PLAY Musica background per fine PAUSA_TASTO_GUI in SpriteUpdater");
                    view.playMusicaBackground();
                }
                break;
			// Costanti.INIZIO_MORTE_GIOCATORE:
			// Costanti.FINE_MORTE_GIOCATORE:
            default:
                View.getInstance().log("Non faccio nessuna pausa musica in SpriteUpdater  --  Inizio o fine morte giocatore");
                break;
        }
    }

    @Override
    public void close() throws IOException {
        esegui = false;
    }
}
