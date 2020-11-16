package com.jd.classloader;

import java.io.FileInputStream;

/**
 * @Author panligang3
 * @create 2020/11/16 9:46 AM
 */
public class MyClassLoader extends ClassLoader {

    private String path;

    public MyClassLoader(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(path + name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();

                // JDK自身的加载仍满足双亲委派模型
                if (!name.startsWith("com.jd")) {

                    c = getParent().loadClass(name);
                }else{

                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }


    private byte[] loadByte(String name) throws Exception {
        name = name.replaceAll("\\.", "/");
        FileInputStream fis = new FileInputStream(path + "/" + name
                + ".class");
        int len = fis.available();
        byte[] data = new byte[len];
        fis.read(data);
        fis.close();
        return data;

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] data = loadByte(name);
            return defineClass(name, data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    public static void main(String[] args) throws Exception {

        //  /Users/panligang3/test/com/jd/classloader/User.class
        MyClassLoader classLoader1 = new MyClassLoader("/Users/panligang3/test/");
        MyClassLoader classLoader2 = new MyClassLoader("/Users/panligang3/test/");

        Class<?> aClass1 = classLoader1.loadClass("com.jd.classloader.User");
        Class<?> aClass2 = classLoader2.loadClass("com.jd.classloader.User");


        System.out.println(aClass1.getClassLoader());
        System.out.println(aClass2.getClassLoader());
        System.out.println(aClass1 == aClass2);

        System.out.println(User.class.getClassLoader());

    }
}
