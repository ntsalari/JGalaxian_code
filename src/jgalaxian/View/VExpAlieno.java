package jgalaxian.View;

public class VExpAlieno extends VSprite {

    private static final String[] statiEsplosione = {
        "/Resources/Images/explosion_gal_00.png",
        "/Resources/Images/explosion_gal_01.png",
        "/Resources/Images/explosion_gal_02.png",
        "/Resources/Images/explosion_gal_03.png"
    };
    private int expStato = -1;

    public VExpAlieno() {
        super(statiEsplosione[0], 0, 0);

    }

    public void startExp(int x, int y) {
        expStato = 0;
        super.x = x;
        super.y = y;
    }

    @Override
    public boolean isVisualizzabile() {
        return expStato != -1;
    }

    @Override
    public void aggiornaStato() {
        if (expStato > -1) {
            if (expStato == 16) {
                expStato = -1;
                return;
            }
            super.setStato(statiEsplosione[expStato / 4]);
            expStato++;
        }
    }

    public static void setImmaginiEsplosioneAlienoInCache() {
        ImgCache imgcache = ImgCache.getInstance();

        for (int i = 0; i < statiEsplosione.length; i++) {
            imgcache.getImage(statiEsplosione[i]);
            View.getInstance().log("Ho mandato in cache: " + statiEsplosione[i]);
        }
    }

}
