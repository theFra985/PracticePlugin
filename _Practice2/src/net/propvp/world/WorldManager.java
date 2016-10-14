/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.WorldCreator
 */
package net.propvp.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class WorldManager {
    public void loadWorld(String string) {
        Bukkit.getServer().createWorld(new WorldCreator(string));
    }

    public void unloadWorld(World world) {
        if (world != null) {
            Bukkit.getServer().unloadWorld(world, true);
        }
    }

    public boolean deleteWorld(File file) {
        if (file.exists()) {
            File[] arrfile = file.listFiles();
            int n = 0;
            while (n < arrfile.length) {
                if (arrfile[n].isDirectory()) {
                    this.deleteWorld(arrfile[n]);
                } else {
                    arrfile[n].delete();
                }
                ++n;
            }
        }
        return file.delete();
    }

    public void copyWorld(File file, File file2) {
        try {
            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if (!arrayList.contains(file.getName())) {
                if (file.isDirectory()) {
                    String[] arrstring;
                    if (!file2.exists()) {
                        file2.mkdirs();
                    }
                    String[] arrstring2 = arrstring = file.list();
                    int n = arrstring2.length;
                    int n2 = 0;
                    while (n2 < n) {
                        String string = arrstring2[n2];
                        File file3 = new File(file, string);
                        File file4 = new File(file2, string);
                        this.copyWorld(file3, file4);
                        ++n2;
                    }
                } else {
                    int n;
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] arrby = new byte[1024];
                    while ((n = fileInputStream.read(arrby)) > 0) {
                        fileOutputStream.write(arrby, 0, n);
                    }
                    fileInputStream.close();
                    fileOutputStream.close();
                }
            }
        }
        catch (IOException var3_4) {
            Bukkit.getLogger().severe("Error while copying world");
        }
    }

    public void clearClonedWorlds() {
        for (Object object2 : Bukkit.getWorlds()) {
            if (!((File) object2).getName().contains("-clone")) continue;
            this.unloadWorld((World)object2);
            this.deleteWorld(((World) object2).getWorldFolder());
        }
        File[] arrfile = Bukkit.getWorldContainer().listFiles();
        int n = arrfile.length;
        int n2 = 0;
        while (n2 < n) {
            Object object2;
            object2 = arrfile[n2];
            if (object2.getName().contains("-clone")) {
                this.deleteWorld((File)object2);
            }
            ++n2;
        }
    }
}

