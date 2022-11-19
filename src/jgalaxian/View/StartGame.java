package jgalaxian.View;

public class StartGame {

    private static StartGame singleton = null;
    private SpriteUpdater upd;

    private StartGame() {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                VFinestra f = VFinestra.getInstance();
                View.getInstance().setFinestra(f);
                f.setVisible(true);
                ExitManager e = ExitManager.getInstance();
                upd = new SpriteUpdater();
                e.registra(upd);
                upd.start();
            }
        });
    }

    public void pausa(int provenienza) {
        upd.pausa(provenienza);
    }
	
	public boolean getPausa(){
		return upd.inPausa();
	}

    public static StartGame getInstance() {
        if (singleton == null) {
            singleton = new StartGame();
        }
        return singleton;
    }

}
