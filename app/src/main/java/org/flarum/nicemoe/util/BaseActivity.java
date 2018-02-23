package org.flarum.nicemoe.util;


import de.mindpipe.android.logging.log4j.LogConfigurator;
import java.io.File;
import android.os.Environment;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import android.app.*;
import android.os.*;

public class BaseActivity extends Activity {
    public Logger gLogger;

    public void configLog()
    {
        final LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "crifanli_log4j.log");
        //设置根日志级别
        logConfigurator.setRootLevel(Level.DEBUG);
        //设置特定记录器的日志级别
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.configure();

        gLogger = Logger.getLogger(this.getClass());
    }

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configLog();
        gLogger.debug("test android log to file in sd card using log4j");
	}
}
