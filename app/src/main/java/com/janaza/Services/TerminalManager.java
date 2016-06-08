package com.janaza.Services;

import android.content.Context;
import android.os.Environment;
import com.janaza.Models.Account;
import com.janaza.Models.Janaza;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.File;
import java.util.ArrayList;

public class TerminalManager {

    protected static TerminalManager INSTANCE = null;
    protected final String dirname = "JanazaQuebec";
    protected File appDir;
    protected File mediaDir;
    protected Context context = null;

    protected TerminalManager() {
        appDir = new File(Environment.getExternalStorageDirectory() + "/" + dirname);
        if (!appDir.exists())
            appDir.mkdirs();

    }

    public static TerminalManager getInstance() {
        if (INSTANCE == null) {
            synchronized (TerminalManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TerminalManager();
                }
            }
        }
        return INSTANCE;
    }

    public Context getContext() {
        return context;
    }

    public TerminalManager setContext(Context context) {
        this.context = context;
        return this;
    }

    public File getAppDir() {
        return appDir;
    }

    public boolean isLocalPath(String path) {
        return path.startsWith(appDir.getParent());
    }


    public boolean existAccount(String email) {
        File userFile = new File(appDir + "/" + email + ".xml");
        return userFile.exists();
    }

    public File getAccountFile(String email) {
        return new File(appDir + "/" + email + ".xml");
    }

    public File getMediaFile(String filename) {
        return new File(mediaDir + "/" + filename);
    }

    public File getFile(String path) {
        return new File(appDir + "/" + path);
    }


    public boolean existActiveAccount() {
        File userFile = new File(appDir + "/" + "activeAccount.xml");
        return userFile.exists();
    }

    public String updateActiveAccount(Account account) {
        // save active account in two files
        File activeAccountFile = new File(appDir + "/" + "activeAccount.xml");
        try {
            Serializer serializer = new Persister();
            serializer.write(account, activeAccountFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activeAccountFile.getPath();
    }

    public Account getActiveAccount() {
        if (!this.existActiveAccount())
            return null;
        File accountFile = new File(appDir + "/" + "activeAccount.xml");
        Account account = null;
        try {
            Serializer serializer = new Persister();
            account = serializer.read(Account.class, accountFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }


    public String updateJanazat(ArrayList<Janaza> janazat) {
        // save active account in two files
        File janazatFile = new File(appDir + "/" + "janazat.xml");
        try {
            Serializer serializer = new Persister();
            serializer.write(janazat, janazatFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return janazatFile.getPath();
    }

    public ArrayList<Janaza> getJanazat() {

        File janazatFile = new File(appDir + "/" + "janazat.xml");
        ArrayList<Janaza> janazat = new ArrayList<>();
        try {
            Serializer serializer = new Persister();
            janazat = serializer.read(ArrayList.class, janazatFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return janazat;
    }

}
