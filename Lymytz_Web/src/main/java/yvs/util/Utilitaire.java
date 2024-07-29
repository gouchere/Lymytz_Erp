/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 */
public class Utilitaire<T> {

    public Utilitaire() {
    }
    //retourne le nombre d'élément de la collection correspondant à obj

    public int findObject(List<T> list, T obj) {
        return Collections.frequency(list, obj);
    }

    public static byte[] readFile(File file) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            FileInputStream fis = new FileInputStream(file);
            int c = 0;
            try {
                while ((c = fis.read()) != -1) {
                    buffer.write(c);
                    buffer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return buffer.toByteArray();
    }

    /**
     * * souce: nom + extension sous laquelle le flux doit s'ouvrir
     *
     * @param bytes
     * @param source
     * @param ext
     */
    public static void ouvrirLeFlux(byte[] bytes, String source, String ext) {
        HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        resp.setContentType("application/" + ext);
        resp.setHeader("Content-disposition", "attachement;filename=" + source);
        resp.setContentLength(bytes.length);
        try {
            resp.getOutputStream().write(bytes);
            resp.getOutputStream().flush();
        } catch (IOException ex) {
            Logger.getLogger(Utilitaire.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                resp.getOutputStream().close();
            } catch (IOException ex) {
                Logger.getLogger(Utilitaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static int countDayBetweenDate(Date begin, Date end) {
        if (begin != null && end != null) {
            int re = 0;
            Calendar d1 = Calendar.getInstance();
            d1.setTime(begin);
            while (begin.before(end) || begin.equals(end)) {
                re++;
                d1.add(Calendar.DAY_OF_MONTH, 1);
                begin = d1.getTime();
            }
            return re;
        }
        return 0;
    }

    public static Date calculDureeBetweenDate(Date begin, Date end) {
        if (begin != null && end != null) {
            Calendar d1 = Calendar.getInstance();
            d1.setTime(begin);
            Calendar d2 = Calendar.getInstance();
            d2.setTime(end);            
            long l = end.getTime() - begin.getTime() - 3600000;//enlève une heure à cause du décalage GMT
            return new Date(l);
        }
        return null;
    }

    public static double countHourBetweenDate(Date begin, Date end) {
        return (end.getTime() - begin.getTime()) / (1000 * 60 * 60);
    }

    public static String convertHour(long timeMilis) {
        long timeMinute = (timeMilis) / 60000;
        int heure = (int) (timeMinute / 60);
        int min = (int) (timeMinute - (60 * heure));
        String stH = (heure < 9) ? "0" + heure : "" + heure;
        String stM = (min < 9) ? "0" + min : "" + min;
        return stH + ":" + stM;
    }

    public static String doubleToHour(double hour) {
        int i = new Double(hour).intValue(); //recuperer la partie entiere
        int min = (int) ((hour - i) * 60);
        String stH = (i <= 9) ? "0" + i : "" + i;
        String stM = (min <= 9) ? "0" + min : "" + min;
        return stH + ":" + stM;
    }

    public static String giveDay(int day) {
        switch (day) {
            case 0:
                return "Dimanche";
            case 1:
                return "Lundi";
            case 2:
                return "Mardi";
            case 3:
                return "Mercredi";
            case 4:
                return "Jeudi";
            case 5:
                return "Vendredi";
            case 6:
                return "Samedi";
            default:
                return null;
        }
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

    public static String getDay(Date day) {
        Calendar jour = Calendar.getInstance();
        jour.setTime(day);
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

    public static int calculNbYear(Date d1, Date d2) {
        int A1, A2;
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

    public static String calculNbyear(Date d1, Date d2) {
        if (d2 != null && d1 != null) {
            if (d2.after(d1)) {
                int A1 = 0, A2 = 0;
                int M1 = 0, M2 = 0;
                Calendar c1 = Calendar.getInstance();
                c1.setTime(d1);
                A1 = c1.get(Calendar.YEAR);
                M1 = c1.get(Calendar.MONTH);
                c1.setTime(d2);
                A2 = c1.get(Calendar.YEAR);
                M2 = c1.get(Calendar.MONTH);
                int year = ((((A2 * 12) + M2) - ((A1 * 12) + M1)) / 12);
                int month = ((((A2 * 12) + M2) - ((A1 * 12) + M1)) - (year * 12));
                String ry = ((year != 0) ? (year + " Ans") : (""));
                String rm = ((month != 0) ? (" (" + month + " Mois)") : (""));
                return ry + "" + rm;
            }
        }
        return "0";
    }

    public static boolean containsSpace(String str) {
        if (str != null) {
            str = str.trim();
            String[] tb = str.split(" ");
            return tb.length <= 1;
        }
        return false;
    }

    public static Calendar getIniTializeDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public static double timeToDouble(Date time) {
        double re = 0;
        if (time != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            //extraire l'heure
            int h = cal.get(Calendar.HOUR);
            //extraire les minute
            int m = cal.get(Calendar.MINUTE);
            re = (double) ((double) h + ((double) m / (double) 60));
//            System.err.println("-- Heure " + h + "  Minute " + m + " Résultat " + re);
        }
        return re;
    }

    public static Date doubleToTime(double time) {
        int hour = new Double(time).intValue(); //recuperer la partie entiere
        int min = (int) ((time - hour) * 60);
        Date re = new Date();
        Calendar c = getIniTializeDate(re);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        re = c.getTime();
        return re;
    }

    public static Date giveOnlyDate(Date begin) {
        Calendar d1 = Calendar.getInstance();
        if (begin != null) {
            d1.setTime(begin);
        }
        d1.set(Calendar.HOUR, 0);
        d1.set(Calendar.MINUTE, 0);
        d1.set(Calendar.SECOND, 0);
        d1.set(Calendar.MILLISECOND, 0);
        return d1.getTime();
    }

    public static Date fabriqueTimeStamp(Date date, Date heure) {
        Calendar d = Calendar.getInstance();
        Calendar h = Calendar.getInstance();
        d.setTime(date);
        h.setTime((heure != null) ? heure : date);
        d.set(Calendar.HOUR_OF_DAY, h.get(Calendar.HOUR_OF_DAY));
        d.set(Calendar.MINUTE, h.get(Calendar.MINUTE));
        d.set(Calendar.SECOND, h.get(Calendar.SECOND));
        d.set(Calendar.MILLISECOND, h.get(Calendar.MILLISECOND));
        return d.getTime();
    }

}
