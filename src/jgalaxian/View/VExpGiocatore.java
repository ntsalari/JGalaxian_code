package jgalaxian.View;

public class VExpGiocatore extends VSprite {

    private static final String[] statiEsplosione = {
        "/Resources/Images/explosion_player_00.png",
        "/Resources/Images/explosion_player_01.png",
        "/Resources/Images/explosion_player_02.png",
        "/Resources/Images/explosion_player_03.png"
    };
    private int expStato = 90;

    public VExpGiocatore() {
        super(statiEsplosione[0], 0, 0);
    }

    public void startExp(int x, int y) {
        expStato = 0;
        super.x = x - 20;
        super.y = y - 8;
    }

    @Override
    public boolean isVisualizzabile() {
        return expStato < 16;
    }

    @Override
    public void aggiornaStato() {
        if (expStato < 90) {
			if(expStato == 89){
				expStato = 90;
				View.getInstance().resetGiocatore();
				return;
			}
			if(expStato < 16){
				super.setStato(statiEsplosione[expStato / 4]);
			}
            expStato++;
        }
    }

    public static void setImmaginiEsplosioneGiocatoreInCache() {
        ImgCache imgcache = ImgCache.getInstance();

        for (int i = 0; i < statiEsplosione.length; i++) {
            imgcache.getImage(statiEsplosione[i]);
            View.getInstance().log("Ho mandato in cache: " + statiEsplosione[i]);
        }
    }
}
