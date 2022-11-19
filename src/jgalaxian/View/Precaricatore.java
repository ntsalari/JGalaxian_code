package jgalaxian.View;

public class Precaricatore extends Thread {

    public Precaricatore() {
        super("Precaricatore");
    }

    @Override
    public void run() {
        View view = View.getInstance();
        view.precaricaMusica();
        view.fineAvvio();
        synchronized (view) {
            view.notify();
        }
		VFinestra.getInstance().abilitaClickMusiche();
    }
}
