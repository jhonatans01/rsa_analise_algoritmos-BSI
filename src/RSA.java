/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import net.razorvine.pickle.PickleException;
import net.razorvine.pickle.Pickler;
import net.razorvine.pickle.Unpickler;

/**
 *
 * @author Jhonatan
 */
public class RSA {

    public static int mdc(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return mdc(b, a % b);
        }
    }

    public static ArrayList carac_decimal(String string) {
        char x;
        ArrayList list = new ArrayList();
        for (int i = 0; i < string.length(); i++) {
            x = string.charAt(i);
            int z = (int) x;
            list.add(z);
        }
        return list;
    }

    public static String descriptografar(int D, int N, String mensInp) {
        String mensOut = "";

        int i = 0;
        String a = "";
        int formula = 0;
        while (i < mensInp.length() - 1) {
            while (mensInp.charAt(i) != ' ') {
                a += mensInp.charAt(i);
                i++;
            }
            BigInteger bi = new BigInteger("" + a);
            bi.modPow(new BigInteger("" + D), new BigInteger("" + N));

            char b = (char) Integer.parseInt(
                    bi.modPow(new BigInteger("" + D), new BigInteger("" + N)).toString());
            a = "";
            mensOut += b;
            i++;
        }

        return mensOut;
    }

    public static void salvar_arquivo(ArrayList o) {
        OutputStream outputStream;
        try {
            String path;
            if (System.getProperty("os.name").contains("Mac")) {
                path = "/src/criptografia.txt";
            } else {
                path = "\\src\\criptografia.txt";
            }
            outputStream = new FileOutputStream(
                    System.getProperty("user.dir") + path);
            Pickler pickler = new Pickler();
            pickler.dump(o.toArray(), outputStream);
            pickler.close();
            outputStream.close();
        } catch (FileNotFoundException | PickleException ex) {
        } catch (IOException ex) {
        }
    }

    public static String abrir_arquivo() {
        String msg = "";
        InputStream inputStream;
        try {
            String path;
            if (System.getProperty("os.name").contains("Mac")) {
                path = "/src/criptografia.txt";
            } else {
                path = "\\src\\criptografia.txt";
            }
            inputStream = new FileInputStream(
                    System.getProperty("user.dir") + path);
            Unpickler unpickler = new Unpickler();
            Object[] o = (Object[]) unpickler.load(inputStream);
            for (int i = 0; i < o.length; i++) {
                msg += o[i] + " ";
            }
            unpickler.close();
            inputStream.close();
        } catch (FileNotFoundException | PickleException ex) {
        } catch (IOException ex) {
        }
        return msg;
    }

    public static void main(String[] args) {
        int P, Q, N, Z, D, E;
        ArrayList array = new ArrayList();
        P = 17;
        Q = 23;

        N = P * Q;
        Z = (P - 1) * (Q - 1);
        for (int i = 1; i < Z; i++) {
            if (mdc(i, Z) == 1) {
                array.add(i);
            }
        }

        D = (int) array.get(68);

        E = 7;
//        E = 1;
//
//        while ((E * D) % Z != 1) {
//            E++;
//        }

        System.out.printf("Chave pública = (%d, %d)", E, N);
        System.out.printf("\nChave privada = (%d, %d)", D, N);

        String string = "BSI 2013 |============== loading... =====|";
        ArrayList str_dec = carac_decimal(string);

        for (int i = 0; i < string.length(); i++) {
            BigInteger bi = new BigInteger("" + str_dec.get(i));
            str_dec.remove(i);
            str_dec.add(i, bi.modPow(new BigInteger("" + E), new BigInteger("" + N)));
        }

        String texto = "";

        for (Object object : str_dec) {
            texto += object.toString() + " ";
        }
        System.out.println("\nTexto criptografado: " + texto);

        System.out.println("Texto descriptografado: " + descriptografar(D, N, texto));

        salvar_arquivo(str_dec);
        
        
//        System.out.println(descriptografar(D, N, abrir_arquivo()));
    }
}
