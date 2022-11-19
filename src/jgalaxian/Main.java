package jgalaxian;

import jgalaxian.Logic.ILogic;
import jgalaxian.Logic.Logic;
import jgalaxian.View.IView;
import jgalaxian.View.View;
import jgalaxian.Utils.IUtils;
import jgalaxian.Utils.Utils;
import jgalaxian.View.Precaricatore;

import java.util.logging.Logger;

/**
 * @author https://github.com/ntsalari
 * @author https://github.com/Meht-evaS
 */
public class Main {

    public static void main(String[] args) {

        Logger.getLogger("DEBUGLOG").info("Programma avviato");

        ILogic logic = Logic.getInstance();
        IView view = View.getInstance();
        IUtils utils = Utils.getInstance();

        Logger.getLogger("DEBUGLOG").info("Dopo instanze");

        logic.setView(view);
        logic.setUtils(utils);
        view.setLogic(logic);
        view.setUtils(utils);
        utils.setView(view);

        view.startLogger();
        view.inviaTutteImmaginiInCache();
        utils.creaManagerMusica();

        new Precaricatore().start();

        Logic.getInstance().initCurve();

        view.mostraGUI();

    }
}
