/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.pdf.codec.Base64;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.param.YvsSocietes;
import yvs.grh.bean.ContratEmploye;
import yvs.init.Initialisation;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import static yvs.init.Initialisation.USER_DOWNLOAD;
import static yvs.init.Initialisation.USER_DOWNLOAD_LINUX;

/**
 *
 * @author Yves
 */
public class Util {

    public Util() {
    }

    public static DateFormat dm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static DateFormat hf = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    public static DateFormat dj = new SimpleDateFormat("dddd", Locale.FRENCH);
    private static final int ARBITARY_SIZE = 1048;

    public static byte[] imageToByte(String chemin) {
        File file = new File(chemin);
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", baos);
            baos.flush();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static StreamedContent bytesToStreamedContent(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        StreamedContent st = new DefaultStreamedContent(is, "image/png");
        return st;
    }

    public static BufferedImage bytesToImage(byte[] bytes) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return img;
    }
    //génère un numéro de référence pour un produit

    private String generateRefProd(String des) {
        String ref = "";
        return ref;
    }

    public static boolean isWindows() {
        return (Initialisation.OS.contains("win"));
    }

    public static File getFile(UploadedFile upload) {
        InputStream inputStream = null;
        try {
            String name = upload.getFileName();
            inputStream = upload.getInputstream();

            String path = (isWindows() ? USER_DOWNLOAD : USER_DOWNLOAD_LINUX);
            File file = new File(path + name);
            OutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            return file;
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static File createRessource(UploadedFile source) {
        InputStream input = null;
        File file = null;
        try {
            String directorie = Initialisation.getCheminDownload();
            input = source.getInputstream();
            String fileName = Util.giveFileName() + "." + Util.getExtension(source.getFileName());
            try {
                //copie dans le dossier de l'application
                file = new File(directorie);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Util.copyFile(fileName, directorie + "" + Initialisation.FILE_SEPARATOR, input);
                file = new File(new StringBuilder(directorie).append(fileName).toString());
                if (!file.exists()) {
                    File doc = new File(directorie);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        file.createNewFile();
                    } else {
                        file.createNewFile();
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

    //copie le fichier inputStream dans le repertoire dest sous le nom fileName
    public static String copyFile(String fileName, String dest, InputStream in) throws IOException {
        String destination = null;
        try {
            File doc = new File(dest);
            if (!doc.exists()) {
                doc.mkdirs();
                if (!doc.canWrite()) {
                    doc.setWritable(true);
                }
            }
            destination = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dest + "" + fileName);
            File f = new File(destination);
            if (!f.exists()) {
                if (!doc.canWrite()) {
                    doc.setWritable(true);
                }
                f.createNewFile();
                if (!f.canWrite()) {
                    f.setWritable(true);
                }
            }
            try (OutputStream out = new FileOutputStream(f)) {
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
//on ne ferme pas le flux ici, car il est utilisé juete après par la méthode copySVGFile
//                in.close();
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Fichier destination : " + destination);
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, "---- Impossible de créer le fichier de restauration....", e);
        }
        return fileName;
    }

    public static String copyFileS(String fileName, String dest, InputStream in) throws IOException {
        try {
            String destination = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dest + "" + fileName);
            dest = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dest);
            File file = new File(dest);
            if (!file.exists()) {
                file.mkdirs();
            }
            File f = new File(destination);
            if (!f.exists()) {
                f.createNewFile();
            }
            try (OutputStream out = new FileOutputStream(f)) {
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
//on ne ferme pas le flux ici, car il est utilisé juete après par la méthode copySVGFile
//                in.close();
                out.flush();
            }
        } catch (IOException e) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, e);
        }
        return fileName;
    }

    public static String copySVGFile(String fileName, String dest, InputStream in) throws IOException {
        try {
            File doc = new File(dest);
            if (!doc.exists()) {
                doc.mkdirs();
                if (!doc.canWrite()) {
                    doc.setWritable(true);
                }
            }
            String destination = dest + "" + fileName;
            File f = new File(destination);
            if (!f.exists()) {
                f.createNewFile();
                if (!f.canWrite()) {
                    f.setWritable(true);
                }
            }
            try (OutputStream out = new FileOutputStream(f)) {
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
            }
        } catch (IOException e) {
//            System.err.println("---- Impossible de créer le fichier de sauvegarde....");
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, "---- Impossible de créer le fichier de sauvegarde....", e);
        }
        return fileName;
    }

    public static File copyFile(String fileName, String dest, byte[] fileData) {
        try {
            File doc = new File(dest);
            if (!doc.exists()) {
                doc.mkdirs();
                if (!doc.canWrite()) {
                    doc.setWritable(true);
                }
            }
            String destination = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dest + "" + fileName);
            File f = new File(destination);
            if (!f.exists()) {
                f.createNewFile();
                if (!f.canWrite()) {
                    f.setWritable(true);
                }
            }

            try (OutputStream out = new FileOutputStream(f)) {
                out.write(fileData);
                out.flush();
            }
            return f;
        } catch (IOException e) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, "---- Impossible de créer le fichier de sauvegarde....", e);
        }
        return null;
    }

    public static File copySVGFile(String fileName, String dest, byte[] fileData) {
        try {
            File doc = new File(dest);
            if (!doc.exists()) {
                doc.mkdirs();
                if (!doc.canWrite()) {
                    doc.setWritable(true);
                }
            }
            String destination = dest + "" + fileName;
            File f = new File(destination);
            if (!f.exists()) {
                f.createNewFile();
                if (!f.canWrite()) {
                    f.setWritable(true);
                }
            }

            try (OutputStream out = new FileOutputStream(f)) {
                out.write(fileData);
                out.flush();
            }
            return f;
        } catch (IOException e) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, "---- Impossible de créer le fichier de sauvegarde....", e);
        }
        return null;
    }

    public static void delFileOnApps(String dest, String fileName) {
        String destination = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dest + "" + fileName);
        File from = new File(destination);
        if (from.exists()) {
            from.delete();
        }
    }

    public static void delFile(String dest, String fileName) {
        File f = new File(dest + "" + fileName);
        System.err.println("FILE " + f.getAbsolutePath());
        if (f.exists()) {
            f.delete();
        }
    }

    public static void copyDirectory(String dest) throws IOException {
        String destination = FacesContext.getCurrentInstance().getExternalContext().getRealPath(dest);
        File from = new File(destination);
        if (!from.exists()) {
            from.mkdirs();
        }
//        System.err.println("MKDiR " + from.getAbsolutePath() + " Exist " + from.exists());
    }

    public static String CheminLogo() {
//        StringBuilder sb = new StringBuilder(System.getProperty("user.dir"));
//        sb.append(System.getProperty("file.separator")).append("harmonie");
        String scte = ((YvsSocietes) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("societ")).getLogo();
//        if (scte != null) {
//            sb.append(System.getProperty("file.separator")).append(scte.replace(" ", "")).append(System.getProperty("file.separator"))
//                    .append("logo").append(System.getProperty("file.separator"));
//        }
        return (scte != null) ? FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/lymytz/documents/logos_doc/" + scte) : FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/lymytz/documents/logos_doc/default.png");
//        return sb.toString();
    }

    public void synchroFile() {
        //parcourir un dossier du disque dur et le recréer dynamiquement dans l'application

    }

    public static String giveFileName() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getExtension(String name) {
        char[] tab = name.toCharArray();
        int num = 0;
        for (char c : tab) {
            num++;
            if (String.valueOf(c).hashCode() == ".".hashCode()) {
                return name.substring(num);
            }
        }
        return null;
    }

    public static String getExtensionDownload(String extension) {
        switch (extension) {
            case "xls":
            case "xlsx":
                return "vnd.ms-excel";
            default:
                return extension;
        }
    }

    public static byte[] read(File file) {
        return readStream(file).toByteArray();
    }

    public static ByteArrayOutputStream readStream(File file) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            FileInputStream fis = new FileInputStream(file);
            int c = 0;
            try {
                while ((c = fis.read()) != -1) {
                    buffer.write(c);
                    buffer.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return buffer;
    }

    public static String readToString(File file) {
        return Base64.encodeBytes(read(file));
    }

    public static byte[] readToString(String file) {
        return Base64.decodeFromFile(file);
    }

    public static void openFile(FacesContext faces, byte[] bytes, String type, String file) {
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        /*
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * * * * *
         * Pour afficher une boîte de dialogue pour enregistrer le fichier
         * sous le nom rapport.pdf * * * * * * * * * * * * * * * * * * * * *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * * * * * * * * * * * * * * *
         */
        response.setContentType("application/" + type);
        response.addHeader("Content-disposition",
                "attachment;filename=" + file);
        response.setContentLength(bytes.length);
        try {
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        faces.responseComplete();
        /**
         * *********************************
         */
    }

    public static String getCheminDocEmploye(String matricule) {
        String chemin = null;
        if ((matricule != null) ? (!matricule.trim().equals("") && matricule.length() > 0) : false) {
            StringBuilder sb = new StringBuilder(Initialisation.getCheminDocEmps());
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            //chemin des documents employes
            chemin = sb.append(FILE_SEPARATOR).append(matricule).toString();
            file = new File(chemin);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return chemin;
    }

    public static String getCheminDocContratEmps(ContratEmploye contrat) {
        String chemin = null;
        if ((contrat != null) ? (!"".equals(contrat.getReference())) : false) {
            if ((contrat.getEmploye() != null) ? contrat.getEmploye().getId() != 0 : false) {
                String cheminDocEmps = getCheminDocEmploye(contrat.getEmploye().getMatricule());
                StringBuilder sb = new StringBuilder(cheminDocEmps);
                File file = new File(sb.toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                //chemin des documents employes
                chemin = sb.append(FILE_SEPARATOR).append(contrat.getReference()).toString();
                file = new File(chemin);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
        }
        return chemin;
    }

    public static StreamedContent getDownloadFile(String fichier, String contentType, String name) {
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(fichier);
        StreamedContent file = new DefaultStreamedContent(stream, contentType, name);
        return file;
    }

    public static boolean getDownloadFilePDF(String CheminCompletFichier, String nameFichierDownload) {
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        //cette methode permet de récupérer le chemin absolu du repertoire report suivant.               
        File file = new File(ext.getRealPath(CheminCompletFichier));
        if (!file.exists()) {
            return false;
        }
        byte[] bytes = Util.read(file);
        FacesContext faces = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment;filename=" + nameFichierDownload + ".pdf");
        response.setContentLength(bytes.length);
        /*
            
         * Pour afficher une boîte de dialogue pour enregistrer le fichier
         * sous le nom rapport.pdf * * * * * * * * * * * * * * * * * * * * *            
         */
        try {
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        faces.responseComplete();
        return true;
    }

    public static boolean getDownloadFile(String CheminCompletFichier, String nameFichierDownload) {
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        //cette methode permet de récupérer le chemin absolu du repertoire report suivant.               
        File file = new File(CheminCompletFichier);
        if (!file.exists()) {
            file = new File(ext.getRealPath(CheminCompletFichier));
            if (!file.exists()) {
                System.err.println("----- not exist");
                return false;
            }
        }
        String extention = getExtension(file.getName());
        byte[] bytes = Util.read(file);
        FacesContext faces = FacesContext.getCurrentInstance();
        try {
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            response.setContentType("application/" + getExtensionDownload(extention) + "");
            response.addHeader("Content-disposition", "attachment;filename=" + nameFichierDownload + "." + extention);
            response.setContentLength(bytes.length);
            /*
            
             * Pour afficher une boîte de dialogue pour enregistrer le fichier
             * sous le nom rapport.pdf * * * * * * * * * * * * * * * * * * * * *            
             */
            try (FileInputStream in = new FileInputStream(file);
                    OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[ARBITARY_SIZE];
                int numBytesRead;
                while ((numBytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, numBytesRead);
                }
            } catch (Exception ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (Exception ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        faces.responseComplete();
        return true;
    }

    public static int getFieldToDate(Date date, int field) {
        Calendar d = Calendar.getInstance();
        if (date != null) {
            d.setTime(date);
        }
        return d.get(field);
    }

    public static Date dateTimeWithOutSecond(Date date) {
        try {
            Calendar c_date = dateToCalendar(date);
            c_date.set(Calendar.SECOND, 0);
            c_date.set(Calendar.MILLISECOND, 0);
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static Date datesToTimestamp(Date date, Date heure) {
        try {
            Calendar c_date = dateToCalendar(date);
            if (heure != null) {
                Calendar c_heure = dateToCalendar(heure);
                c_date.set(Calendar.HOUR_OF_DAY, c_heure.get(Calendar.HOUR_OF_DAY));
                c_date.set(Calendar.MINUTE, c_heure.get(Calendar.MINUTE));
                c_date.set(Calendar.SECOND, c_heure.get(Calendar.SECOND));
            }
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public static Date buildTimeStamp(Date date, Date heure) {
        return buildTimeStamp(date, heure, true, true, true);
    }

    public static Date buildTimeStamp(Date date, Date heure, boolean addSecond) {
        return buildTimeStamp(date, heure, true, addSecond, false);
    }

    public static Date buildTimeStamp(Date date, Date heure, boolean addSecond, boolean addMilli) {
        return buildTimeStamp(date, heure, true, addSecond, addMilli);
    }

    public static Date buildTimeStamp(Date date, Date heure, boolean addMinute, boolean addSecond, boolean addMilli) {
        Calendar d = Calendar.getInstance();
        if (date != null) {
            d.setTime(date);
        }
        Calendar h = Calendar.getInstance();
        if (heure != null) {
            h.setTime(heure);
        }
        d.set(Calendar.HOUR_OF_DAY, h.get(Calendar.HOUR_OF_DAY));
        d.set(Calendar.MINUTE, addMinute ? h.get(Calendar.MINUTE) : 0);
        d.set(Calendar.SECOND, addSecond ? h.get(Calendar.SECOND) : 0);
        d.set(Calendar.MILLISECOND, addMilli ? h.get(Calendar.MILLISECOND) : 0);
        return d.getTime();
    }

    public static Date addTimeInDate(Date date, Date heure) {
        Calendar d = Calendar.getInstance();
        d.setTime(date);
        Calendar h = Calendar.getInstance();
        h.setTime(heure);
        d.add(Calendar.HOUR_OF_DAY, h.get(Calendar.HOUR_OF_DAY));
        d.add(Calendar.MINUTE, h.get(Calendar.MINUTE));
        d.add(Calendar.SECOND, h.get(Calendar.SECOND));
        d.add(Calendar.MILLISECOND, h.get(Calendar.MILLISECOND));
        return d.getTime();
    }

    public static Date removeTimeInDate(Date date, Date heure) {
        Calendar d = Calendar.getInstance();
        d.setTime(date);
        Calendar h = Calendar.getInstance();
        h.setTime(heure);
        d.add(Calendar.HOUR_OF_DAY, -h.get(Calendar.HOUR_OF_DAY));
        d.add(Calendar.MINUTE, -h.get(Calendar.MINUTE));
        d.add(Calendar.SECOND, -h.get(Calendar.SECOND));
        d.add(Calendar.MILLISECOND, -h.get(Calendar.MILLISECOND));
        return d.getTime();
    }

    public static Date getTimeStamp(Date date, Date heure) {
        Calendar d = Calendar.getInstance();
        d.setTime(date);
        Calendar h = Calendar.getInstance();
        h.setTime(heure);

        Calendar f = Calendar.getInstance();
        f.set(Calendar.YEAR, d.get(Calendar.YEAR));
        f.set(Calendar.MONTH, d.get(Calendar.MONTH));
        f.set(Calendar.DAY_OF_MONTH, d.get(Calendar.DAY_OF_MONTH));
        f.set(Calendar.HOUR_OF_DAY, h.get(Calendar.HOUR_OF_DAY));
        f.set(Calendar.MINUTE, h.get(Calendar.MINUTE));
        f.set(Calendar.SECOND, h.get(Calendar.SECOND));

        if (!f.after(d)) {
            d = f;
            d.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            d = f;
        }
        return d.getTime();
    }

    public static Date getDate(int day, int month, int year) {
        return yvs.dao.Util.getDate(day, month, year);
    }

    public static Date getHeure(int hour, int minute, int second) {
        return yvs.dao.Util.getHeure(hour, minute, second);
    }

    public static Date getDateTime(int day, int month, int year, int hour, int minute, int second) {
        return yvs.dao.Util.getDateTime(day, month, year, hour, minute, second);
    }

    public static boolean verifyDateHeure(YvsGrhPlanningEmploye p, Date h, YvsParametreGrh param) {
        Calendar d = Calendar.getInstance();
        d.setTime(p.getDateDebut());
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);

        Calendar f = Calendar.getInstance();
        f.setTime(p.getDateFin());
        f.set(Calendar.HOUR_OF_DAY, 0);
        f.set(Calendar.MINUTE, 0);
        f.set(Calendar.SECOND, 0);

        Date dateD = d.getTime();
        Date dateF = f.getTime();

        Calendar heure = Calendar.getInstance();
        heure.setTime(p.getHeureDebut());
        d.set(Calendar.HOUR_OF_DAY, heure.get(Calendar.HOUR_OF_DAY));
        d.set(Calendar.MINUTE, heure.get(Calendar.MINUTE));
        d.set(Calendar.SECOND, 0);
        Date heure_debut = heure.getTime();

        heure.setTime(p.getHeureFin());
        f.set(Calendar.HOUR_OF_DAY, heure.get(Calendar.HOUR_OF_DAY));
        f.set(Calendar.MINUTE, heure.get(Calendar.MINUTE));
        f.set(Calendar.SECOND, 0);
        Date heure_fin = heure.getTime();

        heure_debut = Util.removeTimeInDate(heure_debut, param.getTimeMargeAvance());
        heure_fin = Util.addTimeInDate(heure_fin, param.getTimeMargeAvance());
        if (!heure_debut.after(h) && !h.after(heure_fin)) {
            return true;
        }
        return false;
    }

    public static File convertInputStreamToFile(String fileDirectory, InputStream input) throws FileNotFoundException, IOException {
        File file = null;
        try (FileOutputStream fos = new FileOutputStream(fileDirectory)) {
            // On utilise un tableau comme buffer
            byte[] buf = new byte[8192];
            // Et on utilise une variable pour connaitre le nombre
            // de bytes lus, et donc le nombres qu'il faudra écrire :
            int len;

            while ((len = input.read(buf)) >= 0) {
                fos.write(buf, 0, len);
            }
            file = new File(fileDirectory);
            return file;
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return file;
        }
    }

    public static List<List<String>> readFileCSV(String fileName) {
        return readFileCSV(new File(fileName));
    }

    public static List<List<String>> readFileCSV(UploadedFile file) {
        return readFileCSV(getFile(file));
    }

    public static List<List<String>> readFileCSV(File file) {
        List<List<String>> result = new ArrayList<>();
        try {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    List<String> line = parseLine(scanner.nextLine());
                    if (line != null ? !line.isEmpty() : false) {
                        result.add(line);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, Constantes.DEFAULT_SEPARATOR, Constantes.DEFAULT_QUOTE);
    }

    private static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, Constantes.DEFAULT_QUOTE);
    }

    private static List<String> parseLine(String cvsLine, char separators, char customQuote) {
        List<String> result = new ArrayList<>();
        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }
        if (customQuote == ' ') {
            customQuote = Constantes.DEFAULT_QUOTE;
        }
        if (separators == ' ') {
            separators = Constantes.DEFAULT_SEPARATOR;
        }
        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {
            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {
                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }
                }
            } else {
                if (ch == customQuote) {
                    inQuotes = true;
                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }
                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }
                } else if (ch == separators) {
                    result.add(curVal.toString());
                    curVal = new StringBuffer();
                    startCollectChar = false;
                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }
        }
        result.add(curVal.toString());
        return result;
    }

    public static void createFileCSVByObject(String fileDestination, List<Object> mappedData) throws IOException {
        File file = new File(fileDestination);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(file), ';')) {
            if (mappedData == null) {
                throw new IllegalArgumentException("la liste ne peut pas être nulle");
            }
            if (mappedData.isEmpty()) {
                throw new IllegalArgumentException("la liste ne peut pas être vide");
            }
            // Gestion titre
            final Object[] oneData = (Object[]) mappedData.get(0);
            final String[] titles = new String[oneData.length];
            int i = 0;
            for (Object key : oneData) {
                titles[i++] = key != null ? key.toString() : "";
            }
            if (titles == null) {
                throw new IllegalArgumentException("les titres ne peuvent pas être nuls");
            }
            writer.writeNext(titles);
            // Gestion titre
            boolean first = true;
            for (Object o : mappedData) {
                Object[] element = (Object[]) o;
                if (first) {
                    first = false;
                } else {
                    try {
                        String[] values = Arrays.copyOf(element, element.length, String[].class);
                        writer.writeNext(values);
                    } catch (Exception ex) {
                        Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public static void createFileCSV(String fileDestination, List<String[]> mappedData) throws IOException {
        File file = new File(fileDestination);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(file), ';')) {
            if (mappedData == null) {
                throw new IllegalArgumentException("la liste ne peut pas être nulle");
            }
            if (mappedData.isEmpty()) {
                throw new IllegalArgumentException("la liste ne peut pas être vide");
            }
            // Gestion titre
            final String[] oneData = mappedData.get(0);
            final String[] titles = new String[oneData.length];
            int i = 0;
            for (String key : oneData) {
                titles[i++] = key;
            }
            if (titles == null) {
                throw new IllegalArgumentException("les titres ne peuvent pas être nuls");
            }
            writer.writeNext(titles);
            // Gestion titre
            boolean first = true;
            for (String[] element : mappedData) {
                if (first) {
                    first = false;
                } else {
                    writer.writeNext(element);
                }
            }
        }
    }

    public static void createFileTXTByObject(String fileDestination, List<Object> mappedData, String sep) {
        try {
            String separateur = sep != null ? sep.trim().length() > 0 ? sep : ";" : ";";
            File file = new File(fileDestination);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (FileWriter fw = new FileWriter(file)) {
                if (mappedData == null) {
                    throw new IllegalArgumentException("la liste ne peut pas être nulle");
                }
                if (mappedData.isEmpty()) {
                    throw new IllegalArgumentException("la liste ne peut pas être vide");
                }
                try (BufferedWriter out = new BufferedWriter(fw)) {
                    for (Object o : mappedData) {
                        try {
                            Object[] lines = (Object[]) o;
                            boolean _fisrt = true;
                            for (Object element : lines) {
                                if (!_fisrt) {
                                    out.append(separateur);
                                }
                                out.append(element != null ? element.toString() : "");
                                _fisrt = false;
                            }
                            out.newLine();
                        } catch (IOException ex) {
                            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createFileTXTByString(String fileDestination, List<String[]> mappedData, String sep) throws IOException {
        String separateur = sep != null ? sep.trim().length() > 0 ? sep : ";" : ";";
        File file = new File(fileDestination);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileWriter fw = new FileWriter(file)) {
            if (mappedData == null) {
                throw new IllegalArgumentException("la liste ne peut pas être nulle");
            }
            if (mappedData.isEmpty()) {
                throw new IllegalArgumentException("la liste ne peut pas être vide");
            }
            try (BufferedWriter out = new BufferedWriter(fw)) {
                for (String[] lines : mappedData) {
                    boolean _fisrt = true;
                    for (String element : lines) {
                        if (!_fisrt) {
                            out.append(separateur);
                        }
                        out.append(element != null ? element : "");
                        _fisrt = false;
                    }
                    out.newLine();
                }
            }
        }
    }

    public static List<String> inputStreamToStrings(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        List<String> strings = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(isr)) {
            String line = reader.readLine();
            while (line != null) {
                strings.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public static String[] buildStringToList(String input, String caractere) {
        String[] list = new String[]{};
        if ((input != null) ? input.trim() != null && !"".equals(input.trim()) : false) {
            list = input.trim().split(caractere);
        }
        return list;
    }

    public static String moveSpaceInString(String lien) {
        String r = null;
        for (int i = 0; i < lien.length(); i++) {
            if (lien.charAt(i) == ' ') {
                r += "%20";
            } else {
                if (r == null || "".equals(r)) {
                    r = String.valueOf(lien.charAt(i));
                } else {
                    r += String.valueOf(lien.charAt(i));
                }
            }
        }
        return r;
    }

    public static Connection onConnexion() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");
            String url = "jdbc:postgresql://192.168.1.2:5432/lymytz";
            String user = "postgres";
            String passwd = "yves1910/";
            Connection conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            return conn;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static boolean correctStringToInt(String texte) {
        if ((texte != null) ? !texte.trim().equals("") : false) {
            if (!StringUtils.isNumeric(texte)) {
                return StringUtils.isNumericSpace(texte);
            }
            return true;
        }
        return false;
    }

    public static Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    public static String getDateToStringComplet(Date date) {
        return Managed.ldf.format(date);
    }

    public static String getDateToString(Date date) {
        return Managed.dft.format(date);
    }

    public static String getDouble(double montant) {
        double value = Double.parseDouble("" + montant);
        if (value == 0) {
            return "0";
        } else {
            return Managed.Lnf.format(value);
        }
    }

    public static Timestamp currentTimeStamp(Date date) {
        if (date != null) {
            return new java.sql.Timestamp(date.getTime());
        }
        return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public static String getDateToString_(Date date) {
        return Managed.dft.format(date);
    }

    public static boolean correctEmail(String email) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");
        if ((email != null) ? !email.equals("") : false) {
            return p.matcher(email).matches();
        }
        return false;
    }

    public static boolean onEmail(String email) {
        return correctEmail(email);
    }

    public static boolean isMajuscule(String mot) {
        if (mot != null ? mot.trim().length() > 0 : false) {
            Pattern p = Pattern.compile("^[a-z]$");
            for (int i = 0; i < mot.length(); i++) {
                if (p.matcher(String.valueOf(mot.charAt(i))).matches()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isCapitalise(String mot) {
        if (mot != null ? mot.trim().length() > 0 : false) {
            Pattern p = Pattern.compile("^[a-z]$");
            for (int i = 0; i < mot.length(); i++) {
                if (i == 0) {
                    if (p.matcher(String.valueOf(mot.charAt(i))).matches()) {
                        return false;
                    }
                } else {
                    if (!p.matcher(String.valueOf(mot.charAt(i))).matches()) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static String capitalise(String mot) {
        String result = "";
        if (mot != null ? mot.trim().length() > 0 : false) {
            for (int i = 0; i < mot.length(); i++) {
                if (i == 0) {
                    result = String.valueOf(mot.charAt(i)).toUpperCase();
                } else {
                    result += String.valueOf(mot.charAt(i)).toLowerCase();
                }
            }
        }
        return result;
    }

    /*
     BEGIN INT
     */
    public static int getDay(String jour) {
        switch (jour) {
            case "Lundi":
            case "Lun":
                return Calendar.MONDAY;
            case "Mardi":
            case "Mar":
                return Calendar.TUESDAY;
            case "Mercredi":
            case "Mer":
                return Calendar.WEDNESDAY;
            case "Jeudi":
            case "Jeu":
                return Calendar.THURSDAY;
            case "Vendredi":
            case "Ven":
                return Calendar.FRIDAY;
            case "Samedi":
            case "Sam":
                return Calendar.SATURDAY;
            case "Dimanche":
            case "Dim":
                return Calendar.SUNDAY;
            default:
                return -1;
        }
    }

    public static int getMonth(String mois) {
        switch (mois) {
            case "Jan":
            case "Janvier":
                return 0;
            case "Fév":
            case "FEv":
            case "Fevrier":
            case "Février":
                return 1;
            case "Mars":
                return 2;
            case "Avr":
            case "Avril":
                return 3;
            case "Mai":
                return 4;
            case "Juin":
                return 5;
            case "Juil":
            case "Juillet":
                return 6;
            case "Aout":
            case "Août":
                return 7;
            case "Sept":
            case "Septembre":
                return 8;
            case "Oct":
            case "Octobre":
                return 9;
            case "Nov":
            case "Novembre":
                return 10;
            case "Dec":
            case "Déc":
            case "Decembre":
            case "Décembre":
                return 11;
            default:
                return -1;
        }
    }

    public static String getDay(Date jour) {
        return Util.getDay(Util.dateToCalendar(jour));
    }

    public static String getDay(Calendar jour) {
        int d = jour.get(Calendar.DAY_OF_WEEK);
        String str = "";
        if (d == Calendar.MONDAY) {
            str = "Lundi";
        } else if (d == Calendar.TUESDAY) {
            str = "Mardi";
        } else if (d == Calendar.WEDNESDAY) {
            str = "Mercredi";
        } else if (d == Calendar.THURSDAY) {
            str = "Jeudi";
        } else if (d == Calendar.FRIDAY) {
            str = "Vendredi";
        } else if (d == Calendar.SATURDAY) {
            str = "Samedi";
        } else if (d == Calendar.SUNDAY) {
            str = "Dimanche";
        }
        return str;
    }

    public static String getDay(int day) {
        switch (day) {
            case 1:
                return "Dimanche";
            case 2:
                return "Lundi";
            case 3:
                return "Mardi";
            case 4:
                return "Mercredi";
            case 5:
                return "Jeudi";
            case 6:
                return "Vendredi";
            case 7:
                return "Samedi";
            default:
                return "";
        }
    }

    public static double arrondi(double d, int round) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(round, BigDecimal.ROUND_DOWN);
        return bd.doubleValue();
    }

    public static Date[] getDayFirstEndWeek(Date date) {
        Date[] dates = new Date[2];

        // Date du jour
        Calendar today = dateToCalendar(date);

        // Date du lundi
        Calendar monday = (Calendar) today.clone();
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        dates[0] = monday.getTime();

        // Date du dimanche
        Calendar sunday = (Calendar) today.clone();
        sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        dates[1] = sunday.getTime();

        return dates;
    }

    public static String getTimeToString(Date time) {
        return Managed.time.format(time);
    }

    public static int countNumberMonth(Date debut_, Date fin_) {
        Calendar debut = Calendar.getInstance();
        debut.setTime(debut_ != null ? debut_ : new Date());
        Calendar fin = Calendar.getInstance();
        fin.setTime(fin_ != null ? fin_ : new Date());
        int moisDebut = debut.get(Calendar.YEAR) * 12 + debut.get(Calendar.MONTH);
        int moisFin = fin.get(Calendar.YEAR) * 12 + fin.get(Calendar.MONTH);
        return moisFin - moisDebut;
    }

    public static int calculNbYear(Date d1, Date d2) {
        int A1 = 0, A2 = 0;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        A1 = c1.get(Calendar.YEAR);
        c1.setTime(d2);
        A2 = c1.get(Calendar.YEAR);
        return ((A2 - A1) >= 0) ? (A2 - A1) : 0;
    }

    public static int calculNbMonth(Date d1, Date d2) {
        int A1 = 0, A2 = 0;
        int M1 = 0, M2 = 0;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        A1 = c1.get(Calendar.YEAR);
        M1 = c1.get(Calendar.MONTH);
        c1.setTime(d2);
        A2 = c1.get(Calendar.YEAR);
        M2 = c1.get(Calendar.MONTH);
        int year = ((A2 - A1) >= 0) ? (A2 - A1) : 0;
        int month = (M2 - M1);
        month = (int) ((year + (month / 12)) * 12);
        return (month >= 0) ? month : 0;
    }

    public static int calculNbDay(Date d1, Date d2) {
        int M1 = 0, M2 = 0;
        int D1 = 0, D2 = 0;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        M1 = c1.get(Calendar.MONTH);
        D1 = c1.get(Calendar.DAY_OF_MONTH);
        c1.setTime(d2);
        M2 = c1.get(Calendar.MONTH);
        D2 = c1.get(Calendar.DAY_OF_MONTH);
        int month = (M2 + D2 - M1 - D1);
        return (month >= 0) ? month : 0;
    }

    public static String calculNbyear(Date d1, Date d2) {
        int A1 = 0, A2 = 0;
        int M1 = 0, M2 = 0;
        int D1 = 0, D2 = 0;
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        A1 = c1.get(Calendar.YEAR);
        M1 = c1.get(Calendar.MONTH);
        D1 = c1.get(Calendar.DAY_OF_MONTH);
        c1.setTime(d2);
        A2 = c1.get(Calendar.YEAR);
        M2 = c1.get(Calendar.MONTH);
        D2 = c1.get(Calendar.DAY_OF_MONTH);
        int year = ((A2 - A1) >= 0) ? (A2 - A1) : 0;
        int month = ((M2 - M1) < 0) ? Math.abs((M2 - M1)) : (M2 - M1);
        int jour = ((D2 - D1) < 0) ? Math.abs((D2 - D1)) : (D2 - D1);
        String ry = ((year != 0) ? (year + " An(s)") : (""));
        String rm = ((month != 0) ? (" " + month + " Mois") : (""));
        String rj = ((jour != 0) ? (" " + jour + " Jour(s)") : (""));
        return (ry + "" + rm + "" + rj).trim();
    }

    public static Date getFirstDayMonth(Date date) {
        Calendar cal = dateToCalendar(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        cal.clear();
        cal.set(year, month, 01);
        return cal.getTime();
    }

    public static Date getLastDayMonth(Date date) {
        Calendar cal = dateToCalendar(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.clear();
        cal.set(year, month, day);
        return cal.getTime();
    }

    public static int ecartOnDate(Date dateDebut, Date dateFin, String on) {
        if (dateDebut != null && dateFin != null) {
            Calendar cal1 = dateToCalendar(dateDebut);
            Calendar cal2 = dateToCalendar(dateFin);
            Calendar cal;
            switch (on) {
                case "an":
                    int an = 0;
                    cal = cal1;
                    while (cal.before(cal2)) {
                        an++;
                        cal.add(Calendar.YEAR, 1);
                    }
                    return an;
                case "mois":
                    int mois = 0;
                    cal = cal1;
                    while (cal.before(cal2)) {
                        mois++;
                        cal.add(Calendar.MONTH, 1);
                    }
                    if (mois > 12) {
                        return 12;
                    }
                    return mois;
                case "jour":
                    int jour = 0;
                    cal = cal1;
                    while (cal.before(cal2)) {
                        jour++;
                        cal.add(Calendar.DATE, 1);
                    }
                    if (jour > 366) {
                        return 366;
                    }
                    return jour;
                case "heure":
                    int heure = 0;
                    cal = cal1;
                    while (cal.before(cal2)) {
                        heure++;
                        cal.add(Calendar.HOUR_OF_DAY, 1);
                    }
                    if (heure > 24) {
                        return 24;
                    }
                    return heure;
                default:
                    break;
            }
        }
        return 0;
    }

    public static int getOccurenceDay(String jour, Date dateDebut, Date dateFin) {
        int all = 0;
        if (jour != null) {
            if (!jour.equals("") && dateDebut != null & dateFin != null) {
                int day = getDay(jour);
                Calendar calD = dateToCalendar(dateDebut);
                Calendar calF = dateToCalendar(dateFin);
                Calendar cal = calD;
                while (cal.before(calF)) {
                    if (cal.get(Calendar.DAY_OF_WEEK) == day) {
                        all++;
                    }
                    cal.add(Calendar.DATE, 1);
                }
            }
        }
        return all;
    }

    public static boolean isNumeric(String value) {
        try {
            Double.valueOf(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean asString(String value) {
        if (value != null ? value.trim().length() > 0 : false) {
            return true;
        }
        return false;
    }

    public static boolean asValeur(Object value) {
        if (value != null ? asString(value.toString()) : false) {
            return true;
        }
        return false;
    }

    public static boolean asLong(Long value) {
        if (value != null ? value > 0 : false) {
            return true;
        }
        return false;
    }

    public static String nextValue(String oldValue) {
        // TODO code application logic here
        String newValue = "A";
        if (oldValue != null ? oldValue.trim().length() > 0 : false) {
            newValue = oldValue.toUpperCase();
            String temp = newValue;
            boolean continu = true;
            boolean increment = false;
            int length = newValue.length() - 1;//Recuperer la taille de la chaine entrée
            for (int i = length; i > -1; i--) {//Boucle inverse sur la taille
                char ch = newValue.charAt(i);//Recuperer le caracter a la position i
                int index = Constantes.ALPHABET.indexOf(ch);//Recupere l'index dans l'alphabet
                char rs = 'A';
                if (index == Constantes.ALPHABET.length() - 1) {//Verifie si on est a la dernier lettre de l'alphabet
                    increment = true;//On precise On va revenir a la lettre A
                } else {
                    rs = Constantes.ALPHABET.charAt(index + 1);//Recupere la lettre suivante
                    if (!continu) {//Verifie si on est passé a la lettre A
                        break;
                    }
                    if (increment) {//Definie si on est passé a la lettre A
                        continu = false;
                    }
                    index = Constantes.ALPHABET.indexOf(rs);//Recupere l'index dans l'alphabet
                    if (index != Constantes.ALPHABET.length() - 1) {//Verifie si on n'est pas a la dernier lettre de l'alphabet
                        continu = false;
                    }
                }
                newValue = newValue.substring(0, i) + String.valueOf(rs);//Change la lettre courante en sa nouvelle valeur
                if (i < length) {//Verifie si on n'est plus a la 1ere lettre a modifier
                    newValue += temp.substring(i + 1, temp.length());//Ajoute la suite des lettres a la nouvelle chaine
                }
                temp = newValue;//Sauvegarde la nouvelle valeur
            }
            increment = true;//Cas ou on a est passé a la lettre A pour toutes les lettres
            for (int i = 0; i < newValue.length(); i++) {
                if (newValue.charAt(i) != 'A') {
                    increment = false;
                    break;
                }
            }
            if (increment) {//On ajoute une nouvelle lettre A
                newValue += "A";
            }
        }
        return newValue;
    }

    public static int executeSqlFile(DataSource ds, String file) {
        try {
            String user = ds.getConnection().getMetaData().getUserName();
            String url = ds.getConnection().getMetaData().getURL();
            url = url.substring(5);
            URI uri = URI.create(url);
            String schema = uri.getScheme();
            String serveur = uri.getHost();
            String port = uri.getPort() + "";
            String databale = uri.getPath().replace("/", "");
            String command = "psql -U " + user + " -h " + serveur + " -p " + port + " -d " + databale + " -f \"" + file + "\"";
            return executeCmd(command);
        } catch (SQLException ex) {
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static String createQRCode(String qrCodeData) {
        try {
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
            String filePath = path + FILE_SEPARATOR + "QRCode.png";
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map hintMap = new HashMap();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            int qrCodeheight = 200, qrCodewidth = 200;

            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), new File(filePath));
            return filePath;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriterException | IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String readQRCode(String filePath) {
        String value = null;
        try {
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map hintMap = new HashMap();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
            Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
            value = qrCodeResult.getText();
        } catch (NotFoundException | FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public static int executeCmd(String commande) {
        try {
            Process pr = Runtime.getRuntime().exec(commande);
            return pr.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
